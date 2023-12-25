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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InventorySystem inventorySystem = new InventorySystem();
            inventorySystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            inventorySystem.setVisible(false); // Initially hide the main window
            inventorySystem.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    inventorySystem.process(); // Call the process method when closing
                    System.exit(0);
                }
            });
        });
    }
    public LoginScreen(InventorySystem inventorySystem) {
        super("Login for Lots4Less IMS");
        this.inventorySystem = inventorySystem;
        setSize(600, 400); // Adjusted height to accommodate social media icons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Customize the background color here
        getContentPane().setBackground(new Color(244, 190, 240, 200));

        // Set a custom icon
        setIconImage(new ImageIcon("InvMngSys_UI/Lots4Less_LOGO.jpg").getImage());

        // Use null layout for absolute positioning
        setLayout(null);

        // Add components for username, password, and login button
        JLabel lblUsername = new JLabel("Username:");
        txtUsername = new JTextField(10);
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(10);

        btnLogin = new JButton("Login", new ImageIcon("InvMngSys_UI/"));
        btnLogin.addActionListener(this);

        // Set bounds for each component
        lblUsername.setBounds(50, 10, 150, 30);
        txtUsername.setBounds(50, 50, 150, 30);
        lblPassword.setBounds(50, 90, 150, 30);
        txtPassword.setBounds(50, 130, 150, 30);
        btnLogin.setBounds(50, 170, 80, 30);

        // Create a subPanel for the image
        JPanel imagePanel = createImagePanel();

        // Set bounds for the image panel
        imagePanel.setBounds(300, 20, 250, 250);

        // Add components to the frame
        add(lblUsername);
        add(txtUsername);
        add(lblPassword);
        add(txtPassword);
        add(btnLogin);
        add(imagePanel);

        // Add icons for Facebook, Instagram, and Twitter
        ImageIcon facebookIcon = new ImageIcon("InvMngSys_UI/FacebookLOGO.png");
        JLabel facebookLabel = new JLabel(new ImageIcon(facebookIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        facebookLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        facebookLabel.setToolTipText("Visit Lots4Less Facebook");
        facebookLabel.addMouseListener(new LinkMouseListener("https://www.facebook.com/LotsForLessPH/"));

        ImageIcon instagramIcon = new ImageIcon("InvMngSys_UI/InstagramICON.png");
        JLabel instagramLabel = new JLabel(new ImageIcon(instagramIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        instagramLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        instagramLabel.setToolTipText("Visit Lots4Less Instagram");
        instagramLabel.addMouseListener(new LinkMouseListener("https://www.instagram.com/explore/tags/pixelart/top/"));

        ImageIcon twitterIcon = new ImageIcon("InvMngSys_UI/TwitterXLOGO.png");
        JLabel twitterLabel = new JLabel(new ImageIcon(twitterIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH)));
        twitterLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        twitterLabel.setToolTipText("Visit Lots4Less Twitter");
        twitterLabel.addMouseListener(new LinkMouseListener("https://twitter.com/pixelartjourney?lang=en"));

        // Set bounds for each social media icon
        facebookLabel.setBounds(50, 220, 50, 50);
        instagramLabel.setBounds(110, 220, 50, 50);
        twitterLabel.setBounds(170, 220, 45, 45);

        // Add the social media icons to the frame
        add(facebookLabel);
        add(instagramLabel);
        add(twitterLabel);

        // Add your custom image and link below
        ImageIcon customIcon = new ImageIcon("InvMngSys_UI/WELCOME.png");
        JLabel customLabel = new JLabel(new ImageIcon(customIcon.getImage().getScaledInstance(250, 100, Image.SCALE_SMOOTH)));

        customLabel.setBounds(275, 270, 300, 100);
        add(customLabel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel();

        // Create an ImageIcon for the image
        ImageIcon sideImageIcon = new ImageIcon("InvMngSys_UI/Lots4Less_LOGO.jpg");
        Image sideImage = sideImageIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        ImageIcon scaledSideImageIcon = new ImageIcon(sideImage);

        // Add JLabel for the image to the image panel
        JLabel sideImageLabel = new JLabel(scaledSideImageIcon);
        imagePanel.add(sideImageLabel);

        return imagePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnLogin)) {
            // Check username and password
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
