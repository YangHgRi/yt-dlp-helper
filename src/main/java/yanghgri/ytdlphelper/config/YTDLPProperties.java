package yanghgri.ytdlphelper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "yt-dlp")
public class YTDLPProperties {
    private String programLocation;
    private String configLocation;
    private Map<String, String> urlList;
}