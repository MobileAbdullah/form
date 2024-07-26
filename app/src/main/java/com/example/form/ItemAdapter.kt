package com.example.form

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val context: Context, private var items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val originalItems = items.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name

        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Sil")
                .setMessage("Bu öğeyi silmek istediğinizden emin misiniz?")
                .setPositiveButton("Evet") { _, _ ->
                    (context as MainActivity).dbHelper.deleteItem(item.id)
                    updateItems((context as MainActivity).dbHelper.getAllItems())
                }
                .setNegativeButton("Hayır", null)
                .show()
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FormActivity::class.java).apply {
                putExtra("item_id", item.id)
                putExtra("name", item.name)
                putExtra("surname", item.surname)
                putExtra("address", item.address)
                putExtra("phone", item.phone)
                putExtra("department", item.department)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<Item>) {
        items = newItems
        originalItems.clear()
        originalItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        val filteredList = originalItems.filter { it.name.contains(query, ignoreCase = true) }
        updateItems(filteredList)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvName)
        val deleteButton: Button = view.findViewById(R.id.btnDelete)
    }
}