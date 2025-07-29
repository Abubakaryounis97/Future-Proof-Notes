package youniquenotes;

import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final NoteManager manager = new NoteManager();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createNote();
                case "2" -> readNote();
                case "3" -> updateNote();
                case "4" -> deleteNote();
                case "5" -> listNotes();
                case "6" -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }

        System.out.println("Exiting program.");
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Note Manager ---");
        System.out.println("1. Create Note");
        System.out.println("2. Read Note");
        System.out.println("3. Update Note");
        System.out.println("4. Delete Note");
        System.out.println("5. List Notes");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static void createNote() {
        System.out.print("Enter Note ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Tags (comma-separated): ");
        List<String> tags = Arrays.asList(scanner.nextLine().split("\\s*,\\s*"));

        System.out.print("Enter Author: ");
        String author = scanner.nextLine();

        System.out.print("Enter Body: ");
        String body = scanner.nextLine();

        try {
            Note note = manager.createNote(id, title, tags, author, body);
            System.out.println("Note created:\n" + note);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void readNote() {
        System.out.print("Enter Note ID: ");
        String id = scanner.nextLine();

        Note note = manager.getNote(id);
        if (note == null || note.isDeleted()) {
            System.out.println("Note not found.");
        } else {
            System.out.println(note.getBody());
        }
    }

    private static void updateNote() {
        System.out.print("Enter Note ID to update: ");
        String id = scanner.nextLine();

        System.out.print("New Title (leave blank to skip): ");
        String title = scanner.nextLine();
        title = title.isBlank() ? null : title;

        System.out.print("New Body (leave blank to skip): ");
        String body = scanner.nextLine();
        body = body.isBlank() ? null : body;

        System.out.print("New Tags (comma-separated, leave blank to skip): ");
        String tagsInput = scanner.nextLine();
        List<String> tags = tagsInput.isBlank() ? null : Arrays.asList(tagsInput.split("\\s*,\\s*"));

        boolean success = manager.updateNote(id, title, body, tags);
        if (success) {
            System.out.println("Note updated.");
        } else {
            System.out.println("Note not found or has been deleted.");
        }
    }

    private static void deleteNote() {
        System.out.print("Enter Note ID to delete: ");
        String id = scanner.nextLine();

        boolean success = manager.deleteNote(id);
        if (success) {
            System.out.println("Note marked as deleted.");
        } else {
            System.out.println("Note not found or already deleted.");
        }
    }

    private static void listNotes() {
        List<Note> notes = manager.listNotes();
        if (notes.isEmpty()) {
            System.out.println("No notes to display.");
        } else {
            System.out.println("\nActive Notes:");
            for (Note note : notes) {
                System.out.println(note);
            }
        }
    }
}
