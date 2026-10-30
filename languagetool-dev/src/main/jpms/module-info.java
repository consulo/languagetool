// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.dev {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.google.common;
    requires java.desktop;
    requires java.xml;
    requires jwordsplitter;
    requires lucene.analyzers.common;
    requires lucene.core;
    requires transitive org.apache.commons.cli;
    requires org.apache.commons.compress;
    requires org.apache.commons.csv;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.carrot2.morfologik.fsa;
    requires org.carrot2.morfologik.stemming;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.commandline;
    requires transitive org.languagetool.language.all;
    requires transitive org.languagetool.wikipedia;
    requires org.mariadb.jdbc;
    requires org.tukaani.xz;

    exports org.languagetool.dev;
    exports org.languagetool.dev.archive;
    exports org.languagetool.dev.bigdata;
    exports org.languagetool.dev.diff;
    exports org.languagetool.dev.errorcorpus;
    exports org.languagetool.dev.eval;
    exports org.languagetool.dev.httpchecker;
    exports org.languagetool.dev.messagechecker;
    exports org.languagetool.dev.simulation;
    exports org.languagetool.dev.suggestions;
    exports org.languagetool.dev.wiktionary;
    exports org.languagetool.dev.wordsimilarity;
}
