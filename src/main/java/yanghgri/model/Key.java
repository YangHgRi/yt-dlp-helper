package yanghgri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author YangHgRi
 */
@Data
public class Key {
    @JsonProperty
    private String name;
    @JsonProperty
    private String urlListPath;
    @JsonProperty
    private String workDir;
}