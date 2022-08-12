package yanghgri.ytdlphelper.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Key {
    private String name;
    private String urlList;
    private String workDir;
}