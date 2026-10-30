// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.ro {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.ro;
    exports org.languagetool.rules.ro;
    exports org.languagetool.synthesis.ro;
    exports org.languagetool.tagging.ro;
    exports org.languagetool.tokenizers.ro;
}
