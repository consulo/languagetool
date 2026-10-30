The two Java sources are unmodified AhoCorasickDoubleArrayTrie 1.2.2 sources
from the published sources jar. Attribution and Apache 2.0 headers are retained.
This ordinary Maven module adds a JPMS descriptor; it does not relocate packages
or combine dependency jars. The upstream jar has no descriptor and its filename
contains the Java keyword `double`, which prevents automatic module naming.

The full Apache License 2.0 text is in [LICENSE](LICENSE); attribution and
source provenance are in [NOTICE](NOTICE). Both are packaged under `META-INF/`
in the library JAR. The original copyright headers remain in both Java files.
