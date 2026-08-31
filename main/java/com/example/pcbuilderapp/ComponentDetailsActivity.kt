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

        val componentName = findViewById<TextView>(R.id.componentName)
        val componentPrice = findViewById<TextView>(R.id.componentPrice)
        val specsContainer = findViewById<LinearLayout>(R.id.specsContainer)
        val pricesContainer = findViewById<LinearLayout>(R.id.pricesContainer)

        val componentId = intent.getIntExtra("component_id", -1)
        if (componentId == -1) return

        val country = CountryManager.getCountry(getSharedPreferences("CountryPreferences", MODE_PRIVATE))
        val db = DatabaseProvider.get(this)

        // Detalizētas informācijas apstrāde par komponentiem, ieskaitot to detalizētos tehniskos parametrus un cenas elektronikas veikalos
        lifecycleScope.launch {
            val component = withContext(Dispatchers.IO) {
                db.getComponentById(componentId)
            }

            val specs = withContext(Dispatchers.IO) {
                db.getComponentSpecs(componentId)
            }

            val averagePrice = withContext(Dispatchers.IO) {
                db.getAveragePriceByCountry(componentId, country.code)
            }

            val storePrices = withContext(Dispatchers.IO) {
                db.getStorePrices(componentId, country.code)
            }

            componentName.text = component.name
            componentPrice.text = "${averagePrice?.let { price -> "%.2f".format(price) } ?: "-"} ${country.currency}"

            specsContainer.removeAllViews()
            for (spec in specs) {
                val view = layoutInflater.inflate(R.layout.item_specs_details, specsContainer, false)
                view.findViewById<TextView>(R.id.specName).text = SpecsFormatter.formatKey(spec.key)
                view.findViewById<TextView>(R.id.specValue).text = SpecsFormatter.formatValue(spec.key, spec.value)
                specsContainer.addView(view)
            }

            pricesContainer.removeAllViews()
            for (storePrice in storePrices) {
                val view = layoutInflater.inflate(R.layout.item_prices, pricesContainer, false)
                view.findViewById<TextView>(R.id.storeName).text = storePrice.name
                view.findViewById<TextView>(R.id.storePrice).text = "${storePrice.price ?: "-"} ${country.currency}"
                pricesContainer.addView(view)
            }
        }

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
    }
}