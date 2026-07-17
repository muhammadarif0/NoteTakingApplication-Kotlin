package model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Note(
    val id: Int,
    var title: String,
    var content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun displayFormat(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        return """
            |ID: $id
            |Title: $title
            |Content: $content
            |Created: ${createdAt.format(formatter)}
            |Updated: ${updatedAt.format(formatter)}
            |----------------------------------
        """.trimMargin()
    }
}