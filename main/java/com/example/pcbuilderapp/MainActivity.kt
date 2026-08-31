package com.example.pcbuilderapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.LocaleList
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.card.MaterialCardView
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import org.intellij.lang.annotations.Language

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val countryBtn = findViewById<TextView>(R.id.chooseCountryBtn)
        val country = getSharedPreferences("CountryPreferences", MODE_PRIVATE)

        updateCountry(countryBtn, country)
        setupCountry(countryBtn,country)
        setupCards()
    }

    private fun updateCountry(countryBtn: TextView, sharedPreferences: SharedPreferences) {
        val country = CountryManager.getCountry(sharedPreferences)
        countryBtn.text = "${country.name} / ${country.currency}"
    }

    private fun setupCountry(countryBtn: TextView, sharedPreferences: android.content.SharedPreferences) {
        countryBtn.setOnClickListener {
            val popupMenu = PopupMenu(this, countryBtn)

            CountryManager.countries.forEachIndexed { index, region ->
                popupMenu.menu.add(0, index, 0, "${region.name} / ${region.currency}")
            }

            popupMenu.setOnMenuItemClickListener { item ->
                val selected = CountryManager.countries.getOrNull(item.itemId) ?: return@setOnMenuItemClickListener false
                sharedPreferences.edit { putString("country", selected.code) }
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(selected.language))
                updateCountry(countryBtn, sharedPreferences)
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
            startActivity(Intent(this, InstructionActivity::class.java))
        }
    }
}