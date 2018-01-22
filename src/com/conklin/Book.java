package com.conklin;

public class Book {
    public String bookID;
    public String authorTitle;
    public String price;


    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public Book(String bookID, String authorTitle, String price) {
        this.bookID = bookID;
        this.authorTitle = authorTitle;
        this.price = price;
    }

    public String toString() {
        return this.bookID + " " + this.authorTitle + " " + this.price;
    }
}
