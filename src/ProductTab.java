import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ProductTab {
    JScrollPane scrollPane;
    DefaultTableModel model;

    public ProductTab() {
        // Define column names
        String[] columnNames = {"id", "Product Name", "Category", "Quantity", "Availability", "Price"};
        String[] dbColumnNames = {"id", "name", "category", "quantity", "availability", "price"};
        // Create a DefaultTableModel with no data initially
        model = new DefaultTableModel(columnNames, 0);
        // Fetch initial data from the database
        fetchDataAndUpdateModel();
        // Create a JTable with the DefaultTableModel
        JTable   table = new JTable(model);

        // Allow cell editing
        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));

        // Listen for cell edits to update the database
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (row >= 0 && column >= 0) {
                    System.out.println("Row: " + row + " Column: " + column);
                    System.out.println("Data: " + table.getValueAt(row, column));
//                    get id from table
                    int id = (int) table.getValueAt(row, 0);
                    String fieldName = dbColumnNames[column];
                    Object data = table.getValueAt(row, column);
                    updateDatabase(id, fieldName , data);
                }
            }
        });



        // Create a JScrollPane and add the table to it
        scrollPane = new JScrollPane(table);
    }

    // Method to fetch data from the database and update the model
    private void fetchDataAndUpdateModel() {
        try {
            // JDBC URL for MySQL database (change accordingly)
            String url = "jdbc:mysql://localhost:6033/hala";
            String username = "root";
            String password = "hani2003";

            // Establishing the connection
            Connection conn = DriverManager.getConnection(url, username, password);

            // SQL SELECT statement to fetch stock data
            String selectSQL = "SELECT  id ,name , availability, price, category, quantity FROM stock";

            // Creating a Statement object to execute the SQL statement
            Statement statement = conn.createStatement();

            // Executing the SELECT statement
            ResultSet resultSet = statement.executeQuery(selectSQL);

            // Clear existing data from the model
            model.setRowCount(0);

            // Iterate over the result set and add data to the DefaultTableModel
            while (resultSet.next()) {
                String productName = resultSet.getString("name");
                int id = resultSet.getInt("id");
                String category = resultSet.getString("category");
                int quantity = resultSet.getInt("quantity");
                String availability = resultSet.getString("availability");
                String price = resultSet.getString("price");
                // Add a new row to the DefaultTableModel
                model.addRow(new Object[]{ id,productName, category, quantity, availability, price});
            }

            // Closing resources
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException error) {
            // Connection error or SQL error
            System.err.println("Error: " + error.getMessage());
        }
    }

    // Method to refresh the table
    public void refreshTable() {
        fetchDataAndUpdateModel();
    }


    private void updateDatabase(int id, String fieldName, Object data) {
        System.out.println("Updating database for ID: " + id + ", Field: " + fieldName);
        try {
            // JDBC URL for MySQL database (change accordingly)
            String url = "jdbc:mysql://localhost:6033/hala";
            String username = "root";
            String password = "hani2003";

            // Establishing the connection
            Connection conn = DriverManager.getConnection(url, username, password);

            // Construct SQL UPDATE statement
            String updateSQL = "UPDATE stock SET " + fieldName + " = ? WHERE id = ?";

            // Creating a PreparedStatement object to execute the SQL statement
            PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);

            // Set parameters for the PreparedStatement
            preparedStatement.setObject(1, data);
            preparedStatement.setInt(2, id);

            // Execute the SQL statement
            preparedStatement.executeUpdate();

            // Closing resources
            preparedStatement.close();
            conn.close();
        } catch (SQLException error) {
            // Connection error or SQL error
            System.err.println("Error: " + error.getMessage());
        }
    }


}