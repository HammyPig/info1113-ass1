import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;


public class TestLibrary {

    public static void main(String[] args) {
        Library library = new Library();
        library.addCollection("test.csv");
        System.out.println(library);
        library.getAllBooks(false);
    }

    public static void compareRenterHistory(List<Member> expected, List<Member> actual) {
        try {
            assert expected.equals(actual);
        } catch (AssertionError e) {
            e.printStackTrace();
            System.out.println("View difference in lists:");
            System.out.println("Expected: " + Arrays.toString(expected.toArray()));
            System.out.println("Actual: " + Arrays.toString(actual.toArray()));
        }
    }
}