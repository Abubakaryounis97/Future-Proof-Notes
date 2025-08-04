package youniquenotes;

import java.io.IOException;                                                                                             // importing libraries required
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NoteManager {                                                                                              // decalsring notemanager class

    private static final Path NOTES_DIR = Paths.get("notes");
    private Map<String, Note> notes;                                                                                    // notes hasmap

    public NoteManager() {
        this.notes = new HashMap<>();
    }

    public Note createNote(String id, String title, List<String> tags, String author) {                                 // creating a note 
        Path filePath = NOTES_DIR.resolve(id + ".txt");

        if (notes.containsKey(id) || Files.exists(filePath)) {
        throw new IllegalArgumentException("Note with this ID already exists.");
    }
        String body = "Title:";                                                                // setting a default body

        Note note = new Note(id, title, tags, author, body, false);                                             // creates a note object and saves it hashmap
        notes.put(id, note);
        try {

            String content = "---\n" + note.toYAML() + "---\n\n" + body;                            // laod the Yaml heading and preloads the string
            Files.writeString(filePath, content);

            ProcessBuilder pb = new ProcessBuilder("nano", filePath.toString());        // class that lets me to run an external program in CLT like nano
            pb.inheritIO();                                                                        // shares same screen as terminal and takes input and output from user
            Process process = pb.start();                                                           // starts the nano process
            process.waitFor();                                                                      // wait for user to finish updating or writing their file
            note = Note.fromFile(filePath);
            notes.put(id, note);
         //// prases YAML and body to text from the text file saves as a note object
    
        } catch (IOException | InterruptedException e) {
            System.err.println("Error opening nano: " + e.getMessage());
        }
        return note;
    }

    // reading an existing note with ID number
    public Note readNote(String id) {                                                                                   // returns a note object by searching by id parameter
        Path filePath = NOTES_DIR.resolve(id + ".txt");                                                                 // build the path to the folder with the id number
        if (!Files.exists(filePath)) {
            System.out.println("Note file does not exist: " + filePath);                                                // checks if file actullay exists
            return null;
        }

        try {
            return Note.fromFile(filePath);                                                                             // retunrs the file as a note object
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error reading note: " + e.getMessage());                                                // prevents crashing or any issues with the method
            return null;
        }
    }

   
    // better version of editong a note
    public Note editNote(String id) {
        Path filePath = NOTES_DIR.resolve(id + ".txt");

        if (!Files.exists(filePath)) {
            System.out.println("File doesn't exist: " + filePath);
            return null;
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("nano", filePath.toString());
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();

            Note note = readNote(id);  // reuse your readNote method
            if (note != null) {
                notes.put(id, note);
                System.out.println("Note edited and reloaded.");
            }
            return note;

        } catch (IOException | InterruptedException e) {
            System.err.println("Error editing note: " + e.getMessage());
            return null;
        }
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
 
    // create a search method by keyword which checks all the fields
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


    // loads all notes and lists them

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

    // Stats function
    public void stats() {
    int totalNotes = notes.size();
    int totalWords = 0;
    Set<String> uniqueTags = new HashSet<>();

    for (Note note : notes.values()) {
        // Count words in the body
        if (note.getBody() != null && !note.getBody().isEmpty()) {
            totalWords += note.getBody().split("\\s+").length;
        }

        // Collect unique tags
        uniqueTags.addAll(note.getTags());
    }

    System.out.println("===== Simple Notes Stats =====");
    System.out.println("Total notes: " + totalNotes);
    System.out.println("Total words across all notes: " + totalWords);
    System.out.println("Unique tags used: " + uniqueTags.size());
}
}
