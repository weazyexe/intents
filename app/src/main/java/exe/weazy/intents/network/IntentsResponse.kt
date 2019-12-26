package exe.weazy.intents.network

import exe.weazy.intents.entity.IntentEntity

data class IntentsResponse(
    val result : List<IntentEntity>,
    val pagination : PaginationInfo,
    val error : String?
)