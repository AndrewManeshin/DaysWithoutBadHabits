package com.example.dayswithoutbadhabits.data

import com.example.dayswithoutbadhabits.Now
import com.example.dayswithoutbadhabits.domain.Card

interface CardMapper<T> {

    fun map(id: Long, countStartTime: Long, text: String): T

    class Base(private val now: Now) : CardMapper<Card> {

        override fun map(id: Long, countStartTime: Long, text: String): Card {
            val diff = now.time() - countStartTime
            val days = (diff / (24 * 3600 * 1000)).toInt()
            return if (days > 0)
                Card.NonZeroDays(id, days, text)
            else
                Card.ZeroDays(id, text)
        }
    }
}