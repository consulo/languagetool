// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.en {
    requires transitive com.google.common;
    requires transitive com.hankcs.aho.corasick;
    requires english.pos.dict;
    requires indriya;
    requires it.unimi.dsi.fastutil;
    requires opennlp.chunk.models;
    requires opennlp.postag.models;
    requires opennlp.tokenize.models;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires org.apache.opennlp.tools;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;
    requires org.slf4j;
    requires unit.api;

    exports org.languagetool.chunking.en;
    exports org.languagetool.language.en;
    exports org.languagetool.rules.en;
    exports org.languagetool.rules.en.translation;
    exports org.languagetool.synthesis.en;
    exports org.languagetool.tagging.en;
    exports org.languagetool.tokenizers.en;
}
