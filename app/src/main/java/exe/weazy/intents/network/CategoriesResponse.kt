package exe.weazy.intents.network

import exe.weazy.intents.entity.CategoryEntity

data class CategoriesResponse(
    val result : List<CategoryEntity>,
    val error : String?
)