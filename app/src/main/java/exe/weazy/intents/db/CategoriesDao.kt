package exe.weazy.intents.db

import androidx.paging.DataSource
import androidx.room.*
import exe.weazy.intents.entity.CategoryEntity

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM categoryentity ORDER BY id ASC")
    fun getAll(): DataSource.Factory<Int, CategoryEntity>

    @Query("SELECT * FROM categoryentity WHERE id = :id")
    fun getById(id: Int): CategoryEntity?

    @Query("SELECT * FROM categoryentity ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getWithOffset(offset : Int, limit: Int) : DataSource.Factory<Int, CategoryEntity>

    @Insert
    fun insert(category: CategoryEntity)

    @Update
    fun update(category: CategoryEntity)

    @Delete
    fun delete(category: CategoryEntity)

    @Query("DELETE FROM categoryentity")
    fun nukeTable()
}