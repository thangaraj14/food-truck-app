package com.oracle.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Resource {

    private int dayorder;
    private String dayofweekstr;
    private String starttime;
    private String endtime;
    private String location;
    private String locationdesc;
    private int locationid;
    private String start24;
    private String end24;
    private String applicant;

}
