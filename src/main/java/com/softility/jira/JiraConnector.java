package com.softility.jira;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Component
public class JiraConnector {
    @Autowired
    private Environment env;
    public JiraSearchResult getIssuesForUser(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JiraSearchResult> exchange = restTemplate.exchange
                (generateSelectionQuery(),
                        HttpMethod.GET,
                        new HttpEntity(createHeaders(env.getProperty("jira.user.login"), env.getProperty("jira.user.password"))),
                        JiraSearchResult.class);

        return exchange.getBody();
    }

    private String generateSelectionQuery() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");
        return MessageFormat.format("{0}search?jql=assignee={1} AND REVIEWER={1} and updated >={2}",
                env.getProperty("jira.api.url"),
                env.getProperty("jira.user.login"),
                dateFormat.format(new Date())  );
    }

    private HttpHeaders createHeaders( String username, String password ){
        return new HttpHeaders(){
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.getEncoder().encode(
                        auth.getBytes(Charset.forName("UTF-8")) );
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
            }
        };
    }
}
