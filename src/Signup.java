import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Signup extends JDialog {
    private JLabel lable;
    private JButton signUpButton;
    private JButton signInButton;
    private JButton cancelButton;
    private JTextField name;
    private JTextField email;
    private JTextField phone;
    private JPasswordField password;
    private JPanel signupPanel;



    public Signup() {
        super();
        setContentPane(signupPanel);
        setTitle("Sign Up");
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        signInButton.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Sign In Button Clicked");
                dispose();
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nameField = name.getText();
                String emailField = email.getText();
                String phoneField = phone.getText();
                String passwordField = password.getText();



                if (!nameField.matches("[a-zA-Z\\s]+")) {
                    JOptionPane.showMessageDialog(null, "Invalid name. Please enter only letters and spaces.");
                } else if (!emailField.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
                    JOptionPane.showMessageDialog(null, "Invalid email address.");
                } else if (!phoneField.matches("^\\d{10}$")) {
                    JOptionPane.showMessageDialog(null, "Invalid phone number. Please enter 10 digits only.");
                } else if (!passwordField.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")) {
                    JOptionPane.showMessageDialog(null, "Invalid password. Password must be at least 8 characters long and contain at least one digit, one lowercase letter, one uppercase letter, and no spaces.");
                } else {
                    PreparedStatement preparedStatement = null;

                    String url = "jdbc:mysql://localhost:6033/hala";
                    String username = "root";
                    String passwordSql = "hani2003";

                    // Establishing the connection
                    Connection   conn = null;
                    try {
                        conn = DriverManager.getConnection(url, username, passwordSql);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        if (!doesTableExist(conn)) {
                            // Table does not exist, create it
                            createTableIfNotExists(conn);
                        }
                        // JDBC URL for MySQL database (change accordingly)
                        if (!doesUserExist(conn, emailField)) {
                            // User does not exist, save the user
                            saveUser(conn, nameField, emailField, phoneField, passwordField);
                            JOptionPane.showMessageDialog(null, "User created successfully.");
                            dispose();
                            LoginForm loginForm = new LoginForm();
                            loginForm.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "User already exists with email: " + emailField);
                        }
                    } catch (SQLException er) {
                        er.printStackTrace(); // Handle any SQL exceptions
                    } finally {
                        // Close the database connection
                        try {
                            if (conn != null) {
                                conn.close();
                            }
                        } catch (SQLException err) {
                            err.printStackTrace();
                        }
                    }
                }

            }

        });
    }

    private void setLocationRelativeTo() {
    }




    public static boolean doesUserExist(Connection connection, String email) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        }
        return false;
    }

    // Method to save user details if the user does not already exist
    public static void saveUser(Connection connection, String name, String email, String phone, String password) throws SQLException {
        String insertSql = "INSERT INTO users (name, email, phone, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setString(4, password);
            statement.executeUpdate();
        }
    }

    public static boolean doesTableExist(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM information_schema.tables WHERE table_schema = ? AND table_name = 'users'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, connection.getCatalog());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        }
        return false;
    }

    public static void createTableIfNotExists(Connection connection) throws SQLException {
        String createTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) UNIQUE NOT NULL," +
                "phone VARCHAR(20) NOT NULL," +
                "password VARCHAR(255) NOT NULL" +
                ")";
        try (PreparedStatement statement = connection.prepareStatement(createTableSql)) {
            statement.executeUpdate();
        }
    }

}
