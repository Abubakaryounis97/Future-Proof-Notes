package youniquenotes;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final NoteManager manager = new NoteManager();

    public static void main(String[] args) {
        manager.loadAllNotes();  // Load notes from disk at startup
        System.out.println("📘 Welcome to Youniquenotes!");
        printMenu();

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("👋 Exiting. Goodbye!");
                break;
            }

            switch (input) {
                case "menu":
                    printMenu();
                    break;

                case "create":
                    handleCreate();
                    break;

                case "list":
                    handleListAll();
                    break;

                case "list --tag":
                    handleListByTag();
                    break;

                case "read":
                    handleRead();
                    break;

                case "edit":
                    handleEdit();
                    break;

                case "delete":
                    handleDelete();
                    break;

                case "search":
                    handleSearch();
                    break;

                case "stats":
                    handleStats();
                    break;

                default:
                    System.out.println("Unknown command. Type `menu` to see available commands.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n📋 Commands:");
        System.out.println("  create       => Create a new note");
        System.out.println("  list         => List all notes");
        System.out.println("  list --tag   => List notes with specific tag");
        System.out.println("  read         => Display a specific note");
        System.out.println("  edit         => Edit a specific note");
        System.out.println("  delete       => Delete a specific note");
        System.out.println("  search       => Search notes by keyword");
        System.out.println("  stats        => Display statistics about your notes");
        System.out.println("  menu         => Show commands");
        System.out.println("  exit         => Quit the application");
    }

    private static void handleCreate() {
        System.out.print("Enter note ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter title: ");
        String title = scanner.nextLine();

        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        System.out.print("Enter tags (comma-separated): ");
        String tagInput = scanner.nextLine();
        List<String> tags = Arrays.asList(tagInput.split("\\s*,\\s*"));

        manager.createNote(id, title, tags, author);
        System.out.println("✅ Note created and opened in nano!");
    }

    private static void handleListAll() {
        List<Note> notes = manager.listAllNotes();
        if (notes.isEmpty()) {
            System.out.println("No notes found.");
        } else {
            System.out.println("📄 Notes:");
            for (Note note : notes) {
                System.out.printf("  - [%s] %s (%s)\n", note.getId(), note.getTitle(), note.getAuthor());
            }
        }
    }

    private static void handleListByTag() {
        System.out.print("Enter tag: ");
        String tag = scanner.nextLine().trim();
        List<Note> notes = manager.searchByTag(tag);
        if (notes.isEmpty()) {
            System.out.println("No notes found with that tag.");
        } else {
            System.out.println("📌 Notes with tag '" + tag + "':");
            for (Note note : notes) {
                System.out.printf("  - [%s] %s (%s)\n", note.getId(), note.getTitle(), note.getAuthor());
            }
        }
    }

    private static void handleRead() {
        System.out.print("Enter note ID: ");
        String id = scanner.nextLine();
        Note note = manager.readNote(id);
        if (note != null) {
            System.out.println("\n" + note);
        }
    }

    private static void handleEdit() {
        System.out.print("Enter note ID: ");
        String id = scanner.nextLine();
        manager.editNote(id);
    }

    private static void handleDelete() {
        System.out.print("Enter note ID to delete: ");
        String id = scanner.nextLine();
        boolean deleted = manager.deleteNoteFile(id);
        if (deleted) {
            System.out.println("🗑️ Note deleted.");
        } else {
            System.out.println("Failed to delete note.");
        }
    }

    private static void handleSearch() {
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine().trim();
        List<Note> matches = manager.search(keyword);
        if (matches.isEmpty()) {
            System.out.println("No matching notes found.");
        } else {
            System.out.println("🔍 Found notes:");
            for (Note note : matches) {
                System.out.printf("  - [%s] %s (%s)\n", note.getId(), note.getTitle(), note.getAuthor());
            }
        }
    }

    private static void handleStats() {
        List<Note> notes = manager.listAllNotes();
        int total = notes.size();
        int deleted = (int) notes.stream().filter(Note::isDeleted).count();

        System.out.println("📊 Note Stats:");
        System.out.println("  Total Notes: " + total);
        System.out.println("  Deleted Notes: " + deleted);
    }
}
