import java.awt.*;
import javax.swing.*;

public class InventoryFrame extends JFrame {
    // Data fields for width and height
    private int W, H;

    /** Constructor: with default screen/window size (800x500)
     * @author Myles Lozano */
    public InventoryFrame() {
        super(); // calls superclass JFrame
        H = 800;
        W = 500;
        setWindowSize(W, H);
    }

    /** Parameterized constructor: user may set based on preference
     * @author Myles Lozano
     * @param width  - size of the width
     * @param height - size of the length */
    public InventoryFrame(int width, int height) {
        super();
        setWindowSize(width, height);
    }

    /** Parameterized constructor: user may set based on preference
     * @author Myles Lozano
     * @param title  - caption of the frame or window
     * @param width  - size of the width
     * @param height - size of the length */
    public InventoryFrame(String title, int width, int height) {
        super(title);
        setWindowSize(width, height);
    }

    /** Parameterized constructor: user may set based on preference
     * @author Myles Lozano
     * @param title   - caption of the frame or window
     * @param width   - size of the width
     * @param height  - size of the length
     * @param visible - setting frame to show when true */
    public InventoryFrame(String title, int width, int height, boolean visible) {
        super(title);
        setWindowSize(width, height);
        setVisible(visible);
    }

    /** Parameterized constructor: user may set based on preference
     * @author Myles Lozano
     * @param width  - size of the width
     * @param height - size of the length */
    public void setWindowSize(int width, int height) {
        H = height;
        W = width;
        setSize(width, height);
    }

    /** Parameterized constructor: user may set based on preference
     * @author Myles Lozano
     * @param title - caption of the frame or window
     * @param width - size of the width
     * @param height- size of the length */
    public void setInventoryFrame(String title, int width, int height) {
        setTitle(title);
        setWindowSize(width, height);
    }

    /** Parameterized constructor: user may set based on preference
     * @author Myles Lozano
     * @param title        - caption of the frame or window
     * @param width        - size of the width
     * @param height       - size of the length
     * @param visible      - setting frame to show when true
     * @param closeOperation - 0 to 3 values */
    public void setInventoryFrame(String title, int width, int height, boolean visible, int closeOperation) {
        setInventoryFrame(title, width, height);
        setVisible(visible);
        setDefaultCloseOperation(closeOperation);
    }

    /** Parameterized constructor: user may set based on preference
     * @author Myles Lozano
     * @param title        - caption of the frame or window
     * @param width        - size of the width
     * @param height       - size of the length
     * @param visible      - setting frame to show when true
     * @param closeOperation - 0 to 3 values
     * @param resize        - prevent frame from resizing if false */
    public void setInventoryFrame(String title, int width, int height, boolean visible, int closeOperation,
                                  boolean resize) {
        setInventoryFrame(title, width, height, visible, closeOperation);
        setResizable(resize);
    }

    /** Set the background color of the frame
     * @param red     - 0 to 255
     * @param green   - 0 to 255
     * @param blue    - 0 to 255
     * @param opacity - 0 to 100 */
    public void setBackgroundColor(int red, int green, int blue, int opacity) {
        getContentPane().setBackground(new Color(red, green, blue, opacity));
    }

    /** Set the background image of the frame
     * @param file - location of the image file
     * @return JPanel - returns the method as a JPanel */
    public JPanel setBackgroundImage(String file) {
        JPanel panelBG = new JPanel();
        JLabel img = new JLabel(new ImageIcon(file)); // set image to JLabel
        panelBG.add(img); // add label to panelBG
        return panelBG;
    }
    /** Set the application icon for the frame
     * @param iconFilePath - location of the image file for the icon */
    public void setAppIcon(String iconFilePath) {
        ImageIcon icon = new ImageIcon(iconFilePath);
        setIconImage(icon.getImage());
    }
}
