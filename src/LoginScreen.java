import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends JFrame implements ActionListener {
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JButton btnLogin;

    private final InventorySystem inventorySystem;

    public LoginScreen(InventorySystem inventorySystem) {
        super("Login for Lots4Less Inventory Management System");
        this.inventorySystem = inventorySystem;
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Customize the background color here
        getContentPane().setBackground(new Color(244, 160, 170, 100)); // Example: Light Gray

        // Set a custom icon (replace "path/to/icon.png" with your icon file path)
        setIconImage(new ImageIcon("InvSys_UI/Lots4Less_LOGO.jpg").getImage());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(btnLogin, gbc);

        add(panel);
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnLogin)) {
            // Check username and password (replace with your authentication logic)
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (validateCredentials(username, password)) {
                // Clear password field for security
                txtPassword.setText("");

                // Close the login screen
                dispose();

                // Show the main InventorySystem window
                inventorySystem.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);

                // Clear password field for security after a failed attempt
                txtPassword.setText("");
            }
        }
    }

    private boolean validateCredentials(String username, String password) {
        // Retrieve credentials from the file
        Map<String, String> credentials = readCredentialsFromFile();

        // Check if the entered credentials match any of the valid combinations
        return credentials.containsKey(username) && credentials.get(username).equals(password);
    }

    private Map<String, String> readCredentialsFromFile() {
        Map<String, String> credentials = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    credentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return credentials;
    }
}
