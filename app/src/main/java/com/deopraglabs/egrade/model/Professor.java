package com.deopraglabs.egrade.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Professor extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
//    private List<Subject> subjects;
}
