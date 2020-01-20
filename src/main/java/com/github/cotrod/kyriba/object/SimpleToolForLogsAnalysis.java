package com.github.cotrod.kyriba.object;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class SimpleToolForLogsAnalysis {
    private static String pathToFolderWithLogs = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "csvFiles";
    private static ResourceBundle resource = ResourceBundle.getBundle("conf");
    private static String datePattern = resource.getString("dateTimePattern");

    public static void startTool(FilterForAnalysis filter, GroupForAnalysis groupParams, int threads, String nameOfFile) {
        String pathToSummaryFile = pathToFolderWithLogs + File.separator + "result" + File.separator + nameOfFile;
        try {
// ************* Collecting files to collection *************
            List<File> logFiles = Files.list(Paths.get(pathToFolderWithLogs))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            File summaryFile = new File(pathToSummaryFile);
            if (summaryFile.createNewFile()) {

                ExecutorService executorService = Executors.newFixedThreadPool(threads);
                List<Future> futureList = new ArrayList<>();
// ************* Read files according filters and write logs to file *************
                for (File file : logFiles) {
                    futureList.add(executorService.submit(getTask(file, filter, pathToSummaryFile)));
                }
                for (Future future : futureList) {
                    future.get();
                }
                executorService.shutdown();
// ************* Read summary file and write information according grouping *************
                try (FileReader fr = new FileReader(pathToSummaryFile); Scanner sc = new Scanner(fr); FileWriter fw = new FileWriter(pathToSummaryFile, true)) {
                    Set<TypeOfGroup> types = groupParams.getTypes();
                    if (types.contains(TypeOfGroup.USERNAME)) {
                        Map<String, Integer> users = new HashMap<>();
                        while (sc.hasNextLine()) {
                            String line = sc.nextLine();
                            String username = getUsername(line);
                            if (users.containsKey(username)) {
                                int amount = users.get(username);
                                users.put(username, ++amount);
                            } else {
                                users.put(username, 1);
                            }
                        }
                        for (String username : users.keySet()) {
                            fw.write("Username : " + username + "\n" +
                                    "amount of logs : " + users.get(username) + "\n");
                        }
                    }
                    if (types.contains(TypeOfGroup.DAY)) {
                        Map<LocalDate, Integer> dates = new HashMap<>();
                        while (sc.hasNextLine()) {
                            String sdate = getStringDate(sc.nextLine());
                            LocalDate date = LocalDate.parse(sdate, DateTimeFormatter.ofPattern(datePattern));
                            if (dates.containsKey(date)) {
                                int count = dates.get(date);
                                dates.put(date, ++count);
                            } else {
                                dates.put(date, 1);
                            }
                        }
                        for (LocalDate date : dates.keySet()) {
                            fw.write(date.format(DateTimeFormatter.ofPattern(datePattern)) + " : " + dates.get(date) + "\n");
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static String getStringDate(String line) {
        return Arrays.stream(line.split(",")).map(s -> s.replaceAll("\\d{2}[:]\\d{2}[:]\\d{2}", "")).map(String::trim).filter(s -> s.matches("\\d{2}[/]\\d{2}[/]\\d{4}")).findAny().get();
    }

    private static String getUsername(String line) {
        List<String> elements = Arrays.stream(line.split(",")).map(String::trim).collect(Collectors.toList());
        return elements.get(3);
    }

    private static Runnable getTask(File file, FilterForAnalysis filter, String pathToSummaryFile) {
        return () -> {
            try (FileReader fr = new FileReader(file); Scanner sc = new Scanner(fr); FileWriter fw = new FileWriter(pathToSummaryFile, true)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (filter.getUsername() != null) {
                        if (!line.contains(filter.getUsername())) {
                            continue;
                        }
                    }
                    if (filter.getTypeOfMessage() != null) {
                        if (!line.contains(filter.getTypeOfMessage().name())) {
                            continue;
                        }
                    }
                    if (filter.getFromDate() != null) {
                        String sdate = getStringDate(line);
                        if (!isCorrectDate(filter, sdate)) {
                            continue;
                        }
                    }
                    fw.write(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private static boolean isCorrectDate(FilterForAnalysis filter, String sdate) {
        LocalDate date = LocalDate.parse(sdate, DateTimeFormatter.ofPattern(datePattern));
        return (date.isAfter(filter.getFromDate()) || date.isEqual(filter.getFromDate())) &&
                (date.isBefore(filter.getToDate()) || date.isEqual(filter.getToDate()));
    }
}
