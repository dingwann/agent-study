package org.example.dingwan.model.entity;

import lombok.Data;

@Data
public class Results {

    private String url;
    private String title;
    private String content;
    private double score;
    private String raw_content;
    private String favicon;

}