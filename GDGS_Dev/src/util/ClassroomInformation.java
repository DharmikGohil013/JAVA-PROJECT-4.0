package util;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ClassroomInformation {

    JFrame frame;
    JTable classroomTable;
    DefaultTableModel model;
    JScrollPane scrollPane;
    JButton addButton, editButton, deleteButton, backButton;
    
    public ClassroomInformation() {
        frame = new JFrame("Classroom Information");
        frame.setLayout(new BorderLayout());

        // Table model to hold classroom data
        model = new DefaultTableModel();
        model.addColumn("Classroom ID");
        model.addColumn("Room Number");
        model.addColumn("Building Name");
        model.addColumn("Capacity");
        model.addColumn("Resources");
        model.addColumn("Availability");

        // Table to display classroom information
        classroomTable = new JTable(model);
        scrollPane = new JScrollPane(classroomTable);
        
        // Fetch the data from the database and load into the table
        loadClassroomData();

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Add button
        addButton = new JButton("Add New Classroom");
        addButton.addActionListener(e -> addNewClassroom());

        // Edit button
        editButton = new JButton("Edit Classroom");
        editButton.addActionListener(e -> editClassroom());

        // Delete button
        deleteButton = new JButton("Delete Classroom");
        deleteButton.addActionListener(e -> deleteClassroom());

        // Back button
        backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.dispose());

        // Add buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        // Add the scroll pane and button panel to the frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Frame settings
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Fetch classroom data from the database
    private void loadClassroomData() {
        String url = "jdbc:mysql://localhost:3306/test_db"; // Update with your DB details
        String user = "root";
        String password = "DHARMIKgohil@2006"; // Update with your MySQL password

        String query = "SELECT * FROM classroom_info";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Remove previous data
            model.setRowCount(0);

            // Loop through the result set and add data to the table
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(String.valueOf(rs.getInt("classroom_id")));
                row.add(rs.getString("room_number"));
                row.add(rs.getString("building_name"));
                row.add(String.valueOf(rs.getInt("capacity")));
                row.add(rs.getString("resources"));
                row.add(rs.getString("availability_status"));

                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a new classroom
    private void addNewClassroom() {
        // Create a new form to input classroom details
        JTextField roomField = new JTextField();
        JTextField buildingField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField resourcesField = new JTextField();
        String[] statusOptions = {"Available", "Not Available"};
        JComboBox<String> availabilityBox = new JComboBox<>(statusOptions);

        Object[] message = {
            "Room Number:", roomField,
            "Building Name:", buildingField,
            "Capacity:", capacityField,
            "Resources:", resourcesField,
            "Availability:", availabilityBox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add New Classroom", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String roomNumber = roomField.getText();
            String buildingName = buildingField.getText();
            int capacity = Integer.parseInt(capacityField.getText());
            String resources = resourcesField.getText();
            String availability = (String) availabilityBox.getSelectedItem();

            insertClassroom(roomNumber, buildingName, capacity, resources, availability);
            loadClassroomData();  // Refresh the table
        }
    }

    // Insert new classroom data into the database
    private void insertClassroom(String roomNumber, String buildingName, int capacity, String resources, String availability) {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String user = "root";
        String password = "DHARMIKgohil@2006";

        String query = "INSERT INTO classroom_info (room_number, building_name, capacity, resources, availability_status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, roomNumber);
            pstmt.setString(2, buildingName);
            pstmt.setInt(3, capacity);
            pstmt.setString(4, resources);
            pstmt.setString(5, availability);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Edit selected classroom record
    private void editClassroom() {
        int selectedRow = classroomTable.getSelectedRow();
        if (selectedRow >= 0) {
            int classroomId = Integer.parseInt((String) classroomTable.getValueAt(selectedRow, 0));

            JTextField roomField = new JTextField((String) classroomTable.getValueAt(selectedRow, 1));
            JTextField buildingField = new JTextField((String) classroomTable.getValueAt(selectedRow, 2));
            JTextField capacityField = new JTextField((String) classroomTable.getValueAt(selectedRow, 3));
            JTextField resourcesField = new JTextField((String) classroomTable.getValueAt(selectedRow, 4));
            String[] statusOptions = {"Available", "Not Available"};
            JComboBox<String> availabilityBox = new JComboBox<>(statusOptions);
            availabilityBox.setSelectedItem(classroomTable.getValueAt(selectedRow, 5));

            Object[] message = {
                "Room Number:", roomField,
                "Building Name:", buildingField,
                "Capacity:", capacityField,
                "Resources:", resourcesField,
                "Availability:", availabilityBox
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Edit Classroom", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String roomNumber = roomField.getText();
                String buildingName = buildingField.getText();
                int capacity = Integer.parseInt(capacityField.getText());
                String resources = resourcesField.getText();
                String availability = (String) availabilityBox.getSelectedItem();

                updateClassroom(classroomId, roomNumber, buildingName, capacity, resources, availability);
                loadClassroomData();  // Refresh the table
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a classroom to edit.");
        }
    }

    // Update classroom information in the database
    private void updateClassroom(int classroomId, String roomNumber, String buildingName, int capacity, String resources, String availability) {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String user = "root";
        String password = "DHARMIKgohil@2006";

        String query = "UPDATE classroom_info SET room_number = ?, building_name = ?, capacity = ?, resources = ?, availability_status = ? WHERE classroom_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, roomNumber);
            pstmt.setString(2, buildingName);
            pstmt.setInt(3, capacity);
            pstmt.setString(4, resources);
            pstmt.setString(5, availability);
            pstmt.setInt(6, classroomId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete selected classroom record
    private void deleteClassroom() {
        int selectedRow = classroomTable.getSelectedRow();
        if (selectedRow >= 0) {
            int classroomId = Integer.parseInt(null);
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this classroom?", "Delete Classroom", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteClassroomFromDB(classroomId);
                loadClassroomData();  // Refresh the table after deletion
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a classroom to delete.");
        }
    }
    
    // Method to delete classroom from the database
    private void deleteClassroomFromDB(int classroomId)
     {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String user = "root";
        String password = "DHARMIKgohil@2006";
    
        String query = "DELETE FROM classroom_info WHERE classroom_id = ?";
    
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setInt(1, classroomId);
            pstmt.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    // Main method to run the program
    public static void main(String[] args) 
    {
        new ClassroomInformation();
    }
    
}