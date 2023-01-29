package com.example.dayswithoutbadhabits

interface Now {

    fun time(): Long

    class Base : Now {

        override fun time() = System.currentTimeMillis()
    }
}