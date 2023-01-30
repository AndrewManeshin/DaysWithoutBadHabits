package com.example.dayswithoutbadhabits

import android.content.Intent
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class AbstractUiTest {

    @get:Rule
    val activityScenarioRule = lazyActivityScenarioRule<MainActivity>(launchActivity = false)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<App>()
        val sharedPref = SharedPref.Test().make(context)
        init(sharedPref)
        activityScenarioRule.launch(Intent(context, MainActivity::class.java))
    }

    protected abstract fun init(sharedPref: SharedPreferences)

    protected fun Int.view() = onView(withId(this))
}