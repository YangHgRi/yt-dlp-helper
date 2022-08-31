package yanghgri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ConfigProperties {
    @JsonProperty
    private String configLocation;

    @JsonProperty
    private List<Key> keyList;

    @JsonProperty
    private List<String> legalSuffixList;

    @JsonProperty
    private List<String> illegalSuffixList;
}