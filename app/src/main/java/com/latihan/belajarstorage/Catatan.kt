package com.latihan.belajarstorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class yang merepresentasikan tabel catatan dalam Room Database.
 * Table Name: tabel_catatan
 */
@Entity(tableName = "tabel_catatan")
data class Catatan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "judul")
    val judul: String,

    @ColumnInfo(name = "isi")
    val isi: String,

    @ColumnInfo(name = "tanggal")
    val tanggal: String
)
