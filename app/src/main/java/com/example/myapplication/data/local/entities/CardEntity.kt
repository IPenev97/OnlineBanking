package com.example.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.enums.CardBrand
import com.example.myapplication.domain.enums.CardType

@Entity(tableName = "card")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pan: String,
    val type: CardType,
    val isFavorite: Boolean,
    val brand: CardBrand
) {

}