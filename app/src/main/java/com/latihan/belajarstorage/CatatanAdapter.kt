package com.latihan.belajarstorage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter untuk menampilkan daftar Catatan ke dalam RecyclerView.
 */
class CatatanAdapter : ListAdapter<Catatan, CatatanAdapter.CatatanViewHolder>(DiffCallback) {

    class CatatanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tvCatatanId)
        private val tvJudul: TextView = itemView.findViewById(R.id.tvCatatanJudul)
        private val tvIsi: TextView = itemView.findViewById(R.id.tvCatatanIsi)
        private val tvTanggal: TextView = itemView.findViewById(R.id.tvCatatanTanggal)

        fun bind(catatan: Catatan) {
            tvId.text = "ID: ${catatan.id}"
            tvJudul.text = "Judul: ${catatan.judul}"
            tvIsi.text = "Isi: ${catatan.isi}"
            tvTanggal.text = "Tanggal: ${catatan.tanggal}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatatanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_catatan, parent, false)
        return CatatanViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatatanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Catatan>() {
            override fun areItemsTheSame(oldItem: Catatan, newItem: Catatan): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Catatan, newItem: Catatan): Boolean {
                return oldItem == newItem
            }
        }
    }
}
