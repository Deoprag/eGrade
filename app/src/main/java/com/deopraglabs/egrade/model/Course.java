package com.deopraglabs.egrade.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String description;
    private Coordinator coordinator;
    private List<Subject> subjects;

}
