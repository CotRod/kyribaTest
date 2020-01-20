package com.github.cotrod.kyriba;

import com.github.cotrod.kyriba.object.FilterForAnalysis;
import com.github.cotrod.kyriba.object.GroupForAnalysis;
import com.github.cotrod.kyriba.object.SimpleToolForLogsAnalysis;

import static com.github.cotrod.kyriba.Utils.*;

public class Main {
    public static void main(String[] args) {
        int choice;
// ************* Set Filter *************
        FilterForAnalysis filter = new FilterForAnalysis();
        do {
            choice = getFilterChoice(filter);
            setFilter(choice, filter);
        } while (!(choice == 4 && filter.hasFilter()));     // Will be repeated until choice won't be 4 and filter won't have one parameter at least
// ************* Set Grouping *************
        GroupForAnalysis groupParams = new GroupForAnalysis();
        do {
            choice = getGroupChoice(groupParams);
            setGroupParams(choice, groupParams);
        } while (!(choice == 3) && groupParams.hasGroupParam());     // Will be repeated until choice won't be 3 and groupParams won't have one parameter at least
// ************* Set Amount Of Threads *************
        int amountOfThreads = getAmountOfThreads();
// ************* Set a file name *************
        String fileName = getFileName();

        SimpleToolForLogsAnalysis.startTool(filter,groupParams,amountOfThreads,fileName);
    }
}
