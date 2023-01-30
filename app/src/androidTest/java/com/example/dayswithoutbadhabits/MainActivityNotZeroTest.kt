package com.example.dayswithoutbadhabits

import android.content.SharedPreferences
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityNotZeroTest : AbstractUiTest() {

    override fun init(sharedPref: SharedPreferences) {
        CacheDataSource.Base(sharedPref).save(System.currentTimeMillis() - 17L * 24 * 3600 * 1000)
    }

    @Test
    fun test_n_days_and_reset() {
        MainPage().run {
            mainText.view().check(matches(withText("17")))
            resetButton.view().check(matches(isDisplayed()))
            resetButton.view().perform(click())
            mainText.view().check(matches(withText("0")))
            resetButton.view().check(matches(not(isDisplayed())))
        }
    }
}