package com.example.pcbuilderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SelectComponentAdapter (
    private val items: List<ComponentCard>,
    private val region: CountryEntity,
    private val onComponent: (ComponentCard) -> Unit,
    private val onAdd: (ComponentCard) -> Unit,
) : RecyclerView.Adapter<SelectComponentAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentName: TextView = view.findViewById(R.id.componentName)
        val componentPrice: TextView = view.findViewById(R.id.componentPrice)
        val addBtn: ImageView = view.findViewById(R.id.addBtn)
        val specificationsGrid: GridLayout = view.findViewById(R.id.specificationsGrid)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_component_preview_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.setOnClickListener {
            onComponent(item)
        }

        holder.addBtn.setOnClickListener {
            onAdd(item)
        }

        holder.componentName.text = item.name
        holder.componentPrice.text = PriceFormatter.format(item.averagePrice, region)
        holder.specificationsGrid.removeAllViews()

        val context = holder.itemView.context
        val inflater = LayoutInflater.from(context)
        val allowed = SpecsPriority.getPrioritySpecifications(item.type)
        val filtered = item.specs.filter {
            it.key in allowed
        }

        filtered.forEach { spec ->
            val view = inflater.inflate(R.layout.item_specs_priority, holder.specificationsGrid, false)
            val specName = view.findViewById<TextView>(R.id.specName)
            val specValue = view.findViewById<TextView>(R.id.specValue)

            specName.text = SpecsFormatter.formatKey(spec.key)
            specValue.text = SpecsFormatter.formatValue(spec.key, spec.value)

            holder.specificationsGrid.addView(view)
        }
    }
    override fun getItemCount() = items.size
}