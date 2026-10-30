// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.gui.commons {
    requires transitive java.desktop;
    requires org.apache.commons.lang3;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.gui.commons;
}
