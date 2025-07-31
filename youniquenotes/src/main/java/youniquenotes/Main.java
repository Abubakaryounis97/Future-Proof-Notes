package youniquenotes;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        NoteManager manager = new NoteManager();
        Scanner scanner = new Scanner(System.in);
        String input;
        manager.loadAllNotes(); // preload notes

        System.out.println("📓 Welcome to YouNique Notes Manager!");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create Note");
            System.out.println("2. Read Note");
            System.out.println("3. Edit Note");
            System.out.println("4. Delete Note");
            System.out.println("5. Exit");
            System.out.println("10. Search Notes by Keyword (all fields)");
            System.out.print("> ");
            input = scanner.nextLine();

            switch (input) {
                case "1":
                    System.out.print("Enter note ID: ");
                    String id = scanner.nextLine();

                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();

                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();

                    System.out.print("Enter tags (comma-separated): ");
                    String tagInput = scanner.nextLine();
                    List<String> tags = Arrays.asList(tagInput.split("\\s*,\\s*"));

                    Note newNote = manager.createNote(id, title, tags, author);
                    System.out.println("✅ Note created and saved!");
                    break;

                case "2":
                    System.out.print("Enter note ID to read: ");
                    Note readNote = manager.readNote(scanner.nextLine());
                    if (readNote != null) {
                        System.out.println("\n📝 Note Details:\n" + readNote);
                        System.out.println("\n📄 Body:\n" + readNote.getBody());
                    }
                    break;

                case "3":
                    System.out.print("Enter note ID to edit: ");
                    String editId = scanner.nextLine();

                    System.out.print("Enter new title (leave empty to keep current): ");
                    String newTitle = scanner.nextLine();

                    System.out.println("Enter new body (end with a single '.' on a new line, leave empty to keep current):");
                    StringBuilder newBodyBuilder = new StringBuilder();
                    String bodyLine = scanner.nextLine();
                    if (!bodyLine.equals(".")) {
                        while (!bodyLine.equals(".")) {
                            newBodyBuilder.append(bodyLine).append("\n");
                            bodyLine = scanner.nextLine();
                        }
                    }
                    String newBody = newBodyBuilder.toString().trim();

                    // boolean updated = manager.editNoteFromFile(editId, newTitle, newBody.isEmpty() ? null : newBody);
                    // if (updated) {
                    //     System.out.println("✅ Note updated.");
                    // }
                    break;

                case "4":
                    System.out.print("Enter note ID to delete: ");
                    boolean deleted = manager.deleteNoteFile(scanner.nextLine());
                    System.out.println(deleted ? "🗑️ Note deleted." : "⚠️ Note could not be deleted.");
                    break;

                case "5":
                    System.out.println("👋 Exiting. Goodbye!");
                    scanner.close();
                    return;
                case "10":
                    System.out.print("Enter search keyword: ");
                    String keyword = scanner.nextLine().trim();
                    manager.loadAllNotes(); // load all notes from disk into memory
                    List<Note> results = manager.search(keyword);

                    if (results.isEmpty()) {
                        System.out.println("No matching notes found.");
                    } else {
                        System.out.println("Matching notes:");
                        for (Note note : results) {
                            System.out.println(note);
                        }
                    }
                    break;

                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }
    }
}
