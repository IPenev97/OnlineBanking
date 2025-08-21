package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.local.entities.CardEntity

@Dao
interface CardDAO {
    @Query("SELECT * FROM card")
    suspend fun getAll(): List<CardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(vararg cards: CardEntity)
}