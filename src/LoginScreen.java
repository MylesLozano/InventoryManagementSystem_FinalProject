import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        setSize(700, 200); // Adjusted size to accommodate the image
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Customize the background color here
        getContentPane().setBackground(new Color(244, 160, 170, 100)); // Example: Light Gray

        // Set a custom icon (replace "path/to/icon.png" with your icon file path)
        setIconImage(new ImageIcon("InvSys_UI/Lots4Less_LOGO.jpg").getImage());

        // Create a panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create an ImageIcon for the image (replace "path/to/image.jpg" with your image file path)
        ImageIcon sideImageIcon = new ImageIcon("InvSys_UI/Lots4Less_LOGO.jpg");
        Image sideImage = sideImageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon scaledSideImageIcon = new ImageIcon(sideImage);

        // Add JLabel for the image
        JLabel sideImageLabel = new JLabel(scaledSideImageIcon);
        gbc.gridx = 2; // Adjust the column based on your layout
        gbc.gridy = 0; // Adjust the row based on your layout
        gbc.gridheight = 3; // Adjust the grid height based on your layout
        panel.add(sideImageLabel, gbc);

        // Add components for username, password, and login button
        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);

        btnLogin = new JButton("Login");
        btnLogin.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1; // Reset grid height
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

        // Add icons for Facebook, Instagram, and Twitter
        ImageIcon facebookIcon = new ImageIcon("InvSys_UI/FacebookLOGO.png");
        JLabel facebookLabel = new JLabel(new ImageIcon(facebookIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        facebookLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        facebookLabel.setToolTipText("Visit Lots4Less Facebook");
        facebookLabel.addMouseListener(new LinkMouseListener("https://www.facebook.com/LotsForLessPH/"));

        ImageIcon instagramIcon = new ImageIcon("InvSys_UI/InstagramICON.png");
        JLabel instagramLabel = new JLabel(new ImageIcon(instagramIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        instagramLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        instagramLabel.setToolTipText("Visit Lots4Less Instagram");
        instagramLabel.addMouseListener(new LinkMouseListener("https://www.instagram.com/explore/tags/pixelart/top/"));

        ImageIcon twitterIcon = new ImageIcon("InvSys_UI/TwitterXLOGO.png");
        JLabel twitterLabel = new JLabel(new ImageIcon(twitterIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        twitterLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        twitterLabel.setToolTipText("Visit Lots4Less Twitter");
        twitterLabel.addMouseListener(new LinkMouseListener("https://twitter.com/pixelartjourney?lang=en"));

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(facebookLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(instagramLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        panel.add(twitterLabel, gbc);

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

    private static class LinkMouseListener extends MouseAdapter {
        private final String url;

        public LinkMouseListener(String url) {
            this.url = url;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            openWebpage(url);
        }

        private void openWebpage(String url) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                // Handle exceptions if necessary
            }
        }
    }
}
