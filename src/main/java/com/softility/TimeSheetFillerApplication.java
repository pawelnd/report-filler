package com.softility;

import com.softility.jira.JiraConnector;
import com.softility.jira.JiraIssue;
import com.softility.jira.JiraSearchResult;
import com.softility.pgs.PGSTimeSheetConnector;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@Log
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
			logToFile(reportGenerator.issuesDetailedInfo(issuesSelectedByUser));
			System.out.println("Sending report to MyPGS");
			pgsTimeSheetConnector.addReport(reportBody);
			System.out.println("Report has been sent");
		}catch (Exception e){
			System.out.println("Error occurred:" + e.getMessage());
		}
	}

	private void logToFile(String content) throws IOException {
		log.info("Saving report to file");
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		Files.write(Paths.get(dateFormat.format(new Date())+ ".txt"),content.getBytes());
	}
}
