import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;


public class BookTest {

    public static void main(String[] args) {
        Book got = new Book("Game of Thrones", "Gearge mearmmt", "scifi", "123");
        Book ua = new Book("Umbrella Academy", "author man", "fantasy", "1234");
        Book hxh = new Book("Hunter x Hunter", "sleepy man", "adventure", "2");

        Member michael = new Member("Michael", "1");
        Member jon = new Member("Jon", "2");
        Member henry = new Member("Henry", "3");

        assert got.rent(michael) == true: "Book rent failed";
        assert got.rent(jon) == false: "Book rent failed";
        assert got.relinquish(michael) == true: "Book rent failed";
        assert got.rent(henry) == true: "Book rent failed";

        List<Member> expected = new ArrayList<Member>();
        expected.add(michael);

        compareRenterHistory(expected, got.renterHistory());

        assert ua.rent(michael) == true: "Book rent failed";
        assert ua.rent(jon) == false: "Book rent failed";
        assert ua.rent(michael) == false: "Book rent failed";
        assert ua.rent(henry) == false: "Book rent failed";

        List<Member> expected2 = new ArrayList<Member>();

        compareRenterHistory(expected2, ua.renterHistory());

        assert hxh.rent(null) == false: "Book rent failed";
        assert hxh.rent(null) == false: "Book rent failed";
        assert hxh.rent(michael) == true: "Book rent failed";
        assert hxh.relinquish(null) == false: "Book rent failed";
        assert hxh.relinquish(michael) == true: "Book rent failed";

        List<Member> expected3 = new ArrayList<Member>();
        expected3.add(michael);

        compareRenterHistory(expected3, hxh.renterHistory());
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