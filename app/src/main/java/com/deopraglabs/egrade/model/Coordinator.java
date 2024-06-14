package com.deopraglabs.egrade.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Coordinator extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
}
