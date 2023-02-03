package com.example.dayswithoutbadhabits.domain

sealed class Card(
    private val id: Long,
    private val text: String,
    private val days: Int,
    private val editable: Boolean
) {

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, text, days, editable)

    interface Mapper<T> {
        fun map(id: Long, text: String, days: Int, editable: Boolean): T

        class Same(private val id: Long) : Mapper<Boolean> {
            override fun map(id: Long, text: String, days: Int, editable: Boolean) = id == this.id
        }

        class Duplicate(private val newText: String) : Mapper<Card> {
            override fun map(id: Long, text: String, days: Int, editable: Boolean) = if (days > 0) {
                if (editable) NonZeroDaysEdit(id, days, newText)
                else NonZeroDays(id, days, newText)
            } else if (days == 0) {
                if (editable) ZeroDaysEdit(id, newText)
                else ZeroDays(id, newText)
            } else {
                if (id == 0L) Add else Make
            }
        }

        class ChangeEditable : Mapper<Card> {
            override fun map(id: Long, text: String, days: Int, editable: Boolean) = if (days > 0) {
                if (editable) NonZeroDays(id, days, text)
                else NonZeroDaysEdit(id, days, text)
            } else if (days == 0) {
                if (editable) ZeroDays(id, text)
                else ZeroDaysEdit(id, text)
            } else {
                if (id == 0L) Add else Make
            }
        }

        class Reset(private val resetCard: ResetCard) : Mapper<Unit> {
            override fun map(id: Long, text: String, days: Int, editable: Boolean) =
                resetCard.resetCard(id)
        }

        class ResetDays : Mapper<Card> {
            override fun map(id: Long, text: String, days: Int, editable: Boolean) =
                ZeroDays(id, text)
        }
    }

    object Add : Card(0L, "", -1, false)

    object Make : Card(1L, "", -1, false)

    data class ZeroDays(private val id: Long, private val text: String) : Card(id, text, 0, false)

    data class ZeroDaysEdit(private val id: Long, private val text: String) :
        Card(id, text, 0, true)

    data class NonZeroDays(
        private val id: Long, private val days: Int, private val text: String
    ) : Card(id, text, days, false)

    data class NonZeroDaysEdit(
        private val id: Long, private val days: Int, private val text: String
    ) : Card(id, text, days, true)
}