package com.example.pcbuilderapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComponentDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_details)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        val componentName = findViewById<TextView>(R.id.componentName)
        val componentPrice = findViewById<TextView>(R.id.componentPrice)
        val specsContainer = findViewById<LinearLayout>(R.id.specsContainer)
        val pricesContainer = findViewById<LinearLayout>(R.id.pricesContainer)

        backBtn.setOnClickListener {
            finish()
        }

        val componentId = intent.getIntExtra("component_id", -1)
        if (componentId == -1) return

        val region = CountryManager.getRegion(getSharedPreferences("settings", MODE_PRIVATE))
        val db = DatabaseProvider.get(this)

        lifecycleScope.launch {
            val component = withContext(Dispatchers.IO) {
                db.getComponentById(componentId)
            }

            val specs = withContext(Dispatchers.IO) {
                db.getComponentSpecs(componentId)
            }

            val averagePrice = withContext(Dispatchers.IO) {
                db.getAveragePriceByRegion(componentId, region.code)
            }

            val storePrices = withContext(Dispatchers.IO) {
                db.getStorePrices(componentId, region.code)
            }

            componentName.text = component.name
            componentPrice.text = "${averagePrice ?: "-"} ${region.currency}"

            specsContainer.removeAllViews()
            for (s in specs) {
                val view = layoutInflater.inflate(R.layout.item_specs_details, specsContainer, false)
                view.findViewById<TextView>(R.id.specName).text = SpecsFormatter.formatKey(s.key)
                view.findViewById<TextView>(R.id.specValue).text = SpecsFormatter.formatValue(s.key, s.value)
                specsContainer.addView(view)
            }

            pricesContainer.removeAllViews()
            for (p in storePrices) {
                val view = layoutInflater.inflate(R.layout.item_prices, pricesContainer, false)
                view.findViewById<TextView>(R.id.storeName).text = p.name
                view.findViewById<TextView>(R.id.storePrice).text = "${p.price ?: "-"} ${region.currency}"
                pricesContainer.addView(view)
            }
        }
    }
}