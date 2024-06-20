package com.deopraglabs.egrade.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String cpf;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private Date birthDate;
    private String profilePicture;
    private String password;
    private boolean active = false;
    private Role role;

}
