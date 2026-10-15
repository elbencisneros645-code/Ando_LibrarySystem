/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
 

import model.Book;
import model.Category;
import model.LibraryManager;
import java.util.List;

/**
 *
 * @author USER
 */
public class LibraryController {
    
  
    private final LibraryManager manager;

    public LibraryController() {
        manager = new LibraryManager();
    }

    public List<Book> getBooks() {
        return manager.getBooks();
    }

    public List<Book> searchBooks(String query) {
        return manager.searchBooks(query);
    }

    public List<Category> getCategories() {
        return manager.getCategories();
    }

    public void addBook(Book book) {
        manager.addBook(book);
    }

    public void updateBook(String isbn, Book book) {
        manager.updateBook(isbn, book);
    }

    public void deleteBook(String isbn) {
        manager.deleteBook(isbn);
    }

    public void addCategory(String name) {
        manager.addCategory(name);
    }

    public void borrowBook(String isbn, String borrower) {
        manager.borrowBook(isbn, borrower);
    }

    public void returnBook(String isbn) {
        manager.returnBook(isbn);
    }
}
    

