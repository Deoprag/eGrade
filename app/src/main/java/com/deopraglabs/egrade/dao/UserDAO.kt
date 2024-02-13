package com.deopraglabs.egrade.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.deopraglabs.egrade.model.User

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM tb_user WHERE id = :id")
    suspend fun getUserById(id: Long): LiveData<User?>

    @Query("SELECT * FROM tb_user WHERE cpf = :cpf")
    suspend fun getUserByCpf(cpf: String): LiveData<User?>

    @Query("SELECT * FROM tb_user WHERE phoneNumber = :phoneNumber")
    suspend fun getUserByPhoneNumber(phoneNumber: String): LiveData<User?>

    @Query("SELECT * FROM tb_user WHERE name LIKE :name")
    suspend fun getUsersByName(name: String): LiveData<List<User>>
}