package com.example.myapplication.domain.models

import com.example.myapplication.domain.enums.CardBrand
import com.example.myapplication.domain.enums.CardType

data class Card(
    val pan: String,
    val type: CardType,
    val isFavorite: Boolean,
    val brand: CardBrand
) {
}