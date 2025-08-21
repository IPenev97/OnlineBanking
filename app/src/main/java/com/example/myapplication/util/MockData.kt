package com.example.myapplication.util

import com.example.myapplication.data.remote.dto.CardDTO
import com.example.myapplication.data.remote.dto.TransactionDTO
import com.example.myapplication.domain.enums.CardBrand
import com.example.myapplication.domain.enums.CardType

object MockData {
    val mockApiCallCards = listOf(
        CardDTO(
            id = 1,
            pan = "4111 1111 1111 1111",
            brand = CardBrand.VISA,
            type = CardType.Debit,
            isFavorite = true
        ),
        CardDTO(
            id = 2,
            pan = "5555 5555 5555 4444",
            brand = CardBrand.MASTERCARD,
            type = CardType.Credit,
            isFavorite = false
        )
    )
    val mockApiCallTransactions = mutableListOf(
        TransactionDTO(id = 1, amount = 4.99, date = "06/12/2024 05:64:33"),
        TransactionDTO(id = 2, amount = 99.99, date = "12/12/2024 01:32:33"),
        TransactionDTO(id = 3, amount = 99.99, date = "12/12/2024 01:32:33"),
        TransactionDTO(id = 4, amount = 99.99, date = "12/12/2024 01:32:33")
    )

    fun addTransaction(){
        mockApiCallTransactions.add(TransactionDTO(id = (mockApiCallTransactions.size+1).toLong(), amount = 99.99, date = "12/12/2024 01:32:33"))
    }
}