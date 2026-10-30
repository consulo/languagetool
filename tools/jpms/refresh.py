#!/usr/bin/env python3
"""Reapply the JPMS adaptation after merging upstream and validate the result."""
from pathlib import Path
import subprocess
import os
import sys

ROOT = Path(__file__).resolve().parents[2]


def run(*command):
    print('+ ' + ' '.join(command), flush=True)
    subprocess.run(command, cwd=ROOT, check=True)


def refresh():
    if os.environ.get('JAVA_HOME'):
        os.environ['PATH'] = str(Path(os.environ['JAVA_HOME']) / 'bin') + os.pathsep + os.environ.get('PATH', '')
    run(sys.executable, 'tools/jpms/relocate.py')
    options = ['mvn', '-B', '-Dassembly.skipAssembly=true']
    # Bootstrap on the classpath, including new modules with no descriptor yet.
    run(*options, '-DskipTests', '-Dmoditect.skip=true', 'install', 'dependency:build-classpath',
        '-DincludeScope=compile', '-Dmdep.outputFile=target/jpms-classpath')
    run(*options, 'dependency:build-classpath', '-DincludeScope=runtime',
        '-Dmdep.outputFile=target/jpms-runtime-classpath')
    run(sys.executable, 'tools/jpms/prepare.py')
    run(sys.executable, 'tools/jpms/descriptors.py')
    run(*options, 'install')
    run(sys.executable, 'tools/jpms/verify.py')
    run('java', '--module-path', 'target/jpms/lib', '--add-modules',
        'org.languagetool.language.all', 'tools/jpms/Smoke.java', '--all')


if __name__ == '__main__':
    refresh()
