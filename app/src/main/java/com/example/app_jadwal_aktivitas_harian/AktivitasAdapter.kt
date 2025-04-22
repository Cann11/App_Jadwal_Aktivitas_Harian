package com.example.app_jadwal_aktivitas_harian

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_jadwal_aktivitas_harian.databinding.ItemAktivitasBinding

class AktivitasAdapter(private val listAktivitas: List<aktivitas>) :
    RecyclerView.Adapter<AktivitasAdapter.AktivitasViewHolder>() {

    inner class AktivitasViewHolder(val binding: ItemAktivitasBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AktivitasViewHolder {
        val binding = ItemAktivitasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AktivitasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AktivitasViewHolder, position: Int) {
        val aktivitas = listAktivitas[position]
        with(holder.binding) {
            tvTanggal.text = "📅 Tanggal: ${aktivitas.tanggal}"
            tvWaktu.text = "⏰ Waktu: ${aktivitas.waktu}"
            tvJudul.text = "📝 Judul: ${aktivitas.judul}"
        }
    }

    override fun getItemCount(): Int = listAktivitas.size
}
