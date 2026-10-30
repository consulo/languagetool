#!/usr/bin/env python3
"""Refresh descriptors from compiled classes and the resolved Maven dependencies."""
from pathlib import Path
from collections import defaultdict
import json
import os
import re
import subprocess
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
NS = {'m': 'http://maven.apache.org/POM/4.0.0'}


def module_name(artifact):
    if artifact == 'languagetool':
        return 'org.languagetool.rpm'
    return 'org.languagetool.' + (artifact.removeprefix('languagetool-').replace('-', '.'))


def generate():
    project_group = ET.parse(ROOT / 'pom.xml').findtext('m:groupId', namespaces=NS)
    mapping = json.loads((ROOT / 'target/jpms/dependencies.json').read_text())
    modules = {}
    for pom in ROOT.glob('**/pom.xml'):
        if any(p in ('target', 'src') for p in pom.relative_to(ROOT).parts):
            continue
        tree = ET.parse(pom)
        artifact = tree.findtext('m:artifactId', namespaces=NS)
        packaging = tree.findtext('m:packaging', default='jar', namespaces=NS)
        if packaging != 'pom':
            modules[artifact] = (pom.parent, tree, module_name(artifact))
    # Metadata for normalized dependencies gives an unambiguous package owner.
    listing = ROOT / 'target/jpms/normalized-jars.txt'
    listing.write_text('\n'.join(str(p) for p in sorted((ROOT / 'target/jpms/lib').glob('*.jar'))))
    output = subprocess.check_output(['java', str(ROOT / 'tools/jpms/ModuleNames.java'), str(listing)], text=True)
    owners = {}
    for line in output.splitlines():
        _, module, packages, _ = line.split('\t')
        for package in packages.split(','):
            owners[package] = module
    for artifact, (directory, tree, name) in modules.items():
        requires = set()
        transitive = set()
        static = set()
        exports = set()
        for dependency in tree.findall('m:dependencies/m:dependency', NS):
            scope = dependency.findtext('m:scope', default='compile', namespaces=NS)
            if scope == 'test':
                continue
            dep = dependency.findtext('m:artifactId', namespaces=NS)
            group = dependency.findtext('m:groupId', namespaces=NS)
            if group == project_group and dep in modules:
                requires.add(modules[dep][2])
                transitive.add(modules[dep][2])
            else:
                for jar, module in mapping.items():
                    if Path(jar).parent.parent.name == dep:
                        requires.add(module)
                        if scope == 'provided' or module in ('lombok', 'org.jetbrains.annotations'):
                            static.add(module)
        classes = directory / 'target/classes'
        cp_file = directory / 'target/jpms-classpath'
        if classes.exists() and cp_file.exists():
            cp = cp_file.read_text().strip()
            for api_only in (False, True):
                command = ['jdeps', '--multi-release', '17', '-verbose:class', '-filter:none', '--class-path', cp]
                if api_only:
                    command.append('--api-only')
                command.append(str(classes))
                result = subprocess.run(command, text=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, check=True)
                for line in result.stdout.splitlines():
                    match = re.match(r'\s+\S+\s+->\s+(\S+)\s+(\S+)', line)
                    if not match:
                        continue
                    target, origin = match.groups()
                    module = origin if origin.startswith(('java.', 'jdk.')) else owners.get(target.rsplit('.', 1)[0])
                    if module and module not in ('java.base', name):
                        requires.add(module)
                        if api_only:
                            transitive.add(module)
        for source in (directory / 'src/main/java').rglob('*.java'):
            text = source.read_text()
            package = re.search(r'^package\s+([\w.]+);', text, re.M)
            if package:
                exports.add(package[1])
            if 'org.jetbrains.annotations' in text:
                requires.add('org.jetbrains.annotations')
                static.add('org.jetbrains.annotations')
            if 'lombok.' in text:
                requires.add('lombok')
                static.add('lombok')
        lines = ['// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.',
                 'open module ' + name + ' {']
        for dependency in sorted(requires):
            modifiers = 'static ' if dependency in static else ('transitive ' if dependency in transitive else '')
            lines.append('    requires ' + modifiers + dependency + ';')
        if requires and exports:
            lines.append('')
        lines.extend('    exports ' + package + ';' for package in sorted(exports))
        # Preserve the existing resource-based language registry on the module path.
        lines.append('}')
        dest = directory / 'src/main/jpms/module-info.java'
        dest.parent.mkdir(parents=True, exist_ok=True)
        dest.write_text('\n'.join(lines) + '\n')
    print(f'Updated {len(modules)} descriptors')


if __name__ == '__main__':
    generate()
