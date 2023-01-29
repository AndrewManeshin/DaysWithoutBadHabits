package com.example.dayswithoutbadhabits

import junit.framework.TestCase.assertEquals
import org.junit.Test

class MainRepositoryTest {

    @Test
    fun `no days`() {
        val cacheDataSource = FakeDataSource()
        val now = FakeNow.Base()
        val repository = MainRepository.Base(cacheDataSource, now)
        now.addTime(1700)
        val actual = repository.days()
        assertEquals(0, actual)
        assertEquals(1700, cacheDataSource.time(-1))
    }

    @Test
    fun `some days`() {
        val cacheDataSource = FakeDataSource()
        val now = FakeNow.Base()
        val day = 24 * 3600 * 1000
        cacheDataSource.save(2L * day)
        now.addTime(7L * day)
        val repository = MainRepository.Base(cacheDataSource, now)
        val actual = repository.days()
        val expected = 7L
        assertEquals(expected, actual)
    }

    @Test
    fun `test reset`() {
        val cacheDataSource = FakeDataSource()
        val now = FakeNow.Base()
        val repository = MainRepository.Base(cacheDataSource, now)
        now.addTime(54321)
        repository.reset()
        assertEquals(54321, cacheDataSource.time(-1))
    }
}

private interface FakeNow : Now {

    fun addTime(diff: Long)

    class Base : FakeNow {

        private var time = 0L

        override fun time(): Long = time

        override fun addTime(diff: Long) {
            this.time += diff
        }
    }
}

private class FakeDataSource : CacheDataSource {

    private var time = -100L

    override fun save(time: Long) {
        this.time = time
    }

    override fun time(default: Long): Long =
        if (time == -100L)
            default
        else
            time
}