package uz.gita.dictionary.model.local.room.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query
import uz.gita.dictionary.model.local.room.entity.WordEntity

@Dao
interface WordDao : BaseDao<WordEntity> {
    @Query("SELECT * FROM dictionary")
    fun getAllWords(): List<WordEntity>

    @Query("UPDATE dictionary SET is_favourite = :favorite WHERE id = :id")
    fun updateFav(id: Int, favorite: Int)

    @Query("SELECT *FROM dictionary")
    fun getAllWordsCursor(): Cursor

    @Query("SELECT * FROM dictionary WHERE english LIKE :search_query||'%'")
    fun searchDatabase(search_query: String): List<WordEntity>

    @Query("SELECT * FROM dictionary WHERE is_favourite LIKE :select")
    fun favoriteDatabase(select: Int): List<WordEntity>

    @Query("SELECT * FROM dictionary WHERE english LIKE :select||'%'")
    fun selectDatabase(select: String): List<WordEntity>

    @Query("SELECT * FROM dictionary WHERE is_favourite LIKE :select and english LIKE :query||'%'")
    fun favoritesDatabase(select: Int, query: String): List<WordEntity>
}