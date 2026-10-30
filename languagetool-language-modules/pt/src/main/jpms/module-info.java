// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.pt {
    requires transitive com.hankcs.aho.corasick;
    requires indriya;
    requires org.apache.commons.lang3;
    requires org.carrot2.morfologik.stemming;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;
    requires portuguese.pos.dict;
    requires unit.api;

    exports org.languagetool.language.pt;
    exports org.languagetool.rules.pt;
    exports org.languagetool.synthesis.pt;
    exports org.languagetool.tagging.disambiguation.pt;
    exports org.languagetool.tagging.pt;
    exports org.languagetool.tokenizers.pt;
}
