package com.example.myapplication.data.repositories


import com.example.myapplication.data.local.dao.CardDAO
import com.example.myapplication.data.local.dao.TransactionDAO
import com.example.myapplication.data.local.entities.CardEntity
import com.example.myapplication.data.local.entities.TransactionEntity

import com.example.myapplication.data.remote.BankingApi
import com.example.myapplication.data.remote.dto.CardDTO
import com.example.myapplication.data.remote.dto.TransactionDTO
import com.example.myapplication.domain.BankingRepository
import com.example.myapplication.domain.models.Card
import com.example.myapplication.domain.enums.DataError
import com.example.myapplication.domain.models.Transaction
import com.example.myapplication.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BankingRepositoryImpl @Inject constructor(
    val cardDao: CardDAO,
    val transacionDAO: TransactionDAO,
    val bankingApi: BankingApi
) : BankingRepository {
    override fun getAllCards(): Flow<Resource<List<Card>>> {
        return flow {
            emit(Resource.Loading())
            delay(1000L)
            val cardList: List<CardDTO> = try {
                bankingApi.getCards()
            } catch (e: Exception) {
                emit(Resource.Success(data = cardDao.getAll().map { it.toModel() }))
                emit(Resource.Error(error = DataError.NO_INTERNET_CON))
                return@flow
            }
            if (cardList.isNotEmpty()) {
                emit(Resource.Success(data = cardList.map { cardDto -> cardDto.toModel() }))
                try {
                    val cardEntityList: List<CardEntity> =
                        cardList.map { currency -> currency.toEntity() }

                    cardDao.insertCard(*cardEntityList.toTypedArray())
                } catch (e: Exception) {
                    emit(Resource.Error(DataError.ERROR_SAVING_DB))
                    return@flow
                }

                emit(Resource.Success(cardList.map { cardDto -> cardDto.toModel() }))
                return@flow
            } else {
                emit(Resource.Success(cardList.map{cardDTO -> cardDTO.toModel()}))
            }


        }
    }

    override suspend fun loadLocalTransactions(): List<Transaction> {
        return transacionDAO.getAll().map{it.toModel()}
    }

    override suspend fun loadLocalCards(): List<Card> {
      return cardDao.getAll().map{it.toModel()}
    }

    override fun getTransactions(): Flow<Resource<List<Transaction>>> {
        return flow {
            emit(Resource.Loading())
            delay(1000L)
            val transacctionsList: List<TransactionDTO> = try {
                bankingApi.getTransactions()
            } catch (e: Exception) {
                emit(Resource.Error(error = DataError.NO_INTERNET_CON))
                return@flow
            }
            if (transacctionsList.isNotEmpty()) {
                emit(Resource.Success(data = transacctionsList.map { transactionDto -> transactionDto.toModel() }))
                try {
                    val transactionEntityList: List<TransactionEntity> =
                        transacctionsList.map { currency -> currency.toEntity() }

                    transacionDAO.insertTransaction(*transactionEntityList.toTypedArray())
                } catch (e: Exception) {
                    emit(Resource.Error(DataError.ERROR_SAVING_DB))
                    return@flow
                }

                emit(Resource.Success(transacctionsList.map { transactionDTO -> transactionDTO.toModel() }))
                return@flow
            } else {
                emit(Resource.Success(transacctionsList.map{transactionDTO -> transactionDTO.toModel() }))
            }


        }
    }




    private fun CardEntity.toModel(): Card {
        return Card(pan = maskPan(this.pan), brand = this.brand, type = this.type, isFavorite = this.isFavorite)
    }

    private fun CardDTO.toEntity(): CardEntity {
        return CardEntity(id = this.id, pan = this.pan, brand = this.brand, type = this.type, isFavorite = this.isFavorite)
    }

    private fun CardDTO.toModel(): Card {
        return Card(pan = maskPan(this.pan), brand = this.brand, type = this.type, isFavorite = this.isFavorite)
    }

    private fun TransactionEntity.toModel() : Transaction {
        return Transaction(amount = this.amount, date = this.date)
    }

    private fun TransactionDTO.toEntity() : TransactionEntity {
        return TransactionEntity(id = this.id, amount = this.amount, date = this.date)
    }

    private fun TransactionDTO.toModel() : Transaction {
        return Transaction(amount = this.amount, date = this.date)
    }


    fun maskPan(cardNumber: String): String {

        val digitsOnly = cardNumber.replace(" ", "")
        if (digitsOnly.length < 4) return cardNumber

        val masked = "*".repeat(digitsOnly.length - 4) + digitsOnly.takeLast(4)

        return masked.chunked(4).joinToString(" ")
    }




}