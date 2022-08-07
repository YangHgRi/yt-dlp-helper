package yanghgri.ytdlphelper;

import yanghgri.ytdlphelper.service.ArgsParser;
import yanghgri.ytdlphelper.service.FileOperator;
import yanghgri.ytdlphelper.service.URLOperator;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Application {
    public static void main(String[] args) {
        //解析参数键值对
        Map<String, String> argsMap = ArgsParser.parse2Map(args);
        //参数初始化和校验
        String mode = argsMap.get("m");
        if (mode == null) {
            throw new IllegalArgumentException("参数m不能为空，请以m=xxx格式填入，参数分隔符是空格！");
        }
        File file;
        try {
            file = new File(argsMap.get("p"));
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("参数p不能为空，请以p=xxx格式填入，参数分隔符是空格！");
        }
        //按照m值决定执行模式
        if (mode.equals("ext") || mode.equals("extract")) {
            List<String> extractResult = URLOperator.extract(file);
            FileOperator.writeNewFileContent(file, extractResult);
            System.out.println("YT-DLP-Helper: 链接提取成功！");
            System.out.println(String.join(", ", extractResult));
            System.out.println("\n");
        } else if (mode.equals("del") || mode.equals("delete")) {
            FileOperator.deleteAll(file);
            System.out.println("\n");
            System.out.println("YT-DLP-Helper: 已下载链接清除成功！");
        } else {
            throw new IllegalArgumentException("参数m目前只支持ext（extract）或del（delete）！");
        }
    }
}