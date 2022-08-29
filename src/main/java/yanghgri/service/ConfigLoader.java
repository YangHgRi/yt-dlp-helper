package yanghgri.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import yanghgri.Application;
import yanghgri.model.ConfigProperties;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class ConfigLoader {
    public String getCurrentJarPath() throws URISyntaxException {
        String configPath = new File(Application.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
        int firstIndex = configPath.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = configPath.lastIndexOf(File.separator) + 1;
        return configPath.substring(firstIndex, lastIndex);
    }


    public ConfigProperties load() throws URISyntaxException, IOException {
        String configPath = getCurrentJarPath() + "config.yaml";
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        return om.readValue(new File(configPath), ConfigProperties.class);
    }
}