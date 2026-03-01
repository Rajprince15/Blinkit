package com.example.blinkit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blinkit.R
import com.example.blinkit.models.Address

class AddressAdapter(
    private val items: MutableList<Address>,
    private val onEdit: (Address) -> Unit,
    private val onDelete: (Address) -> Unit,
    private val onSetDefault: (Address) -> Unit
) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tvName = v.findViewById<android.widget.TextView>(R.id.tvName)
        private val tvAddress = v.findViewById<android.widget.TextView>(R.id.tvAddress)
        private val tvLabelDefault = v.findViewById<android.widget.TextView>(R.id.tvLabelDefault)
        private val btnEdit = v.findViewById<android.widget.ImageButton>(R.id.btnEdit)
        private val btnDelete = v.findViewById<android.widget.ImageButton>(R.id.btnDelete)

        fun bind(address: Address) {
            tvName.text = address.name
            tvAddress.text = "${address.line1}, ${address.line2}, ${address.city}, ${address.state} - ${address.zipcode}, ${address.country}"
            tvLabelDefault.visibility = if (address.isDefault) View.VISIBLE else View.GONE

            btnEdit.setOnClickListener { onEdit(address) }
            btnDelete.setOnClickListener { onDelete(address) }
            itemView.setOnClickListener {
                if (!address.isDefault) {
                    onSetDefault(address)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_address, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Address>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
