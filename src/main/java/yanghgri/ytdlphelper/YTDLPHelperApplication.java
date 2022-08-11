package yanghgri.ytdlphelper;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import yanghgri.ytdlphelper.config.YTDLPProperties;
import yanghgri.ytdlphelper.enums.URLListType;
import yanghgri.ytdlphelper.model.Resource;
import yanghgri.ytdlphelper.service.FileOperator;
import yanghgri.ytdlphelper.service.URLOperator;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@ConfigurationPropertiesScan
@Data
public class YTDLPHelperApplication {
    @Value("${m:#{null}}")
    private String mode;
    @Value("${p:#{null}}")
    private String filePath;
    @Value("${t:#{null}}")
    private String targetListIndex;
    @Value("${url:#{null}}")
    private String originalURL;
    @Value("${listID:#{null}}")
    private String playListID;
    @Value("${listCount:#{null}}")
    private String playListCount;
    @Value("${listIndex:#{null}}")
    private String playListIndex;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(YTDLPHelperApplication.class, args);
        YTDLPHelperApplication application = applicationContext.getBean(YTDLPHelperApplication.class);
        YTDLPProperties properties = applicationContext.getBean(YTDLPProperties.class);

        Resource resource = new Resource(application.getMode(), null, application.getOriginalURL(), application.getPlayListID(), application.getPlayListCount(), application.getPlayListIndex());
        String filePath = application.getFilePath();
        if (StringUtils.isNotEmpty(filePath)) {
            resource.setFilePath(new File(filePath));
        }

        application.validate(resource);

        application.operateURLListFileByMode(resource, application, properties);
    }

    public static boolean isInPlayList(String playlistID, String playlistCount, String playlistIndex) {
        return playlistID != null && playlistCount != null && playlistIndex != null;
    }

    public static boolean isLastInPlayList(String playlistCount, String playlistIndex) {
        return playlistCount.equals(playlistIndex);
    }

    public static String getMapKeyByListOrdinal(String ordinal) {
        //按照输入参数对应枚举次序获取枚举对象，e.g：1 -> YOUTUBE
        URLListType listType = Arrays.stream(URLListType.values()).filter(urlListType -> urlListType.ordinal() == (Integer.parseInt(ordinal)) - 1).collect(Collectors.toList()).get(0);
        if (listType == null) {
            throw new IllegalArgumentException("参数t不能为空或超出给定范围，请以--t=xxx格式输入，参数分隔符是空格！");
        }
        //获取枚举名,转为小写，YOUTUBE -> youtube
        return listType.getName().toLowerCase();
    }

    public static String getListPathByOrdinal(String ordinal, YTDLPProperties properties) {
        //获取配置文件中与youtube对应配置项值，获得file path
        return properties.getUrlList().get(getMapKeyByListOrdinal(ordinal));
    }

    public static String getWorkDirByOrdinal(String ordinal, YTDLPProperties properties) {
        return properties.getWorkDir().get(getMapKeyByListOrdinal(ordinal));
    }

    public void operateURLListFileByMode(Resource resource, YTDLPHelperApplication application, YTDLPProperties properties) {
        File file = resource.getFilePath();
        String originalURL = resource.getOriginalURL();
        String playListID = resource.getPlayListID();
        String playListCount = resource.getPlayListCount();
        String playListIndex = resource.getPlayListIndex();
        //按照m值决定执行模式
        switch (resource.getMode()) {
            case "ext":
                List<String> extractResult = URLOperator.extract(file);
                FileOperator.writeByStringList(file, extractResult);
                System.out.println("\nYT-DLP-Helper: 链接提取成功！\n");
                System.out.println(String.join("\n", extractResult) + "\n");
                break;
            case "getPath":
                System.out.println(getListPathByOrdinal(application.getTargetListIndex(), properties));
                break;
            case "getWorkDir":
                System.out.println(getWorkDirByOrdinal(application.getTargetListIndex(), properties));
                break;
            case "getProgram":
                System.out.println(properties.getProgramLocation());
                break;
            case "getConfig":
                System.out.println(properties.getConfigLocation());
                break;
            case "delAll":
                FileOperator.deleteAll(file);
                System.out.println("\nYT-DLP-Helper: 全部链接清除成功！");
                break;
            case "delFirst":
                if (isInPlayList(playListID, playListCount, playListIndex)) {
                    if (isLastInPlayList(playListCount, playListIndex)) {
                        FileOperator.deleteFirstLine(file);
                        System.out.println("\nYT-DLP-Helper: 已删除首行链接！\n");
                    }
                } else {
                    FileOperator.deleteFirstLine(file);
                    System.out.println("\nYT-DLP-Helper: 已删除首行链接！\n");
                }
                break;
            case "delURL":
                if (isInPlayList(playListID, playListCount, playListIndex)) {
                    if (isLastInPlayList(playListCount, playListIndex)) {
                        FileOperator.deleteByURL(file, playListID);
                        System.out.println("\nYT-DLP-Helper: 已删除ID为 " + playListID + " 的合集的链接！\n");
                    }
                } else {
                    FileOperator.deleteByURL(file, originalURL);
                    System.out.println("\nYT-DLP-Helper: 已删除 " + originalURL + " 链接！\n");
                }
                break;
            default:
                throw new IllegalArgumentException("参数m目前只支持ext/delAll/delFirst/delURL！");
        }
    }

    public void validate(Resource resource) {
        //参数初始化和校验
        String mode = resource.getMode();
        if (StringUtils.isEmpty(mode)) {
            throw new IllegalArgumentException("参数m不能为空，请以--m=xxx格式输入，参数分隔符是空格！");
        }
        //yt-dlp当参数为空时，输出模板默认以"NA"作为占位符传入，很阴险啊
        final String placeholder = "NA";
        //合集ID,是合集链接中的一部分
        String playlistID = resource.getPlayListID();
        if (placeholder.equals(playlistID)) {
            resource.setPlayListID(null);
        }
        //合集视频数
        String playlistCount = resource.getPlayListCount();
        if (placeholder.equals(playlistCount)) {
            resource.setPlayListCount(null);
        }
        //当前视频在合集中索引
        String playlistIndex = resource.getPlayListIndex();
        if (placeholder.equals(playlistIndex)) {
            resource.setPlayListIndex(null);
        }
    }
}