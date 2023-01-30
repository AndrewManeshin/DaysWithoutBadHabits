package com.example.dayswithoutbadhabits

import android.content.SharedPreferences
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityZeroTest : AbstractUiTest() {

    override fun init(sharedPref: SharedPreferences) {
        sharedPref.edit().clear().apply()
    }

    @Test
    fun test_n_days_and_reset() {
        MainPage().run {
            mainText.view().check(matches(withText("0")))
            resetButton.view().check(matches(not(isDisplayed())))
        }
    }
}