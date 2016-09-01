package com.softility.jira;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class JiraSearchResult {
    private Integer maxResults;
    private List<JiraIssue> issues;
}
