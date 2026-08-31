package com.example.pcbuilderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfigurationActivity : AppCompatActivity() {
    private lateinit var adapter: ConfigurationAdapter
    private lateinit var compatibilityValue: TextView
    private lateinit var compatibilityIssuesDescription: TextView
    private lateinit var totalPrice: TextView
    private lateinit var totalTdp: TextView
    private lateinit var country: CountryEntity
    private val selectedComponents = mutableMapOf<String, ComponentSpecs>()
    private var configurationItems = mutableListOf<ConfigurationItem>()
    private var configurationId: Int = -1
    private val componentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != RESULT_OK) return@registerForActivityResult

        val id = result.data?.getIntExtra("id", -1) ?: -1
        val type = result.data?.getStringExtra("type") ?: return@registerForActivityResult
        val name = result.data?.getStringExtra("name")
        val tdp = result.data?.getIntExtra("tdp", 0) ?: 0
        val price = result.data?.getDoubleExtra("price", 0.0)

        val item = configurationItems.find {it.type == type}

        item?.apply {
            componentId = id
            selectedComponents = name
            this.price = price
            this.tdp = tdp
        }

        val db = DatabaseProvider.get(this)

        lifecycleScope.launch {
            val specs = withContext(Dispatchers.IO) {
                db.getComponentSpecs(id)
            }

            selectedComponents[type] = ComponentSpecs(
                id = id,
                type = type,
                name = name ?: "",
                specs = specs,
                tdp = tdp
            )

            adapter.notifyDataSetChanged()
            updateStats()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        totalPrice = findViewById(R.id.totalPriceValue)
        totalTdp = findViewById(R.id.totalTdpValue)
        compatibilityValue = findViewById(R.id.compatibilityValue)
        compatibilityIssuesDescription = findViewById(R.id.compatibilityIssuesDescription)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSelectComponentsActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)

        configurationItems.addAll(listOf(
            ConfigurationItem("cpu",getString(R.string.cpu)),
            ConfigurationItem("cpu_cooler",getString(R.string.cpu_cooler)),
            ConfigurationItem("gpu",getString(R.string.gpu)),
            ConfigurationItem("motherboard",getString(R.string.motherboard)),
            ConfigurationItem("ram",getString(R.string.ram)),
            ConfigurationItem("storage",getString(R.string.storage)),
            ConfigurationItem("psu",getString(R.string.psu)),
            ConfigurationItem("case",getString(R.string.pc_case)),
            ConfigurationItem("case_cooler",getString(R.string.case_cooler))))

        country = CountryManager.getCountry(getSharedPreferences("CountryPreferences", MODE_PRIVATE))

        adapter = ConfigurationAdapter(
            configurationItems = configurationItems, country,

            onAdd = { item ->
                val selectedId = HashMap(selectedComponents.mapValues { it.value.id })
                val intent = Intent(this, SelectComponentActivity::class.java)
                intent.putExtra("type", item.type)
                intent.putExtra("selected_id", selectedId)
                componentLauncher.launch(intent) },

            onClear = { item ->
                item.selectedComponents = null
                item.componentId = null
                item.price = null
                item.tdp = null

                selectedComponents.remove(item.type)
                adapter.notifyDataSetChanged()
                updateStats()})

        recyclerView.adapter = adapter

        configurationId = intent.getIntExtra("config_id", -1)
        if (configurationId != -1) loadConfiguration(configurationId)
        updateStats()

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        val saveConfigurationBtn = findViewById<ImageView>(R.id.saveConfigurationBtn)
        saveConfigurationBtn.setOnClickListener {
            val db = DatabaseProvider.get(this)
            val totalPrice = configurationItems.sumOf { it.price ?: 0.0 }
            db.saveConfiguration(configurationId, selectedComponents, totalPrice)
            Toast.makeText(this,getString(R.string.save_text), Toast.LENGTH_SHORT).show()
        }

        val analyzeConfigurationBtn = findViewById<Button>(R.id.analyzeConfigurationBtn)
        analyzeConfigurationBtn.setOnClickListener {
            ConfigurationAnalysisResults.clear()
            val db = DatabaseProvider.get(this)
            val analyze = AnalyzeConfiguration(this)
            val maxCpuValues = db.getMaxCpuSpecsValues()
            val maxGpuValues = db.getMaxGpuSpecsValues()
            val cpu = selectedComponents["cpu"]
            val gpu = selectedComponents["gpu"]

            if (cpu != null && gpu != null) {
                val cpuScore = analyze.calculateCpuScore(cpu.specs, maxCpuValues)
                val gpuScore = analyze.calculateGpuScore(gpu.specs, maxGpuValues)
                val bottleneck = analyze.calculateCpuGpuBottleneck(cpuScore, gpuScore)
                val balance = analyze.getBottleneckStatus(bottleneck)

                ConfigurationAnalysisResults.cpuScore = cpuScore
                ConfigurationAnalysisResults.gpuScore = gpuScore
                ConfigurationAnalysisResults.bottleneck = bottleneck
                ConfigurationAnalysisResults.bottleneckResult = balance
            }
            val intent = Intent(this, AnalyzeConfigurationActivity::class.java)
            intent.putExtra("selectedComponents", HashMap(selectedComponents))
            startActivity(intent)
        }
    }

    private fun loadConfiguration(configurationId: Int) {
        val db = DatabaseProvider.get(this)
        val components = db.getSavedConfigurationsItems(configurationId)

        components.forEach { component ->
            val item = configurationItems.find { it.type == component.type }

            item?.selectedComponents = component.name
            item?.componentId = component.id
            item?.tdp = component.tdp
            item?.price = db.getAveragePriceByCountry(component.id, country.code)

            selectedComponents[component.type] = component
        }
        adapter.notifyDataSetChanged()
        updateStats()
    }

    private fun updateStats() {
        val hasComponent = configurationItems.any { it.componentId != null }
        val valueTotalPrice = configurationItems.sumOf { it.price ?: 0.0 }
        val valueTotalTdp = configurationItems.sumOf { it.tdp ?: 0 }

        val compatibilityData = CheckCompatibility.checkAll(this, selectedComponents)
        val isCompatible = compatibilityData.isEmpty()

        totalPrice.text = if (hasComponent) PriceFormatter.format(valueTotalPrice, country) else "-"
        totalTdp.text = if (hasComponent) "${valueTotalTdp}W" else "-"

        compatibilityValue.text = if (isCompatible) "Ok" else getString(R.string.no)

        if (isCompatible) {
            compatibilityValue.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            compatibilityValue.setTextColor(ContextCompat.getColor(this, R.color.red))
        }

        compatibilityIssuesDescription.text = if (isCompatible) "" else compatibilityData.joinToString("\n")
    }
}