package com.example.app_jadwal_aktivitas_harian

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_jadwal_aktivitas_harian.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val aktivitasList = mutableListOf<aktivitas>()
    private lateinit var adapter: AktivitasAdapter

    private var tanggalDipilih = ""
    private var waktuDipilih = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AktivitasAdapter(aktivitasList)
        binding.rvAktivitas.layoutManager = LinearLayoutManager(this)
        binding.rvAktivitas.adapter = adapter

        loadData()

        binding.btnTanggal.setOnClickListener {
            val kalender = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                tanggalDipilih = "$day/${month + 1}/$year"
                binding.btnTanggal.text = tanggalDipilih
            }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnWaktu.setOnClickListener {
            val kalender = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                waktuDipilih = String.format("%02d:%02d", hour, minute)
                binding.btnWaktu.text = waktuDipilih
            }, kalender.get(Calendar.HOUR_OF_DAY), kalender.get(Calendar.MINUTE), true).show()
        }

        binding.btnTambah.setOnClickListener {
            val judul = binding.etJudul.text.toString()
            if (judul.isEmpty() || tanggalDipilih.isEmpty() || waktuDipilih.isEmpty()) {
                showAlert(getString(R.string.lengkapi_data))
            } else {
                val pesan = getString(R.string.konfirmasi_tambah, judul, tanggalDipilih, waktuDipilih)
                AlertDialog.Builder(this)
                    .setMessage(pesan)
                    .setPositiveButton(getString(R.string.ya)) { _, _ ->
                        val aktivitas = aktivitas(tanggalDipilih, waktuDipilih, judul)
                        aktivitasList.add(aktivitas)
                        adapter.notifyItemInserted(aktivitasList.size - 1)
                        saveData()
                    }
                    .setNegativeButton(getString(R.string.batal), null)
                    .show()
            }
        }

        binding.btnReset.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.reset_konfirmasi))
                .setPositiveButton(getString(R.string.ya)) { _: DialogInterface, _: Int ->
                    aktivitasList.clear()
                    adapter.notifyDataSetChanged()
                    saveData()
                }
                .setNegativeButton(getString(R.string.batal), null)
                .show()
        }
    }

    private fun saveData() {
        val sharedPref = getSharedPreferences("jadwal_pref", MODE_PRIVATE)
        val json = Gson().toJson(aktivitasList)
        sharedPref.edit().putString("data", json).apply()
    }

    private fun loadData() {
        val sharedPref = getSharedPreferences("jadwal_pref", MODE_PRIVATE)
        val json = sharedPref.getString("data", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<aktivitas>>() {}.type
            val data = Gson().fromJson<MutableList<aktivitas>>(json, type)
            aktivitasList.addAll(data)
        }
    }

    private fun showAlert(msg: String) {
        AlertDialog.Builder(this)
            .setMessage(msg)
            .setPositiveButton("OK", null)
            .show()
    }
}
