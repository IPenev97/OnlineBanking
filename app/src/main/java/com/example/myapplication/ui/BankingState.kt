package com.example.myapplication.ui

import com.example.myapplication.domain.models.Card
import com.example.myapplication.domain.models.Transaction

data class BankingState(
   val cards: List<Card>? = null,
   val balance: Double = 0.0,
   val isTransactionsRefreshing: Boolean = false,
   val showConnectionDialog: Boolean = false,
   val noConnection: Boolean = false,
   val transactions: List<Transaction>? = null,
   val cardsLoading: Boolean = false,
   val transactionsLoading: Boolean = false
) {

}