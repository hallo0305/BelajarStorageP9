package com.latihan.belajarstorage

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.getTotalChangedRows
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CatatanDao_Impl(
  __db: RoomDatabase,
) : CatatanDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCatatan: EntityInsertAdapter<Catatan>
  init {
    this.__db = __db
    this.__insertAdapterOfCatatan = object : EntityInsertAdapter<Catatan>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `tabel_catatan` (`id`,`judul`,`isi`,`tanggal`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Catatan) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.judul)
        statement.bindText(3, entity.isi)
        statement.bindText(4, entity.tanggal)
      }
    }
  }

  public override suspend fun insertCatatan(catatan: Catatan): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfCatatan.insertAndReturnId(_connection, catatan)
    _result
  }

  public override suspend fun getAllCatatan(): List<Catatan> {
    val _sql: String = "SELECT * FROM tabel_catatan ORDER BY id DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfJudul: Int = getColumnIndexOrThrow(_stmt, "judul")
        val _columnIndexOfIsi: Int = getColumnIndexOrThrow(_stmt, "isi")
        val _columnIndexOfTanggal: Int = getColumnIndexOrThrow(_stmt, "tanggal")
        val _result: MutableList<Catatan> = mutableListOf()
        while (_stmt.step()) {
          val _item: Catatan
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpJudul: String
          _tmpJudul = _stmt.getText(_columnIndexOfJudul)
          val _tmpIsi: String
          _tmpIsi = _stmt.getText(_columnIndexOfIsi)
          val _tmpTanggal: String
          _tmpTanggal = _stmt.getText(_columnIndexOfTanggal)
          _item = Catatan(_tmpId,_tmpJudul,_tmpIsi,_tmpTanggal)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll(): Int {
    val _sql: String = "DELETE FROM tabel_catatan"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
        getTotalChangedRows(_connection)
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
