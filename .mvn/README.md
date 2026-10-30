This directory marks the Maven multi-module root. The inherited JPMS packaging
step uses it to locate `tools/jpms`, including when building a child project
with `mvn -f path/to/pom.xml`.
