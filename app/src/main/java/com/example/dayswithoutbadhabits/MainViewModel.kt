package com.example.dayswithoutbadhabits

import android.opengl.Visibility
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.*

class MainViewModel(
    private val repository: MainRepository,
    private val communication: MainCommunication.Mutable
) : ViewModel(), MainCommunication.Observe {

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            val days = repository.days()
            communication.put(
                if (days == 0)
                    UiState.ZeroDays
                else
                    UiState.NDays(days)
            )
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<UiState>) =
        communication.observe(owner, observer)
}

sealed class UiState {

    abstract fun apply(daysTextView: TextView, resetButton: Button)

    object ZeroDays : UiState() {
        override fun apply(daysTextView: TextView, resetButton: Button) {
            daysTextView.text = "0"
            resetButton.visibility = View.GONE
        }
    }

    data class NDays(private val days: Int) : UiState() {
        override fun apply(daysTextView: TextView, resetButton: Button) {
            daysTextView.text = days.toString()
            resetButton.visibility = View.VISIBLE
        }
    }
}

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

interface MainRepository {

    fun days(): Int
}