// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.ca {
    requires catalan.pos.dict;
    requires com.google.common;
    requires org.apache.commons.lang3;
    requires static org.jetbrains.annotations;
    requires org.json;
    requires transitive org.languagetool.core;
    requires org.slf4j;

    exports org.languagetool.language.ca;
    exports org.languagetool.rules.ca;
    exports org.languagetool.synthesis.ca;
    exports org.languagetool.tagging.ca;
    exports org.languagetool.tagging.disambiguation.ca;
    exports org.languagetool.tokenizers.ca;
}
