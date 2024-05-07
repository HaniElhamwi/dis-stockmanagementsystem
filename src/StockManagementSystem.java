import javax.swing.*;

public class StockManagementSystem {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginDialog = new LoginForm();
            loginDialog.setVisible(true);
        });
    }
}
