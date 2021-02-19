import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Member {

    private String name;
    private String memberNumber;
    private List<Book> rentedBooks = new ArrayList<Book>();
    private List<Book> rentHistory = new ArrayList<Book>();

    // Constructor
    public Member(String name, String memberNumber) {
        this.name = name;
        this.memberNumber = memberNumber;
    }

    @Override
    public String toString() { return memberNumber; } // print member number by default
    
    public String getName() { return name; }
    public String getMemberNumber() { return memberNumber; }
    public List<Book> history() { return rentHistory; }
    public List<Book> renting() { return rentedBooks; }

    public boolean rent(Book book) {

        if (book == null) {
            return false;
        }
        
        boolean rentedSuccess = book.rent(this); // check if book is free to be rented
        
        if (rentedSuccess) {
            rentedBooks.add(book);
        }

        return rentedSuccess;

    }

    public boolean relinquish(Book book) {
        
        if (book == null) {
            return false;
        }

        // Check if member is actually the renter, also resets book's renter if true
        boolean bookRelinquished = book.relinquish(this); 
        
        if (bookRelinquished) {
            rentHistory.add(book);
            rentedBooks.remove(book);

            return bookRelinquished;
        }
        

        return false;

    }
    
    public void relinquishAll() {
        
        for (Book book : rentedBooks) {
            rentHistory.add(book);
            book.relinquish(this); // resets book's renter
        }

        rentedBooks.clear(); // remove all books

    }

    public static List<Book> commonBooks(Member[] members) {

        // Check if members list is invalid
        if (members == null || members.length == 0) {
            return null;
        }

        // Check if each member is valid
        for (Member member : members) {
            if (member == null || member.history() == null) {
                return null;
            }
        }

        int commonCount = members.length;
        List<Book> commonBooks = new ArrayList<Book>();
        Map<Book, Integer> bookCount = new HashMap<Book, Integer>();

        // Shortcut if one of the lists are empty (as no books would be common in this case)
        for (Member member : members) {
            if (member.history().isEmpty()) {
                return commonBooks;
            }
        }

        for (Member member : members) {

            // Remove duplicate books from member history
            Set<Book> duplicateFreeHistory = new HashSet<Book>(member.history());
            List<Book> rentHistory = new ArrayList<Book>(duplicateFreeHistory);

            for (Book book : rentHistory) {

                // Check if books are valid
                if (book == null) {
                    return null;
                }

                // Count how many members rented each book (not counting duplicates) 
                if (!bookCount.containsKey(book)) {
                    bookCount.put(book, 1);
                } else {
                    bookCount.put(book, bookCount.get(book) + 1);
                }

            }
            
        }

        // If the book count is the same as the number of members, it must be an intersection
        for (Book book : bookCount.keySet()) {
            if (bookCount.get(book) == commonCount) {
                commonBooks.add(book);
            }
        }

        // Sort books by serial number
        commonBooks.sort(Comparator.comparing(Book::getSerialNumber));

        return commonBooks;
    }
    
}