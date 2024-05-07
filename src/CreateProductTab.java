import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CreateProductTab {
    JPanel panel;

    CreateProductTab(ProductTab productTab){
        // Create a JPanel
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(8,2 , 10, 5)); // GridLayout with 4 rows and 2 columns
        // Create JLabels for input fields
        JLabel nameLabel = new JLabel("Name:");
        JLabel categoryLabel = new JLabel("Category:");
        JLabel quantityLabel = new JLabel("Quantity:");
        JLabel location = new JLabel("Location:");
//        JLabel statusLabel = new JLabel("Status:");
        JLabel priceLabel = new JLabel("Price:");

        // Create JTextFields for input
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        nameField.setMaximumSize(new Dimension(200, 20));
        JTextField categoryField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();
        // Create JCheckBox for status
//        JCheckBox statusCheckBox = new JCheckBox("In Stock");
//        statusCheckBox.setPreferredSize(new Dimension(200, 20));
        // Create JRadioButtons for availability
        JRadioButton availableRadioButton = new JRadioButton("Available");
        JRadioButton outOfStockRadioButton = new JRadioButton("Out of Stock");
        ButtonGroup availabilityGroup = new ButtonGroup();
        availabilityGroup.add(availableRadioButton);
        availabilityGroup.add(outOfStockRadioButton);


//        menues starts


        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a "View" menu
        JMenu viewMenu = new JMenu("View");

        // Create 10 countries as menu items
        String[] countries = {"iStanbul", "bursa", "gaziantep", "karabük", "izmir",
                "izmit", "sakarya", "ankara", "konya", "antalya"};

        // Add countries as menu items to the "View" menu
        for (String country : countries) {
            JMenuItem countryItem = new JMenuItem(country);
            viewMenu.add(countryItem);
        }

        // Add the "View" menu to the menu bar
        menuBar.add(viewMenu);

//        menus ends


        // Add components to the panel
//        panel2.add(menuBar, BorderLayout.NORTH);
        panel2.add(nameLabel);
        panel2.add(nameField);
        panel2.add(categoryLabel);
        panel2.add(categoryField);
        panel2.add(quantityLabel);
        panel2.add(quantityField);
//        panel2.add(statusLabel);
//        panel2.add(statusCheckBox);
        panel2.add(availableRadioButton);
        panel2.add(outOfStockRadioButton);
        panel2.add(location);
        panel2.add(menuBar);
        panel2.add(priceLabel);
        panel2.add(priceField);

        // Create a JButton for adding the product
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Code to add the product to the database or perform any necessary action
                String name = nameField.getText();
                String category = categoryField.getText();
                String price = priceField.getText();
                int quantity = 0; // Initialize quantity to handle potential parsing errors
                try {
//                    quantity must be greater than 0 and must be added regex for nunmber
                    if(quantityField.getText().length() == 0 && !quantityField.getText().matches("^[0-9]*$")){
                        JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter numbers only.");
                        return;
                    }
                    quantity = Integer.parseInt(quantityField.getText());
                } catch (NumberFormatException err) {
                    System.err.println("Invalid quantity format: " + err.getMessage());
                    JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter numbers only.");
                    return;
                }
                if (!name.matches("[a-zA-Z\\s]+")) {
                    JOptionPane.showMessageDialog(null, "Invalid product name. Please enter alphabets and spaces only.");
                    return;
                }
                if(!category.matches("[a-zA-Z\\s]+")){
                    JOptionPane.showMessageDialog(null, "Invalid category name. Please enter alphabets and spaces only.");
                    return;
                }
//                price regext && price must be greater than 0 must be added
                if(!priceField.getText().matches("^[0-9]*\\$$") || priceField.getText().length() == 0){
                    JOptionPane.showMessageDialog(null, "Invalid price. Please enter numbers only. with dollar sign at the end");
                    return;
                }

                String availability = availableRadioButton.isSelected() ? "Available" : "Out of Stock";
                System.out.println("Product Name: " + name);
                System.out.println("Category: " + category);
                System.out.println("Quantity: " + quantity);
                System.out.println("Availability: " + availability);
                System.out.println("Price: " + priceField.getText());

                Connection conn = null;
                PreparedStatement preparedStatement = null;
                try {
                    // JDBC URL for MySQL database (change accordingly)
                    String url = "jdbc:mysql://localhost:6033/hala";
                    String username = "root";
                    String password = "hani2003";

                    // Establishing the connection
                    conn = DriverManager.getConnection(url, username, password);

                    // SQL CREATE TABLE statement
                    String createTableSQL = "CREATE TABLE IF NOT EXISTS stock (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "name VARCHAR(255) NOT NULL," +
                            "category VARCHAR(255) NOT NULL," +
                            "quantity INT NOT NULL," +
                            "availability VARCHAR(255) NOT NULL," +
                            "price VARCHAR(255) NOT NULL" +
                            ")";

                    // Creating a Statement object to execute the SQL statement
                    Statement statement = conn.createStatement();

                    // Executing the CREATE TABLE statement
                    statement.executeUpdate(createTableSQL);

                    // SQL INSERT statement
                    String insertSQL = "INSERT INTO stock (name, category, quantity, availability, price) VALUES (?, ?, ?, ?, ?)";

                    // Creating a PreparedStatement object to execute the SQL statement
                    preparedStatement = conn.prepareStatement(insertSQL);

                    // Setting the parameters for the PreparedStatement object
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, category);
                    preparedStatement.setInt(3, quantity);
                    preparedStatement.setString(4, availability);
                    preparedStatement.setString(5, price);


                    preparedStatement.executeUpdate();
                    // Connection successful
                    System.out.println("Table created (if not exists) successfully!");
                    // Connection successful
                    System.out.println("Data inserted into the stock table!");
                } catch (SQLException error) {
                    // Connection error or SQL error
                    System.err.println("Error: " + error.getMessage());
                } finally {
                    // Closing PreparedStatement and Connection objects
                    productTab.refreshTable();
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException error) {
                        System.err.println("Error closing resources: " + error.getMessage());
                    }
                }


            }
        });
        panel2.add(addButton, BorderLayout.CENTER);
    panel = new JPanel();
    panel.setLayout(new BorderLayout(10 , 10));
    panel.add(panel2, BorderLayout.NORTH);
    }

}
