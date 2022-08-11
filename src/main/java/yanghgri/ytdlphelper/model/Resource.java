package yanghgri.ytdlphelper.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class Resource {
    private  String mode;
    private  File filePath;
    private  String originalURL;
    private  String playListID;
    private  String playListCount;
    private  String playListIndex;
}