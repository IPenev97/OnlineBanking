package com.example.myapplication.domain

import com.example.myapplication.domain.models.Card
import com.example.myapplication.domain.models.Transaction
import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface BankingRepository {
        fun getAllCards() : Flow<Resource<List<Card>>>
        suspend fun loadLocalTransactions() : List<Transaction>
        suspend fun loadLocalCards() : List<Card>
        fun getTransactions() : Flow<Resource<List<Transaction>>>
    }
