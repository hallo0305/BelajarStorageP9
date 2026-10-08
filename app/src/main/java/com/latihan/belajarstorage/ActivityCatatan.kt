package com.latihan.belajarstorage

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ActivityCatatan : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase
    private lateinit var catatanAdapter: CatatanAdapter

    // Views
    private lateinit var etJudulCatatan: TextInputEditText
    private lateinit var etIsiCatatan: TextInputEditText
    private lateinit var etTanggalCatatan: TextInputEditText
    private lateinit var tilTanggalCatatan: TextInputLayout
    private lateinit var btnSimpanCatatan: MaterialButton
    private lateinit var btnKosongkanDatabase: MaterialButton
    private lateinit var btnNavToPref: MaterialButton
    private lateinit var rvCatatan: RecyclerView
    private lateinit var cardEmptyState: MaterialCardView
    private lateinit var tvCountBadge: TextView
    private lateinit var bottomNav: BottomNavigationView

    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catatan)

        // Inisialisasi Room Database (Singleton)
        appDatabase = AppDatabase.getDatabase(this)

        // Inisialisasi Views
        initViews()

        // Setup RecyclerView
        setupRecyclerView()

        // Setup Date Picker
        setupDatePicker()

        // Setup Listener Tombol
        setupListeners()

        // Setup Bottom Navigation
        setupNavigation()

        // Muat seluruh catatan dari Room Database secara asinkron
        muatDaftarCatatan()
    }

    private fun initViews() {
        etJudulCatatan = findViewById(R.id.etJudulCatatan)
        etIsiCatatan = findViewById(R.id.etIsiCatatan)
        etTanggalCatatan = findViewById(R.id.etTanggalCatatan)
        tilTanggalCatatan = findViewById(R.id.tilTanggalCatatan)
        btnSimpanCatatan = findViewById(R.id.btnSimpanCatatan)
        btnKosongkanDatabase = findViewById(R.id.btnKosongkanDatabase)
        btnNavToPref = findViewById(R.id.btnNavToPref)
        rvCatatan = findViewById(R.id.rvCatatan)
        cardEmptyState = findViewById(R.id.cardEmptyState)
        tvCountBadge = findViewById(R.id.tvCountBadge)
        bottomNav = findViewById(R.id.bottomNav)

        // Isi default tanggal hari ini dengan format DD-MM-YYYY
        resetTanggalHariIni()
    }

    private fun resetTanggalHariIni() {
        etTanggalCatatan.setText(dateFormat.format(calendar.time))
    }

    private fun setupRecyclerView() {
        catatanAdapter = CatatanAdapter()
        rvCatatan.apply {
            layoutManager = LinearLayoutManager(this@ActivityCatatan)
            adapter = catatanAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            etTanggalCatatan.setText(dateFormat.format(calendar.time))
        }

        // Tampilkan dialog ketika icon kalender diklik
        tilTanggalCatatan.setEndIconOnClickListener {
            showDatePickerDialog(dateSetListener)
        }

        // Tampilkan dialog juga jika edittext tanggal diklik
        etTanggalCatatan.setOnClickListener {
            showDatePickerDialog(dateSetListener)
        }
    }

    private fun showDatePickerDialog(listener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(
            this,
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setupListeners() {
        // TOMBOL SIMPAN CATATAN
        btnSimpanCatatan.setOnClickListener {
            val judul = etJudulCatatan.text.toString().trim()
            val isi = etIsiCatatan.text.toString().trim()
            val tanggal = etTanggalCatatan.text.toString().trim()

            // Validasi input
            if (judul.isEmpty() || isi.isEmpty() || tanggal.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.toast_field_catatan_kosong),
                    Toast.LENGTH_SHORT
                ).show()

                if (judul.isEmpty()) etJudulCatatan.error = "Judul tidak boleh kosong"
                if (isi.isEmpty()) etIsiCatatan.error = "Isi tidak boleh kosong"
                if (tanggal.isEmpty()) etTanggalCatatan.error = "Tanggal tidak boleh kosong"
                return@setOnClickListener
            }

            etJudulCatatan.error = null
            etIsiCatatan.error = null
            etTanggalCatatan.error = null

            val catatanBaru = Catatan(
                judul = judul,
                isi = isi,
                tanggal = tanggal
            )

            // Jalankan operasi insert pada Dispatchers.IO menggunakan lifecycleScope
            lifecycleScope.launch(Dispatchers.IO) {
                // Simpan ke Room Database
                appDatabase.catatanDao().insertCatatan(catatanBaru)

                // Ambil daftar terbaru
                val listTerbaru = appDatabase.catatanDao().getAllCatatan()

                // Perbarui antarmuka pengguna pada Main Thread
                withContext(Dispatchers.Main) {
                    // Bersihkan formulir input
                    etJudulCatatan.setText("")
                    etIsiCatatan.setText("")
                    resetTanggalHariIni()

                    // Perbarui RecyclerView
                    updateUIList(listTerbaru)

                    Toast.makeText(
                        this@ActivityCatatan,
                        getString(R.string.toast_catatan_disimpan),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // TOMBOL KOSONGKAN DATABASE
        btnKosongkanDatabase.setOnClickListener {
            // Jalankan operasi delete pada Dispatchers.IO menggunakan lifecycleScope
            lifecycleScope.launch(Dispatchers.IO) {
                appDatabase.catatanDao().deleteAll()

                // Ambil list yang telah kosong
                val listKosong = appDatabase.catatanDao().getAllCatatan()

                // Perbarui UI pada Main Thread
                withContext(Dispatchers.Main) {
                    updateUIList(listKosong)
                    Toast.makeText(
                        this@ActivityCatatan,
                        getString(R.string.toast_database_dikosongkan),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Tombol navigasi kembali ke SharedPreferences
        btnNavToPref.setOnClickListener {
            finish()
        }
    }

    private fun setupNavigation() {
        bottomNav.selectedItemId = R.id.menu_room_db
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_shared_pref -> {
                    finish()
                    true
                }
                R.id.menu_room_db -> true
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNav.selectedItemId = R.id.menu_room_db
        // Refresh daftar data setiap kali halaman dibuka
        muatDaftarCatatan()
    }

    /**
     * Membaca seluruh data catatan dari Room Database menggunakan Dispatchers.IO
     * dan memperbarui UI pada Dispatchers.Main.
     */
    private fun muatDaftarCatatan() {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = appDatabase.catatanDao().getAllCatatan()
            withContext(Dispatchers.Main) {
                updateUIList(list)
            }
        }
    }

    /**
     * Memperbarui data adapter, badge jumlah catatan, dan visibilitas empty state.
     */
    private fun updateUIList(list: List<Catatan>) {
        catatanAdapter.submitList(list)
        tvCountBadge.text = "${list.size} Catatan"

        if (list.isEmpty()) {
            cardEmptyState.visibility = View.VISIBLE
            rvCatatan.visibility = View.GONE
        } else {
            cardEmptyState.visibility = View.GONE
            rvCatatan.visibility = View.VISIBLE
        }
    }
}
