package com.example.dayswithoutbadhabits.data

import com.example.dayswithoutbadhabits.BaseTest
import com.example.dayswithoutbadhabits.domain.Card
import junit.framework.TestCase.assertEquals
import org.junit.Test

class RepositoryTest : BaseTest() {

    @Test
    fun `test no cards`() {
        val now = FakeNow.Base()
        val cacheDataSource = FakeCacheDataSource(emptyList())
        val repository = NewRepository(cacheDataSource, now)

        val actual = repository.cards()
        val expected = emptyList<Card>()

        assertEquals(expected, actual)
    }

    @Test
    fun `test cards`() {
        val now = FakeNow.Base()
        val day = 24 * 3600 * 1000
        val sevenDays: Long = 7L * day
        val threeDays: Long = 3L * day
        now.addTime(sevenDays)
        val cacheDataSource = FakeCacheDataSource(
            listOf(
                CardCache(id = 0L, countStartTime = 0L, text = "a"),
                CardCache(id = threeDays, countStartTime = threeDays, text = "c"),
                CardCache(id = sevenDays, countStartTime = sevenDays, text = "b")
            )
        )
        val repository = NewRepository(cacheDataSource, now)

        val actual = repository.cards()
        val expected = listOf(
            Card.NonZeroDays(0, 7, "a"),
            Card.NonZeroDays(threeDays, 4, "c"),
            Card.ZeroDays(sevenDays, "b")
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `test new card`() {
        val now = FakeNow.Base()
        val day = 24 * 3600 * 1000
        val sevenDays: Long = 7L * day
        now.addTime(sevenDays)
        val cacheDataSource = FakeCacheDataSource(
            listOf(
                CardCache(id = 0L, countStartTime = 0L, text = "a")
            )
        )
        val repository = NewRepository(cacheDataSource, now)

        repository.newCard(text = "new habit")

        val actual = repository.cards()
        val expected = listOf(Card.NonZeroDays(0L, 7, "a"), Card.ZeroDays(sevenDays, "new habit"))
        assertEquals(expected, actual)
    }

    @Test
    fun `test update card`() {
        val now = FakeNow.Base()
        val cacheDataSource = FakeCacheDataSource(
            listOf(
                CardCache(id = 0, countStartTime = 0L, text = "a")
            )
        )
        val repository = NewRepository(cacheDataSource, now)

        repository.updateCard(id = 0L, "new habit")

        val actual = repository.cards()
        val expected = listOf(Card.ZeroDays(0L, "new habit"))
        assertEquals(expected, actual)
    }

    @Test
    fun `test delete card`() {
        val now = FakeNow.Base()
        val cacheDataSource = FakeCacheDataSource(
            listOf(
                CardCache(id = 10L, countStartTime = 10L, text = "b"),
                CardCache(id = 0L, countStartTime = 10L, text = "a"),
            )
        )
        val repository = NewRepository(cacheDataSource, now)

        repository.deleteCard(id = 10L)

        val actual = repository.cards()
        val expected = listOf(Card.ZeroDays(0L, "a"))
        assertEquals(expected, actual)
    }

    @Test
    fun `test reset card`() {
        val now = FakeNow.Base()
        val day = 24 * 3600 * 1000
        val sevenDays: Long = 7L * day
        now.addTime(sevenDays)
        val cacheDataSource = FakeCacheDataSource(
            listOf(
                CardCache(id = 0L, countStartTime = 0L, text = "a")
            )
        )
        val repository = NewRepository(cacheDataSource, now)

        var actual = repository.cards()
        var expected: List<Card> = listOf(Card.NonZeroDays(0L, 7, "a"))
        assertEquals(expected, actual)

        repository.resetCard(0L)

        actual = repository.cards()
        expected = listOf(Card.ZeroDays(id = 0L, text = "a"))
        assertEquals(expected, actual)
        assertEquals(
            CardCache(id = 0L, countStartTime = sevenDays, text = "a"), cacheDataSource.read()[0]
        )
    }
}

private class FakeCacheDataSource(
    list: List<CardCache>
) : NewCacheDataSource {

    private val cards = ArrayList<CardCache>()

    init {
        cards.addAll(list)
    }

    override fun read(): MutableList<CardCache> {
        return cards.toMutableList()
    }

    override fun save(list: MutableList<CardCache>) {
        cards.clear()
        cards.addAll(list)
    }
}