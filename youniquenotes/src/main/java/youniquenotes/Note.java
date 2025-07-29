package youniquenotes;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class Note {
    private String id;
    private String title;
    private LocalDateTime created;
    private LocalDateTime modified;
    private List<String> tags;
    private String body;
    private Path filepath;
    private String author;
    private boolean isDeleted;

     
    public Note(String id, String title, List<String> tags, String author, String body) { // Constructor
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.author = author;
        this.body = body;
        this.created = LocalDateTime.now();
        this.modified = this.created;
        this.isDeleted = false;
    }
     // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public Path getFilepath() {
        return filepath;
    }

    public boolean isDeleted() {
        return isDeleted;
    }


    // Setters
    public void setTitle(String title) {
        this.title = title;
        this.modified = LocalDateTime.now();
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        this.modified = LocalDateTime.now();
    }

    public void setBody(String body) {
        this.body = body;
        this.modified = LocalDateTime.now();
    }

    public void setFilepath(Path filepath) {
        this.filepath = filepath;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
        @Override
    public String toString() {
        return "Note{" +
               "id='" + id + '\'' +
               ", title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", created=" + created +
               ", modified=" + modified +
               ", tags=" + tags +
               ", isDeleted=" + isDeleted +
               '}';
    }  
}