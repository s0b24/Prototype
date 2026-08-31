package com.example.pcbuilderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnalyzeConfigurationAdapter(private val componentCardsList: List<ComponentCard>, private val country: CountryEntity, ) : RecyclerView.Adapter<AnalyzeConfigurationAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentName: TextView = view.findViewById(R.id.componentName)
        val componentType: TextView = view.findViewById(R.id.componentType)
        val componentPrice: TextView = view.findViewById(R.id.componentPrice)
        val specificationsGrid: GridLayout = view.findViewById(R.id.specificationsGrid)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_recommendation_component_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = componentCardsList[position]

        viewHolder.componentName.text = item.name
        viewHolder.componentType.text = item.type.replaceFirstChar { it.uppercase() }
        viewHolder.componentPrice.text = PriceFormatter.format(item.averagePrice, country)
        viewHolder.specificationsGrid.removeAllViews()

        val context = viewHolder.itemView.context
        val inflater = LayoutInflater.from(context)
        val componentPrioritySpecs = SpecsPriority.getPrioritySpecifications(item.type)

        val filtered = item.specs.filter {
            it.key in componentPrioritySpecs
        }

        filtered.forEach { spec ->
            val view = inflater.inflate(R.layout.item_specs_priority, viewHolder.specificationsGrid, false)
            val specName = view.findViewById<TextView>(R.id.specName)
            val specValue = view.findViewById<TextView>(R.id.specValue)

            specName.text = SpecsFormatter.formatKey(spec.key)
            specValue.text = SpecsFormatter.formatValue(spec.key, spec.value)

            viewHolder.specificationsGrid.addView(view)
        }
    }
    override fun getItemCount() = componentCardsList.size
}