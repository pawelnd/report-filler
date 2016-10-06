package com.softility;

import com.softility.jira.JiraIssue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ReportGenerator {
    public String issuesToReportConversion(List<JiraIssue> issuesSelectedByUser) {
        return issuesSelectedByUser.stream()
                .map(JiraIssue::getKey)
                .collect(Collectors.joining(", "));
    }

    public String issuesDetailedInfo(List<JiraIssue> issuesSelectedByUser) {
        return issuesSelectedByUser.stream()
                .map(x->x.getKey() + " : " +x.getFields().getSummary())
                .collect(Collectors.joining("\n"));
    }
}
