package com.tfg.hosthotel

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Hotel(val name: String? = null, val city: String? = null,  val description: String? = null, val url: String? = null, @Exclude val key: String? = null) {
}
