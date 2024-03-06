package com.deopraglabs.egrade.model;

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate;

@Entity
@Table(name = "tb_user")
data class User(

    @Id
    val id: Long = 0,
    val name: String,
    val birthdate: LocalDate,
    val cpf: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val role: Role
)