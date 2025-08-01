package youniquenotes;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static NoteManager manager = new NoteManager();
    private static String input = "";

    public static void main(String[] args) {
        System.out.println("Welcome to Youniquenotes!");

        menu();

        while (true) {

            input = scanner.nextLine().trim();
            switch (input.toLowerCase()) {
                case "create":
                    createFunction();
                    break;
                case "list":
                   listFunction();
                    break;
                // case "list --tag":
                //     listTagsFunction();
                    break;
                case "read":
                    readFunction();
                    break;
                case "edit":
                    editFunction();
                    break;
                case "delete":
                    deleteFunction();
                    break;
                case "search":
                    searchFunction();
                    break;
                case "stats":
                    //statsFunction();
                    break;
                default:
                    System.out.println("please try again, command not found!");
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

        Note newNote = manager.createNote(id, title, tags, author);

        if (newNote == null) {
            System.out.println("Note not created");
        } else {
            System.out.println("Note saved");
        }
    }
     // listFunction();
        private static void listFunction(){
            manager.loadAllNotes();
        }
     //listTagsFunctions();
      private static void listTagsFunction(){
            System.out.println("Please enter tag:");
            String tag= scanner.nextLine().trim();
            manager.searchByTag(tag);
        }
     //readFunction();
     private static void  readFunction()
     {
          System.out.println("Please enter ID number:");
          String id= scanner.nextLine().trim();
          manager.readNote(id);
     }

    // editFunction();
    private static void  editFunction(){
        System.out.println("Please enter ID number:");
        String id= scanner.nextLine().trim();
          manager.editNote(id);
    }
    //deleteFunction();
    private static void deleteFunction(){

        System.out.println("Please enter ID number:");
        String id= scanner.nextLine().trim();
          manager.deleteNoteFile(id);

    }
    // searchFunction();
    private static void  searchFunction(){
          System.out.println("Please enter keyword:");
        String keyword= scanner.nextLine().trim();
          manager.search(keyword);

    }
}
