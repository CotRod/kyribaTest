package com.github.cotrod.kyriba;

import com.github.cotrod.kyriba.object.FilterForAnalysis;
import com.github.cotrod.kyriba.object.GroupForAnalysis;
import com.github.cotrod.kyriba.object.TypeOfGroup;
import com.github.cotrod.kyriba.object.TypeOfMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Utils {
    private static Scanner sc = new Scanner(System.in);
    private static ResourceBundle resource = ResourceBundle.getBundle("conf");
    private static String datePattern = resource.getString("dateTimePattern");

    public static int getFilterChoice(FilterForAnalysis filter) {
        int choice;
        do {
            System.out.println("Choose filter parameter:");
            System.out.println("1 - Username");
            System.out.println("2 - Time period");
            System.out.println("3 - Type of log");
            if (filter.hasFilter()) {
                System.out.println("4 - Continue");
            }
            choice = sc.nextInt();
        } while (choice != 1 && choice != 2 && choice != 3 && choice != 4);
        return choice;
    }

    public static void setFilter(int choice, FilterForAnalysis filter) {
        switch (choice) {
            case 1:
                System.out.println("Input username: ");
                filter.setUsername(sc.next());
                break;
            case 2:
                System.out.println("Input Date (fromDate) in " + datePattern + " : ");
                filter.setFromDate(LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern(datePattern)));
                System.out.println("Input Date (toDate) in " + datePattern + " : ");
                filter.setFromDate(LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern(datePattern)));
                break;
            case 3:
                do {
                    System.out.println("Choose type of logs");
                    System.out.println("1 - WARNING");
                    System.out.println("2 - ERROR");
                    choice = sc.nextInt();
                } while (choice != 1 && choice != 2);
                filter.setTypeOfMessage(TypeOfMessage.valueOf(choice == 1 ? "WARNING" : "ERROR"));
                break;
        }
    }

    public static int getGroupChoice(GroupForAnalysis groupParams) {
        int choice;
        do {

            System.out.println("Choose grouping");
            System.out.println("1 - By username");
            System.out.println("2 - By Day");
            if (groupParams.hasGroupParam()) {
                System.out.println("3 - Continue");
            }
            choice = sc.nextInt();
        } while (choice != 1 && choice != 2 && choice != 3);
        return choice;
    }

    public static void setGroupParams(int choice, GroupForAnalysis groupParams) {
        if (choice == 1) {
            groupParams.setTypes(TypeOfGroup.USERNAME);
        } else if (choice == 2) {
            groupParams.setTypes(TypeOfGroup.DAY);
        }
    }

    public static int getAmountOfThreads() {
        System.out.println("Set amount of threads (input 1 to use default value == 1 or 2 to use thread for each file)");
        int value = sc.nextInt();
        return Math.max(value, 1);
    }

    public static String getFileName() {
        System.out.println("Input a file name (with extends, for ex file.txt) : ");
        return sc.next();
    }
}
