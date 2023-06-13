package com.tfg.hosthotel

data class Review(
    var id: String? = null,
    var hotelName: String? = null,
    var userEmail: String? = null,
    var reviewText: String? = null,
    var currentDate: String? = null,
    var userName: String? = null,
) {
    // Constructor sin argumentos requerido para Firebase Firestore
    constructor() : this(null, null, null, null, null)
}
