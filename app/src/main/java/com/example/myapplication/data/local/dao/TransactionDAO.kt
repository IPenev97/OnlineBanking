package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.local.entities.TransactionEntity

@Dao
interface TransactionDAO {
    @Query("SELECT * FROM `transaction`")
    suspend fun getAll() : List<TransactionEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(vararg transaction: TransactionEntity)
}