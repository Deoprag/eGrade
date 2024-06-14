package com.deopraglabs.egrade.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Student extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private Course course;
    private List<Certificate> certificates;
    private List<Grade> grades;
    private List<Attendance> attendances;

}
