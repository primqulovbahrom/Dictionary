package uz.gita.dictionary.model.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.dictionary.model.local.room.dao.WordDao
import uz.gita.dictionary.model.local.room.entity.WordEntity

@Database(entities = [WordEntity::class], version = 1)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        private var dictionaryDatabase: DictionaryDatabase? = null

        fun init(context: Context) {
            if (dictionaryDatabase == null)
                dictionaryDatabase = Room.databaseBuilder(
                    context,
                    DictionaryDatabase::class.java, "Dictionary.db")
                    .createFromAsset("dictionary.db")
                    .allowMainThreadQueries()
                    .build()
        }

        fun getInstance(): DictionaryDatabase = dictionaryDatabase!!
    }
}