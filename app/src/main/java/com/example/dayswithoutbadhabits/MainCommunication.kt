package com.example.dayswithoutbadhabits

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface MainCommunication {

    interface Put {
        fun put(value: UiState)
    }

    interface Observe {
        fun observe(owner: LifecycleOwner, observer: Observer<UiState>)
    }

    interface Mutable : Put, Observe

    class Base(private val liveData: MutableLiveData<UiState>) : Mutable {

        override fun put(value: UiState) {
            liveData.value = value
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<UiState>) {
            liveData.observe(owner, observer)
        }
    }
}