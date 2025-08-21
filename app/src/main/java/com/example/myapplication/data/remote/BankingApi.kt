package com.example.myapplication.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.myapplication.data.remote.dto.CardDTO
import com.example.myapplication.data.remote.dto.TransactionDTO
import com.example.myapplication.util.MockData

class BankingApi(val context: Context) {



    suspend fun getCards() : List<CardDTO>  {
        if(hasInternetConnection())
            return MockData.mockApiCallCards
        else
            throw Exception()
    }

    suspend fun getTransactions() : List<TransactionDTO>{
        if(hasInternetConnection())
            return MockData.mockApiCallTransactions
        else
            throw Exception()

    }

    @SuppressLint("NewApi")
    fun hasInternetConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }


    companion object{
        val BASE_URL: String = "https://api2.binance.com/api/"
    }


}