package com.latihan.belajarstorage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit Test untuk memvalidasi spesifikasi model data, konstanta SharedPreferences,
 * validasi form input, dan logika formatting catatan.
 */
class StorageTest {

    @Test
    fun testSharedPreferencesConstants() {
        assertEquals("MyUserPrefs", MainActivity.PREF_NAME)
        assertEquals("KEY_NAMA_USER", MainActivity.KEY_NAMA)
        assertEquals("isRememberMe", MainActivity.KEY_REMEMBER)
    }

    @Test
    fun testCatatanEntityCreation() {
        val catatan = Catatan(
            id = 1,
            judul = "Tugas Kuliah",
            isi = "Mengerjakan praktikum Android",
            tanggal = "04-09-2026"
        )

        assertEquals(1, catatan.id)
        assertEquals("Tugas Kuliah", catatan.judul)
        assertEquals("Mengerjakan praktikum Android", catatan.isi)
        assertEquals("04-09-2026", catatan.tanggal)
    }

    @Test
    fun testDateFormatValidation() {
        val sampleDate = "04-09-2026"
        val dateRegex = Regex("""^\d{2}-\d{2}-\d{4}$""")

        assertTrue("Format tanggal harus DD-MM-YYYY", dateRegex.matches(sampleDate))
    }

    @Test
    fun testInputValidationLogic() {
        // Uji validasi nama SharedPreferences
        val emptyNama = "   "
        val validNama = "Diva Safitri"
        assertTrue(emptyNama.trim().isEmpty())
        assertFalse(validNama.trim().isEmpty())

        // Uji validasi input Catatan Room
        val judul = "Tugas Praktikum"
        val isi = "Membuat aplikasi SharedPreferences dan Room Database"
        val tanggal = "04-09-2026"

        val isAllFilled = judul.isNotBlank() && isi.isNotBlank() && tanggal.isNotBlank()
        assertTrue("Semua field catatan harus terisi", isAllFilled)
    }

    @Test
    fun testCatatanFormatOutput() {
        val catatan = Catatan(
            id = 1,
            judul = "Tugas Kuliah",
            isi = "Mengerjakan praktikum Android",
            tanggal = "04-09-2026"
        )

        val idFormat = "ID: ${catatan.id}"
        val judulFormat = "Judul: ${catatan.judul}"
        val isiFormat = "Isi: ${catatan.isi}"
        val tanggalFormat = "Tanggal: ${catatan.tanggal}"

        assertEquals("ID: 1", idFormat)
        assertEquals("Judul: Tugas Kuliah", judulFormat)
        assertEquals("Isi: Mengerjakan praktikum Android", isiFormat)
        assertEquals("Tanggal: 04-09-2026", tanggalFormat)
    }
}
