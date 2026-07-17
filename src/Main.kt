import model.Note
import service.NoteManager

fun main() {
    val manager = NoteManager()
    var running = true

    while (running) {
        printMenu()
        when (readLine()?.trim()) {
            "1" -> addNote(manager)
            "2" -> viewAllNotes(manager)
            "3" -> updateNote(manager)
            "4" -> deleteNote(manager)
            "5" -> searchNotes(manager)
            "6" -> sortMenu(manager)
            "7" -> exportNotes(manager)
            "8" -> { running = false; println("Exiting... Bye!") }
            else -> println("❌ Invalid choice, try again.")
        }
    }
}

fun printMenu() {
    println(
        """
        |
        |===== NOTE TAKING APP =====
        |1. Add Note
        |2. View All Notes
        |3. Update Note
        |4. Delete Note
        |5. Search Notes
        |6. Sort Notes
        |7. Export Notes
        |8. Exit
        |Choose an option: 
        """.trimMargin()
    )
}

fun addNote(manager: NoteManager) {
    print("Enter title: ")
    val title = readLine()?.trim() ?: ""
    if (title.isEmpty()) {
        println("❌ Title cannot be empty!")
        return
    }
    if (title.length > 100) {
        println("❌ Title too long! Max 100 characters allowed.")
        return
    }

    print("Enter content: ")
    val content = readLine()?.trim() ?: ""
    if (content.isEmpty()) {
        println("❌ Content cannot be empty!")
        return
    }
    if (content.length > 1000) {
        println("❌ Content too long! Max 1000 characters allowed.")
        return
    }

    val note = manager.addNote(title, content)
    println("✅ Note added with ID: ${note.id}")
}

fun viewAllNotes(manager: NoteManager) {
    val notes = manager.getAllNotes()
    if (notes.isEmpty()) {
        println("No notes found.")
    } else {
        notes.forEach { println(it.displayFormat()) }
    }
}

fun updateNote(manager: NoteManager) {
    print("Enter note ID to update: ")
    val id = readLine()?.toIntOrNull() ?: return println("Invalid ID")
    print("New title (leave blank to skip): ")
    val title = readLine()?.ifBlank { null }
    print("New content (leave blank to skip): ")
    val content = readLine()?.ifBlank { null }
    val success = manager.updateNote(id, title, content)
    println(if (success) "✅ Updated!" else "❌ Note not found.")
}

fun deleteNote(manager: NoteManager) {
    print("Enter note ID to delete: ")
    val id = readLine()?.toIntOrNull() ?: return println("Invalid ID")
    val success = manager.deleteNote(id)
    println(if (success) "🗑️ Deleted!" else "❌ Note not found.")
}

fun searchNotes(manager: NoteManager) {
    print("Enter keyword: ")
    val keyword = readLine() ?: ""
    val results = manager.searchNotes(keyword)
    if (results.isEmpty()) println("No matches found.")
    else results.forEach { println(it.displayFormat()) }
}

fun sortMenu(manager: NoteManager) {
    println("1. Sort by Title\n2. Sort by Date")
    when (readLine()?.trim()) {
        "1" -> manager.sortByTitle().forEach { println(it.displayFormat()) }
        "2" -> manager.sortByDate().forEach { println(it.displayFormat()) }
        else -> println("Invalid choice.")
    }
}

fun exportNotes(manager: NoteManager) {
    print("Enter file name (e.g. notes.txt): ")
    val fileName = readLine() ?: "notes.txt"
    val success = manager.exportToFile(fileName)
    println(if (success) "✅ Exported to $fileName" else "❌ Export failed.")
}