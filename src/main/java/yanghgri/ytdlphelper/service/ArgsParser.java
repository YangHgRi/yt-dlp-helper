package yanghgri.ytdlphelper.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgsParser {
    public static Map<String, String> parse2Map(String[] args) {
        List<String> argsList = Arrays.asList(args);

        Map<String, String> argsMap = new HashMap<>(argsList.size());

        argsList.forEach(arg -> {
            int indexOfEqual = arg.lastIndexOf("=");

            if (indexOfEqual != -1) {
                //等号左边
                String key = arg.substring(0, indexOfEqual);
                //等号右边
                String value = arg.substring(indexOfEqual + 1);

                argsMap.put(key, value);
            }
        });
        return argsMap;
    }
}