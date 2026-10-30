#!/usr/bin/env python3
"""Reapply the fork's Maven coordinates without changing upstream dictionary jars."""
from pathlib import Path
import re
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
GROUP = 'consulo.internal.org.languagetool'
NS = {'m': 'http://maven.apache.org/POM/4.0.0'}


def migrate():
    poms = [pom for pom in ROOT.glob('**/pom.xml')
            if not any(part in ('target', 'src') for part in pom.relative_to(ROOT).parts)]
    artifacts = {ET.parse(pom).findtext('m:artifactId', namespaces=NS) for pom in poms}
    changed = 0
    for pom in poms:
        original = pom.read_bytes().decode('utf-8')
        updated = re.sub(
            r'(<groupId>)org\.languagetool(</groupId>\s*<artifactId>)([^<]+)(</artifactId>)',
            lambda match: match[1] + (GROUP if match[3] in artifacts else 'org.languagetool')
            + match[2] + match[3] + match[4], original)
        if updated != original:
            pom.write_bytes(updated.encode('utf-8'))
            changed += 1
    for directory in ('languagetool-standalone', 'languagetool-wikipedia'):
        descriptor = ROOT / directory / 'src/main/assembly/zip.xml'
        original = descriptor.read_bytes().decode('utf-8')
        updated = original.replace('org.languagetool:', '${project.groupId}:')
        updated = updated.replace(GROUP + ':', '${project.groupId}:')
        if updated != original:
            descriptor.write_bytes(updated.encode('utf-8'))
            changed += 1
    print(f'Updated Maven coordinates in {changed} files')


if __name__ == '__main__':
    migrate()
