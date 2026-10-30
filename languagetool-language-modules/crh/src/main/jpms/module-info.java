// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.crh {
    requires morfologik.crh.lt;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.crh;
    exports org.languagetool.rules.crh;
    exports org.languagetool.synthesis.crh;
    exports org.languagetool.tagging.crh;
    exports org.languagetool.tokenizers.crh;
}
