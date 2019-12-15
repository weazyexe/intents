package exe.weazy.intents.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import exe.weazy.intents.entity.CategoryEntity
import exe.weazy.intents.entity.IntentEntity


@Database(entities = [CategoryEntity::class, IntentEntity::class], version = 5)
@TypeConverters(CategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun intentsDao() : IntentsDao

    abstract fun categoriesDao() : CategoriesDao
}