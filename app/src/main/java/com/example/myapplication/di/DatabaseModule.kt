package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.local.Database
import com.example.myapplication.data.local.dao.CardDAO
import com.example.myapplication.data.local.dao.TransactionDAO
import com.example.myapplication.data.remote.BankingApi
import com.example.myapplication.data.repositories.BankingRepositoryImpl
import com.example.myapplication.domain.BankingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            Database.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun provideCardDAO(database: Database) : CardDAO {
        return database.cardDao()
    }
    @Singleton
    @Provides
    fun provideTransactionDao(database: Database) : TransactionDAO {
        return database.transactionDao()
    }

    @Singleton
    @Provides
    fun provideBankingRepository(cardDao: CardDAO, bankingApi: BankingApi, transactionDAO: TransactionDAO) : BankingRepository {
        return BankingRepositoryImpl(cardDao = cardDao, bankingApi = bankingApi, transacionDAO = transactionDAO)
    }
}