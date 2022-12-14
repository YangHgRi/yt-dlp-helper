package yanghgri.service;

import org.apache.commons.lang3.StringUtils;
import yanghgri.model.Key;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YangHgRi
 */
public class MessageOutput {
    public static void outputMessage(InputStream input) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = br.readLine()) != null) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            if (line.startsWith("[download]")) {
                if (line.startsWith("[download] Resuming") || line.startsWith("[download] Destination")) {
                    System.out.println(line);
                } else {
                    System.out.print("\r");
                    System.out.print(line);
                }
            } else if (line.startsWith("[DL") || line.startsWith("[#")) {
                System.out.print("\r");
                System.out.print(line);
            } else {
                System.out.println(line);
            }
        }
    }

    public static void outputSupportKeysName(List<Key> keyList) {
        final String tab = "\t";
        List<String> keyNameList = new ArrayList<>();
        keyList.forEach(key -> keyNameList.add(key.getName()));
        System.out.println("\n当前支持关键字与其序号：");
        for (int i = 0; i < keyList.size(); i++) {
            System.out.println(tab + i + " -> " + keyNameList.get(i));
        }
        System.out.print("目标关键字序号：");
    }
}
