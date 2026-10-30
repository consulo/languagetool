import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;

/**
 * Record only Java class packages in ModulePackages. Without this optional
 * class-file attribute the JDK infers packages from resources as well, turning
 * shared MessagesBundle directories into split packages. Uses the documented
 * class-file format, without depending on internal JDK APIs or a bytecode library.
 */
public class ModulePackages {
  private static int u2(byte[] data, int offset) {
    return ((data[offset] & 255) << 8) | (data[offset + 1] & 255);
  }

  public static byte[] patch(byte[] data, SortedSet<String> packages) throws IOException {
    Map<String, Integer> utf8 = new HashMap<>();
    Map<String, Integer> packageConstants = new HashMap<>();
    Map<Integer, String> strings = new HashMap<>();
    Map<Integer, Integer> packageNames = new HashMap<>();
    int count = u2(data, 8);
    int offset = 10;
    for (int index = 1; index < count; index++) {
      int tag = data[offset++] & 255;
      switch (tag) {
        case 1: {
          int length = u2(data, offset);
          String value;
          try (var input = new DataInputStream(new ByteArrayInputStream(data, offset, length + 2))) {
            value = input.readUTF();
          }
          utf8.put(value, index);
          strings.put(index, value);
          offset += length + 2;
          break;
        }
        case 20:
          packageNames.put(index, u2(data, offset));
          offset += 2;
          break;
        case 7: case 8: case 16: case 19:
          offset += 2;
          break;
        case 3: case 4: case 9: case 10: case 11: case 12: case 17: case 18:
          offset += 4;
          break;
        case 5: case 6:
          offset += 8;
          index++;
          break;
        case 15:
          offset += 3;
          break;
        default:
          throw new IOException("Unknown constant pool tag: " + tag);
      }
    }
    int constantPoolEnd = offset;
    for (var entry : packageNames.entrySet()) {
      packageConstants.put(strings.get(entry.getValue()), entry.getKey());
    }
    var extraBytes = new ByteArrayOutputStream();
    var extra = new DataOutputStream(extraBytes);
    List<Integer> indices = new ArrayList<>();
    for (String name : packages) {
      Integer packageIndex = packageConstants.get(name);
      if (packageIndex == null) {
        Integer stringIndex = utf8.get(name);
        if (stringIndex == null) {
          stringIndex = count++;
          extra.writeByte(1);
          extra.writeUTF(name);
          utf8.put(name, stringIndex);
        }
        packageIndex = count++;
        extra.writeByte(20);
        extra.writeShort(stringIndex);
      }
      indices.add(packageIndex);
    }
    Integer attributeName = utf8.get("ModulePackages");
    if (attributeName == null) {
      attributeName = count++;
      extra.writeByte(1);
      extra.writeUTF("ModulePackages");
    }
    // module-info has no interfaces, fields, or methods (JVMS 4.1).
    offset += 6; // access_flags, this_class, super_class
    if (u2(data, offset) != 0 || u2(data, offset + 2) != 0 || u2(data, offset + 4) != 0) {
      throw new IOException("Expected a module-info class");
    }
    offset += 6;
    int attributesOffset = offset;
    int attributes = u2(data, offset);
    offset += 2;
    List<byte[]> kept = new ArrayList<>();
    for (int i = 0; i < attributes; i++) {
      int nameIndex = u2(data, offset);
      int length = ((data[offset + 2] & 255) << 24) | ((data[offset + 3] & 255) << 16)
        | ((data[offset + 4] & 255) << 8) | (data[offset + 5] & 255);
      if (!"ModulePackages".equals(strings.get(nameIndex))) {
        kept.add(Arrays.copyOfRange(data, offset, offset + 6 + length));
      }
      offset += 6 + length;
    }
    var bytes = new ByteArrayOutputStream();
    var output = new DataOutputStream(bytes);
    output.write(data, 0, 8);
    output.writeShort(count);
    output.write(data, 10, constantPoolEnd - 10);
    output.write(extraBytes.toByteArray());
    output.write(data, constantPoolEnd, attributesOffset - constantPoolEnd);
    output.writeShort(kept.size() + 1);
    for (byte[] attribute : kept) {
      output.write(attribute);
    }
    output.writeShort(attributeName);
    output.writeInt(2 + indices.size() * 2);
    output.writeShort(indices.size());
    for (int index : indices) {
      output.writeShort(index);
    }
    return bytes.toByteArray();
  }

  public static void main(String[] args) throws Exception {
    Path jar = Path.of(args[0]);
    if (!Files.exists(jar)) {
      return; // Parent POM projects have no jar.
    }
    SortedSet<String> packages = new TreeSet<>();
    byte[] descriptor;
    try (var input = new JarFile(jar.toFile())) {
      var moduleInfo = input.getJarEntry("module-info.class");
      if (moduleInfo == null) {
        return;
      }
      descriptor = input.getInputStream(moduleInfo).readAllBytes();
      var entries = input.entries();
      while (entries.hasMoreElements()) {
        String name = entries.nextElement().getName();
        if (name.startsWith("META-INF/versions/")) {
          name = name.replaceFirst("META-INF/versions/[0-9]+/", "");
        }
        if (name.endsWith(".class") && !name.equals("module-info.class") && name.contains("/")) {
          packages.add(name.substring(0, name.lastIndexOf('/')));
        }
      }
    }
    byte[] updated = patch(descriptor, packages);
    // The zip filesystem changes just the descriptor, preserving all resources.
    try (var archive = FileSystems.newFileSystem(jar)) {
      Files.write(archive.getPath("/module-info.class"), updated);
    }
  }
}
