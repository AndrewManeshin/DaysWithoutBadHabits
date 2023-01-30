package com.example.dayswithoutbadhabits

import android.app.Application
import androidx.lifecycle.MutableLiveData

class App : Application(), ProvideViewModel {

    private lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        val sharedPref = SharedPref.Factory(BuildConfig.DEBUG).make(this)
        viewModel = MainViewModel(
            MainRepository.Base(
                CacheDataSource.Base(
                    sharedPref
                ), Now.Base()
            ),
            MainCommunication.Base(MutableLiveData())
        )
    }

    override fun provideMainViewModel(): MainViewModel {
        return viewModel
    }
}

interface ProvideViewModel {
    fun provideMainViewModel(): MainViewModel
}