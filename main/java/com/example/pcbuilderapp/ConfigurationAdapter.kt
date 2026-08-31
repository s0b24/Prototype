package com.example.pcbuilderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConfigurationAdapter(
    private val configurationItems: List<ConfigurationItem>,
    private val country: CountryEntity,
    private val onAdd: (ConfigurationItem) -> Unit,
    private val onClear: (ConfigurationItem) -> Unit
) : RecyclerView.Adapter<ConfigurationAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentType = view.findViewById<TextView>(R.id.componentType)
        val componentName = view.findViewById<TextView>(R.id.componentName)
        val componentPrice = view.findViewById<TextView>(R.id.componentPrice)
        val actionBtn = view.findViewById<ImageView>(R.id.actionBtn)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_component_type_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = configurationItems[position]

        viewHolder.componentType.text = item.name
        viewHolder.componentName.text = item.selectedComponents ?: ""
        viewHolder.componentPrice.text = PriceFormatter.format(item.price, country)

        viewHolder.actionBtn.setImageResource(
            if (item.selectedComponents == null) R.drawable.plus else R.drawable.trash
        )

        viewHolder.actionBtn.setOnClickListener {
            if (item.selectedComponents == null) onAdd(item) else onClear(item)
        }
    }
    override fun getItemCount() = configurationItems.size
}