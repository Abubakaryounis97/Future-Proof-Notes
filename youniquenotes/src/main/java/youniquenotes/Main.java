package youniquenotes;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        NoteManager manager = new NoteManager();
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("📓 Welcome to YouNique Notes Manager!");
        
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Create Note");
            System.out.println("2. Read Note");
            System.out.println("3. Edit Note");
            System.out.println("4. Delete Note");
            System.out.println("5. Exit");
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

                    System.out.println("Enter body (end with a single '.' on a new line):");
                    StringBuilder bodyBuilder = new StringBuilder();
                    String line;
                    while (!(line = scanner.nextLine()).equals(".")) {
                        bodyBuilder.append(line).append("\n");
                    }

                    Note newNote = manager.createNote(id, title, tags, author, bodyBuilder.toString().trim());
                    manager.saveNoteToFile(newNote);
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

                    boolean updated = manager.editNoteFromFile(editId, newTitle, newBody.isEmpty() ? null : newBody);
                    if (updated) {
                        System.out.println("✅ Note updated.");
                    }
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

                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }
    }
}