package com.example.myurltester.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myurltester.models.UrlItem

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    enum class SortingMode (val query: String) {
        DEFAULT("$ID ASC"),
        NAME_ASCENDING("$PATH ASC"),
        NAME_DESCENDING("$PATH DESC"),
        ACCESSIBILITY("$IS_ACCESSIBLE = 1"),
        RESPONSE_TIME_ASCENDING("$RESPONSE_TIME ASC"),
        RESPONSE_TIME_DESCENDING("$RESPONSE_TIME DESC");
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_NAME " +
                    "($ID Integer PRIMARY KEY," +
                    " $PATH TEXT," +
                    " $RESPONSE_TIME INTEGER," +
                    " $IS_ACCESSIBLE INTEGER," +
                    " $IS_CHECKED INTEGER)"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

    fun addUrlItem(urlItem: UrlItem) {
        val db = writableDatabase
        val values = ContentValues()

        values.put(ID, urlItem.id)
        values.put(PATH, urlItem.path)
        values.put(RESPONSE_TIME, urlItem.responseTime)
        values.put(IS_ACCESSIBLE, if (urlItem.isAccessible) 1 else 0)
        values.put(IS_CHECKED, if (urlItem.isChecked) 1 else 0)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    fun getAllSortedUrlItems(sortingMode: SortingMode): ArrayList<UrlItem> {
        val db = readableDatabase
        val cursor : Cursor = if(sortingMode == SortingMode.ACCESSIBILITY){
            db.query(TABLE_NAME,
                null,
                sortingMode.query,
                null,
                null, null,
                null)
        }else {
            db.query(TABLE_NAME,
                null,
                null,
                null,
                null, null,
                sortingMode.query)
        }

        val urlsList: ArrayList<UrlItem> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                val urlItem = UrlItem()
                urlItem.id = cursor.getLong(cursor.getColumnIndex(ID))
                urlItem.path = cursor.getString(cursor.getColumnIndex(PATH))
                urlItem.responseTime = cursor.getLong(cursor.getColumnIndex(RESPONSE_TIME))
                urlItem.isAccessible = cursor.getInt(cursor.getColumnIndex(IS_ACCESSIBLE)) == 1
                urlItem.isChecked = cursor.getInt(cursor.getColumnIndex(IS_CHECKED)) == 1

                urlsList.add(urlItem)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return urlsList
    }

    fun makeAllUrlItemsUnchecked(){
        val db = writableDatabase
        val values = ContentValues()
        values.put(IS_CHECKED, 0)
        db.update(TABLE_NAME, values, null, null)
        db.close()
    }

    fun updateUrlItem(urlItem: UrlItem) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(ID, urlItem.id)
        values.put(PATH, urlItem.path)
        values.put(RESPONSE_TIME, urlItem.responseTime)
        values.put(IS_ACCESSIBLE, if (urlItem.isAccessible) 1 else 0)
        values.put(IS_CHECKED, if (urlItem.isChecked) 1 else 0)
        db.update(TABLE_NAME, values, "$ID = "+urlItem.id, null)
        db.close()
    }

    fun deleteUrlItem(urlItem: UrlItem) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$ID = ?", arrayOf(urlItem.id.toString()))
        db.close()
    }

    companion object {
        private const val DB_NAME = "URLsDB"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "urls"
        private const val ID = "id"
        private const val PATH = "Path"
        private const val RESPONSE_TIME = "ResponseTime"
        private const val IS_ACCESSIBLE = "IsAccessible"
        private const val IS_CHECKED = "IsChecked"
    }
}