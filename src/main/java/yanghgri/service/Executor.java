package yanghgri.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static yanghgri.service.MessageOutput.outputMessage;

public class Executor {
    public static int startProcess(String workDir, List<String> commandList) throws IOException, InterruptedException {
        System.out.println();
        ProcessBuilder pb = new ProcessBuilder(commandList);
        pb.directory(new File(workDir));
        pb.redirectErrorStream(true);
        Process process = pb.start();
        outputMessage(process.getInputStream());
        return process.waitFor();
    }

    public static List<String> buildCommandList(String urlListPath, String configLocation, boolean audioOnly) {
        List<String> commandList = new ArrayList<>();
        commandList.add("yt-dlp");
        if (audioOnly) {
            commandList.add("-x");
        }
        commandList.add("--config-location");
        commandList.add(configLocation);
        commandList.add("-a");
        commandList.add(urlListPath);
        return commandList;
    }
}
