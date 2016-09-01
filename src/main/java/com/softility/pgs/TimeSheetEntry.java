package com.softility.pgs;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeSheetEntry {

    private Integer loggedHours;

    private Integer loggedOvertimeHours;

    private String title;

    private boolean isFree;

    private String projectId;

    private String date;

}
