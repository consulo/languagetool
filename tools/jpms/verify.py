#!/usr/bin/env python3
"""Check source ownership, compile descriptors, and assemble a validated module path."""
from pathlib import Path
from collections import defaultdict
import re
import shutil
import subprocess

ROOT = Path(__file__).resolve().parents[2]
OUTPUT = ROOT / 'target/jpms'


def verify():
    packages = defaultdict(set)
    classes = set()
    for source in ROOT.glob('**/src/main/java/**/*.java'):
        text = source.read_text()
        match = re.search(r'^package\s+([\w.]+);', text, re.M)
        if match:
            module = str(source.relative_to(ROOT)).split('/src/')[0]
            packages[match[1]].add(module)
            classes.add(match[1] + '.' + source.stem)
    splits = {pkg: owners for pkg, owners in packages.items() if len(owners) > 1}
    if splits:
        raise RuntimeError('Split source packages: ' + str(splits))
    for properties in ROOT.glob('**/src/main/resources/META-INF/org/languagetool/language-module.properties'):
        for line in properties.read_text().splitlines():
            if line.startswith('languageClasses='):
                for name in line.split('=', 1)[1].split(','):
                    if name.strip() not in classes:
                        raise RuntimeError(f'{properties}: unknown language class {name}')
    source_path = OUTPUT / 'src'
    compiled = OUTPUT / 'classes'
    if source_path.exists():
        shutil.rmtree(source_path)
    if compiled.exists():
        shutil.rmtree(compiled)
    source_path.mkdir(parents=True)
    command = ['javac', '--release', '17', '--module-path', str(OUTPUT / 'lib'),
               '--module-source-path', str(source_path), '-d', str(compiled)]
    sources = []
    modules = []
    for descriptor in sorted(ROOT.glob('**/src/main/jpms/module-info.java')):
        directory = descriptor.parents[3]
        classes_dir = directory / 'target/classes'
        jars = [p for p in (directory / 'target').glob('*.jar')
                if not p.name.endswith(('-tests.jar', '-sources.jar', '-javadoc.jar', '-jar-with-dependencies.jar'))]
        if not jars:
            print('Not built (outside the default reactor): ' + str(directory.relative_to(ROOT)))
            continue
        if len(jars) != 1:
            raise RuntimeError(f'Ambiguous main jar in {directory}: {jars}')
        classes_dir.mkdir(parents=True, exist_ok=True)
        text = descriptor.read_text()
        name = re.search(r'\bmodule\s+([\w.]+)', text)[1]
        dest = source_path / name / 'module-info.java'
        dest.parent.mkdir(parents=True)
        dest.write_text(text)
        command.extend(['--patch-module', name + '=' + str(classes_dir)])
        sources.append(str(dest))
        modules.append((name, jars[0], classes_dir))
    subprocess.run(command + sources, check=True)
    comparisons = OUTPUT / 'descriptor-comparisons.txt'
    comparisons.write_text('\n'.join(str(jar) + '\t' + str(compiled / name / 'module-info.class') for name, jar, _ in modules))
    subprocess.run(['java', str(ROOT / 'tools/jpms/ModuleNames.java'), '--compare', str(comparisons)], check=True)
    for name, jar, _ in modules:
        dest = OUTPUT / 'lib' / (name + '.jar')
        shutil.copyfile(jar, dest)
    subprocess.run(['java', '--module-path', str(OUTPUT / 'lib'), '--add-modules', 'ALL-MODULE-PATH', '--validate-modules'], check=True)
    print(f'Validated {len(modules)} LanguageTool modules; module path: {OUTPUT / "lib"}')


if __name__ == '__main__':
    verify()
