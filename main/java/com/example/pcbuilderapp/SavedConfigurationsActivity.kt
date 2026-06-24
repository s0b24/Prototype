package com.example.pcbuilderapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedConfigurationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_configurations)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.savedConfigurations)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = DatabaseProvider.get(this)
        val configurations = db.getSavedConfigurations()

        recyclerView.adapter = SavedConfigurationsAdapter(
            configurations,

            onOpen = {
                val intent = Intent(this, ConfigurationActivity::class.java)
                intent.putExtra("config_id", it.id)
                startActivity(intent)
            },

            onDelete = {
                db.deleteConfiguration(it.id)
                recreate()
            }
        )
    }
}