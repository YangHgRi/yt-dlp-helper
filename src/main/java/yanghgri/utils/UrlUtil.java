package yanghgri.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UrlUtil {
    private static final Pattern regexPattern = Pattern.compile("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()!@:%_+.~#?&/=]*)");

    /**
     * 读取文件内容，提取其中的url，组成url集合，返回给调用方
     *
     * @param path 路径
     */
    public static List<String> extract(File path) {
        List<String> originalFileContent = FileUtil.readAsStringList(path);

        List<String> newFileContent = new ArrayList<>();
        //模式判断，提取url
        originalFileContent.forEach(line -> {
            Matcher matcher = regexPattern.matcher(line);

            while (matcher.find()) {
                String url = escape(matcher.group());
                newFileContent.add(url);
            }
        });
        //返回去重后的url
        return newFileContent.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 转义url中特殊字符
     *
     * @param target 目标
     * @return {@link String}
     */
    public static String escape(String target) {
        List<String> needToEscape = Stream.of("&").collect(Collectors.toList());
        AtomicReference<String> result = new AtomicReference<>(target);
        needToEscape.forEach(needEscape -> {
            for (int i = 0; i < StringUtils.countMatches(target, needEscape); i++) {
                int indexOfEscapeInOrdinal = StringUtils.ordinalIndexOf(result.get(), needEscape, i + 1);
                if (result.get().charAt(indexOfEscapeInOrdinal - 1) != '^') {
                    result.set(result.get().substring(0, indexOfEscapeInOrdinal) + "^" + needEscape + result.get().substring(indexOfEscapeInOrdinal + 1));
                }
            }
        });
        return result.get();
    }
}