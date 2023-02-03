package com.example.dayswithoutbadhabits.domain

interface NewMainInteractor : ResetCard {

    fun cards(): List<Card>

    fun canAddNewCard(): Boolean

    fun newCard(text: String): Card

    fun deleteCard(id: Long)

    fun updateCard(id: Long, newText: String)
}

interface ResetCard {
    fun resetCard(id: Long)
}
