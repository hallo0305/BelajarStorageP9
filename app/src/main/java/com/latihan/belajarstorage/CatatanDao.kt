package com.latihan.belajarstorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object (DAO) untuk operasi tabel_catatan.
 * Menggunakan coroutine suspend functions agar dieksekusi secara asinkron di background thread (Dispatchers.IO).
 */
@Dao
interface CatatanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatatan(catatan: Catatan): Long

    @Query("SELECT * FROM tabel_catatan ORDER BY id DESC")
    suspend fun getAllCatatan(): List<Catatan>

    @Query("DELETE FROM tabel_catatan")
    suspend fun deleteAll(): Int
}
