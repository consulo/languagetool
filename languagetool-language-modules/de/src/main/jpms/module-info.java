// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.de {
    requires com.google.common;
    requires transitive com.hankcs.aho.corasick;
    requires german.pos.dict;
    requires indriya;
    requires it.unimi.dsi.fastutil.core;
    requires jwordsplitter;
    requires openregex;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires org.carrot2.morfologik.speller;
    requires org.carrot2.morfologik.stemming;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;
    requires org.slf4j;
    requires unit.api;

    exports org.languagetool.chunking.de;
    exports org.languagetool.language.de;
    exports org.languagetool.rules.de;
    exports org.languagetool.synthesis.de;
    exports org.languagetool.tagging.de;
    exports org.languagetool.tagging.disambiguation.rules.de;
    exports org.languagetool.tokenizers.de;
}
