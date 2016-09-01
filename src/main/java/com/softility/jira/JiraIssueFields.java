package com.softility.jira;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueFields {
    String summary;
    List<Object> subtasks;
    JiraIssue parent;
}
