package com.example.pcbuilderapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InstructionActivity : AppCompatActivity() {
    private lateinit var adapter: InstructionAdapter
    private var currentStep = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instruction)

        val title = findViewById<TextView>(R.id.title)

        adapter = InstructionAdapter()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewInstructionActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fun showStep() {
            val step = InstructionData.getSteps(this)[currentStep]
            title.text = step.title
            adapter.updateList(step.items)
        }

        showStep()

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val nextStepBtn = findViewById<Button>(R.id.nextStepBtn)
        nextStepBtn.setOnClickListener {
            if(currentStep < InstructionData.getSteps(this).lastIndex) {
                currentStep++
                showStep()
            }
        }

        val previousStepBtn = findViewById<Button>(R.id.previousStepBtn)
        previousStepBtn.setOnClickListener {
            if(currentStep > 0) {
                currentStep--
                showStep()
            }
        }
    }
}