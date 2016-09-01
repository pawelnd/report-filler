package com.softility;

import com.softility.jira.JiraConnector;
import com.softility.jira.JiraIssue;
import com.softility.jira.JiraSearchResult;
import com.softility.pgs.PGSTimeSheetConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TimeSheetFillerApplication implements CommandLineRunner {
	@Autowired
	JiraConnector jiraConnector;

	@Autowired
	IssueSelector issueSelector;

	@Autowired
	ReportGenerator reportGenerator;

	@Autowired
	PGSTimeSheetConnector pgsTimeSheetConnector;

	public static void main(String[] args) {
		SpringApplication.run(TimeSheetFillerApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		try{
			System.out.println("Fetching task list from Jira");
			JiraSearchResult result = jiraConnector.getIssuesForUser();
			List<JiraIssue> issuesSelectedByUser = issueSelector.selectIssues(result.getIssues());
			String reportBody = reportGenerator.issuesToReportConversion(issuesSelectedByUser);
			System.out.println("Sending report to MyPGS");
			pgsTimeSheetConnector.addReport(reportBody);
			System.out.println("Report has been sent");
		}catch (Exception e){
			System.out.println("Error occurred:" + e.getMessage());
		}
	}
}
