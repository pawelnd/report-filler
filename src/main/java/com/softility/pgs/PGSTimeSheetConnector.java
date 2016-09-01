package com.softility.pgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PGSTimeSheetConnector {
    @Autowired
    private Environment env;

    private List<String> cookieList = new ArrayList<>();

    public void addReport(String reportBody) {
        authenticate();
        sendReport(generatePayload(reportBody));
    }

    private TimeSheetEntry generatePayload(String reportBody) {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        TimeSheetEntry timeSheetEntry = new TimeSheetEntry();
        timeSheetEntry.setDate(dateFormat.format(new Date()));
        timeSheetEntry.setProjectId(env.getProperty("pgs.projectId"));
        timeSheetEntry.setLoggedHours(8);
        timeSheetEntry.setLoggedOvertimeHours(0);
        timeSheetEntry.setTitle(reportBody);
        return timeSheetEntry;
    }

    private void sendReport(TimeSheetEntry payload) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", cookieList.stream().collect(Collectors.joining(";")));

        HttpEntity<TimeSheetEntry> entity = new HttpEntity(payload ,headers);

        restTemplate.postForEntity(env.getProperty("pgs.url") + "karta-pracy-logowanie-potwierdz-nowy-log",
                entity,
                String.class);
    }

    private void authenticate() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders pgsLoginHeaders = new HttpHeaders();
        pgsLoginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("UserName",  env.getProperty("pgs.user.login"));
        map.add("Password", env.getProperty("pgs.user.password"));
        map.add("RememberMe", "false");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, pgsLoginHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(env.getProperty("pgs.url") + "logowanie?ReturnUrl=%2f", request, String.class);
        cookieList.addAll(response.getHeaders().get("Set-Cookie"));
    }
}
