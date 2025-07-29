package youniquenotes;
import java.util.*;
import java.util.stream.Collectors;


public class NoteManager {
    private Map<String, Note> notes;  // notes hasmap
 

    public NoteManager() {
        this.notes = new HashMap<>();
    }


    public Note createNote(String id, String title, List<String> tags, String author, String body) { // creating a note and saving it to a hashmap 
        if (notes.containsKey(id)) {
        throw new IllegalArgumentException("A note with this ID already exists.");
    }
    Note note = new Note(id, title, tags, author, body);
    notes.put(id, note);
    return note;     
    }

     public Note getNote(String id) {
        return notes.get(id);
    }

    public boolean updateNote(String id, String newTitle, String newBody, List<String> newTags) {
        Note note = notes.get(id);
        if (note != null && !note.isDeleted()) {
            if (newTitle != null) note.setTitle(newTitle);
            if (newBody != null) note.setBody(newBody);
            if (newTags != null) note.setTags(newTags);
            return true;
        }
        return false;
    }

     public boolean deleteNote(String id) {
        Note note = notes.get(id);
        if (note != null && !note.isDeleted()) {
            note.setDeleted(true);
            return true;
        }
        return false;
    }

        public List<Note> listNotes() {
        return notes.values().stream()
                .filter(note -> !note.isDeleted())
                .collect(Collectors.toList());
    }

    public List<Note> listDeletedNotes() {
        return notes.values().stream()
                .filter(Note::isDeleted)
                .collect(Collectors.toList());
    }





    
}
