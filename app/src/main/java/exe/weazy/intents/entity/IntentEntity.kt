package exe.weazy.intents.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class IntentEntity(
    @PrimaryKey
    val id: Int,

    var content : String,

    @SerializedName("is_marked_up")
    var isMarkedUp : Boolean,

    var categories : MutableList<CategoryEntity>?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readParcelableArray(CategoryEntity.javaClass.classLoader)?.toMutableList() as MutableList<CategoryEntity>?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(content)
        parcel.writeByte(if (isMarkedUp) 1 else 0)
        parcel.writeParcelableArray(categories?.toTypedArray(), 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IntentEntity> {
        override fun createFromParcel(parcel: Parcel): IntentEntity {
            return IntentEntity(parcel)
        }

        override fun newArray(size: Int): Array<IntentEntity?> {
            return arrayOfNulls(size)
        }
    }

}