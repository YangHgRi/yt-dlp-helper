package yanghgri.ytdlphelper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "yt-dlp")
public class YTDLPProperties {
    private String configLocation;
    private List<String> keyList;
}