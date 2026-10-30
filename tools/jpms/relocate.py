#!/usr/bin/env python3
"""Reapply the fork's minimal package relocations after an upstream merge."""
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[2]
# Keep core packages stable; only relocate packages shared by multiple artifacts.
MOVES = {}
for module in (ROOT / 'languagetool-language-modules').iterdir():
    if (module / 'pom.xml').exists() and module.name != 'all':
        suffix = module.name.replace('-', '_')
        MOVES[str(module.relative_to(ROOT))] = {
            'org.languagetool.language': 'org.languagetool.language.' + suffix,
            **{f'org.languagetool.{p}': f'org.languagetool.{p}.{suffix}'
               for p in ('chunking', 'synthesis', 'tokenizers', 'tools')},
        }
MOVES['languagetool-language-modules/ml']['org.languagetool.rules.ml'] = 'org.languagetool.rules.ml.spelling'
MOVES['languagetool-gui-commons'] = {'org.languagetool.gui': 'org.languagetool.gui.commons'}
MOVES['languagetool-standalone'] = {'org.languagetool.dev': 'org.languagetool.dev.standalone'}
MOVES['languagetool-wikipedia'] = {'org.languagetool.dev': 'org.languagetool.dev.wikipedia'}
MOVES['languagetool-tools'] = {'org.languagetool.tools': 'org.languagetool.tools.dictionary'}
MOVES['languagetool-dev'] = {'org.languagetool.rules.spelling.suggestions': 'org.languagetool.dev.suggestions'}

def read(path):
    return path.read_bytes().decode('utf-8')


def apply():
    import subprocess
    # This test exercises a package-private core class; keep it with that class.
    predicate_test = ROOT / 'languagetool-language-modules/de/src/test/java/org/languagetool/chunking/TokenPredicateTest.java'
    if predicate_test.exists():
        dest = ROOT / 'languagetool-core/src/test/java/org/languagetool/chunking/TokenPredicateTest.java'
        dest.parent.mkdir(parents=True, exist_ok=True)
        if dest.exists() and dest.read_bytes() != predicate_test.read_bytes():
            raise RuntimeError('Resolve the two TokenPredicateTest versions before relocating')
        dest.write_bytes(predicate_test.read_bytes())
        predicate_test.unlink()
    files = [ROOT / name for name in subprocess.check_output(
        ['git', 'ls-files', '-z'], cwd=ROOT).decode().split('\0') if name]
    sources = []
    classes = {}
    packages = {}
    for f in files:
        if f.suffix != '.java' or '/src/' not in str(f) or not f.exists():
            continue
        text = read(f)
        match = re.search(r'^package\s+([\w.]+)\s*;', text, re.M)
        if not match:
            continue
        package = match[1]
        module = str(f.relative_to(ROOT)).split('/src/')[0]
        moves = MOVES.get(module, {})
        new = moves.get(package, package)
        sources.append((f, text, package, new, module))
        if '/src/main/java/' in str(f):
            # Already relocated sources still describe the old FQCN after a merge.
            old = next((k for k, v in moves.items() if v == package), package)
            classes[old + '.' + f.stem] = new + '.' + f.stem
            packages.setdefault(old, []).append((f.stem, new + '.' + f.stem))
    changed_classes = {old: new for old, new in classes.items() if old != new}
    pattern = re.compile(r'(?<![\w.])(?:' + '|'.join(
        re.escape(c) for c in sorted(changed_classes, key=len, reverse=True)) + r')(?![\w$])')
    source_map = {f: (text, package, new, module) for f, text, package, new, module in sources}
    changed = 0
    for f in files:
        if not f.is_file() or f.suffix not in ('.java', '.xml', '.properties', '.md', '.txt', '.sh', '.yml', '.yaml', '.json', '.html'):
            continue
        try:
            text = read(f)
        except UnicodeDecodeError:
            continue
        updated = pattern.sub(lambda m: changed_classes[m[0]], text)
        if f in source_map:
            _, package, new, module = source_map[f]
            updated = re.sub(r'(?m)^package ' + re.escape(package) + r'\s*;', 'package ' + new + ';', updated)
            imports = set()
            # A move loses implicit imports from the old package. Also handle
            # references to moved siblings in a package that stayed in place.
            candidates = list(packages.get(package, []))
            for old, target in changed_classes.items():
                pkg, name = old.rsplit('.', 1)
                if 'import ' + pkg + '.*;' in updated:
                    candidates.append((name, target))
            for name, fqcn in candidates:
                target_package = fqcn.rsplit('.', 1)[0]
                if name == f.stem or target_package == new:
                    continue
                code = re.sub(r'/\*.*?\*/|//[^\r\n]*|"(?:\\.|[^"\\])*"', '', updated, flags=re.S)
                if not re.search(r'\b' + re.escape(name) + r'\b', code):
                    continue
                if re.search(r'^import [\w.]+\.' + re.escape(name) + r';', updated, re.M):
                    continue
                if 'import ' + target_package + '.*;' not in updated:
                    imports.add('import ' + fqcn + ';')
            if imports:
                declaration = 'package ' + new + ';'
                updated = updated.replace(declaration, declaration + '\n\n' + '\n'.join(sorted(imports)), 1)
        if updated != text:
            # Keep upstream line endings to avoid unrelated merge conflicts.
            if '\r\n' in text:
                updated = updated.replace('\r\n', '\n').replace('\n', '\r\n')
            f.write_bytes(updated.encode('utf-8'))
            changed += 1
        if f in source_map:
            _, package, new, _ = source_map[f]
            if package != new:
                base = Path(str(f).split('/java/')[0] + '/java')
                dest = base / new.replace('.', '/') / f.name
                dest.parent.mkdir(parents=True, exist_ok=True)
                f.rename(dest)
    print(f'Updated {changed} files')


if __name__ == '__main__':
    apply()
