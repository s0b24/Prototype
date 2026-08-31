package com.example.pcbuilderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InstructionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var InstructioniItems = listOf<InstructionItem>()

    fun updateList(list: List<InstructionItem>) {
        InstructioniItems = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when(InstructioniItems[position]) {
            is InstructionItem.Text -> 0
            is InstructionItem.Image -> 1
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)

        return if (viewType == 0) {
            val view = inflater.inflate(R.layout.item_text, viewGroup, false)
            TextVH(view)
        } else {
            val view = inflater.inflate(R.layout.item_image, viewGroup, false)
            ImageVH(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = InstructioniItems[position]

        when(viewHolder) {
            is TextVH -> viewHolder.attach(item as InstructionItem.Text)
            is ImageVH -> viewHolder.attach(item as InstructionItem.Image)
        }
    }

    override fun getItemCount() = InstructioniItems.size

    class TextVH(view: View) : RecyclerView.ViewHolder(view) {
        private val text = view.findViewById<TextView>(R.id.text)

        fun attach(item: InstructionItem.Text) {
            text.setText(item.text)
        }
    }

    class ImageVH(view: View) : RecyclerView.ViewHolder(view) {
        private val image = view.findViewById<ImageView>(R.id.image)

        fun attach(item: InstructionItem.Image) {
            image.setImageResource(item.image)
        }
    }
}