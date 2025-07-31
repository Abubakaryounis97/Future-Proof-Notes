package youniquenotes;

import java.io.IOException;                                                                                             // importing libraries required
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteManager {                                                                                              // decalsring notemanager class

    private static final Path NOTES_DIR = Paths.get("notes");
    private Map<String, Note> notes;                                                                                    // notes hasmap

    public NoteManager() {
        this.notes = new HashMap<>();
    }

    public Note createNote(String id, String title, List<String> tags, String author, String body) {                    // creating a note 
        if (notes.containsKey(id)) {
            throw new IllegalArgumentException("note with this ID already exists.");                                   // checks if id already exists
        }
        Note note = new Note(id, title, tags, author, body, false);                                             // creates a note object and saves it hashmap
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
    // search by tag

    public List<Note> searchByTag(String tag) {
        List<Note> matchingNotes = new ArrayList<>();

        for (Note note : notes.values()) {
            if (!note.isDeleted() && note.getTags().contains(tag)) {
                matchingNotes.add(note);

            }

        }
        return matchingNotes;
    }
    //     // search by author
    // //public List<Note> searchByAuthor(String author) {
    // List<Note> matchingNotes = new ArrayList<>();

    // for (Note note : notes.values()) {
    //     if (!note.isDeleted() && note.getAuthor().equalsIgnoreCase(author)) {
    //         matchingNotes.add(note);
    //     }
    // }
    // return matchingNotes;
    // }
    // better way is to create a search method by keyword which checks all the fields
    public List<Note> search(String keyword) {
        List<Note> matchingNotes = new ArrayList<>();
        String keywordLower = keyword.toLowerCase();

        for (Note note : notes.values()) {
            if (note.isDeleted()) {
                continue;
            }

            boolean idMatch = note.getId().toLowerCase().contains(keywordLower);                                // compares if keyword matches the fields
            boolean titleMatch = note.getTitle().toLowerCase().contains(keywordLower);
            boolean authorMatch = note.getAuthor().toLowerCase().contains(keywordLower);
            boolean bodyMatch = note.getBody().toLowerCase().contains(keywordLower);

            boolean tagMatch = false;                                                                           // Set to false as we multiple tags therefore another for loop is required to check all of them
            for (String tag : note.getTags()) {
                if (tag.toLowerCase().contains(keywordLower)) {
                    tagMatch = true;
                    break;
                }
            }

            if (idMatch || titleMatch || authorMatch || tagMatch || bodyMatch) {                                // AND if condition any of those matches will save the note to matching notes
                matchingNotes.add(note);
            }
        }
        return matchingNotes;                                                                                   // return notes
    }

    public void loadAllNotes() {
        if (!Files.exists(NOTES_DIR)) {                                                                         // check if notes directory exists
            System.out.println("Notes directory does not exist. No notes loaded.");
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(NOTES_DIR, "*.txt")) {                 // open folders and gets all files .txt 
            for (Path filePath : stream) {
                try {
                    Note note = Note.fromFile(filePath);                                                           // for each file it creates and note object
                    notes.put(note.getId(), note);                                                                 // put the note in a hashmap with key as their set ID
                } catch (IOException | IllegalArgumentException e) {
                    System.err.println("Failed to load note from " + filePath + ": " + e.getMessage());            // error message if anything goes wrong
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read notes directory: " + e.getMessage());
        }
    }

    public List<Note> listAllNotes() {
        return new ArrayList<>(notes.values());                                                                     // return all files loaded which can be used to list all items
    }
}
