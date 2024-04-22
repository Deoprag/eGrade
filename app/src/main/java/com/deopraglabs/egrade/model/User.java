package com.deopraglabs.egrade.model;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import lombok.Data;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String cpf;
    private String email;
    private String phoneNumber;
    private Date birthDate;
    private Blob profilePicture;
    private String password;
    private boolean active = false;
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
