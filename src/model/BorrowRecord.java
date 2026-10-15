/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */

import java.io.Serializable;
import java.time.LocalDate;

public class BorrowRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String isbn;
    private final String borrower;
    private final LocalDate borrowedOn;
    private LocalDate returnedOn;

    public BorrowRecord(String isbn, String borrower) {
        this.isbn = isbn;
        this.borrower = borrower;
        this.borrowedOn = LocalDate.now();
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBorrower() {
        return borrower;
    }

    public LocalDate getBorrowedOn() {
        return borrowedOn;
    }

    public LocalDate getReturnedOn() {
        return returnedOn;
    }

    public void markReturned() {
        returnedOn = LocalDate.now();
    }

    public boolean isActive() {
        return returnedOn == null;
    }
}