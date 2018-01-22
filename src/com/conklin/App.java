/*
 Name: Nick Conklin
 Course: CNT 4714 – Spring 2018
 Assignment title: Program 1 – Event-driven Programming
 Date: Sunday January 28, 2018
*/

package com.conklin;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class App extends JFrame{

    private JPanel pnlMain;
    private JButton btnProcess;
    private JButton btnConfirm;
    private JButton btnViewOrder;
    private JButton btnFinishOrder;
    private JButton btnNewOrder;
    private JButton btnExit;
    private JTextField txtOrderQty;
    private JTextField txtBookId;
    private JTextField txtBookQty;
    private JTextField txtItemInfo;
    private JTextField txtSubtotal;
    private JLabel lblOrderQty;
    private JLabel lblBookId;
    private JLabel lblBookQty;
    private JLabel lblItemInfo;
    private JLabel lblSubtotal;

    int orderQty = 0;
    int bookQtyInt = 0;

    Book getBookById(ArrayList<Book> bookList, String id)
    {
        for (Book b : bookList)
        {
            if (b.bookID.equals(id)) {
                return b;
            }
        }
        JOptionPane.showMessageDialog(null, "Book ID " + id + " not in file.");
        return null;
    }

    // Create an ArrayList of Book objects
    ArrayList<Book> bookList = new ArrayList<Book>();

    // Create and initialize a Book object for use later
    Book foundBook = new Book("", "", "");

    // Initialize the discount
    double discount = 0;

    public App() {

        // Get the file from the directory
        Path fileName = Paths.get("inventory.txt");

        // Read through the file line by line
        // Split the lines into an array called 'tokens'
        // Create a Book object and store it in the ArrayList each time through the loop
        try (InputStream in = Files.newInputStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                Book newBook = new Book(tokens[0], tokens[1], tokens[2]);
                bookList.add(newBook);
                System.out.println(line);
            }
        } catch (IOException x) {
            System.err.println(x);
        }

        // Action Listener for the Exit button
        btnExit.addActionListener(e -> System.exit(0));

        btnProcess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Get the String value of the ID and Qty
                String bookID = txtBookId.getText();

                if (txtOrderQty.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please input an order quantity");
                    return;
                } else if (txtBookQty.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please input a book quantity");
                    return;
                } else if (txtBookId.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please input a book ID");
                    return;
                }

                // Validate the txtOrderQty field to verify a number was entered
                try {
                    orderQty = Integer.parseInt(txtOrderQty.getText());
                } catch (NumberFormatException nfe2) {
                    JOptionPane.showMessageDialog(null, "Invalid entry for order quantity");
                    return;
                }

                try {
                    bookQtyInt = Integer.parseInt(txtBookQty.getText());
                } catch (NumberFormatException nfe1) {
                    JOptionPane.showMessageDialog(null, "Invalid entry for book quantity");
                    return;
                }

                // Calculate the discount based on the number of books ordered
                if (bookQtyInt <= 4) {
                    discount = 0;
                } else if (bookQtyInt >= 5 && bookQtyInt <= 9) {
                    discount = 0.10;
                } else if (bookQtyInt >= 10 && bookQtyInt <= 14) {
                    discount = 0.15;
                } else if (bookQtyInt >= 15) {
                    discount = 0.20;
                }

                foundBook = getBookById(bookList, bookID);

                // Calculate the percent discount, total price and total price with discount
                double percentDiscount = discount * 100;
                double totalPrice = bookQtyInt * Double.parseDouble(foundBook.getPrice());
                double totalPriceWithDiscount = totalPrice - (totalPrice * discount);

                // Append the book info, qty ordered and price with discount to the info text field
                txtItemInfo.setText(foundBook.toString() + " " + bookQtyInt + " " + percentDiscount + "%" + " " +
                        totalPriceWithDiscount);

                // Set the confirm button to enabled and process button to disabled and disable the order qty field
                txtOrderQty.setEditable(false);
                btnConfirm.setEnabled(true);
                btnProcess.setEnabled(false);
            }
        });
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSubtotal.setText(foundBook.getPrice());
                JOptionPane.showMessageDialog(null, "Item accepted");
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().pnlMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
