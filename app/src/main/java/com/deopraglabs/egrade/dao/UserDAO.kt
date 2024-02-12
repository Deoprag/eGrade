package com.deopraglabs.egrade.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.deopraglabs.egrade.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM tb_user WHERE id = :id")
    suspend fun getUserById(id: Long): User?

    @Query("SELECT * FROM tb_user WHERE cpf = :cpf")
    suspend fun getUserByCpf(cpf: String): User?
}