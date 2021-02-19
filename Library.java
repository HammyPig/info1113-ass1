import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.Comparator;
import java.util.Collections;

public class Library {

    public static final String HELP_STRING = "EXIT ends the library process\nCOMMANDS outputs this help string\n\nLIST ALL [LONG] outputs either the short or long string for all books\nLIST AVAILABLE [LONG] outputs either the short of long string for all available books\nNUMBER COPIES outputs the number of copies of each book\nLIST GENRES outputs the name of every genre in the system\nLIST AUTHORS outputs the name of every author in the system\n\nGENRE <genre> outputs the short string of every book with the specified genre\nAUTHOR <author> outputs the short string of every book by the specified author\n\nBOOK <serialNumber> [LONG] outputs either the short or long string for the specified book\nBOOK HISTORY <serialNumber> outputs the rental history of the specified book\n\nMEMBER <memberNumber> outputs the information of the specified member\nMEMBER BOOKS <memberNumber> outputs the books currently rented by the specified member\nMEMBER HISTORY <memberNumber> outputs the rental history of the specified member\n\nRENT <memberNumber> <serialNumber> loans out the specified book to the given member\nRELINQUISH <memberNumber> <serialNumber> returns the specified book from the member\nRELINQUISH ALL <memberNumber> returns all books rented by the specified member\n\nADD MEMBER <name> adds a member to the system\nADD BOOK <filename> <serialNumber> adds a book to the system\n\nADD COLLECTION <filename> adds a collection of books to the system\nSAVE COLLECTION <filename> saves the system to a csv file\n\nCOMMON <memberNumber1> <memberNumber2> ... outputs the common books in members' history";
    private List<Book> inventory = new ArrayList<Book>();
    private List<Member> members = new ArrayList<Member>();
    private List<String> genres = new ArrayList<String>();
    private List<String> authors = new ArrayList<String>();

    // Constructor
    public Library() {}

    public Book lookupBook(String serialNumber) {

        if (serialNumber == null) {
            return null;
        }

        for (Book book : inventory) {
            if (serialNumber.equals(book.getSerialNumber())) {
                return book;
            }
        }

        return null;

    }

    public Member lookupMember(String memberNumber) {

        if (memberNumber == null) {
            return null;
        }

        for (Member member : members) {
            if (memberNumber.equals(member.getMemberNumber())) {
                return member;
            }
        }
        
        return null;

    }

    public boolean membersExist() {

        if (members.isEmpty()) {
            System.out.println("No members in system.");
            return false;
        }

        return true;

    }

    public boolean booksExist() {

        if (inventory.isEmpty()) {
            System.out.println("No books in system.");
            return false;
        }

        return true;
    }

    public void getAllBooks(boolean longString) {
        
        if (longString) {

            // Formatted this way to fix awkward spacing
            System.out.println(inventory.get(0).longString());

            for (int i = 1; i < inventory.size(); i++) {
                System.out.println();
                System.out.println(inventory.get(i).longString());
            }

        } else {
            for (Book book : inventory) {
                System.out.println(book.shortString());
            }
        }
            
    }

    public void getAvailableBooks(boolean longString) {

        List<Book> availableBooks = new ArrayList<Book>();

        for (Book book : inventory) {
            if (book == null || book.isRented()) {
                continue;
            }

            availableBooks.add(book);
        }

        if (availableBooks.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        if (longString) {

            // Formatted this way to fix awkward spacing
            System.out.println(availableBooks.get(0).longString());

            for (int i = 1; i < availableBooks.size(); i++) {
                System.out.println();
                System.out.println(availableBooks.get(i).longString());
            }

        } else {

            for (Book book : availableBooks) {
                System.out.println(book.shortString());
            }

        }

    }

    public void getCopies() {
        
        Map<String, Integer> copies = new TreeMap<String, Integer>();

        for (Book book : inventory) {

            String shortString = book.shortString();

            if (copies.containsKey(shortString)) {
                copies.put(shortString, copies.get(shortString) + 1);
            }  else {
                copies.put(shortString, 1);
            }

        }

        authors.sort(Comparator.comparing(String::toString)); // sort alphabetically

        for (HashMap.Entry book : copies.entrySet()) {
            System.out.println(String.format("%s: %d", book.getKey(), book.getValue()));
        }

    }

    public void getGenres() {

        for (Book book : inventory) {

            String genre = book.getGenre();

            if (genre == null || genres.contains(genre)) {
                continue;
            }

            genres.add(genre);
        }

        genres.sort(Comparator.comparing(String::toString)); // sort alphabetically

        for (String genre : genres) {
            System.out.println(genre);
        }

    }

    public void getAuthors() {
        
        for (Book book : inventory) {
            String author = book.getAuthor();

            if (author == null) {
                continue;
            }

            if (authors.contains(author)) {
                continue;
            }

            authors.add(author);
        }

        authors.sort(Comparator.comparing(String::toString));

        for (String author : authors) {
            System.out.println(author);
        }

    }

    public void getBooksByGenre(String genre) {
        
        List<Book> booksInGenre = Book.filterGenre(inventory, genre);

        if (booksInGenre == null || booksInGenre.isEmpty()) {
            System.out.println(String.format("No books with genre %s.", genre));
            return;
        }

        booksInGenre.sort(Comparator.comparing(Book::getSerialNumber)); // sort by serial number

        for (Book book : booksInGenre) {
            System.out.println(book.shortString());
        }

    }

    public void getBooksByAuthor(String author) {
        
        List<Book> booksByAuthor = Book.filterAuthor(inventory, author);

        if (booksByAuthor.isEmpty()) { 
            System.out.println(String.format("No books by %s", author));
        }

        for (Book book : booksByAuthor) {
            System.out.println(book.longString());
        }

    }

    public void getBook(String serialNumber, boolean longString) {
        
        if (serialNumber == null || !booksExist()) {
            return;
        }

        String result = "No such book in system.";

        for (Book book : inventory) {
            if (serialNumber.equals(book.getSerialNumber())) {

                if (longString) {
                    result = book.longString();
                } else {
                    result = book.shortString();
                }

                break;
            }
        }

        System.out.println(result);

    }

    public void bookHistory(String serialNumber) {
        
        Book book = lookupBook(serialNumber);

        if (book == null) {
            System.out.println("No such book in system.");
            return;
        }

        List<Member> renters = book.renterHistory();

        if (!renters.isEmpty()) {
            for (Member member : renters) {
                System.out.println(member.getMemberNumber());
            }
        } else {
            System.out.println("No rental history.");
        } 

    }

    public void addBook(String bookFile, String serialNumber) {

        if (lookupBook(serialNumber) != null) {
            System.out.println("Book already exists in system.");
            return;
        }

        Book addedBook = Book.readBook(bookFile, serialNumber);

        if (addedBook != null) {
            inventory.add(addedBook);
            System.out.println(String.format("Successfully added: %s.", addedBook.shortString()));
        }

    }

    public void rentBook(String memberNumber, String serialNumber) {

        if (membersExist() && booksExist()) {

            Member member = lookupMember(memberNumber);
            Book book = lookupBook(serialNumber);

            if (member == null) {
                System.out.println("No such member in system.");
            } else if (book == null) {
                System.out.println("No such book in system.");
            } else {
                boolean rented = member.rent(book);

                if (rented) {
                    System.out.println("Success.");
                } else {
                    System.out.println("Book is currently unavailable.");
                }
            }
            
        }
    }

    public void relinquishBook(String memberNumber, String serialNumber) {

        if (membersExist() && booksExist()) {
            Member member = lookupMember(memberNumber);
            Book book = lookupBook(serialNumber);

            if (member == null) {
                System.out.println("No such member in system.");
            } else if (book == null) {
                System.out.println("No such book in system.");
            } else {
                boolean relinquished = member.relinquish(book);

                if (relinquished) {
                    System.out.println("Success.");
                } else {
                    System.out.println("Unable to return book.");
                }
            }
        }
    }

    public void relinquishAll(String memberNumber) {
        
        if (membersExist()) {
            Member member = lookupMember(memberNumber);

            if (member == null) {
                System.out.println("No such member in system.");
                return;
            }

            member.relinquishAll();
            System.out.println("Success.");
        }

    }

    public void getMember(String memberNumber) {

        if (membersExist()) {
            Member member = lookupMember(memberNumber);

            if (member == null) {
                System.out.println("No such member in system.");
                return;
            }

            System.out.println(String.format("%s: %s", member.getMemberNumber(), member.getName()));
        }

    }

    public void getMemberBooks(String memberNumber) {

        Member member = lookupMember(memberNumber);

        if (member == null) {
            System.out.println("No such member in system.");
            return;
        }

        List<Book> memberBooks = member.renting();

        if (memberBooks != null) {
            if (!memberBooks.isEmpty()) {

                for (Book book : memberBooks) {
                    System.out.println(book.shortString());
                }

            } else {
                System.out.println("Member not currently renting.");
            }
        }
        
    }

    public void memberRentalHistory(String memberNumber) {

        Member member = lookupMember(memberNumber);

        if (member == null) {
            System.out.println("No such member in system.");
            return;
        }

        List<Book> rentHistory = member.history();

        if (rentHistory == null || rentHistory.isEmpty()) {
            System.out.println("No rental history for member.");
            return;
        }

        for (Book book : rentHistory) {
            System.out.println(book.shortString());
        }

    }

    public void addMember(String name) {
        Member newMember = new Member(name, String.valueOf(100000 + members.size()));

        if (newMember != null) {
            members.add(newMember);
            System.out.println("Success.");
        }
    }

    public void saveCollection(String filename) {
        if (booksExist()) {
            Book.saveBookCollection(filename, inventory);
        }
    }

    public void addCollection(String filename) {

        List<Book> bookCollection = Book.readBookCollection(filename);

        if (bookCollection != null) {
            int booksAdded = 0;

            bookAlreadyExists:
            for (Book book : bookCollection) {
                for (Book existingBook : inventory) {

                    if (book.getSerialNumber().equals(existingBook.getSerialNumber())) {
                        continue bookAlreadyExists;
                    }

                }

                inventory.add(book);
                booksAdded += 1;
            }

            if (booksAdded < 1) {
                System.out.println("No books have been added to the system.");
            } else {
                System.out.println(String.format("%d books successfully added.", booksAdded));
            }
        }

    }

    public void common(String[] memberNumbers) {

        boolean allMembersValid = true;

        if (membersExist() && booksExist()) {

            List<Member> members = new ArrayList<Member>();

            // Validate members
            for (int i = 1; i < memberNumbers.length; i++) {
                Member member = lookupMember(memberNumbers[i]);

                if (member == null) {
                    allMembersValid = false;
                    System.out.println("No such member in system.");
                    break;
                }

                if (members.contains(member)) {
                    allMembersValid = false;
                    System.out.println("Duplicate members provided.");
                    break;
                }

                members.add(member);
            }

            if (allMembersValid) {
            
                Member[] commonMembers = members.toArray(new Member[members.size()]);

                if (!Arrays.asList(members).contains(null)) {

                    List<Book> commonBooks = Member.commonBooks(commonMembers);

                    if (!commonBooks.isEmpty()) {

                        for (Book book : commonBooks) {
                            System.out.println(book.shortString());
                        }

                    } else {
                        System.out.println("No common books.");
                    }
                    
                } else {
                    System.out.println("No such members in system.");
                }

            }
        }
    }

    public boolean lengthCheck(String[] params) {

        // Checks if user is asking for a long string variant of a command
        if (params.length > 2 && params[2].equalsIgnoreCase("LONG")) {
            return true;
        }

        return false;

    }

    // ALL COMMANDS
    public void addCommand(String[] params) {

        switch(params[1].toUpperCase()) {
            case "MEMBER":
                String name = params[2];

                // For usernames with multiple words
                for (int i = 3; i < params.length; i++) {
                    name = name.concat(" " + params[i]);
                }
                addMember(name);
                break;
            case "BOOK":
                addBook(params[2], params[3]);
                break;
            case "COLLECTION":
                addCollection(params[2]);
                break;
        }

    }

    public void listCommand(String[] params) {

        boolean longString;

        if (params.length > 1) {
            if (booksExist()) {
                switch(params[1].toUpperCase()) {
                    case "ALL":
                        longString = lengthCheck(params);
                        getAllBooks(longString);
                        break;
                    case "AVAILABLE":
                        longString = lengthCheck(params);
                        getAvailableBooks(longString);
                        break;
                    case "GENRES":
                        getGenres();
                        break;
                    case "AUTHORS":
                        getAuthors();
                        break;
                }
            }
        }
    }

    public void memberCommand(String[] params) {

        if (membersExist()) {
            switch(params[1].toUpperCase()) {
                case "BOOKS":
                    getMemberBooks(params[2]);
                    break;
                case "HISTORY":
                    memberRentalHistory(params[2]);
                    break;
                default:
                    // Checks if the given parameter is a valid member number
                    if (params[1].matches("^[0-9]*$")) {
                        getMember(params[1]);
                    }
                    break;
            }
        }

    }

    public void bookCommand(String[] params) {

        switch(params[1].toUpperCase()) {
            case "HISTORY":
                bookHistory(params[2]);
                break;
            default:
                if (params[1].matches("^[0-9]*$")) {
                    getBook(params[1], lengthCheck(params));
                }
                break;
        }
    }

    public void rentCommand(String[] params) {
        rentBook(params[1], params[2]);
    }

    public void relinquishCommand(String[] params) {

        switch (params[1].toUpperCase()) {
            case "ALL":
                relinquishAll(params[2]);
                break;
            default:
                if (params[1].matches("^[0-9]*$")) {
                    relinquishBook(params[1], params[2]);
                }
                break;
        }
    }

    public void genreCommand(String[] params) {

        if (booksExist()) {
            String genre = params[1];

            // For genres with multiple words
            for (int i = 2; i < params.length; i++) {
                genre = genre.concat(" " + params[i]);
            }

            getBooksByGenre(genre);
        }

    }

    public void authorCommand(String[] params) {

        if (booksExist()) {
            String author = params[1];

            for (int i = 2; i < params.length; i++) {
                author = author.concat(" " + params[i]);
            }
            
            List<Book> booksByAuthor = Book.filterAuthor(inventory, author);

            if (!booksByAuthor.isEmpty()) {
                for (Book book : booksByAuthor) {
                    System.out.println(book.shortString());
                }
            } else {
                System.out.println(String.format("No books by %s.", author));
            }
        }

    }

    public void numberCommand(String[] params) {
        if (params[1].equalsIgnoreCase("COPIES") && booksExist()) {
            getCopies();
        }
    }

    public void helpCommand() {
        System.out.println(HELP_STRING);
    }

    public void run() {
        Scanner userInput = new Scanner(System.in);

        System.out.print("user: ");
        String command;

        while (true) {
            if (!userInput.hasNextLine()) {
                break;
            }

            command = userInput.nextLine();
            
            if (command.equalsIgnoreCase("EXIT")) {
                break;
            }

            String[] params = command.split(" ");
            String action = params[0];
            action = action.toUpperCase();

            // All possible main commands
            switch(action) {
                case "LIST":
                    this.listCommand(params);
                    break;
                case "ADD":
                    this.addCommand(params);
                    break;
                case "SAVE":
                    this.saveCollection(params[2]);
                    break;
                case "RENT":
                    this.rentCommand(params);
                    break;
                case "RELINQUISH":
                    this.relinquishCommand(params);
                    break;
                case "MEMBER":
                    this.memberCommand(params);
                    break;
                case "GENRE":
                    this.genreCommand(params);
                    break;
                case "AUTHOR":
                    this.authorCommand(params);
                    break;
                case "COMMON":
                    this.common(params);
                    break;
                case "BOOK":
                    this.bookCommand(params);
                    break;
                case "COMMANDS":
                    this.helpCommand();
                    break;
                case "NUMBER":
                    this.numberCommand(params);
                    break;
            }

            System.out.println();
            System.out.print("user: ");
        }
        System.out.println("Ending Library process.");
    }

    public static void main(String[] args) {
        Library library = new Library();
        library.run();
    }
}