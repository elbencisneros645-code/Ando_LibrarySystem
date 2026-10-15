/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package view;

/**
 *
 * @author USER
 */

import Controller.LibraryController;
import model.Book;
import model.Category;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainForm extends JFrame {

    private final LibraryController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField searchField = new JTextField(22);

    public MainForm() {
        controller = new LibraryController();

        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 540));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.setBackground(new Color(245, 247, 250));
        setContentPane(root);

        JLabel heading = new JLabel("Library Management System");
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        root.add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new Object[]{
                "ISBN", "Title", "Author", "Publisher",
                "Year", "Category", "Status"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);

        root.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setOpaque(false);

        JPanel searchPanel =
            new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);

        searchPanel.add(new JLabel(
            "Search title, author, ISBN, category:"));

        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        bottom.add(searchPanel, BorderLayout.NORTH);

        JPanel actions =
            new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setOpaque(false);

        JButton addButton = new JButton("Add Book");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton borrowButton = new JButton("Borrow");
        JButton returnButton = new JButton("Return");
        JButton categoryButton = new JButton("Add Category");

        actions.add(addButton);
        actions.add(editButton);
        actions.add(deleteButton);
        actions.add(borrowButton);
        actions.add(returnButton);
        actions.add(categoryButton);

        bottom.add(actions, BorderLayout.SOUTH);
        root.add(bottom, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addBook());
        editButton.addActionListener(e -> editBook());
        deleteButton.addActionListener(e -> deleteBook());
        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());
        categoryButton.addActionListener(e -> addCategory());

        searchButton.addActionListener(e ->
            refreshTable(controller.searchBooks(
                searchField.getText())));

        searchField.addActionListener(e ->
            refreshTable(controller.searchBooks(
                searchField.getText())));

        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshTable(controller.getBooks());
        });

        refreshTable(controller.getBooks());
    }

    private void refreshTable(List<Book> books) {
        tableModel.setRowCount(0);

        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getYear(),
                book.getCategory(),
                book.isBorrowed() ? "Borrowed" : "Available"
            });
        }
    }

    private Book selectedBook() {
        int row = table.getSelectedRow();

        if (row < 0) {
            showError("Please select a book from the table first.");
            return null;
        }

        String isbn = tableModel.getValueAt(
            table.convertRowIndexToModel(row), 0
        ).toString();

        for (Book book : controller.getBooks()) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }

        return null;
    }

    private void addBook() {
        showBookDialog(null);
    }

    private void editBook() {
        Book book = selectedBook();

        if (book != null) {
            showBookDialog(book);
        }
    }

    private void showBookDialog(Book existing) {
        JTextField isbn = new JTextField(
            existing == null ? "" : existing.getIsbn());

        JTextField title = new JTextField(
            existing == null ? "" : existing.getTitle());

        JTextField author = new JTextField(
            existing == null ? "" : existing.getAuthor());

        JTextField publisher = new JTextField(
            existing == null ? "" : existing.getPublisher());

        JTextField year = new JTextField(
            existing == null ? "" :
                String.valueOf(existing.getYear()));

        List<Category> categories = controller.getCategories();
        JComboBox<String> category = new JComboBox<>();

        for (Category c : categories) {
            category.addItem(c.getName());
        }

        if (existing != null) {
            category.setSelectedItem(existing.getCategory());
        }

        JPanel panel = new JPanel(
            new GridLayout(0, 2, 8, 8));

        panel.add(new JLabel("ISBN:"));
        panel.add(isbn);

        panel.add(new JLabel("Title:"));
        panel.add(title);

        panel.add(new JLabel("Author:"));
        panel.add(author);

        panel.add(new JLabel("Publisher:"));
        panel.add(publisher);

        panel.add(new JLabel("Publication year:"));
        panel.add(year);

        panel.add(new JLabel("Category:"));
        panel.add(category);

        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            existing == null ? "Add Book" : "Edit Book",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int publicationYear =
                Integer.parseInt(year.getText().trim());

            Book book = new Book(
                isbn.getText(),
                title.getText(),
                author.getText(),
                publisher.getText(),
                publicationYear,
                (String) category.getSelectedItem()
            );

            if (existing == null) {
                controller.addBook(book);
            } else {
                controller.updateBook(
                    existing.getIsbn(), book);
            }

            refreshTable(controller.getBooks());

        } catch (NumberFormatException e) {
            showError("Publication year must be a number.");

        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void deleteBook() {
        Book book = selectedBook();

        if (book == null) {
            return;
        }

        int result = JOptionPane.showConfirmDialog(
            this,
            "Delete '" + book.getTitle() + "'?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            try {
                controller.deleteBook(book.getIsbn());
                refreshTable(controller.getBooks());

            } catch (RuntimeException e) {
                showError(e.getMessage());
            }
        }
    }

    private void borrowBook() {
        Book book = selectedBook();

        if (book == null) {
            return;
        }

        String borrower = JOptionPane.showInputDialog(
            this,
            "Borrower's full name:",
            "Borrow Book",
            JOptionPane.QUESTION_MESSAGE
        );

        if (borrower == null) {
            return;
        }

        try {
            controller.borrowBook(book.getIsbn(), borrower);
            refreshTable(controller.getBooks());

        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void returnBook() {
        Book book = selectedBook();

        if (book == null) {
            return;
        }

        try {
            controller.returnBook(book.getIsbn());
            refreshTable(controller.getBooks());

        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void addCategory() {
        String name = JOptionPane.showInputDialog(
            this,
            "New category name:",
            "Add Category",
            JOptionPane.QUESTION_MESSAGE
        );

        if (name == null) {
            return;
        }

        try {
            controller.addCategory(name);

            JOptionPane.showMessageDialog(
                this,
                "Category added successfully."
            );

        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Library Management",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
