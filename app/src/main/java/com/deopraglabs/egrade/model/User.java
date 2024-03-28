package com.deopraglabs.egrade.model;

import java.sql.Blob;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private String name;
    private String cpf;
    private String email;
    private String phoneNumber;
    private Date birthDate;
    private Blob profilePicture;
    private String password;
    private Role role;
}
