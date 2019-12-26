package exe.weazy.intents.network

import com.google.gson.annotations.SerializedName

data class PaginationInfo(
    @SerializedName("total_pages")
    val totalPages : Int,

    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("next_page")
    val nextPage: Int
)