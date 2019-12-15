package exe.weazy.intents.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import exe.weazy.intents.db.AppDatabase

@Module
class DatabaseModule(private val context: Context) {

    @Provides
    fun provideDatabase() : AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}