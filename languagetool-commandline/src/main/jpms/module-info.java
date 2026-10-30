// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.commandline {
    requires ch.qos.logback.classic;
    requires java.xml;
    requires org.apache.commons.io;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;
    requires transitive org.languagetool.language.all;
    requires org.slf4j;

    exports org.languagetool.commandline;
}
