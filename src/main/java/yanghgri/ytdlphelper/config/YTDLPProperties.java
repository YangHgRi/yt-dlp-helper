package yanghgri.ytdlphelper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "yt-dlp")
public class YTDLPProperties {
    private String programLocation;
    private String configLocation;
    private List<String> keyList;
    private Map<String, String> urlList;
    private Map<String, String> workDir;
}