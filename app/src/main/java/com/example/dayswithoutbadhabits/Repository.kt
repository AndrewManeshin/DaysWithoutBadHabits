package com.example.dayswithoutbadhabits

interface MainRepository {

    fun days(): Int
    fun reset()

    class Base(
        private val cacheDataSource: CacheDataSource,
        private val now: Now
    ) : MainRepository {

        override fun days(): Int {
            val saved = cacheDataSource.time(-1)
            return if (saved == -1L) {
                reset()
                0
            } else {
                val diff = now.time() - saved
                (diff / DAY_MILLIS).toInt()
            }
        }

        override fun reset() =
            cacheDataSource.save(now.time())

        private companion object {
            private const val DAY_MILLIS = 24 * 3600 * 1000
        }
    }
}