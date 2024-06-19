package com.deopraglabs.egrade.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class Professor extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;

}
