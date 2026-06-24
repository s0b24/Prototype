package com.example.pcbuilderapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val regionBtn = findViewById<TextView>(R.id.chooseRegionBtn)
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)

        updateRegion(regionBtn, prefs)
        setupRegion(regionBtn,prefs)
        setupCards()
    }

    private fun updateRegion(regionBtn: TextView, prefs: SharedPreferences) {
        val region = CountryManager.getRegion(prefs)
        regionBtn.text = "${region.name} / ${region.currency}"
    }

    private fun setupRegion(regionBtn: TextView, prefs: android.content.SharedPreferences) {
        regionBtn.setOnClickListener {
            val popupMenu = PopupMenu(this, regionBtn)

            CountryManager.countries.forEachIndexed { index, region ->
                popupMenu.menu.add(0, index, 0, "${region.name} / ${region.currency}")
            }

            popupMenu.setOnMenuItemClickListener { item ->
                val selected = CountryManager.countries.getOrNull(item.itemId) ?: return@setOnMenuItemClickListener false
                prefs.edit { putString("country", selected.code) }
                updateRegion(regionBtn, prefs)
                true
            }
            popupMenu.show()
        }
    }

    private fun setupCards() {
        findViewById<MaterialCardView>(R.id.chooseComponents).setOnClickListener {
            startActivity(Intent(this, ConfigurationActivity::class.java))
        }
        findViewById<MaterialCardView>(R.id.viewConfiguration).setOnClickListener {
            startActivity(Intent(this, SavedConfigurationsActivity::class.java))
        }
        findViewById<MaterialCardView>(R.id.viewAssemblyInstruction).setOnClickListener {
            startActivity(Intent(this, AssemblyInstructionActivity::class.java))
        }
    }
}