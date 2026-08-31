package com.example.pcbuilderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SelectComponentAdapter (
    private val items: MutableList<ComponentCard>,
    private val country: CountryEntity,
    private val viewComponent: (ComponentCard) -> Unit,
    private val addComponent: (ComponentCard) -> Unit,
) : RecyclerView.Adapter<SelectComponentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentName: TextView = view.findViewById(R.id.componentName)
        val componentPrice: TextView = view.findViewById(R.id.componentPrice)
        val addBtn: ImageView = view.findViewById(R.id.addBtn)
        val specificationsGrid: GridLayout = view.findViewById(R.id.specificationsGrid)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_component_preview_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items[position]

        viewHolder.itemView.setOnClickListener {
            viewComponent(item)
        }

        viewHolder.addBtn.setOnClickListener {
            addComponent(item)
        }

        // Datu ievadīšana komponenta kartītē
        viewHolder.componentName.text = item.name
        viewHolder.componentPrice.text = PriceFormatter.format(item.averagePrice, country)
        viewHolder.specificationsGrid.removeAllViews()

        val context = viewHolder.itemView.context
        val inflater = LayoutInflater.from(context)
        val componentPrioritySpecs = SpecsPriority.getPrioritySpecifications(item.type)
        val filtered = item.specs.filter {
            it.key in componentPrioritySpecs
        }

        filtered.forEach { specs ->
            val view = inflater.inflate(R.layout.item_specs_priority, viewHolder.specificationsGrid, false)
            val specName = view.findViewById<TextView>(R.id.specName)
            val specValue = view.findViewById<TextView>(R.id.specValue)

            specName.text = SpecsFormatter.formatKey(specs.key)
            specValue.text = SpecsFormatter.formatValue(specs.key, specs.value)

            viewHolder.specificationsGrid.addView(view)
        }
    }
    override fun getItemCount() = items.size

    // Komponentu saraksta atjaunināšana
    fun updateList(newList: List<ComponentCard>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}