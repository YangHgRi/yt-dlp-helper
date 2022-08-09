package yanghgri.ytdlphelper.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class URLOperator {
    private static final Pattern regexPattern = Pattern.compile("https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()!@:%_+.~#?&/=]*)");

    /**
     * 读取文件内容，提取其中的url，组成url集合，返回给调用方
     *
     * @param path 路径
     */
    public static List<String> extract(File path) {
        List<String> originalFileContent = FileOperator.readAsStringList(path);

        List<String> newFileContent = new ArrayList<>();
        //模式判断，提取url
        originalFileContent.forEach(line -> {
            Matcher matcher = regexPattern.matcher(line);

            while (matcher.find()) {
                newFileContent.add(matcher.group());
            }
        });
        //返回去重后的url
        return newFileContent.stream().distinct().collect(Collectors.toList());
    }
}