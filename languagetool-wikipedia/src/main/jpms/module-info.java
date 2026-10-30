// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.wikipedia {
    requires ch.qos.logback.classic;
    requires java.sql;
    requires transitive java.xml;
    requires lucene.analyzers.common;
    requires transitive lucene.core;
    requires lucene.queries;
    requires org.apache.commons.cli;
    requires org.apache.commons.codec;
    requires org.apache.commons.compress;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;
    requires transitive org.languagetool.language.all;
    requires org.tukaani.xz;
    requires transitive ptk.common;
    requires transitive rats.runtime;
    requires swc.engine;
    requires swc.parser.lazy;

    exports org.languagetool.dev.dumpcheck;
    exports org.languagetool.dev.index;
    exports org.languagetool.dev.wikipedia;
}
