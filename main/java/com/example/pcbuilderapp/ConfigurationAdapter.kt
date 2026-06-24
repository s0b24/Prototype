package com.example.pcbuilderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConfigurationAdapter(
    private val items: List<ConfigurationItem>,
    private val region: CountryEntity,
    private val onAdd: (ConfigurationItem) -> Unit,
    private val onClear: (ConfigurationItem) -> Unit
) : RecyclerView.Adapter<ConfigurationAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentType = view.findViewById<TextView>(R.id.componentType)
        val componentName = view.findViewById<TextView>(R.id.componentName)
        val componentPrice = view.findViewById<TextView>(R.id.componentPrice)
        val actionBtn = view.findViewById<ImageView>(R.id.actionBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_component_type_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.componentType.text = item.name
        holder.componentName.text = item.selectedComponents ?: ""
        holder.componentPrice.text = PriceFormatter.format(item.price, region)

        holder.actionBtn.setImageResource(
            if (item.selectedComponents == null) R.drawable.plus else R.drawable.trash
        )

        holder.actionBtn.setOnClickListener {
            if (item.selectedComponents == null) onAdd(item) else onClear(item)
        }
    }
    override fun getItemCount() = items.size
}