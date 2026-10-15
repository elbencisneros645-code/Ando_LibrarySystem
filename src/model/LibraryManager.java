
package model;

import utils.FileDBHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LibraryManager {

    private List<Book> books;
    private List<Category> categories;
    private List<BorrowRecord> records;

    public LibraryManager() {
        try {
            books = FileDBHelper.load(
                "books.dat", new ArrayList<Book>());

            categories = FileDBHelper.load(
                "categories.dat", new ArrayList<Category>());

            records = FileDBHelper.load(
                "borrow-records.dat",
                new ArrayList<BorrowRecord>());

        } catch (IOException | ClassNotFoundException e) {
            books = new ArrayList<>();
            categories = new ArrayList<>();
            records = new ArrayList<>();
        }

        if (categories.isEmpty()) {
            categories.add(new Category("General"));
            categories.add(new Category("Fiction"));
            categories.add(new Category("Science"));
            categories.add(new Category("History"));
            saveQuietly();
        }
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    public List<BorrowRecord> getRecords() {
        return new ArrayList<>(records);
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException(
                "Book cannot be null.");
        }

        if (findBook(book.getIsbn()) != null) {
            throw new IllegalArgumentException(
                "A book with this ISBN already exists.");
        }

        books.add(book);
        saveQuietly();
    }

    public void updateBook(String originalIsbn, Book updated) {
        if (updated == null) {
            throw new IllegalArgumentException(
                "Updated book cannot be null.");
        }

        Book old = findBook(originalIsbn);

        if (old == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        Book duplicate = findBook(updated.getIsbn());

        if (duplicate != null && duplicate != old) {
            throw new IllegalArgumentException(
                "That ISBN is already used by another book.");
        }

        old.setIsbn(updated.getIsbn());
        old.setTitle(updated.getTitle());
        old.setAuthor(updated.getAuthor());
        old.setPublisher(updated.getPublisher());
        old.setYear(updated.getYear());
        old.setCategory(updated.getCategory());

        saveQuietly();
    }

    public void deleteBook(String isbn) {
        Book book = findBook(isbn);

        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        if (book.isBorrowed()) {
            throw new IllegalArgumentException(
                "Return this book before deleting it.");
        }

        books.remove(book);
        saveQuietly();
    }

    public Book findBook(String isbn) {
        if (isbn == null) {
            return null;
        }

        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn.trim())) {
                return book;
            }
        }

        return null;
    }

    public List<Book> searchBooks(String query) {
        String q = query == null
            ? "" : query.trim().toLowerCase(Locale.ROOT);

        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.getIsbn().toLowerCase(Locale.ROOT).contains(q)
                    || book.getTitle().toLowerCase(Locale.ROOT).contains(q)
                    || book.getAuthor().toLowerCase(Locale.ROOT).contains(q)
                    || book.getCategory().toLowerCase(Locale.ROOT).contains(q)) {
                result.add(book);
            }
        }

        return result;
    }

    public void addCategory(String name) {
        Category category = new Category(name);

        for (Category existing : categories) {
            if (existing.getName().equalsIgnoreCase(
                    category.getName())) {
                throw new IllegalArgumentException(
                    "That category already exists.");
            }
        }

        categories.add(category);
        saveQuietly();
    }

    public void borrowBook(String isbn, String borrower) {
        Book book = findBook(isbn);

        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        if (book.isBorrowed()) {
            throw new IllegalArgumentException(
                "This book is already borrowed.");
        }

        if (borrower == null || borrower.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Borrower's name is required.");
        }

        book.setBorrowed(true);

        records.add(new BorrowRecord(
            book.getIsbn(), borrower.trim()));

        saveQuietly();
    }

    public void returnBook(String isbn) {
        Book book = findBook(isbn);

        if (book == null) {
            throw new IllegalArgumentException("Book not found.");
        }

        if (!book.isBorrowed()) {
            throw new IllegalArgumentException(
                "This book is not currently borrowed.");
        }

        book.setBorrowed(false);

        for (int i = records.size() - 1; i >= 0; i--) {
            BorrowRecord record = records.get(i);

            if (record.getIsbn().equalsIgnoreCase(isbn.trim())
                    && record.isActive()) {
                record.markReturned();
                break;
            }
        }

        saveQuietly();
    }

    private void saveQuietly() {
        try {
            FileDBHelper.save(books, "books.dat");
            FileDBHelper.save(categories, "categories.dat");
            FileDBHelper.save(records, "borrow-records.dat");
        } catch (IOException e) {
            throw new IllegalStateException(
                "Could not save library data: "
                + e.getMessage(), e);
        }
    }
}
