package edu.rice.starvote;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by luej on 7/20/16.
 */
public final class ExternalProcess {

    public static void runOnly(String... args) {
        runInDir(null, args);
    }

    public static void runInDir(File wd, String... args) {
        final ProcessBuilder pfac = new ProcessBuilder(args);
        pfac.directory(wd);
        try {
            pfac.start();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public static String runInDirAndCapture(File wd, String... args) {
        final ProcessBuilder pfac = new ProcessBuilder(args);
        pfac.directory(wd);
        pfac.redirectErrorStream(true);
        try {
            final Process process = pfac.start();
            return getStdout(process);
        } catch (IOException e) {
            return e.toString();
        }
    }

    public static String runAndCapture(String... args) {
        return runInDirAndCapture(null, args);
    }

    private static String getStdout(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ( (line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }
}
