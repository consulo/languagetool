// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.gl {
    requires transitive org.carrot2.morfologik.stemming;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.gl;
    exports org.languagetool.rules.gl;
    exports org.languagetool.synthesis.gl;
    exports org.languagetool.tagging.disambiguation.gl;
    exports org.languagetool.tagging.gl;
    exports org.languagetool.tokenizers.gl;
}
