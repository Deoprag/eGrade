package com.deopraglabs.egrade.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private float grade;
    private GradeType gradeType;
    private Student student;
    private Subject subject;
}
