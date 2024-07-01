package com.deopraglabs.egrade.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Certificate implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private String justification;
    private Date date;
    private String image;
    private Student student;
}
