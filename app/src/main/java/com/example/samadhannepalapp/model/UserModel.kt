package com.example.samadhannepalapp.model

data class UserModel(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val dob: String = "",
    val email: String = "",
    val address: String = "",
    val phone: String = "", //phone number
    val role: String = "", //user/admin/authority
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "firstName" to firstName,
            "lastName" to lastName,
            "gender" to gender,
            "dob" to dob,
            "email" to email,
            "address" to address,
            "phone" to phone,
            "role" to role,
        )
    }
}
