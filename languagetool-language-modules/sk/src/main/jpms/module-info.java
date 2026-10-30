// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.sk {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.sk;
    exports org.languagetool.rules.sk;
    exports org.languagetool.synthesis.sk;
    exports org.languagetool.tagging.sk;
}
