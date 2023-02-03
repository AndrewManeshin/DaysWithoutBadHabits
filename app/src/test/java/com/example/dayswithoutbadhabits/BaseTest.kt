package com.example.dayswithoutbadhabits

abstract class BaseTest {

    protected interface FakeNow : Now {

        fun addTime(diff: Long)

        class Base : FakeNow {

            private var time = 0L

            override fun time(): Long = time

            override fun addTime(diff: Long) {
                this.time += diff
            }
        }
    }
}