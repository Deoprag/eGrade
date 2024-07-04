package com.deopraglabs.egrade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private float n1;
    private float n2;
    private String test1;
    private String test2;
    private Student student;
    private Subject subject;
}
