package file;

import com.sun.jna.platform.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IterateTest {
    public static final String target = "D:\\MediaLib\\数一数二\\油管";

    public static final List<String> deleteSuffixList = Arrays.asList("webm", "webp");

    public static final List<String> keepSuffixList = Arrays.asList("mp4", "opus");

    @Test
    void outputAllFileName() throws IOException {
        // todo 逻辑大大的有问题，会误删
        Files.walkFileTree(new File(target).toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String fileNameString = file.getFileName().toString();
                String nameBeforeDot = fileNameString.substring(0, fileNameString.lastIndexOf("."));
                String suffix = fileNameString.substring((fileNameString.lastIndexOf(".") + 1));
                deleteSuffixList.forEach(deleteSuffix -> {
                    if (suffix.equals(deleteSuffix)) {
                        try {
                            FileUtils.getInstance().moveToTrash(file.toFile());
                            System.out.println("已删除文件：" + file.getFileName());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                AtomicInteger count = new AtomicInteger();
                keepSuffixList.forEach(keepSuffix -> {
                    File keepSuffixWithNameBeforeDot = new File(nameBeforeDot + "." + keepSuffix);
                    if (Files.notExists(keepSuffixWithNameBeforeDot.toPath())) {
                        count.addAndGet(1);
                    }
                    if (count.get() == keepSuffixList.size()) {
                        try {
                            FileUtils.getInstance().moveToTrash(file.toFile());
                            System.out.println("已删除文件：" + file.getFileName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        count.set(0);
                    }
                });
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
