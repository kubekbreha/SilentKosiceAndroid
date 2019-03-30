package sk.spacecode.silentkosice

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DecibelData(
    var timestamp: String? = "",
    var decibel: String? = "",
    var userNumber: String? = "",
    var lat: String? = "",
    var long: String? = ""
)
