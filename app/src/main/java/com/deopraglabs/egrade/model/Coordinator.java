package com.deopraglabs.egrade.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Coordinator extends User {

    private static final long serialVersionUID = 1L;

    private long id;
    private List<Course> courses;
}
