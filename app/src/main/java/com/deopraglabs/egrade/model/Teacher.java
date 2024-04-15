package com.deopraglabs.egrade.model;

import lombok.Data;

@Data
public class Teacher extends com.deopraglabs.egrade.model.User {

    private Long id;
    private Role role = Role.PROFESSOR;
}
