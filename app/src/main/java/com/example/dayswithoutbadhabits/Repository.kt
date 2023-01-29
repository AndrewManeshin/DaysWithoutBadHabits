package com.example.dayswithoutbadhabits

interface MainRepository {

    fun days(): Int
    fun reset()
}