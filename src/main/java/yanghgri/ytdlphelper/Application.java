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
        //获取资源url
        String url = argsMap.get("url");
        //合集ID,是合集链接中的一部分
        String playlistID = argsMap.get("playListID");
        if (playlistID != null && playlistID.equals("NA")) {
            playlistID = null;
        }
        //合集视频数
        String playlistCount = argsMap.get("playlistCount");
        if (playlistCount != null && playlistCount.equals("NA")) {
            playlistCount = null;
        }
        //当前视频在和集中索引
        String playlistIndex = argsMap.get("playlistIndex");
        if (playlistIndex != null && playlistIndex.equals("NA")) {
            playlistIndex = null;
        }

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
        switch (mode) {
            case "ext":
            case "extract":
                List<String> extractResult = URLOperator.extract(file);
                FileOperator.writeByStringList(file, extractResult);
                System.out.println("\nYT-DLP-Helper: 链接提取成功！\n");
                System.out.println(String.join("\n", extractResult) + "\n");
                break;
            case "delAll":
            case "deleteAll":
                FileOperator.deleteAll(file);
                System.out.println("\nYT-DLP-Helper: 全部链接清除成功！");
                break;
            case "delFirst":
            case "deleteFirst":
                if (isPlayList(playlistID, playlistCount, playlistIndex)) {
                    if (isLastInPlayList(playlistCount, playlistIndex)) {
                        FileOperator.deleteFirstLine(file);
                        System.out.println("\nYT-DLP-Helper: 已删除首行链接！\n");
                    }
                } else {
                    FileOperator.deleteFirstLine(file);
                    System.out.println("\nYT-DLP-Helper: 已删除首行链接！\n");
                }
                break;
            case "delURL":
            case "deleteURL":
                if (isPlayList(playlistID, playlistCount, playlistIndex)) {
                    if (isLastInPlayList(playlistCount, playlistIndex)) {
                        FileOperator.deleteByURL(file, playlistID);
                        System.out.println("\nYT-DLP-Helper: 已删除ID为 " + playlistID + " 的合集的链接！\n");
                    }
                } else {
                    FileOperator.deleteByURL(file, url);
                    System.out.println("\nYT-DLP-Helper: 已删除 " + url + " 链接！\n");
                }
                break;
            default:
                throw new IllegalArgumentException("参数m目前只支持ext（extract）或del（delete）！");
        }
    }

    public static boolean isPlayList(String playlistID, String playlistCount, String playlistIndex) {
        return playlistID != null && playlistCount != null && playlistIndex != null;
    }

    public static boolean isLastInPlayList(String playlistCount, String playlistIndex) {
        return playlistCount.equals(playlistIndex);
    }
}