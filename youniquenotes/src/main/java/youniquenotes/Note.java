package youniquenotes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

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

     
    public Note(String id, String title, List<String> tags, String author, String body, boolean deleted) 
    { 
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

    public static Note fromFile(Path filePath) throws IOException {
    String content = Files.readString(filePath);
    String[] parts = content.split("---\\s*\n");

    if (parts.length < 3) {
        throw new IllegalArgumentException("Invalid note format.");
    }

    String yamlPart = parts[1];
    String bodyPart = parts[2].trim();

    Yaml yaml = new Yaml();
    Map<String, Object> data = yaml.load(yamlPart);

    Note note = new Note(
        (String) data.get("id"),
        (String) data.get("title"),
        (List<String>) data.get("tags"),
        (String) data.get("author"),
        bodyPart,
        (Boolean) data.get("isDeleted")
    );

    note.setFilepath(filePath);
    // manually set timestamps if needed
    note.setFilepath(filePath);

    return note;
}

public String toYAML() {
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("id", id);
    data.put("title", title);
    data.put("author", author);
    data.put("tags", tags);
    data.put("created", created.toString());
    data.put("modified", modified.toString());
    data.put("isDeleted", isDeleted);

    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    Yaml yaml = new Yaml(options);
    return yaml.dump(data);
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