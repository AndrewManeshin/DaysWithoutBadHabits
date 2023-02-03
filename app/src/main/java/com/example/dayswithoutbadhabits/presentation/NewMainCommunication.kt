package com.example.dayswithoutbadhabits.presentation

import com.example.dayswithoutbadhabits.core.Communication
import com.example.dayswithoutbadhabits.core.SingleLiveEvent

interface NewMainCommunication {

    interface Put : Communication.Put<NewUiState>

    interface Observe: Communication.Observe<NewUiState>

    interface Mutable: Put, Observe

    class Base: Communication.Abstract<NewUiState>(SingleLiveEvent()), Mutable
}
