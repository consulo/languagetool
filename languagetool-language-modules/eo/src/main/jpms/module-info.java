// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.eo {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.eo;
    exports org.languagetool.rules.eo;
    exports org.languagetool.tagging.eo;
    exports org.languagetool.tokenizers.eo;
}
