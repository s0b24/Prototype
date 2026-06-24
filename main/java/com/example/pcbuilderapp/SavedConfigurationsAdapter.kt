package com.example.pcbuilderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SavedConfigurationsAdapter (
    private val items: List<SavedConfiguration>,
    private val onOpen: (SavedConfiguration) -> Unit,
    private val onDelete: (SavedConfiguration) -> Unit
) : RecyclerView.Adapter<SavedConfigurationsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentName = view.findViewById<TextView>(R.id.componentName)
        val components = view.findViewById<TextView>(R.id.componentsList)
        val deleteBtn = view.findViewById<ImageView>(R.id.deleteBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_configuration_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.componentName.text = item.name
        holder.components.text = item.components

        holder.itemView.setOnClickListener {
            onOpen(item)
        }

        holder.deleteBtn.setOnClickListener {
            onDelete(item)
        }
    }
    override fun getItemCount() = items.size
}
