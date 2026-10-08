package com.latihan.belajarstorage

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _catatanDao: Lazy<CatatanDao> = lazy {
    CatatanDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "5619c5c8cbdf4719c6a093ff63e30e13", "3fb5e6bf2d933b9b708a4c14158e1497") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tabel_catatan` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `judul` TEXT NOT NULL, `isi` TEXT NOT NULL, `tanggal` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5619c5c8cbdf4719c6a093ff63e30e13')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `tabel_catatan`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsTabelCatatan: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTabelCatatan.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTabelCatatan.put("judul", TableInfo.Column("judul", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTabelCatatan.put("isi", TableInfo.Column("isi", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTabelCatatan.put("tanggal", TableInfo.Column("tanggal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTabelCatatan: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTabelCatatan: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTabelCatatan: TableInfo = TableInfo("tabel_catatan", _columnsTabelCatatan, _foreignKeysTabelCatatan, _indicesTabelCatatan)
        val _existingTabelCatatan: TableInfo = read(connection, "tabel_catatan")
        if (!_infoTabelCatatan.equals(_existingTabelCatatan)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tabel_catatan(com.latihan.belajarstorage.Catatan).
              | Expected:
              |""".trimMargin() + _infoTabelCatatan + """
              |
              | Found:
              |""".trimMargin() + _existingTabelCatatan)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "tabel_catatan")
  }

  public override fun clearAllTables() {
    super.performClear(false, "tabel_catatan")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(CatatanDao::class, CatatanDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun catatanDao(): CatatanDao = _catatanDao.value
}
