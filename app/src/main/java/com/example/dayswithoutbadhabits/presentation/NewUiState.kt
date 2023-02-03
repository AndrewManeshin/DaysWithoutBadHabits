package com.example.dayswithoutbadhabits.presentation

import com.example.dayswithoutbadhabits.domain.Card

sealed class NewUiState {

    data class AddAll(private val cards: List<Card>) : NewUiState()
    data class Add(private val card: Card) : NewUiState()
    data class Replace(private val position: Int, private val card: Card): NewUiState()
    data class Remove(private val position: Int): NewUiState()
}