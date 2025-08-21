package com.example.myapplication.data.remote.dto

import com.example.myapplication.domain.enums.CardBrand
import com.example.myapplication.domain.enums.CardType

data class CardDTO(
    val id: Long,
    val pan: String,
    val type: CardType,
    val isFavorite: Boolean,
    val brand: CardBrand
) {
}