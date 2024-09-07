package util;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddMaterialLinkPage {

    private JFrame frame;
    private JTextField classNameField;
    private JTextField titleField;
    private JTextField linkField;

    public AddMaterialLinkPage() {
        frame = new JFrame("Add Material Link");

        // Create components
        JLabel classNameLabel = new JLabel("Class Name:");
        classNameField = new JTextField(30);

        JLabel titleLabel = new JLabel("Material Title:");
        titleField = new JTextField(30);

        JLabel linkLabel = new JLabel("Link:");
        linkField = new JTextField(30);

        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");

        // Set layout and add components
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(classNameLabel, gbc);

        gbc.gridx = 1;
        frame.add(classNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(titleLabel, gbc);

        gbc.gridx = 1;
        frame.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(linkLabel, gbc);

        gbc.gridx = 1;
        frame.add(linkField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(submitButton, gbc);

        gbc.gridx = 0;
        frame.add(backButton, gbc);

        // Set button actions
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitMaterial();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // Navigate to the previous window if needed
                // new SomePreviousWindow();
            }
        });

        // Frame settings
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void submitMaterial() {
        String className = classNameField.getText();
        String title = titleField.getText();
        String link = linkField.getText();

        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO materials (class_name, title, link) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, className);
            stmt.setString(2, title);
            stmt.setString(3, link);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "Material link added successfully!");
                // Clear fields after successful insertion
                classNameField.setText("");
                titleField.setText("");
                linkField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add material link. Please try again.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new AddMaterialLinkPage();
    }
}
