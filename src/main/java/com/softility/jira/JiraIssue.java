package com.softility.jira;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {
    private String key;
    private JiraIssueFields fields;
}
