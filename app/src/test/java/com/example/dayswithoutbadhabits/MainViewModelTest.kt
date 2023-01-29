package com.example.dayswithoutbadhabits

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {

    @Test
    fun `test 0 days and reinit`() {
        val repository = FakeRepository.Base(0)
        val communication = FakeMainCommunication.Base()
        val viewModel = MainViewModel(repository, communication)
        viewModel.init(isFirstRun = true)

        assertEquals(true, communication.checkCalledCounts(1))
        assertEquals(true, communication.isSame(UiState.ZeroDays))

        viewModel.init(false)

        assertEquals(true, communication.checkCalledCounts(1))
    }

    @Test
    fun `test N days and reinit`() {
        val repository = FakeRepository.Base(5)
        val communication = FakeMainCommunication.Base()
        val viewModel = MainViewModel(repository, communication)

        viewModel.init(true)

        assertEquals(true, communication.checkCalledCounts(1))
        assertEquals(true, communication.isSame(UiState.NDays(days = 5)))

        viewModel.init(false)

        assertEquals(true, communication.checkCalledCounts(1))
    }

    @Test
    fun `test reset`() {
        val repository = FakeRepository.Base(5)
        val communication = FakeMainCommunication.Base()
        val viewModel = MainViewModel(repository, communication)

        viewModel.init(true)
        assertEquals(true, communication.checkCalledCounts(1))
        assertEquals(true, communication.isSame(UiState.NDays(days = 5)))

        viewModel.reset()
        assertEquals(true, repository.resetCalledCount(1))
        assertEquals(true, communication.checkCalledCounts(2))
        assertEquals(true, communication.isSame(UiState.ZeroDays))
    }
}

private interface FakeRepository : MainRepository {

    fun resetCalledCount(count: Int): Boolean

    class Base(
        private val days: Int
    ) : FakeRepository {

        private var resetCalledCount = 0

        override fun resetCalledCount(count: Int) = count == resetCalledCount

        override fun reset() {
            resetCalledCount++
        }

        override fun days() = days
    }
}


private interface FakeMainCommunication : MainCommunication.Mutable {

    fun checkCalledCounts(count: Int): Boolean
    fun isSame(uiState: UiState): Boolean

    class Base : FakeMainCommunication {

        private lateinit var state: UiState
        private var callCount = 0

        override fun checkCalledCounts(count: Int) = count == callCount

        override fun isSame(uiState: UiState) = state.equals(uiState)

        override fun observe(owner: LifecycleOwner, observer: Observer<UiState>) = Unit

        override fun put(value: UiState) {
            callCount++
            state = value
        }
    }
}