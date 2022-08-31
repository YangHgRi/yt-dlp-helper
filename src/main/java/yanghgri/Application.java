package yanghgri;

import yanghgri.model.ConfigProperties;
import yanghgri.model.Key;
import yanghgri.service.ConfigLoader;
import yanghgri.utils.FileUtil;
import yanghgri.utils.UrlUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;

import static yanghgri.service.Executor.buildCommandList;
import static yanghgri.service.Executor.startProcess;
import static yanghgri.service.MessageOutput.outputSupportKeysName;

public class Application {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        ConfigLoader parser = new ConfigLoader();
        ConfigProperties properties = parser.load();
        List<Key> keyList = properties.getKeyList();
        String configLocation = properties.getConfigLocation();

        boolean loop;
        boolean skip;
        do {
            outputSupportKeysName(keyList);

            Scanner scanner = new Scanner(System.in);
            String targetKeyIndex = scanner.nextLine();

            Key targetKey = null;
            if (targetKeyIndex.startsWith("D")) {
                deleteAllMode(targetKeyIndex, keyList);
                skip = true;
            } else if (Integer.parseInt(targetKeyIndex) >= keyList.size()) {
                System.out.println("\n" + "输入关键字序号超出现有范围！" + "\n");
                skip = true;
            } else {
                targetKey = keyList.get(Integer.parseInt(targetKeyIndex));
                skip = false;
            }
            if (!skip) {
                execute(targetKey, configLocation);
            }
            System.out.println("\n再来一次？输入1确认");
            String retry = new Scanner(System.in).nextLine();
            loop = retry.equals("1");
        } while (loop);
    }

    public static void execute(Key targetKey, String configLocation) throws IOException, InterruptedException {
        String keyName = targetKey.getName();
        String urlListPath = targetKey.getUrlListPath();
        String workDir = targetKey.getWorkDir();

        FileUtil.writeByStringList(new File(urlListPath), UrlUtil.extract(new File(urlListPath)));
        List<String> commandList = buildCommandList(urlListPath, configLocation, keyName.endsWith("_audio"));
        startProcess(workDir, commandList);
    }

    public static void deleteAllMode(String targetKeyIndex, List<Key> keyList) throws IOException {
        targetKeyIndex = targetKeyIndex.substring(1);
        Key targetKey = keyList.get(Integer.parseInt(targetKeyIndex));
        String urlListPath = targetKey.getUrlListPath();
        FileUtil.deleteAll(new File(urlListPath));
        System.out.print("\n" + urlListPath + "已清空！\n");
    }
}