package com.example.dayswithoutbadhabits.data

interface NewCacheDataSource {

    fun cards(): List<CardCache>
    fun addCard(id: Long, text: String)
    fun updateCard(id: Long, text: String)
    fun deleteCard(id: Long)
    fun resetCard(id: Long, countStartTime: Long)
}