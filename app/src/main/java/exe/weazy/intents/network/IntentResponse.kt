package exe.weazy.intents.network

import exe.weazy.intents.entity.IntentEntity

data class IntentResponse(
    val result : IntentEntity,
    val error: String?
)