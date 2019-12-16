package exe.weazy.intents.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class IntentEntity(
    @PrimaryKey
    val id: Int,

    val content : String,

    @SerializedName("is_marked_up")
    val isMarkedUp : Boolean,

    val categories : List<CategoryEntity>?
) : Serializable