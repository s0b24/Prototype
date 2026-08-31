package com.example.pcbuilderapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectComponentActivity : AppCompatActivity() {
    private lateinit var adapter: SelectComponentAdapter
    private lateinit var country: CountryEntity

    private var filteredComponentCard = mutableListOf<ComponentCard>()
    private var allComponentCards = mutableListOf<ComponentCard>()

    // Komponentu filtrēšanas aktivitātes aizvēršana un izvēlēto parametru datu nodošana, lai atjauninātu komponentu sarakstu
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {

            @Suppress("DEPRECATION")
            val minPrice = result.data?.getDoubleExtra("minPrice", 0.0) ?: 0.0
            val maxPrice = result.data?.getDoubleExtra("maxPrice", 0.0) ?: 0.0
            val filters = result.data?.getSerializableExtra("filters") as? HashMap<String, ArrayList<String>>
            val db = DatabaseProvider.get(this)
            country = CountryManager.getCountry(getSharedPreferences("CountryPreferences", MODE_PRIVATE))
            if (filters != null) {
                applyFilters(db, country.code, filters, minPrice, maxPrice)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_component)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSelectComponentsActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val country = CountryManager.getCountry(getSharedPreferences("CountryPreferences", MODE_PRIVATE))
        val type = intent.getStringExtra("type") ?: ""
        val db = DatabaseProvider.get(this)

        // Komponentu specifikāciju pārskats un komponenta izvēles apstrāde
        lifecycleScope.launch {
            allComponentCards = withContext(Dispatchers.IO) {
                db.getComponentCard(type, country.code).toMutableList()
            }
            filteredComponentCard = allComponentCards.toMutableList()

            adapter = SelectComponentAdapter(
                filteredComponentCard, country,

                viewComponent = { component ->
                    val intent = Intent(this@SelectComponentActivity, ComponentDetailsActivity::class.java)
                    intent.putExtra("component_id", component.id)
                    startActivity(intent) },

                addComponent = { component ->
                    lifecycleScope.launch {

                        val tdp = withContext(Dispatchers.IO) {
                            db.getComponentSpecs(component.id)["TDP (W)"]?.toIntOrNull() ?: 0
                        }

                        intent.putExtra("id", component.id)
                        intent.putExtra("type", component.type)
                        intent.putExtra("name", component.name)
                        intent.putExtra("price", component.averagePrice ?: 0.0)
                        intent.putExtra("tdp", tdp)

                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            )
            recyclerView.adapter = adapter
        }

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val filterBtn = findViewById<ImageView>(R.id.filterBtn)
        filterBtn.setOnClickListener {
            val intent = Intent(this, FilterComponentsActivity::class.java)
            intent.putExtra("type", type)
            launcher.launch(intent)
        }
    }

    private fun applyFilters(db: DatabaseHelper, countryCode: String, filters: Map<String, List<String>>, minPrice: Double, maxPrice: Double) {
        val filteredCards = allComponentCards.filter { card ->
            val price = db.getAveragePriceByCountry(card.id, countryCode) ?: 0.0

            filters.all { (key, values) ->
                card.specs[key] in values
            } && (minPrice == 0.0 || price >= minPrice) && (maxPrice == 0.0 || price <= maxPrice)
        }
        adapter.updateList(filteredCards)
    }
}