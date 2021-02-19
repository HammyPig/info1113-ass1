import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.io.IOException;

public class Book {

    private String title;
    private String author;
    private String genre;
    private String serialNumber;
    private Member currentRenter;
    private List<Member> renterHistory = new ArrayList<Member>();

    // Constructor
    public Book(String title, String author, String genre, String serialNumber) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() { return serialNumber; } // Print serial number by default for testing purposes

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getSerialNumber() { return serialNumber; }

    public String longString() {

        String longString = String.format("%s: %s (%s, %s)\n", serialNumber, title, author, genre);

        // Add whether book is available or has been rented
        if (isRented()) {
            String renterNumber = currentRenter.getMemberNumber();
            longString = longString.concat(String.format("Rented by: %s.", renterNumber));
        } else {
            longString = longString.concat("Currently available.");
        }
        
        return longString;
        
    }

    public String shortString() {
        String shortString = String.format("%s (%s)", title, author);
        return shortString;
    }

    public List<Member> renterHistory() {
        return renterHistory;
    }

    public boolean isRented() {

        // If there is no current renter, then this book is not being rented
        if (currentRenter == null) {
            return false;
        }

        return true;

    }

    public boolean rent(Member member) {

        if (member == null || isRented()) {
            return false;
        }

        currentRenter = member;

        return true;
        
    }

    public boolean relinquish(Member member) {

        if (member == null || !member.equals(currentRenter)) {
            return false;
        }

        currentRenter = null;
        renterHistory.add(member);

        return true;

    }
    
    public static Book readBook(String filename, String serialNumber) {

        if (filename == null || serialNumber == null) {
            return null;
        }

        File f = new File(filename);

        try {

            Scanner input = new Scanner(f);
            
            while (input.hasNextLine()) {

                String line = input.nextLine();
                String[] bookInfo = line.split(",");

                if (serialNumber.equals(bookInfo[0])) {

                    String title = bookInfo[1];
                    String author = bookInfo[2];
                    String genre = bookInfo[3];

                    return new Book(title, author, genre, serialNumber);
                }

            }

            System.out.println("No such book in file.");
            input.close();

            return null;

        } catch (FileNotFoundException e) {
            System.out.println("No such file.");
            return null;
        }

    }
    
    public static List<Book> readBookCollection(String filename) {

        if (filename == null) {
            return null;
        }

        File f = new File(filename);

        try {

            Scanner input = new Scanner(f);
            List<Book> bookCollection = new ArrayList<Book>();

            while (input.hasNextLine()) {

                String line = input.nextLine();
                String[] bookInfo = line.split(",");

                String serialNumber = bookInfo[0];

                // Find valid serial number
                if (!serialNumber.matches("^[0-9]*$")) {
                    continue;
                }

                String title = bookInfo[1];
                String author = bookInfo[2];
                String genre = bookInfo[3];

                bookCollection.add(new Book(title, author, genre, serialNumber));

            }

            input.close();

            return bookCollection;

        } catch (FileNotFoundException e) {
            System.out.println("No such collection.");
            return null;
        }
    }
    
    public static void saveBookCollection(String filename, Collection<Book> books) {
        
        if (filename == null || books == null || books.isEmpty()) {
            return;
        }

        File f = new File(filename);

        try {

            PrintWriter writer = new PrintWriter(f);

            // Write in csv format
            writer.println("serialNumber,title,author,genre"); //header

            for (Book book : books) {
                writer.println(String.format("%s,%s,%s,%s", book.getSerialNumber(), book.getTitle(), book.getAuthor(), book.getGenre()));
            }

            writer.flush();
            writer.close();
            System.out.println("Success.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<Book> filterAuthor(List<Book> books, String author) {

        if (books == null || author == null) {
            return null;
        }

        List<Book> authorFiltered = new ArrayList<Book>();

        for (Book book : books) {
            if (author.equals(book.getAuthor())) {
                authorFiltered.add(book);
            }
        }

        return authorFiltered;

    }

    public static List<Book> filterGenre(List<Book> books, String genre) {
        
        if (books == null || genre == null) {
            return null;
        }

        List<Book> genreFiltered = new ArrayList<Book>();

        for (Book book : books) {
            if (genre.equals(book.getGenre())) {
                genreFiltered.add(book);
            }
        }

        return genreFiltered;
        
    }
    
}