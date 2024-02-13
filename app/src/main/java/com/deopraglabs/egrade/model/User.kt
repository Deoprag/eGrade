package com.deopraglabs.egrade.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDate;

@Entity(tableName = "tb_user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val birthdate: LocalDate,
    val cpf: String,
    val phoneNumber: String,
    val password: String,
    val role: Role
)