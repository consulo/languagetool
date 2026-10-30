#!/usr/bin/env python3
"""Prepare a module path from Maven's dependency jars without changing the cache.

Legacy jars with overlapping packages must be a single automatic module. Other
jars are copied unchanged. Classpath precedence is retained for duplicate files.
"""
from pathlib import Path
from collections import defaultdict
import json
import os
import re
import shutil
import subprocess
import zipfile
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
OUTPUT = ROOT / 'target/jpms'


def prepare():
    OUTPUT.mkdir(parents=True, exist_ok=True)
    namespace = {'m': 'http://maven.apache.org/POM/4.0.0'}
    project_group = ET.parse(ROOT / 'pom.xml').findtext('m:groupId', namespaces=namespace)
    project_group_path = '/' + project_group.replace('.', '/') + '/'
    reactor = {ET.parse(pom).findtext('m:artifactId', namespaces=namespace)
               for pom in ROOT.glob('**/pom.xml')
               if not any(part in ('target', 'src') for part in pom.relative_to(ROOT).parts)}
    jars = []
    artifacts = set()
    for cp in sorted(list(ROOT.glob('**/target/jpms-classpath')) + list(ROOT.glob('**/target/jpms-runtime-classpath')), key=lambda p: ('languagetool-core' not in str(p), str(p))):
        for value in cp.read_text().strip().split(os.pathsep):
            jar = Path(value)
            if jar.suffix != '.jar' or not jar.exists():
                continue
            # Reactor jars are copied separately, after descriptors are added.
            if jar.is_relative_to(ROOT) or ((project_group_path in str(jar) or '/org/languagetool/' in str(jar)) and jar.parent.parent.name in reactor):
                continue
            key = (jar.parent.parent, re.sub(r'-' + re.escape(jar.parent.name), '', jar.name))
            if key not in artifacts:
                artifacts.add(key)
                jars.append(jar)
    listing = OUTPUT / 'jars.txt'
    listing.write_text('\n'.join(map(str, jars)))
    result = subprocess.check_output(['java', str(ROOT / 'tools/jpms/ModuleNames.java'), str(listing)], text=True)
    names = {}
    packages = {}
    required = {}
    for line in result.splitlines():
        path, name, _, needs = line.split('\t')
        jar = Path(path)
        if name == 'ERROR':
            if jar.name.startswith('aho-corasick-double-array-trie-'):
                name = 'com.hankcs.aho.corasick'
            else:
                raise RuntimeError('Invalid dependency module: ' + line)
        names[jar] = name
        required[jar] = set(needs.split(',')) - {''}
        with zipfile.ZipFile(jar) as archive:
            packages[jar] = {n.rsplit('/', 1)[0].replace('/', '.') for n in archive.namelist()
                             if n.endswith('.class') and '/' in n and not n.startswith('META-INF/')}
    groups = [{jar} for jar in jars]
    # Merge connected components of the split-package graph.
    while True:
        merged = False
        for i in range(len(groups)):
            first = set().union(*(packages[jar] for jar in groups[i]))
            first_names = {names[jar] for jar in groups[i]}
            for j in range(i + 1, len(groups)):
                second = set().union(*(packages[jar] for jar in groups[j]))
                if first & second or first_names & {names[jar] for jar in groups[j]}:
                    groups[i] |= groups.pop(j)
                    merged = True
                    break
            if merged:
                break
        if not merged:
            break
    module_path = OUTPUT / 'lib'
    module_path.mkdir(exist_ok=True)
    for old in module_path.glob('*.jar'):
        old.unlink()
    module_groups = []
    for group in groups:
        members = [jar for jar in jars if jar in group]
        module_names = {names[jar] for jar in group}
        if 'org.jetbrains.annotations' in module_names:
            canonical = 'org.jetbrains.annotations'
            members.sort(key=lambda p: names[p] != canonical)
        elif 'java.annotation' in module_names:
            canonical = 'java.annotation'
        elif 'lucene.core' in module_names:
            canonical = 'lucene.core'
        else:
            canonical = names[members[0]]
        module_groups.append((canonical, members))
    available = {canonical for canonical, _ in module_groups}
    jdk_modules = {line.split('@')[0] for line in subprocess.check_output(['java', '--list-modules'], text=True).splitlines()}
    mapping = {}
    report = {}
    for canonical, members in module_groups:
        dest = module_path / (canonical + '.jar')
        missing = required[members[0]] - available - jdk_modules
        if len(members) == 1 and names[members[0]] != 'com.hankcs.aho.corasick' and not missing:
            shutil.copyfile(members[0], dest)
        else:
            entries = {}
            services = defaultdict(list)
            for jar in members:
                with zipfile.ZipFile(jar) as archive:
                    for entry in archive.infolist():
                        name = entry.filename
                        if entry.is_dir() or name.endswith('module-info.class') or name == 'META-INF/MANIFEST.MF' or re.match(r'META-INF/[^/]+\.(SF|RSA|DSA|EC)$', name, re.I):
                            continue
                        data = archive.read(entry)
                        if name.startswith('META-INF/services/'):
                            services[name].extend(data.decode().splitlines())
                        else:
                            entries.setdefault(name, data)
            with zipfile.ZipFile(dest, 'w', zipfile.ZIP_DEFLATED) as archive:
                archive.writestr('META-INF/MANIFEST.MF', 'Manifest-Version: 1.0\r\nAutomatic-Module-Name: ' + canonical + '\r\nMulti-Release: true\r\n\r\n')
                for name, data in sorted(entries.items()):
                    archive.writestr(name, data)
                for name, lines in sorted(services.items()):
                    archive.writestr(name, '\n'.join(dict.fromkeys(lines)) + '\n')
        report[canonical] = [str(jar) for jar in members]
        for jar in members:
            mapping[str(jar)] = canonical
    (OUTPUT / 'dependencies.json').write_text(json.dumps(mapping, indent=2) + '\n')
    (OUTPUT / 'groups.json').write_text(json.dumps(report, indent=2) + '\n')
    print(f'Prepared {len(groups)} dependency modules in {module_path}')
    return mapping


if __name__ == '__main__':
    prepare()
