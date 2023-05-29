package com.example.scarlet.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.scarlet.R

class MainActivity : AppCompatActivity() {
    private lateinit var trainingLogBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trainingLogBtn = findViewById(R.id.trainingLogBtn)

        trainingLogBtn.setOnClickListener{
            val intent = Intent(this, TrainingLogsActivity::class.java)
            startActivity(intent)
        }

    }
}