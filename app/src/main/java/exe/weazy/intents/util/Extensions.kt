package exe.weazy.intents.util

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun Boolean.toCapitalizeString() : String {
    val str = this.toString()
    return str.capitalize()
}