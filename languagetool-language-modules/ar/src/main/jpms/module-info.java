// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.ar {
    requires org.apache.commons.lang3;
    requires transitive org.carrot2.morfologik.stemming;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;
    requires org.slf4j;

    exports org.languagetool.language.ar;
    exports org.languagetool.rules.ar;
    exports org.languagetool.rules.ar.filters;
    exports org.languagetool.synthesis.ar;
    exports org.languagetool.tagging.ar;
    exports org.languagetool.tokenizers.ar;
    exports org.languagetool.tools.ar;
}
