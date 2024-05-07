import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm  extends JDialog{
    private JLabel Email;
    private JTextField textField1;
    private JButton btn;
    private JButton btnCancel;
    private JPanel LoginForm;
    private JButton singup;
    private JPasswordField passwordField1;

    public LoginForm(){
        super();
        setContentPane(LoginForm);
        setTitle("Login");
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel button clicked");
                dispose();
            }
        });
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                dispose();
//                MainWindow mainWindow = new MainWindow();
//                mainWindow.show();


                String url = "jdbc:mysql://localhost:6033/hala";
                String username = "root";
                String passwordSql = "hani2003";


                String enteredEmail = textField1.getText();
                String enteredPassword = new String(passwordField1.getPassword());

                // Connect to your database
                try (Connection connection = DriverManager.getConnection(url, username, passwordSql)) {
                    String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, enteredEmail);
                        statement.setString(2, enteredPassword);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            if (resultSet.next()) {
                                // Authentication successful
                                dispose();
                                MainWindow mainWindow = new MainWindow();
                                mainWindow.show();
                                System.out.println("OK button clicked");
                                System.out.println("Email: " + enteredEmail);
                            } else {
                                // Authentication failed
                                JOptionPane.showMessageDialog(null, "Invalid email or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (SQLException ex) {
                    // Handle any SQL exceptions
                    ex.printStackTrace();
                }
            }
        });
        singup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Signup signup = new Signup();
                signup.setVisible(true);
            }
        });
    }


    public static void main(String[] args){
        LoginForm loginForm = new LoginForm();


        loginForm.singup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Signup signup = new Signup();
                signup.setVisible(true);
            }
        });
        loginForm.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("OK button clicked");
            }
        });
    }
}
