package youniquenotes;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static NoteManager manager = new NoteManager();
    private static String input = "";

    public static void main(String[] args) {
        manager.loadAllNotes();
        System.out.println("Welcome to Youniquenotes!");

        menu();
        while (true) {
    input = scanner.nextLine().trim().toLowerCase();
    switch (input) {
        case "create":
            do {
                createFunction();
            } while (askRepeat());
            break;

        case "list":
            do {
                listFunction();
            } while (askRepeat());
            break;

        case "list --tag":
            do {
                listTagsFunction();
            } while (askRepeat());
            break;

        case "read":
            do {
                readFunction();
            } while (askRepeat());
            break;

        case "edit":
            do {
                editFunction();
            } while (askRepeat());
            break;

        case "delete":
            do {
                deleteFunction();
            } while (askRepeat());
            break;

        case "search":
            do {
                searchFunction();
            } while (askRepeat());
            break;

        case "stats":
            do {
                statsFunction();
            } while (askRepeat());
            break;

        case "exit":
            System.out.println("Exiting program. Goodbye!");
            return;

        default:
            System.out.println("Please try again, command not found!");
            menu();
    }
}

      

    }

    // printing a menu for user
    public static void menu() {

        System.out.println("create => Create a new note");
        System.out.println("list => List all notes ");
        System.out.println("list --tag => List notes with specific tag ");
        System.out.println("read => Display a specific note");
        System.out.println("edit =>Edit a specific note ");
        System.out.println("delete => Delete a specific note");
        System.out.println("search =>Search notes for text (title, tags, content)");
        System.out.println("stats  => Display statistics about your notes");
        System.out.println("exit  => Leave the app");
    }
    // create function

    private static void createFunction() {
        System.out.println("Enter note ID:");
        String id = scanner.nextLine().trim();
        System.out.println("Enter note Title:");
        String title = scanner.nextLine().trim();
        System.out.println("Enter note Author:");
        String author = scanner.nextLine().trim();
        System.out.println("Enter note Tags ( separeted by comma):");
        String tagInput = scanner.nextLine();
        List<String> tags = Arrays.asList(tagInput.split("\\s*,\\s*"));

       try {
        Note newNote = manager.createNote(id, title, tags, author);

        if (newNote == null) {
            System.out.println("Note not created.");
        } else {
            System.out.println("Note saved.");
        }
    } catch (IllegalArgumentException e) {
        System.out.println("Error: " + e.getMessage());
        menu(); // re-display the menu after showing the error
    }
    }
    // listFunction();

    private static void listFunction() {
        manager.loadAllNotes();
        List<Note> allNotes = manager.listAllNotes();

        if (allNotes.isEmpty()) {
            System.out.println("No notes available.");
        } else {
            System.out.println("Listing all notes:");
            for (Note note : allNotes) {
                System.out.println("- ID: " + note.getId() + ", Title: " + note.getTitle());
            }
        }

    }
    //listTagsFunctions();

    private static void listTagsFunction() {
        System.out.println("Please enter tag:");
        String tag = scanner.nextLine().trim();
        List<Note> matchingNotes = manager.searchByTag(tag);

        if (matchingNotes.isEmpty()) {
            System.out.println("No notes found with tag: " + tag);
        } else {
            System.out.println("Notes with tag '" + tag + "':");
            for (Note note : matchingNotes) {
                System.out.println("- ID: " + note.getId() + ", Title: " + note.getTitle());
            }
        }

    }
    //readFunction();

    private static void readFunction() {
        System.out.println("Please enter ID number:");
        String id = scanner.nextLine().trim();
        Note note = manager.readNote(id);
        if (note != null) {
            System.out.println("\nNote details:\n" + note);
            System.out.println("Body:\n" + note.getBody());
        } else {
            System.out.println("Note with ID '" + id + "' not found.");
        }
    }

    // editFunction();
    private static void editFunction() {
        System.out.println("Please enter ID number:");
        String id = scanner.nextLine().trim();
        Note updatedNote = manager.editNote(id);
        if (updatedNote != null) {
            System.out.println("Note updated successfully!");
            System.out.println(updatedNote);
        } else {
            System.out.println("Note with ID '" + id + "' not found or could not be edited.");
        }
    }

    //deleteFunction();
    private static void deleteFunction() {
        System.out.println("Please enter ID number:");
        String id = scanner.nextLine().trim();

        boolean deleted = manager.deleteNoteFile(id);
        if (deleted) {
            System.out.println("Note with ID '" + id + "' deleted successfully.");
        } else {
            System.out.println("Note with ID '" + id + "' not found or could not be deleted.");
        }

    }

    // searchFunction();
    private static void searchFunction() {
        System.out.println("Please enter keyword:");
        String keyword = scanner.nextLine().trim();
        List<Note> results = manager.search(keyword);

        if (results.isEmpty()) {
            System.out.println("No notes found matching keyword: " + keyword);
        } else {
            System.out.println("Found " + results.size() + " notes:");
            for (Note note : results) {
                System.out.println("- ID: " + note.getId() + ", Title: " + note.getTitle());
            }
        }

    }
    //statsfunction
    private static void  statsFunction()
    {
        manager.stats();
    }
    // repeat function
    private static boolean askRepeat() {
    System.out.print("Repeat this function? (y): ");
    String response = scanner.nextLine().trim().toLowerCase();
    return response.equals("y");
}


}
