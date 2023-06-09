package com.tfg.hosthotel

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Hotel( val name_hotel: String? = null, val localtion_hotel: String? = null,  val street_hotel: String? = null,  val info_hotel: String? = null, val url_img: String? = null, @Exclude val key: String? = null) {
}
