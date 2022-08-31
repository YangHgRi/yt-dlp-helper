package yanghgri.service;

import com.sun.jna.platform.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FileCleaner {
    public FileCleaner(List<String> targetList, List<String> legalSuffixList, List<String> illegalSuffixList) {
        this.targetList = targetList;
        this.legalSuffixList = legalSuffixList;
        this.illegalSuffixList = illegalSuffixList;
    }

    public final List<String> targetList;

    public final List<String> legalSuffixList;

    public final List<String> illegalSuffixList;

    public void clean() {
        targetList.forEach(target -> {
            try {
                Files.walkFileTree(new File(target).toPath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        String absolutePath = file.toAbsolutePath().toString();
                        String prefixByAbsolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("."));
                        String suffix = absolutePath.substring((absolutePath.lastIndexOf(".") + 1));
                        boolean skip = deleteByIllegalSuffixList(file, suffix, absolutePath);
                        if (!skip) {
                            deleteByLegalSuffixList(file, prefixByAbsolutePath, absolutePath, suffix);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean deleteByIllegalSuffixList(Path path, String suffix, String absolutePath) {
        AtomicBoolean skip = new AtomicBoolean(false);
        illegalSuffixList.forEach(illegalSuffix -> {
            if (suffix.equals(illegalSuffix)) {
                try {
                    FileUtils.getInstance().moveToTrash(path.toFile());
                } catch (IOException e) {
                    System.out.println("删除失败，不存在的文件：" + absolutePath);
                }
                System.out.println("已删除文件：" + absolutePath);
                skip.set(true);
            }
        });
        return skip.get();
    }

    public boolean deleteByLegalSuffixList(Path path, String prefixByAbsolutePath, String absolutePath, String suffix) {
        AtomicBoolean skip = new AtomicBoolean(false);
        AtomicInteger count = new AtomicInteger();
        if (suffix.equals("bif")) {
            prefixByAbsolutePath = prefixByAbsolutePath.substring(0, prefixByAbsolutePath.lastIndexOf("-320-10"));
        } else if (suffix.equals("ass")) {
            prefixByAbsolutePath = prefixByAbsolutePath.substring(0, prefixByAbsolutePath.lastIndexOf("."));
        }
        String finalPrefixByAbsolutePath = prefixByAbsolutePath;
        legalSuffixList.forEach(legalSuffix -> {
            if (!suffix.equals(legalSuffix)) {
                if (Files.notExists(Paths.get(finalPrefixByAbsolutePath + "." + legalSuffix))) {
                    count.incrementAndGet();
                }
                if (count.get() == legalSuffixList.size()) {
                    try {
                        FileUtils.getInstance().moveToTrash(path.toFile());
                    } catch (IOException e) {
                        System.out.println("删除失败，不存在的文件：" + absolutePath);
                    }
                    System.out.println("已删除文件：" + absolutePath);
                    skip.set(true);
                }
            }
        });
        return skip.get();
    }
}