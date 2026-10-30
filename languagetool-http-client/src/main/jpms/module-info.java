// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.http.client {
    requires com.fasterxml.jackson.databind;
    requires java.xml;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.server;

    exports org.languagetool.remote;
    exports org.languagetool.remote.multiLang;
}
