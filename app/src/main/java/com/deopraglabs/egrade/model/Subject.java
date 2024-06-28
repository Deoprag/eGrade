package com.deopraglabs.egrade.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private String name;
    private Professor professor;
    
}
