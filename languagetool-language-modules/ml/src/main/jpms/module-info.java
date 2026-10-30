// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.ml {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.ml;
    exports org.languagetool.rules.ml.spelling;
    exports org.languagetool.tagging.ml;
    exports org.languagetool.tokenizers.ml;
}
