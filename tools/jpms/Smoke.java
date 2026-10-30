import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import java.util.Locale;

/** Exercise discovery, translated bundles, SRX/JAXB and rule resources in named modules. */
public class Smoke {
  public static void main(String[] args) throws Exception {
    if (!JLanguageTool.class.getModule().isNamed()) {
      throw new AssertionError("LanguageTool must run as a named module");
    }
    var languages = Languages.get();
    if (languages.size() < 30) {
      throw new AssertionError("Language discovery returned only " + languages.size() + " languages");
    }
    String[] codes = {"en-US", "de-DE", "fr"};
    String[] sentences = {"This are a test.", "Das ist ein Fehler Fehler.", "Ceci est un test test."};
    for (int i = 0; i < codes.length; i++) {
      var tool = new JLanguageTool(Languages.getLanguageForShortCode(codes[i]));
      int matches = tool.check(sentences[i]).size();
      if (matches == 0) {
        throw new AssertionError("No matches for " + codes[i]);
      }
      System.out.println(codes[i] + ": " + matches + " matches");
    }
    var bundle = JLanguageTool.getDataBroker().getResourceBundle("org.languagetool.MessagesBundle", Locale.GERMAN);
    if (!"Sonstiges".equals(bundle.getString("category_misc"))) {
      throw new AssertionError("German message bundle was not loaded");
    }
    if (java.util.Arrays.asList(args).contains("--all")) {
      for (var language : languages) {
        var tool = new JLanguageTool(language);
        tool.check("And the the.");
        System.out.println("Checked " + language.getShortCodeWithCountryAndVariant());
      }
    }
    System.out.println("Discovered " + languages.size() + " languages; translated bundles and grammar checks passed");
  }
}
