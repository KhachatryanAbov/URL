package com.example.myurltester.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.myurltester.models.UrlItem
import com.example.myurltester.ui.main_activity.MainActivity

/**
 * Created by Eyehunt Team on 07/06/18.
 */
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSIOM) {

    enum class SortingMode {
        DEFAULT(){
            override fun createQuery() :String {
                return "$ID ASC"
            }
        },
        NAME_ASCENDING(){
            override fun createQuery() :String {
                return "$PATH ASC"
            }
        },
        NAME_DESCENDING(){
            override fun createQuery() :String {
                return "$PATH DESC"
            }
        },
        ACCESSIBILITY(){
            override fun createQuery() :String {
                return "$IS_ACCESSIBLE = 1"
            }
        },
        RESPONSE_TIME_ASCENDING(){
            override fun createQuery() :String {
                return "$RESPONSE_TIME ASC"
            }
        },
        RESPONSE_TIME_DESCENDING(){
            override fun createQuery() :String {
                return "$RESPONSE_TIME DESC"
            }
        };
        abstract fun createQuery():String
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


    fun getAllUrlItems(sortingMode: SortingMode): ArrayList<UrlItem> {
        val urlsList: ArrayList<UrlItem> = ArrayList()
        val db = readableDatabase
        val cursor : Cursor?

        if(sortingMode == SortingMode.ACCESSIBILITY){
            cursor = db.query(TABLE_NAME,
                null,
                sortingMode.createQuery(),
                null,
                null, null,
                null);
        }else {
            cursor = db.query(TABLE_NAME,
                null,
                null,
                null,
                null, null,
                sortingMode.createQuery());
        }


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
        db.update(TABLE_NAME, values, null, null);
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
        db.update(TABLE_NAME, values, "$ID = "+urlItem.id, null);
        db.close();
    }

    fun deleteUrlItem(urlItem: UrlItem) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$ID = ?", arrayOf(urlItem.id.toString()))
        db.close();
    }

    companion object {
        private val DB_NAME = "URLsDB"
        private val DB_VERSIOM = 1;
        private val TABLE_NAME = "urls"
        private val ID = "id"
        private val PATH = "Path"
        private val RESPONSE_TIME = "ResponseTime"
        private val IS_ACCESSIBLE = "IsAccessible"
        private val IS_CHECKED = "IsChecked"
    }
}