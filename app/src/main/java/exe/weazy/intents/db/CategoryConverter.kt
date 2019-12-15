package exe.weazy.intents.db

import androidx.room.TypeConverter
import exe.weazy.intents.entity.CategoryEntity


class CategoryConverter {

    @TypeConverter
    fun categoryToString(categories : List<CategoryEntity>?) : String {
        val res = StringBuilder()
        categories?.forEach {
            res.append("${it.id}/${it.name}\n")
        }

        return res.toString()
    }

    @TypeConverter
    fun stringToCategory(string: String?) : List<CategoryEntity> {
        val splitted = string?.split('\n')?.filter { it != "" }
        val categories = mutableListOf<CategoryEntity>()

        splitted?.forEach {
            val entry = it.split('/')
            categories.add(CategoryEntity(entry[0].toInt(), entry[1]))
        }

        return categories
    }
}