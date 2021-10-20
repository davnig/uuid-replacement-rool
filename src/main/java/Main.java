import exception.ReplacementException;
import exception.ReplacementMapGenerationException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments. Looking for default keycloak export file 'realm-export.json'");
            replaceAllUuid("realm-export.json");
            System.exit(0);
        }
        replaceAllUuid(args[0]);
    }

    private static void replaceAllUuid(String filename) {
        try {
            final Map<String, String> replacementMap = generateReplacementMap(filename);
            doReplacement(filename, replacementMap);
        } catch (ReplacementMapGenerationException | ReplacementException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void doReplacement(String filename, Map<String, String> replacementMap) throws ReplacementException {
        File file = new File("./" + filename);
        try (
                BufferedReader br = new BufferedReader(new FileReader(file));
                BufferedWriter bw = new BufferedWriter(new FileWriter("output.json"));
                LineNumberReader lineReader = new LineNumberReader(br)
        ) {
            String line;
            boolean replaced;
            while ((line = lineReader.readLine()) != null) {
                replaced = false;
                for (String uuid : replacementMap.keySet()) {
                    if (line.contains(uuid)) {
                        final String newLine = line.replace(uuid, replacementMap.get(uuid));
                        bw.write(newLine + "\n");
                        replaced = true;
                        break;
                    }
                }
                if (!replaced) {
                    bw.write(line + "\n");
                }
            }
        } catch (IOException e) {
            throw new ReplacementException("Error while opening the configuration file", e);
        }
    }

    private static Map<String, String> generateReplacementMap(String filename) throws ReplacementMapGenerationException {
        File file = new File("./" + filename);
        try (
                BufferedReader br = new BufferedReader(new FileReader(file));
                LineNumberReader lineReader = new LineNumberReader(br)
        ) {
            Map<String, String> replacementMap = new HashMap<>();
            Pattern pattern = Pattern.compile("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
            Matcher matcher = pattern.matcher("");
            String line;
            while ((line = lineReader.readLine()) != null) {
                matcher.reset(line);
                if (matcher.find()) {
                    final int start = matcher.start();
                    final int end = matcher.end();
                    final String uuidToReplace = line.substring(start, end);
                    final String newUUid = UUID.randomUUID().toString();
                    if (!replacementMap.containsKey(uuidToReplace)) {
                        replacementMap.put(uuidToReplace, newUUid);
                    }
                }
            }
            return replacementMap;
        } catch (IOException e) {
            throw new ReplacementMapGenerationException("Error while opening the configuration file", e);
        }
    }
}
