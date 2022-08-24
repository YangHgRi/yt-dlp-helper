package yanghgri.ytdlphelper;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import yanghgri.ytdlphelper.config.YTDLPProperties;
import yanghgri.ytdlphelper.model.Key;
import yanghgri.ytdlphelper.service.FileOperator;
import yanghgri.ytdlphelper.service.URLOperator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ConfigurationPropertiesScan
@Data
public class YTDLPHelperApplication {
    @Value("${m:#{null}}")
    private String mode;
    @Value("${p:#{null}}")
    private String urlList;
    @Value("${t:#{null}}")
    private String ordinal;

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(YTDLPHelperApplication.class, args);
        YTDLPHelperApplication application = applicationContext.getBean(YTDLPHelperApplication.class);
        YTDLPProperties properties = applicationContext.getBean(YTDLPProperties.class);

        String mode = application.getMode();
        if (StringUtils.isEmpty(mode)) {
            throw new IllegalArgumentException("参数m不能为空，请以--m=xxx格式输入，参数分隔符是空格！");
        }

        switch (mode) {
            case "keys":
                outputSupportKeysName(properties);
                break;
            case "init":
                String ordinal;
                if (StringUtils.startsWith(application.getOrdinal(), "D")) {
                    ordinal = application.getOrdinal().substring(1);
                    Key key = getKeyByOrdinal(ordinal, properties);
                    String urlList = key.getUrlList();
                    FileOperator.deleteAll(new File(urlList));
                    System.out.print(String.join("|", new String[]{"", "", "", ""}));
                } else {
                    ordinal = application.getOrdinal();
                    Key key = getKeyByOrdinal(ordinal, properties);
                    String audioOnly = checkAudioOnly(key.getName());
                    System.out.print(String.join("|", new String[]{key.getUrlList(), key.getWorkDir(), properties.getConfigLocation(), audioOnly}));
                }
                break;
            case "ext":
                File extTarget = new File(application.getUrlList());
                List<String> extractResult = URLOperator.extract(extTarget);
                FileOperator.writeByStringList(extTarget, extractResult);
                System.out.println("\n链接提取成功！\n");
                System.out.println(String.join("\n", extractResult) + "\n");
                break;
            default:
                throw new IllegalArgumentException("参数m目前只支持keys/init/del！");
        }
    }

    public static Key getKeyByOrdinal(String ordinalString, YTDLPProperties properties) {
        int ordinal;
        try {
            ordinal = Integer.parseInt(ordinalString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("参数t的值必须为数字且非空，请以--t=xxx格式输入，参数分隔符是空格！");
        }
        String[] keyUrlListWorkDir;
        try {
            keyUrlListWorkDir = properties.getKeyList().get(ordinal).split("\\|");
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("参数t不能超出给定范围，请以--t=xxx格式输入，参数分隔符是空格！");
        }

        String keyName = keyUrlListWorkDir[0];
        String urlList = keyUrlListWorkDir[1];
        String workDir = keyUrlListWorkDir[2];

        return new Key(keyName, urlList, workDir);
    }

    public static void outputSupportKeysName(YTDLPProperties properties) {
        final String tab = "\t";
        List<String> keyList = properties.getKeyList();
        List<String> keyNameList = new ArrayList<>();
        keyList.forEach(key -> keyNameList.add(key.split("\\|")[0]));
        System.out.println("\n当前支持关键字与其序号：");
        for (int i = 0; i < keyList.size(); i++) {
            System.out.println(tab + i + " -> " + keyNameList.get(i));
        }
        System.out.print("目标关键字序号：");
    }

    public static String checkAudioOnly(String keyName) {
        return StringUtils.endsWith(keyName, "_audio") ? "1" : "0";
    }
}