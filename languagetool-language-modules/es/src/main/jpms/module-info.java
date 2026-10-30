// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.es {
    requires org.apache.commons.lang3;
    requires transitive org.carrot2.morfologik.stemming;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;
    requires spanish.pos.dict;

    exports org.languagetool.language.es;
    exports org.languagetool.rules.es;
    exports org.languagetool.synthesis.es;
    exports org.languagetool.tagging.disambiguation.es;
    exports org.languagetool.tagging.es;
    exports org.languagetool.tokenizers.es;
}
