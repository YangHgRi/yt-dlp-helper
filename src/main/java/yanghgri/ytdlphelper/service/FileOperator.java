package yanghgri.ytdlphelper.service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileOperator {
    /**
     * 读取原始文件内容，组成字符串集合后返回
     *
     * @param path 路径
     * @return {@link List}<{@link String}>
     */
    public static List<String> readAsStringList(File path) {
        try (FileReader fileReader = new FileReader(path); BufferedReader reader = new BufferedReader(fileReader)) {
            List<String> originalURLList = new ArrayList<>();
            reader.lines().forEach(
                    originalURLList::add
            );
            return originalURLList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 清空文件，然后将集合内一个字符串元素对应一行的输入到文件中
     *
     * @param path       路径
     * @param newContent 新内容
     */
    public static void writeByStringList(File path, List<String> newContent) {
        try (FileWriter fileWriter = new FileWriter(path); BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(String.join("\n", newContent));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteAll(File path) {
        writeByStringList(path, Collections.emptyList());
    }

    public static void deleteFirstLine(File path) {
        List<String> stringList = readAsStringList(path);
        stringList.remove(0);
        try (FileWriter fileWriter = new FileWriter(path); BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(String.join("\n", stringList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteByURL(File path, String targetURL) {
        List<String> stringList = readAsStringList(path);
        stringList = stringList.stream().filter(s -> !s.contains(targetURL)).collect(Collectors.toList());
        writeByStringList(path, stringList);
    }
}