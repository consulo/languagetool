// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.standalone {
    requires ch.qos.logback.classic;
    requires java.datatransfer;
    requires transitive java.desktop;
    requires junit;
    requires lucene.core;
    requires org.apache.commons.collections4;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.commandline;
    requires transitive org.languagetool.core;
    requires transitive org.languagetool.gui.commons;
    requires transitive org.languagetool.language.all;
    requires transitive org.languagetool.server;
    requires transitive org.languagetool.tools;
    requires org.slf4j;

    exports org.languagetool.dev.standalone;
    exports org.languagetool.gui;
}
