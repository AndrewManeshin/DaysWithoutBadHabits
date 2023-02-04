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
        val list = cacheDataSource.read()
        val card = list.find { it.same(id) }!!
        val index = list.indexOf(card)
        val newCard = card.updateCountStartTime(now.time())
        list[index] = newCard
        cacheDataSource.save(list)
    }

    override fun newCard(text: String): Card {
        val list = cacheDataSource.read()
        val id = now.time()
        list.add(CardCache(id, id, text))
        cacheDataSource.save(list)
        return Card.ZeroDays(id, text)
    }

    override fun cards(): List<Card> {
        val cacheList = cacheDataSource.read()
        return cacheList.map { it.map(mapper) }
    }

    override fun updateCard(id: Long, newText: String) {
        val list = cacheDataSource.read()
        val card = list.find { it.same(id) }!!
        val index = list.indexOf(card)
        val newCard = card.updateText(newText)
        list[index] = newCard
        cacheDataSource.save(list)
    }

    override fun deleteCard(id: Long) {
        val list = cacheDataSource.read()
        val card = list.find { it.same(id) }!!
        list.remove(card)
        cacheDataSource.save(list)
    }
}