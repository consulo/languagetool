# JPMS adaptation

Every Maven jar project has a descriptor in `src/main/jpms/module-info.java`.
Maven compiles the existing sources on the classpath and ModiTect adds the
descriptor to the thin jar at `package`. `ModulePackages.java` records only
Java class packages in the descriptor: shared resource directories must not be
inferred as split Java packages. This preserves upstream's compiler,
test setup, and classpath launchers. Descriptors use open modules because
LanguageTool discovers classes reflectively and loads resources across modules.
All existing public packages remain exported. Language modules require core
transitively, and `org.languagetool.language.all` requires the supported languages.

## Build and verify

Use JDK 17 or 21, Maven, and Python 3.9+. Set `JAVA_HOME` to select the JDK
for Maven and the verification helpers. The inherited Lombok version does not
support compiling the project on JDK 25. From the repository root:

```sh
python3 tools/jpms/refresh.py
```

This reapplies package relocations, builds and installs the reactor, collects
compile and runtime dependencies, refreshes descriptors, and builds again.
It then checks source package ownership and language metadata, compiles every
built descriptor with `javac --release 17`, validates the entire module graph,
and runs English, German and French grammar checks, a translated bundle check,
and a basic check with every registered language.
The bootstrap compiles tests without running them; the final install runs the
full upstream test suite and fails if any test fails.

The resulting module path is `target/jpms/lib`. For example:

```sh
java --module-path target/jpms/lib --add-modules org.languagetool.language.all tools/jpms/Smoke.java
```

Use these thin jars and prepared dependencies together for modular deployment.
The regular distribution and classpath launchers still use the ordinary Maven
dependencies. `mvn package` alone adds the descriptors but does not prepare the
third-party module path.

## Third-party compatibility

The existing dependency graph cannot be placed directly on a module path:
several jars split Java packages, the trie artifact's filename contains the Java
keyword `double`, and some explicit dependency descriptors require artifacts
excluded by the upstream POMs.

`prepare.py` writes compatibility copies under `target/jpms/lib`; it never edits
sources or the Maven cache. It combines jars sharing packages into one automatic
module, preserves classpath precedence for duplicate files, and combines service
registrations. JAXB's two implementation generations are kept together because
they share bundled helpers. The annotation group uses `org.jetbrains.annotations`,
Lucene uses `lucene.core`, JAXB uses `org.languagetool.jaxb`, and the trie uses
`com.hankcs.aho.corasick`. A dependency whose explicit descriptor requires a
missing or combined module gets an automatic descriptor with its original name.
Other dependencies are copied unchanged. `target/jpms/groups.json` records every
input jar, so dependency updates are reviewable.

## Upstream merges

Keep the relocation table in `relocate.py` small. Core packages stay unchanged;
only conflicting packages move. Language implementations use
`org.languagetool.language.<code>`, and shared chunking, synthesis, tokenizer and
Arabic helper packages get the same language suffix. The Malayalam spelling
package moves below `org.languagetool.rules.ml.spelling` to avoid core's generated
ML protocol classes. GUI commons, dictionary tools, and developer utilities use
artifact-specific packages. Resources retain their upstream paths.

`relocate.py` updates package declarations, imports, fully qualified references,
and language registration properties while preserving line endings. It is
idempotent and handles new upstream classes in the known conflicting packages.
The core `TokenPredicateTest` lives in core because it tests a package-private
class. A few shared GUI methods and suggestion configuration types are public
because their callers now live in another package.

After a merge, run `refresh.py` and review the relocation and descriptor diffs.
New split packages fail verification and need an explicit relocation decision.
The resource broker uses the class loader for cross-module resources, and the
premium-language matcher accepts the language package suffix.

The default reactor remains unchanged. Serbian still targets the archived 4.5
parent and is excluded upstream; RPM packaging is also excluded. Both have
descriptors but are not built by the validation command. The separate client
example now targets this fork's version and includes the descriptor packaging
step; build it manually after installing the reactor if needed.

## Maven coordinates

Fork artifacts use `consulo.internal.org.languagetool`; artifact IDs, Java
packages and JPMS module names retain their existing names. Dictionary jars
that are not built in this repository keep their upstream group IDs.
`coordinates.py`, called by `refresh.py`, reapplies this distinction after an
upstream merge. Assembly filters use `${project.groupId}`.
