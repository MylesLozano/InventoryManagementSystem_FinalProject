import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.io.File;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.Vector;
import java.util.stream.IntStream;

public class InventorySystem extends InventoryFrame implements ActionListener {
    private JTextField txtInventoryNo, txtInventoryName, txtStockQuantity, txtStockPrice, txtSearch;

    private JComboBox<Integer> cboDay;
    private JComboBox<Month> cboMonth;
    private JComboBox<Integer> cboYear;
    private static final Font comboBoxFont = new Font("Arial", Font.PLAIN, 16);

    private JButton btnAdd, btnUpdate, btnRemove, btnClear, btnClose, btnSearch;

    private JTable tbl_Inventory;

    private DefaultTableModel model_inventory;
    private Vector<Object> rowData;
    private final InventoryDatabase inventory_db;

    private boolean editingMode = false;

    public InventorySystem() {
        initializedComponents();
        add(panelInventoryInfo()).setBounds(10, 10, 450, 200);

        // Move the Search functionality above the table
        JPanel searchPanel = new JPanel(new GridLayout(1, 3, 4, 2));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        add(searchPanel).setBounds(480, 320, 400, 20);

        add(panelInventoryTable()).setBounds(480, 20, 600, 290);
        add(panelInventoryButtons()).setBounds(10, 215, 450, 50);
        add(setBackgroundImage("InvMngSys_UI/BG_UI_Splash.png"));
        setAppIcon("InvMngSys_UI/Lots4Less_LOGO.jpg");

        setInventoryFrame("Inventory Management System for Lots4Less", 1100, 400, true, JFrame.EXIT_ON_CLOSE, true);
        setLocationRelativeTo(null);

        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnRemove.addActionListener(this);
        btnClear.addActionListener(this);
        btnClose.addActionListener(this);
        btnSearch.addActionListener(this);

        inventory_db = new InventoryDatabase("stock_inventory.txt");
        //It follows this format -
        //"Inventory No." # "Inventory Name" # "Stock Quantity" # "Stock Price" # "Selling Price" # "Date Added"
        inventory_db.displayRecord(model_inventory);

        resetComponents();

        getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle mouse click outside the table
                if (!tbl_Inventory.isEditing()) {
                    setEditingMode();
                    resetComponents();
                }
            }
        });
        LoginScreen loginScreen = new LoginScreen(this);
    }

    private void initializedComponents() {
        txtInventoryNo = new JTextField(10);
        txtInventoryNo.setEditable(false);

        txtInventoryName = new JTextField(10);
        txtStockQuantity = new JTextField(10);
        txtStockPrice = new JTextField(10);

        txtSearch = new JTextField("");

        cboMonth = new JComboBox<>(Month.values());
        cboDay = new JComboBox<>(getDaysInMonth());
        cboYear = new JComboBox<>(getYears());
        // Set the font for JComboBox components
        cboMonth.setFont(comboBoxFont);
        cboDay.setFont(comboBoxFont);
        cboYear.setFont(comboBoxFont);

        btnAdd = makeCircularButton(new ImageIcon("InvMngSys_UI/Add_Button.png"), "InvMngSys_UI/Add_Button.png");
        btnAdd.setToolTipText("Adds Inventory");

        btnUpdate = makeCircularButton(new ImageIcon("InvMngSys_UI/Update_Button.png"), "InvMngSys_UI/Update_Button.png");
        btnUpdate.setToolTipText("Updates Inventory");

        btnClear = makeCircularButton(new ImageIcon("InvMngSys_UI/Clear_Button.png"), "InvMngSys_UI/Clear_Button.png");
        btnClear.setToolTipText("Clears Inventory Form");

        btnRemove = makeCircularButton(new ImageIcon("InvMngSys_UI/Remove_Button.png"), "InvMngSys_UI/Remove_Button.png");
        btnRemove.setToolTipText("Removes Inventory");

        btnClose = makeCircularButton(new ImageIcon("InvMngSys_UI/Close_Button.png"), "InvMngSys_UI/Close_Button.png");
        btnClose.setToolTipText("Closes the Program");

        btnSearch = makeCircularButton(new ImageIcon("InvMngSys_UI/Search_Button.png"), "InvMngSys_UI/Search_Button.png");
        btnSearch.setToolTipText("Searches the Inventory");


        model_inventory = new DefaultTableModel();
        Vector<Object> columns = new Vector<>();
        String[] cols = {"Inventory No.", "Inventory Name", "Stock Quantity",
                         "Stock Price", "Selling Price", "Date Added"};
        Collections.addAll(columns, cols);
        model_inventory.setColumnIdentifiers(columns);

        btnUpdate.setEnabled(false);
    }

    private JPanel panelInventoryInfo() {
        JPanel panelInventoryInfo = new JPanel();
        panelInventoryInfo.setBorder(BorderFactory.createTitledBorder("Inventory Information"));
        panelInventoryInfo.setLayout(new GridLayout(6, 2));
        panelInventoryInfo.setOpaque(false);

        panelInventoryInfo.add(new JLabel("Inventory No.:"));
        panelInventoryInfo.add(txtInventoryNo);

        panelInventoryInfo.add(new JLabel("Inventory Name:"));
        panelInventoryInfo.add(txtInventoryName);

        panelInventoryInfo.add(new JLabel("Stock Quantity:"));
        panelInventoryInfo.add(txtStockQuantity);

        panelInventoryInfo.add(new JLabel("Stock Price:"));
        panelInventoryInfo.add(txtStockPrice);

        panelInventoryInfo.add(new JLabel("Date Added:"));
        JPanel datePanel = new JPanel(new GridLayout(1, 3, 5, 5));

        datePanel.add(cboMonth);
        datePanel.add(cboDay);
        datePanel.add(cboYear);

        panelInventoryInfo.add(datePanel);

        return panelInventoryInfo;
    }

    private JPanel panelInventoryButtons() {
        JPanel panelInventoryButtons = new JPanel();
        panelInventoryButtons.setLayout(new GridLayout(1, 3, 5, 2));
        panelInventoryButtons.setOpaque(false);
        panelInventoryButtons.add(btnAdd);
        panelInventoryButtons.add(btnUpdate);
        panelInventoryButtons.add(btnRemove);
        panelInventoryButtons.add(btnClear);
        panelInventoryButtons.add(btnClose);

        return panelInventoryButtons;
    }

    private JButton makeCircularButton(Icon icon, String imagePath) {
        // Check if the image file exists
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            JOptionPane.showMessageDialog(this, "Image file not found: " + imagePath, "Error", JOptionPane.ERROR_MESSAGE);
            return new JButton();  // Return an empty button or handle it appropriately
        }

        JButton button = new JButton("", icon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        Image img = ((ImageIcon) icon).getImage();
        int diameter = 50;
        Image scaledImg = img.getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImg));
        button.setPreferredSize(new Dimension(diameter, diameter));
        return button;
    }

    private JPanel panelInventoryTable() {
        JPanel panelInventoryTable = new JPanel();
        tbl_Inventory = new JTable(model_inventory) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbl_Inventory.setModel(model_inventory);
        tbl_Inventory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_Inventory.getTableHeader().setReorderingAllowed(false); // Disable column reordering
        tbl_Inventory.setCellSelectionEnabled(true); // Enable cell selection
        tbl_Inventory.getSelectionModel().addListSelectionListener(e -> handleTableSelection(e, tbl_Inventory));
        panelInventoryTable.setLayout(new BorderLayout());
        panelInventoryTable.add(panelInventoryButtons(), BorderLayout.NORTH);
        panelInventoryTable.add(new JScrollPane(tbl_Inventory), BorderLayout.CENTER);

        // Set preferred column widths
        int[] columnWidths = {80, 150, 100, 100, 100, 120}; // Adjust these values based on your preference
        setColumnWidths(tbl_Inventory, columnWidths);

        return panelInventoryTable;
    }

    private void resetComponents() {
        txtInventoryNo.setText(getRowCount());
        txtInventoryName.setText("");
        txtStockQuantity.setText("");
        txtStockPrice.setText("");
        cboMonth.setSelectedIndex(0);  // Set default month
        cboDay.setSelectedIndex(0);    // Set default day
        cboYear.setSelectedItem(Year.now().getValue());  // Set default year

        // After resetting components, enable the "Update" button if a cell is selected
        int selectedRow = tbl_Inventory.getSelectedRow();
        btnUpdate.setEnabled(selectedRow >= 0);
        btnRemove.setEnabled(selectedRow >= 0);
    }

    // Getters and Setters

    private String getRowCount() {
        return String.valueOf(model_inventory.getRowCount() + 1);
    }

    private void getData() {
        rowData = new Vector<>();
        rowData.add(txtInventoryNo.getText());

        // Validate and add Inventory Name
        String inventoryName = txtInventoryName.getText().trim();
        if (inventoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inventory Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            rowData.add(inventoryName);
        }

        // Validate and add Stock Quantity
        String stockQuantityText = txtStockQuantity.getText();
        try {
            int stockQuantity = Integer.parseInt(stockQuantityText);
            if (stockQuantity < 0) {
                throw new NumberFormatException();
            }
            rowData.add(stockQuantity);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock Quantity must be a non-negative integer", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if txtStockPrice is not empty before parsing
        String stockPriceText = txtStockPrice.getText();
        if (!stockPriceText.isEmpty()) {
            try {
                double stockPrice = Double.parseDouble(stockPriceText);
                if (stockPrice < 0) {
                    throw new NumberFormatException();
                }
                rowData.add(stockPrice);
                double sellingPrice = stockPrice * 1.1;
                rowData.add(String.format("%.2f", sellingPrice)); // Selling Price is 10% more than Stock Price
            } catch (NumberFormatException e) {
                // Handle the case where parsing fails (e.g., invalid double format)
                JOptionPane.showMessageDialog(this, "Invalid Stock Price format. Please enter a valid number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            // Handle the case where txtStockPrice is empty
            JOptionPane.showMessageDialog(this, "Stock Price cannot be empty. Setting default value.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            // Set a default value for stock price
            rowData.add(0.0);
        }
        rowData.add(getFormattedDate());
    }

    private String getFormattedDate() {
        Integer selectedDay = (Integer) cboDay.getSelectedItem();
        Month selectedMonth = (Month) cboMonth.getSelectedItem();
        Integer selectedYear = (Integer) cboYear.getSelectedItem();

        // Set default values to current date if any of the items is null
        if (selectedDay == null || selectedMonth == null || selectedYear == null) {
            JOptionPane.showMessageDialog(this, "Please select a valid date", "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }

        return String.format("%02d/%02d/%04d", selectedMonth.getValue(), selectedDay, selectedYear);
    }

    private Integer[] getDaysInMonth() {
        int selectedMonth = (cboMonth.getSelectedItem() != null) ? ((Month) cboMonth.getSelectedItem()).length(Year.now().isLeap()) : 31;
        return IntStream.rangeClosed(1, selectedMonth).boxed().toArray(Integer[]::new);
    }

    private Integer[] getYears() {
        int currentYear = Year.now().getValue();
        return IntStream.rangeClosed(currentYear - 10, currentYear + 10).boxed().toArray(Integer[]::new);
    }

    private void setColumnWidths(JTable table, int[] widths) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < widths.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    private void setEditingMode() {
        editingMode = false;
        btnAdd.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnRemove.setEnabled(false);
        btnClear.setEnabled(true);
        btnClose.setEnabled(true);
        btnSearch.setEnabled(true);
    }

    private void handleTableSelection(ListSelectionEvent e, JTable table) {
        if (!editingMode && !e.getValueIsAdjusting()) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Populate form fields with selected row data
                txtInventoryNo.setText(String.valueOf(model_inventory.getValueAt(selectedRow, 0)));
                txtInventoryName.setText(String.valueOf(model_inventory.getValueAt(selectedRow, 1)));
                txtStockQuantity.setText(String.valueOf(model_inventory.getValueAt(selectedRow, 2)));
                txtStockPrice.setText(String.valueOf(model_inventory.getValueAt(selectedRow, 3)));

                // Set the selected date in the combo boxes
                String dateStr = String.valueOf(model_inventory.getValueAt(selectedRow, 5));
                String[] dateParts = dateStr.split("/");
                if (dateParts.length == 3) {
                    cboMonth.setSelectedIndex(Integer.parseInt(dateParts[0]) - 1);
                    cboDay.setSelectedIndex(Integer.parseInt(dateParts[1]) - 1);
                    cboYear.setSelectedItem(Integer.parseInt(dateParts[2]));
                }

                // Enable the "Add" and "Remove" buttons when not in editing mode
                setEditingMode();

                // Enable the "Update" and "Remove" buttons when a cell is selected
                btnUpdate.setEnabled(true);
                btnRemove.setEnabled(true);
                btnAdd.setEnabled(false);
            } else {
                // No cell is selected, reset components and disable "Update" button
                resetComponents();
                btnUpdate.setEnabled(false);
                btnRemove.setEnabled(false);
            }
        }
    }

    private void performSearch() {
        String searchText = txtSearch.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term", "Error", JOptionPane.ERROR_MESSAGE);
            tbl_Inventory.setRowSorter(null);
            txtSearch.setText("");
            return;
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model_inventory);
        tbl_Inventory.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        if (tbl_Inventory.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No matching results found", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            tbl_Inventory.setRowSorter(null);
            txtSearch.setText("");
        }
    }

    public void process() {
        StringBuilder records = new StringBuilder();
        for (int r = 0; r < model_inventory.getRowCount(); r++) {
            for (int c = 0; c < model_inventory.getColumnCount(); c++) {
                records.append(model_inventory.getValueAt(r, c)).append("#");
            }
            records.append("\n");
        }
        inventory_db.storeToFile(records.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnAdd) || e.getSource().equals(btnUpdate)) {
            getData();

            int selectedRow = tbl_Inventory.getSelectedRow();
            if (selectedRow >= 0) {
                int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this item?", "Update Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Update the selected row with the new data
                    model_inventory.setValueAt(rowData.get(0), selectedRow, 0);
                    model_inventory.setValueAt(rowData.get(1), selectedRow, 1);
                    model_inventory.setValueAt(rowData.get(2), selectedRow, 2);
                    model_inventory.setValueAt(rowData.get(3), selectedRow, 3);
                    model_inventory.setValueAt(rowData.get(4), selectedRow, 4);
                    model_inventory.setValueAt(rowData.get(5), selectedRow, 5);

                    // Refresh the table display
                    model_inventory.fireTableDataChanged();

                    resetComponents();
                }
            } else {
                // Add a new item
                model_inventory.addRow(rowData);
                txtInventoryNo.setText(getRowCount());
                resetComponents();
            }
            JOptionPane.showMessageDialog(this, "Item has been successfully " + (selectedRow >= 0 ? "updated" : "added"), "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource().equals(btnRemove)) {
            int selectedRow = tbl_Inventory.getSelectedRow();
            if (selectedRow >= 0) {
                int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this item?", "Remove Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    model_inventory.removeRow(selectedRow);

                    // Update inventory numbers after removal
                    for (int i = selectedRow; i < model_inventory.getRowCount(); i++) {
                        model_inventory.setValueAt(String.valueOf(i + 1), i, 0);
                    }

                    resetComponents();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource().equals(btnClear)) {
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear the form?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                resetComponents();
            }
        } else if (e.getSource().equals(btnClose)) {
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the program?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Process and exit
                process();
                System.exit(0);
            }
        } else if (e.getSource().equals(btnSearch)) {
            // Handle the search button
            performSearch();
        }
        // Disable the "Add New" button when in editing mode
        btnAdd.setEnabled(tbl_Inventory.getSelectedRow() == -1);
    }

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
}
