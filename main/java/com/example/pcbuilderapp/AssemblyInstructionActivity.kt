package com.example.pcbuilderapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AssemblyInstructionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assembly_instruction)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
    }
}