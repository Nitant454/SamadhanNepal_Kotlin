package com.example.samadhannepalapp.model

data class UserIssueModel(
    var id: String = "",
    var category: String = "",
    var description: String = "",
    var ward: String = "",
    var status: String = "Pending",
    var imageUrl: String? = null,
    var timestamp: Long = 0L,
    var userId: String = ""
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "category" to category,
            "description" to description,
            "ward" to ward,
            "status" to status,
            "imageUrl" to imageUrl,
            "timestamp" to timestamp,
            "userId" to userId
        )
    }
}
