package exe.weazy.intents.network

import exe.weazy.intents.entity.CategoryEntity

data class CategoryResponse(
    val result: CategoryEntity,
    val error: String?
)