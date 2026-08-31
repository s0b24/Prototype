package com.example.pcbuilderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.button.MaterialButton

class FilterComponentsAdapter (private val filters: Map<String, List<String>>) : RecyclerView.Adapter<FilterComponentsAdapter.ViewHolder>() {
    private val selectedFilters = mutableMapOf<String, MutableSet<String>>()
    private val items = filters.toList().toMutableList()

    fun getSelectedFilters(): HashMap<String, ArrayList<String>> {
        val data = HashMap<String, ArrayList<String>>()

        selectedFilters.forEach { (key, values)->
            data[key] = ArrayList(values)
        }
        return data
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val specName = view.findViewById<TextView>(R.id.specName)
        val specContainer = view.findViewById<FlexboxLayout>(R.id.specsContainer)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_filter, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items[position]
        val name = item.first
        val values = item.second

        viewHolder.specName.text = name
        viewHolder.specContainer.removeAllViews()

        // Filtrēšanas pogas izveide un to apstrāde
        values.forEach { value ->
            val button = LayoutInflater.from(viewHolder.itemView.context).inflate(R.layout.item_filter_button, viewHolder.specContainer, false) as MaterialButton
            button.text = value

            button.alpha =
                if (selectedFilters[name]?.contains(value) == true) {
                    0.3f
                } else {
                    1f
                }

            button.setOnClickListener {
                val set = selectedFilters.getOrPut(name) {
                    mutableSetOf()
                }

                if (set.contains(value)) {
                    set.remove(value)

                    if (set.isEmpty()) {
                        selectedFilters.remove(name)
                    }
                    button.alpha = 1f

                } else {
                    set.add(value)
                    button.alpha = 0.3f
                }
            }
            viewHolder.specContainer.addView(button)
        }
    }
    override fun getItemCount() = items.size
}