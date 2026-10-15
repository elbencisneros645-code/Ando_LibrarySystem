/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
/**
 *
 * @author USER
 */

    

 public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private int year;
    private boolean borrowed;

    public Book(String isbn, String title, String author,
                String publisher, int year, String category) {

        setIsbn(isbn);
        setTitle(title);
        setAuthor(author);
        setPublisher(publisher);
        setYear(year);
        setCategory(category);

        this.borrowed = false;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = required(isbn, "ISBN");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = required(title, "Title");
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = required(author, "Author");
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher == null
                ? "" : publisher.trim();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (year < 0 || year > 9999) {
            throw new IllegalArgumentException(
                "Enter a valid publication year."
            );
        }
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = required(category, "Category");
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    private String required(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                label + " cannot be empty."
            );
        }
        return value.trim();
    }
}

