package com.deopraglabs.egrade.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private Date date;
    boolean isPresent = false;
    private Student student;
    private Subject subject;
}
