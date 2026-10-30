import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.nio.file.*;
import java.util.stream.Collectors;

/** Read the JDK's effective module metadata, including multi-release jars. */
public class ModuleNames {
  private static java.util.Set<String> requirements(ModuleDescriptor descriptor) {
    return descriptor.requires().stream().map(r -> r.name() + ":" + r.modifiers())
      .collect(Collectors.toSet());
  }

  private static void compare(String file) throws Exception {
    for (String line : Files.readAllLines(Path.of(file))) {
      String[] paths = line.split("\\t");
      var actual = ModuleFinder.of(Path.of(paths[0])).findAll().iterator().next().descriptor();
      ModuleDescriptor expected;
      try (var input = Files.newInputStream(Path.of(paths[1]))) {
        expected = ModuleDescriptor.read(input);
      }
      if (!actual.name().equals(expected.name()) || actual.isOpen() != expected.isOpen() || actual.isAutomatic() != expected.isAutomatic()
          || !requirements(actual).equals(requirements(expected))
          || !actual.exports().equals(expected.exports()) || !actual.opens().equals(expected.opens())
          || !actual.uses().equals(expected.uses()) || !actual.provides().equals(expected.provides())
          || !actual.packages().equals(expected.packages())) {
        throw new IllegalStateException("Packaged descriptor differs from source: " + paths[0]);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 2 && args[0].equals("--compare")) {
      compare(args[1]);
      return;
    }
    for (String line : Files.readAllLines(Path.of(args[0]))) {
      try {
        var descriptor = ModuleFinder.of(Path.of(line)).findAll().iterator().next().descriptor();
        String requires = descriptor.requires().stream()
          .filter(r -> !r.modifiers().contains(ModuleDescriptor.Requires.Modifier.STATIC))
          .map(ModuleDescriptor.Requires::name).sorted().collect(Collectors.joining(","));
        System.out.println(line + "\t" + descriptor.name() + "\t"
          + String.join(",", descriptor.packages()) + "\t" + requires);
      } catch (Exception e) {
        System.out.println(line + "\tERROR\t" + e + "\t");
      }
    }
  }
}
