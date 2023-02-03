package com.example.dayswithoutbadhabits.domain

import junit.framework.TestCase.assertEquals
import org.junit.Test

class InteractorTest {

    @Test
    fun `test initial less max count`() {
        val repository = FakeRepository(listOf(Card.ZeroDays(1L, "a")))
        val interactor = NewMainInteractor.Base(repository, maxItemsCount = 2)

        val actual = interactor.cards()
        val expected = listOf(Card.ZeroDays(1L, "a"), Card.Add)
        assertEquals(expected, actual)
    }

    @Test
    fun `test initial equals max count`() {
        val repository = FakeRepository(listOf(Card.ZeroDays(1L, "a"), Card.ZeroDays(2L, "b")))
        val interactor = NewMainInteractor.Base(repository, maxItemsCount = 2)

        val actual = interactor.cards()
        val expected = listOf(Card.ZeroDays(1L, "a"), Card.ZeroDays(2L, "b"))
        assertEquals(expected, actual)
    }

    @Test
    fun `test can add new card`() {
        val repository = FakeRepository(listOf(Card.ZeroDays(1L, "a")))
        val interactor = NewMainInteractor.Base(repository, maxItemsCount = 2)

        val actual = interactor.canAddNewCard()
        val expected = true
        assertEquals(expected, actual)
    }

    @Test
    fun `test cannot add new card`() {
        val repository = FakeRepository(listOf(Card.ZeroDays(1L, "a"), Card.ZeroDays(2L, "b")))
        val interactor = NewMainInteractor.Base(repository, maxItemsCount = 2)

        val actual = interactor.canAddNewCard()
        val expected = false
        assertEquals(expected, actual)
    }

    @Test
    fun `test new card`() {
        val repository = FakeRepository(listOf(Card.ZeroDays(1L, "a")))
        val interactor = NewMainInteractor.Base(repository, maxItemsCount = 2)

        val actual = interactor.newCard("new")
        val expected = Card.ZeroDays(7L, "new")
        assertEquals(expected, actual)

        val cardsActual = interactor.cards()
        val cardsExpected = listOf(Card.ZeroDays(1L, "a"), Card.ZeroDays(7L, "new"))
        assertEquals(cardsExpected, cardsActual)
    }


    @Test
    fun `test delete card`() {
        val repository = FakeRepository(listOf(Card.ZeroDays(1L, "a"), Card.ZeroDays(2L, "b")))
        val interactor = NewMainInteractor.Base(repository, maxItemsCount = 2)

        interactor.deleteCard(2L)

        val actual = interactor.cards()
        val expected = listOf(Card.ZeroDays(1L, "a"), Card.Add)
        assertEquals(expected, actual)
    }

    @Test
    fun `test update card`() {
        val repository = FakeRepository(listOf(Card.ZeroDays(1L, "a"), Card.ZeroDays(2L, "b")))
        val interactor = NewMainInteractor.Base(repository, maxItemsCount = 2)

        interactor.updateCard(1L, "c")

        val actual = interactor.cards()
        val expected = listOf(Card.ZeroDays(1L, "c"), Card.ZeroDays(2L, "b"))
        assertEquals(expected, actual)
    }

    @Test
    fun `test reset card`() {
        val repository =
            FakeRepository(listOf(Card.NonZeroDays(1L, 12, "a"), Card.NonZeroDays(2L, 13, "b")))
        val interactor = NewMainInteractor.Base(repository, maxItemsCount = 2)

        interactor.resetCard(1L)

        val actual = interactor.cards()
        val expected = listOf(Card.ZeroDays(1L, "a"), Card.NonZeroDays(2L, 13, "b"))
        assertEquals(expected, actual)
    }
}

private class FakeRepository(private val items: List<Card>) : Repository {

    private val list: MutableList<Card> = ArrayList()

    init {
        list.addAll(items)
    }

    override fun cards(): List<Card> {
        return list
    }

    override fun newCard(text: String): Card {
        val newCard = Card.ZeroDays(7L, text)
        list.add(newCard)
        return newCard
    }

    override fun deleteCard(id: Long) {
        val item = list.find { it.map(Card.Mapper.Same(id)) }
        list.remove(item)
    }

    override fun updateCard(id: Long, text: String) {
        val item = list.find { it.map(Card.Mapper.Same(id)) }
        val index = list.indexOf(item)
        val newItem = item!!.map(Card.Mapper.Duplicate(text))
        list[index] = newItem
    }

    override fun resetCard(id: Long) {
        val item = list.find { it.map(Card.Mapper.Same(id)) }
        val index = list.indexOf(item)
        val newItem = item!!.map(Card.Mapper.ResetDays())
        list[index] = newItem
    }
}