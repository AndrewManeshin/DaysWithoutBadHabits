package com.example.dayswithoutbadhabits.data

import com.example.dayswithoutbadhabits.Now
import com.example.dayswithoutbadhabits.domain.CRUDCards
import com.example.dayswithoutbadhabits.domain.Card

class NewRepository(
    private val cacheDataSource: NewCacheDataSource,
    private val now: Now,
    private val mapper: CardMapper<Card> = CardMapper.Base(now)
) : CRUDCards {

    override fun resetCard(id: Long) {
        cacheDataSource.resetCard(id, now.time())
    }

    override fun newCard(text: String): Card {
        val id = now.time()
        cacheDataSource.addCard(id, text)
        return Card.ZeroDays(id, text)
    }

    override fun cards(): List<Card> {
        val cacheList = cacheDataSource.cards()
        return cacheList.map { it.map(mapper) }
    }

    override fun updateCard(id: Long, newText: String) {
        cacheDataSource.updateCard(id, newText)
    }

    override fun deleteCard(id: Long) {
        cacheDataSource.deleteCard(id)
    }
}