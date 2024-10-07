package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class toFacultyScheduleSection extends JFrame {

    private JTable tasksTable;
    private DefaultTableModel tableModel;
    private JTextField taskField;
    private JButton addButton, editButton, deleteButton, backButton;

    // Constructor
    public toFacultyScheduleSection() {
        // Initialize the GUI components
        initializeComponents();
    }

    private void initializeComponents() {
        // Set frame title
        setTitle("Faculty Schedule - To-Do List");

        // Create table model and JTable
        String[] columnNames = {"Task ID", "Task Description"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tasksTable = new JTable(tableModel);
        
        // Set table properties
        tasksTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tasksTable);
        
        // Task input field
        taskField = new JTextField(20);

        // Buttons
        addButton = createLargeButton("Add Task");
        editButton = createLargeButton("Edit Task");
        deleteButton = createLargeButton("Delete Task");
        backButton = createLargeButton("Back");

        // Action Listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editTask();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });

        // Layout setup
        JPanel inputPanel = new JPanel();
        inputPanel.add(taskField);
        inputPanel.add(addButton);
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(backButton, BorderLayout.NORTH);

        // Frame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen mode
        setVisible(true);
    }

    // Method to create a large button with max width
    private JButton createLargeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 22)); // Bigger font for buttons
        button.setMaximumSize(new Dimension(600, 80)); // Set maximum width and height
        button.setPreferredSize(new Dimension(600, 80)); // Set preferred size
        return button;
    }

    // Method to add a task
    private void addTask() {
        String taskDescription = taskField.getText();
        if (!taskDescription.trim().isEmpty()) {
            int taskId = tableModel.getRowCount() + 1; // Simple ID generation
            tableModel.addRow(new Object[]{taskId, taskDescription});
            taskField.setText(""); // Clear input field
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a task description.");
        }
    }

    // Method to edit a selected task
    private void editTask() {
        int selectedRow = tasksTable.getSelectedRow();
        if (selectedRow != -1) {
            String taskDescription = taskField.getText();
            if (!taskDescription.trim().isEmpty()) {
                tableModel.setValueAt(taskDescription, selectedRow, 1);
                taskField.setText(""); // Clear input field
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a task description.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to edit.");
        }
    }

    // Method to delete a selected task
    private void deleteTask() {
        int selectedRow = tasksTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to delete.");
        }
    }

    public static void main(String[] args) {
        // Create and show the Faculty Schedule section window
        new toFacultyScheduleSection();
    }
}
