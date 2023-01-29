package com.example.dayswithoutbadhabits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.mainTextView)
        val resetButton = findViewById<Button>(R.id.resetButton)

        val viewModel =(application as ProvideViewModel).provideMainViewModel()

        viewModel.observe(this) {uiState ->
            uiState.apply(textView, resetButton)
        }

        resetButton.setOnClickListener {
            viewModel.reset()
        }

        viewModel.init(savedInstanceState == null)
    }
}