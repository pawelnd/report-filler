package com.softility;


import com.softility.jira.JiraIssue;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class IssueSelector {
    public List<JiraIssue> selectIssues(List<JiraIssue> issues) {
        Map<JiraIssue, List<JiraIssue>> groupedByParent = groupByParent(issues);
        HashMap<Integer, JiraIssue> indexToTaskMapping = printSelection(groupedByParent);
        return askUserForTasks(indexToTaskMapping);
    }

    private List<JiraIssue> askUserForTasks(HashMap<Integer, JiraIssue> indexToTaskMapping) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter numbers of tasks to be attached separated with [space]: ");
        String next = scanner.nextLine();
        List<String> items = Arrays.asList(next.split(" "));
        return indexToTaskMapping.entrySet().stream()
                .filter(x -> items.contains(x.getKey().toString()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private HashMap<Integer, JiraIssue> printSelection(Map<JiraIssue, List<JiraIssue>> groupedByParent) {
        final int[] index = {1};
        final HashMap<Integer,JiraIssue> indexToTaskMap = new HashMap<>();
        groupedByParent.entrySet().stream()
                .forEach(x->{
                    System.out.println(String.format("%s - %s:", x.getKey().getKey(), x.getKey().getFields().getSummary()));
                    x.getValue().stream().forEach(y -> {
                        indexToTaskMap.put(index[0],y);
                        System.out.println(String.format("[%d]		-%s - %s", index[0]++,y.getKey(), y.getFields().getSummary()));
                    });
                });
        return indexToTaskMap;

    }

    private Map<JiraIssue, List<JiraIssue>> groupByParent(List<JiraIssue> issues) {
        return  issues.stream()
                .collect(Collectors.groupingBy(x -> x.getFields().getParent()));
    }
}
