package service

import model.Note
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteManager {
    private val notes = mutableListOf<Note>()
    private var nextId = 1
    private val dataFile = File("notes_data.txt")
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    init {
        loadFromFile()
    }

    fun addNote(title: String, content: String): Note {
        val note = Note(nextId++, title, content)
        notes.add(note)
        saveToFile()
        return note
    }

    fun getAllNotes(): List<Note> = notes

    fun getNoteById(id: Int): Note? = notes.find { it.id == id }

    fun updateNote(id: Int, newTitle: String?, newContent: String?): Boolean {
        val note = getNoteById(id) ?: return false
        newTitle?.let { note.title = it }
        newContent?.let { note.content = it }
        note.updatedAt = LocalDateTime.now()
        saveToFile()
        return true
    }

    fun deleteNote(id: Int): Boolean {
        val removed = notes.removeIf { it.id == id }
        if (removed) saveToFile()
        return removed
    }

    fun searchNotes(keyword: String): List<Note> {
        return notes.filter {
            it.title.contains(keyword, ignoreCase = true) ||
                    it.content.contains(keyword, ignoreCase = true)
        }
    }

    fun sortByTitle(): List<Note> = notes.sortedBy { it.title.lowercase() }

    fun sortByDate(): List<Note> = notes.sortedByDescending { it.updatedAt }

    fun exportToFile(fileName: String): Boolean {
        return try {
            val file = File(fileName)
            file.writeText(notes.joinToString("\n\n") { it.displayFormat() })
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun saveToFile() {
        try {
            val lines = notes.map { note ->
                "${note.id}|${note.title.replace("|", " ")}|${note.content.replace("|", " ")}|${note.createdAt.format(formatter)}|${note.updatedAt.format(formatter)}"
            }
            dataFile.writeText(lines.joinToString("\n"))
        } catch (e: Exception) {
            println("⚠️ Auto-save failed: ${e.message}")
        }
    }

    private fun loadFromFile() {
        if (!dataFile.exists()) return
        try {
            dataFile.readLines().forEach { line ->
                if (line.isBlank()) return@forEach
                val parts = line.split("|")
                if (parts.size == 5) {
                    val id = parts[0].toInt()
                    val note = Note(
                        id = id,
                        title = parts[1],
                        content = parts[2],
                        createdAt = LocalDateTime.parse(parts[3], formatter),
                        updatedAt = LocalDateTime.parse(parts[4], formatter)
                    )
                    notes.add(note)
                    if (id >= nextId) nextId = id + 1
                }
            }
        } catch (e: Exception) {
            println("⚠️ Could not load saved notes: ${e.message}")
        }
    }
}