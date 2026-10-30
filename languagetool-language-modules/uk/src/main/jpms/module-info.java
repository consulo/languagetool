// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.uk {
    requires morfologik.ukrainian.lt;
    requires org.apache.commons.lang3;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;
    requires org.slf4j;

    exports org.languagetool.language.uk;
    exports org.languagetool.rules.uk;
    exports org.languagetool.synthesis.uk;
    exports org.languagetool.tagging.disambiguation.uk;
    exports org.languagetool.tagging.uk;
    exports org.languagetool.tokenizers.uk;
}
