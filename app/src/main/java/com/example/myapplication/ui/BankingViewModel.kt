package com.example.myapplication.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.BankingRepository
import com.example.myapplication.domain.enums.DataError
import com.example.myapplication.util.MockData
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BankingViewModel @Inject constructor(
    private val bankingRepository: BankingRepository
) : ViewModel() {

    private val _state = mutableStateOf(BankingState())
    val state: State<BankingState> = _state

    var getCardsJob: Job? = null
    var getTransactionsJob: Job? = null

    fun onBankingEvent(event: BankingEvent) {
        when(event){
            BankingEvent.LoadTransactions -> {
                MockData.addTransaction()
                getTransactionsJob?.cancel()
                getTransactionsJob = viewModelScope.launch(Dispatchers.IO) {
                    bankingRepository.getTransactions().collectLatest { result ->
                        when(result){
                            is Resource.Error -> {
                                when(result.error){
                                    DataError.NO_INTERNET_CON ->{
                                        getTransactionsJob?.cancel()
                                        getTransactionsJob = viewModelScope.launch(Dispatchers.IO){
                                            val transactionsList = bankingRepository.loadLocalTransactions()
                                            withContext(Dispatchers.Main){
                                                _state.value = state.value.copy(noConnection = true, transactions = transactionsList, isTransactionsRefreshing = false)
                                            }
                                        }
                                    }
                                    DataError.ERROR_SAVING_DB -> {}
                                }
                            }
                            is Resource.Loading -> {
                                withContext(Dispatchers.Main) {
                                    _state.value = state.value.copy(isTransactionsRefreshing = true)
                                }
                            }
                            is Resource.Success -> {
                                withContext(Dispatchers.Main) {
                                    _state.value = state.value.copy(transactions = result.data, isTransactionsRefreshing = false, noConnection = false)
                                }
                            }
                        }
                    }
                }
            }


            BankingEvent.RefreshData -> {
                _state.value = state.value.copy(showConnectionDialog = false)
                loadData()
            }

            is BankingEvent.ToggleDialog -> {
                _state.value = state.value.copy(showConnectionDialog = event.visible)
            }
        }
    }


    init {
      loadData()
    }

    fun loadData(){
        getCardsJob?.cancel()
        getCardsJob =  viewModelScope.launch(Dispatchers.IO) {
            bankingRepository.getAllCards().collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        when(result.error){
                            DataError.NO_INTERNET_CON -> {
                                getCardsJob?.cancel()
                                getCardsJob = viewModelScope.launch(Dispatchers.IO){
                                    val cardsList = bankingRepository.loadLocalCards()
                                    withContext(Dispatchers.Main){
                                        _state.value = state.value.copy(noConnection = true, cards = cardsList, cardsLoading = false)
                                    }
                                }
                            }
                            DataError.ERROR_SAVING_DB -> {}
                        }
                    }
                    is Resource.Loading -> {
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(cardsLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(cards = result.data, cardsLoading = false, noConnection = false)
                        }
                    }
                }
            }
        }
        getTransactionsJob?.cancel()
        getTransactionsJob = viewModelScope.launch(Dispatchers.IO) {
            bankingRepository.getTransactions().collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        when(result.error){
                            DataError.NO_INTERNET_CON ->{
                                getTransactionsJob?.cancel()
                                getTransactionsJob = viewModelScope.launch(Dispatchers.IO){
                                    val transactionsList = bankingRepository.loadLocalTransactions()
                                    withContext(Dispatchers.Main){
                                        _state.value = state.value.copy(noConnection = true, transactions = transactionsList, transactionsLoading = false)
                                    }
                                }
                            }
                            DataError.ERROR_SAVING_DB -> {}
                        }
                    }
                    is Resource.Loading -> {
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(transactionsLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(transactions = result.data, transactionsLoading = false, noConnection = false)
                        }
                    }
                }
            }
        }
    }





}