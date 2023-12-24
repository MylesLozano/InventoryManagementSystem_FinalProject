import javax.swing.table.DefaultTableModel;
import java.io.*;

public class InventoryDatabase {
    private final String filename;

    public InventoryDatabase(String filename) {
        this.filename = filename;
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        try {
            File file = new File(filename);
            if (file.createNewFile()) {
                // File is created, initialize with default content if needed.
                FileWriter writer = new FileWriter(file);

                // Default content here
                String defaultContent = "This is the default content.";
                writer.write(defaultContent);

                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void storeToFile(String records) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayRecord(DefaultTableModel model) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("#");
                model.addRow(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
