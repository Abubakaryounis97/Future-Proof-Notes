package youniquenotes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteManager {

    private static final Path NOTES_DIR = Paths.get("notes");
    private Map<String, Note> notes;  // notes hasmap

    public NoteManager() {
        this.notes = new HashMap<>();
    }

    public Note createNote(String id, String title, List<String> tags, String author, String body) { // creating a note 
        if (notes.containsKey(id)) {
            throw new IllegalArgumentException("note with this ID already exists.");
        }
        Note note = new Note(id, title, tags, author, body, false);
        notes.put(id, note);
        return note;
    }

    // reading an existing note with ID number
    public Note readNote(String id) {
    Path filePath = NOTES_DIR.resolve(id + ".txt");
    if (!Files.exists(filePath)) {
        System.out.println("Note file does not exist: " + filePath);
        return null;
    }

    try {
        return Note.fromFile(filePath);
    } catch (IOException | IllegalArgumentException e) {
        System.err.println("Error reading note: " + e.getMessage());
        return null;
    }
}

    // updating a note
    public boolean editNoteFromFile(String id, String newTitle, String newBody) {
        Note note = readNote(id);
        if (note == null) {
            System.out.println("Note not found.");
            return false;
        }

        // Update in-memory values
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            note.setTitle(newTitle.trim());
        }

        if (newBody != null && !newBody.trim().isEmpty()) {
            note.setBody(newBody.trim());
        }

        // Save updated note back to file
        saveNoteToFile(note);
        System.out.println("Note updated and saved to file.");
        return true;
    }
    // deleting a note

    public boolean deleteNoteFile(String id) {
        Path filePath = NOTES_DIR.resolve(id + ".txt");

        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Failed to delete note file: " + e.getMessage());
            return false;
        }
    }

    // Saving file method
    public void saveNoteToFile(Note note) {
    try {
        if (!Files.exists(NOTES_DIR)) {
            Files.createDirectories(NOTES_DIR);
        }

        String yamlHeader = "---\n" + note.toYAML() + "---\n\n";
        String fullContent = yamlHeader + note.getBody();

        Path filePath = NOTES_DIR.resolve(note.getId() + ".txt");
        Files.writeString(filePath, fullContent);
        note.setFilepath(filePath);

    } catch (IOException e) {
        System.err.println("Failed to save note '" + note.getId() + "': " + e.getMessage());
    }
}

}
