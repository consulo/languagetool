// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.tl {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.tl;
    exports org.languagetool.language.tokenizers;
    exports org.languagetool.tagging.tl;
}
