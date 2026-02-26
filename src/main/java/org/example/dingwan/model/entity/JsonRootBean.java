package org.example.dingwan.model.entity;

import lombok.Data;
import java.util.List;

@Data
public class JsonRootBean {

    private String query;
    private double response_time;
    private String follow_up_questions;
    private String answer;
    private List<String> images;
    private List<Results> results;
    private String request_id;

}