package com.example.pcbuilderapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AnalyzeConfigurationActivity : AppCompatActivity() {
    private lateinit var country: CountryEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze_configuration)

        val cpuScore = findViewById<TextView>(R.id.cpuScoreValue)
        val gpuScore = findViewById<TextView>(R.id.gpuScoreValue)
        val bottleneck = findViewById<TextView>(R.id.bottleneckValue)
        val balanceStatus = findViewById<TextView>(R.id.balanceValue)
        val balanceDescription = findViewById<TextView>(R.id.balanceDescriptionText)

        val result = ConfigurationAnalysisResults

        cpuScore.text = "${String.format("%.2f", result.cpuScore)}"
        gpuScore.text = "${String.format("%.2f", result.gpuScore)}"
        bottleneck.text = "${String.format("%.2f", result.bottleneck)} %"

        result.bottleneckResult?.let { balanceData ->
            balanceStatus.text = "${balanceData.status}"
            balanceDescription.text = balanceData.description
        }

        val db = DatabaseProvider.get(this)
        val analyzeConfiguration = AnalyzeConfiguration(this)

        @Suppress("DEPRECATION")
        val selectedComponents = intent.getSerializableExtra("selectedComponents") as? Map<String, ComponentSpecs> ?: hashMapOf()

        country = CountryManager.getCountry(getSharedPreferences("CountryPreferences", MODE_PRIVATE))

        // Konfigurācijas komponentu jaunināšanas ieteikumu attēlošana
        val findRecommendationsBtn = findViewById<Button>(R.id.findRecommendationsBtn)
        findRecommendationsBtn.setOnClickListener {
            val cpuBudget = findViewById<EditText>(R.id.cpuBudgetValue).text.toString().toDoubleOrNull()
            val gpuBudget = findViewById<EditText>(R.id.gpuBudgetValue).text.toString().toDoubleOrNull()
            val ramBudget = findViewById<EditText>(R.id.ramBudgetValue).text.toString().toDoubleOrNull()
            val recommendations = analyzeConfiguration.getComponentsRecommendation(db, selectedComponents, country.code, cpuBudget, gpuBudget, ramBudget)

            NewRecommendedComponents.list = recommendations

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewRecommendations)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = AnalyzeConfigurationAdapter(NewRecommendedComponents.list, country)
        }

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
    }
}