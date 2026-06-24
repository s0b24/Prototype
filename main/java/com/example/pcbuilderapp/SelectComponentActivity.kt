package com.example.pcbuilderapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectComponentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_component)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSelectComponentsActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val country = CountryManager.getRegion(prefs)
        val type = intent.getStringExtra("type") ?: ""
        val db = DatabaseProvider.get(this)

        lifecycleScope.launch {
            val cards = withContext(Dispatchers.IO) {
                db.getComponentCard(type, country.code)
            }

            recyclerView.adapter = SelectComponentAdapter(
                cards, country,

                onComponent = { component ->
                    val intent = Intent(this@SelectComponentActivity, ComponentDetailsActivity::class.java)
                    intent.putExtra("component_id", component.id)
                    startActivity(intent) },

                onAdd = { component ->
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
        }
    }
}