package exe.weazy.intents.db

import androidx.paging.DataSource
import androidx.room.*
import exe.weazy.intents.entity.IntentEntity

@Dao
interface IntentsDao {
    @Query("SELECT * FROM intententity ORDER BY id ASC")
    fun getAll(): DataSource.Factory<Int, IntentEntity>

    @Query("SELECT * FROM intententity WHERE isMarkedUp = :isMarkedUp ORDER BY id ASC")
    fun getAllByMarkedUp(isMarkedUp : Boolean) : DataSource.Factory<Int, IntentEntity>

    @Query("SELECT * FROM intententity WHERE id = :id")
    fun getById(id: Int): IntentEntity?

    @Query("SELECT * FROM intententity WHERE isMarkedUp = :isMarkedUp ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getByMarkedUpWithOffset(isMarkedUp: Boolean, offset: Int,limit: Int) : DataSource.Factory<Int, IntentEntity>

    @Insert
    fun insert(intent: IntentEntity)

    @Update
    fun update(intent: IntentEntity)

    @Delete
    fun delete(intent: IntentEntity)

    @Query("DELETE FROM intententity WHERE isMarkedUp = :isMarkedUp")
    fun nukeTable(isMarkedUp: Boolean)
}