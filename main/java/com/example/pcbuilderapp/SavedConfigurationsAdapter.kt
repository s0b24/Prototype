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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_saved_configuration_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items[position]

        viewHolder.componentName.text = item.name
        viewHolder.components.text = item.components

        viewHolder.itemView.setOnClickListener {
            onOpen(item)
        }

        viewHolder.deleteBtn.setOnClickListener {
            onDelete(item)
        }
    }
    override fun getItemCount() = items.size
}
