package com.example.myapplication.data.local

import androidx.room.RoomDatabase
import androidx.room.Database
import com.example.myapplication.data.local.dao.CardDAO
import com.example.myapplication.data.local.dao.TransactionDAO
import com.example.myapplication.data.local.entities.CardEntity
import com.example.myapplication.data.local.entities.TransactionEntity


@Database(
    entities = [CardEntity::class, TransactionEntity::class], version = 8, exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun cardDao(): CardDAO
    abstract fun transactionDao() : TransactionDAO

    companion object {
        val DATABASE_NAME = "interviewtask.db"
    }
}