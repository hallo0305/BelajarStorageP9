package com.latihan.belajarstorage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    companion object {
        const val PREF_NAME = "MyUserPrefs"
        const val KEY_NAMA = "KEY_NAMA_USER"
        const val KEY_REMEMBER = "isRememberMe"
    }

    private lateinit var sharedPreferences: SharedPreferences

    // Views
    private lateinit var etNama: TextInputEditText
    private lateinit var cbIngatSaya: CheckBox
    private lateinit var btnSimpan: MaterialButton
    private lateinit var btnMuat: MaterialButton
    private lateinit var btnHapus: MaterialButton
    private lateinit var btnNavToRoom: MaterialButton
    private lateinit var tvNamaTersimpan: TextView
    private lateinit var tvStatusIngat: TextView
    private lateinit var tvStatusPenyimpanan: TextView
    private lateinit var tvStatusBadge: TextView
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Inisialisasi View berdasarkan ID di XML
        initViews()

        // Setup Listener Tombol
        setupListeners()

        // Setup Navigasi Bawah
        setupNavigation()

        // Cek persistensi saat aplikasi dibuka
        loadInitialData()
    }

    private fun initViews() {
        etNama = findViewById(R.id.etNama)
        cbIngatSaya = findViewById(R.id.cbIngatSaya)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnMuat = findViewById(R.id.btnMuat)
        btnHapus = findViewById(R.id.btnHapus)
        btnNavToRoom = findViewById(R.id.btnNavToRoom)
        tvNamaTersimpan = findViewById(R.id.tvNamaTersimpan)
        tvStatusIngat = findViewById(R.id.tvStatusIngat)
        tvStatusPenyimpanan = findViewById(R.id.tvStatusPenyimpanan)
        tvStatusBadge = findViewById(R.id.tvStatusBadge)
        bottomNav = findViewById(R.id.bottomNav)
    }

    private fun setupListeners() {
        // TOMBOL SIMPAN
        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val isRemember = cbIngatSaya.isChecked

            // Validasi nama tidak boleh kosong
            if (nama.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_nama_kosong), Toast.LENGTH_SHORT).show()
                etNama.error = getString(R.string.toast_nama_kosong)
                return@setOnClickListener
            }

            etNama.error = null

            // Simpan ke SharedPreferences menggunakan editor.apply()
            val editor = sharedPreferences.edit()
            editor.putString(KEY_NAMA, nama)
            editor.putBoolean(KEY_REMEMBER, isRemember)
            editor.apply()

            Toast.makeText(this, getString(R.string.toast_pref_disimpan), Toast.LENGTH_SHORT).show()

            // Perbarui tampilan hasil di Card
            updateResultCard(nama, isRemember)
        }

        // TOMBOL MUAT DATA
        btnMuat.setOnClickListener {
            val savedNama = sharedPreferences.getString(KEY_NAMA, null)
            val isRemember = sharedPreferences.getBoolean(KEY_REMEMBER, false)

            if (!savedNama.isNullOrEmpty()) {
                updateResultCard(savedNama, isRemember)
                // Jika ingin memuat juga ke formulir:
                if (isRemember) {
                    etNama.setText(savedNama)
                    cbIngatSaya.isChecked = true
                }
                Toast.makeText(this, getString(R.string.toast_pref_dimuat), Toast.LENGTH_SHORT).show()
            } else {
                updateResultCard(null, false)
                Toast.makeText(this, getString(R.string.status_belum_ada_data), Toast.LENGTH_SHORT).show()
            }
        }

        // TOMBOL HAPUS
        btnHapus.setOnClickListener {
            // Hapus data dari SharedPreferences
            val editor = sharedPreferences.edit()
            editor.remove(KEY_NAMA)
            editor.remove(KEY_REMEMBER)
            editor.apply()

            // Reset formulir input
            etNama.setText("")
            etNama.error = null
            cbIngatSaya.isChecked = false

            // Perbarui tampilan hasil
            updateResultCard(null, false)

            Toast.makeText(this, getString(R.string.toast_pref_dihapus), Toast.LENGTH_SHORT).show()
        }

        // Navigasi cepat ke Halaman Room Database
        btnNavToRoom.setOnClickListener {
            navigateToRoom()
        }
    }

    private fun setupNavigation() {
        bottomNav.selectedItemId = R.id.menu_shared_pref
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_shared_pref -> true
                R.id.menu_room_db -> {
                    navigateToRoom()
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToRoom() {
        val intent = Intent(this, ActivityCatatan::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // Pastikan tab menu aktif sesuai activity ini
        bottomNav.selectedItemId = R.id.menu_shared_pref
    }

    /**
     * Memeriksa persistensi saat aplikasi dibuka kembali.
     * Jika ada data yang tersimpan, tampilkan secara otomatis.
     * Jika "Ingat Saya" aktif, isi formulir input secara otomatis.
     */
    private fun loadInitialData() {
        val savedNama = sharedPreferences.getString(KEY_NAMA, null)
        val isRemember = sharedPreferences.getBoolean(KEY_REMEMBER, false)

        if (!savedNama.isNullOrEmpty()) {
            updateResultCard(savedNama, isRemember)
            if (isRemember) {
                etNama.setText(savedNama)
                cbIngatSaya.isChecked = true
            }
        } else {
            updateResultCard(null, false)
        }
    }

    /**
     * Memperbarui tampilan Card Hasil Penyimpanan
     */
    private fun updateResultCard(nama: String?, isRemember: Boolean) {
        if (!nama.isNullOrEmpty()) {
            tvNamaTersimpan.text = nama
            tvStatusIngat.text = if (isRemember) getString(R.string.status_ingat_aktif) else getString(R.string.status_ingat_tidak_aktif)
            tvStatusPenyimpanan.text = getString(R.string.status_tersimpan)

            // Ubah badge status ke mode tersimpan (hijau)
            tvStatusBadge.text = getString(R.string.status_tersimpan)
            tvStatusBadge.background = ContextCompat.getDrawable(this, R.drawable.bg_badge_success)
            tvStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.success))
        } else {
            tvNamaTersimpan.text = "-"
            tvStatusIngat.text = getString(R.string.status_ingat_tidak_aktif)
            tvStatusPenyimpanan.text = getString(R.string.status_belum_ada_data)

            // Ubah badge status ke mode belum ada data (netral)
            tvStatusBadge.text = getString(R.string.status_belum_ada_data)
            tvStatusBadge.background = ContextCompat.getDrawable(this, R.drawable.bg_badge_neutral)
            tvStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
        }
    }
}
