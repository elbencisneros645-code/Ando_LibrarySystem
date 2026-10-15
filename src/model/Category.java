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

public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public Category(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Category name cannot be empty."
            );
        }

        this.name = name.trim();
    }

    @Override
    public String toString() {
        return name;
    }
}
