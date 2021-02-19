public class Test {

    public static void main(String[] args) {
        Book.readBookCollection("test.csv");

        Book book = new Book("title", "author", "genre", "sn");
        System.out.println(book.longString());
    }
}