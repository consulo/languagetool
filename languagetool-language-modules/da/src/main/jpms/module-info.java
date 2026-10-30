// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.da {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.da;
    exports org.languagetool.tagging.da;
}
