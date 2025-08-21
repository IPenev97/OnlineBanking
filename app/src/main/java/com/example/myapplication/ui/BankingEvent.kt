package com.example.myapplication.ui

sealed class BankingEvent {
    object LoadTransactions : BankingEvent()
    object RefreshData : BankingEvent()
    class ToggleDialog(val visible: Boolean) : BankingEvent()



}