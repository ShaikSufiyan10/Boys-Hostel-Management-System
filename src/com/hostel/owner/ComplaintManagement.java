package com.hostel.owner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import com.hostel.database.DBConnection;

public class ComplaintManagement extends JFrame {

    JLabel residentLabel;
    JLabel categoryLabel;
    JLabel descriptionLabel;
    JLabel dateLabel;
    JLabel statusLabel;

    JTextField residentField;
    JComboBox<String> categoryCombo;
    JTextArea descriptionArea;
    JTextField dateField;
    JComboBox<String> statusCombo;

    JButton updateButton;
    JButton deleteButton;
    JButton searchButton;
    JButton clearButton;
    JButton viewButton;

    JTextField searchField;

    JTable table;
    DefaultTableModel model;

    public ComplaintManagement() {

        // ---------------- WINDOW ----------------

        setTitle("Complaint Management");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---------------- COLORS ----------------

        Color navy = new Color(25, 55, 90);
        Color blue = new Color(40, 100, 160);
        Color lightBackground = new Color(245, 247, 250);

        // ---------------- MAIN PANEL ----------------

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(lightBackground);

        // ---------------- HEADER ----------------

        JPanel headerPanel = new JPanel(new BorderLayout());

        headerPanel.setBackground(navy);

        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 25, 18, 25));

        JLabel titleLabel =
                new JLabel("COMPLAINT MANAGEMENT");

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24));

        JLabel subtitleLabel =
                new JLabel(
                        "Manage Resident Complaints and Their Status");

        subtitleLabel.setForeground(Color.WHITE);

        subtitleLabel.setFont(
                new Font("Arial", Font.PLAIN, 13));

        JPanel titlePanel =
                new JPanel(new BorderLayout());

        titlePanel.setOpaque(false);

        titlePanel.add(
                titleLabel,
                BorderLayout.NORTH);

        titlePanel.add(
                subtitleLabel,
                BorderLayout.SOUTH);

        headerPanel.add(
                titlePanel,
                BorderLayout.WEST);

        // ---------------- FORM PANEL ----------------

        JPanel formPanel =
                new JPanel(new GridBagLayout());

        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 225, 230)),
                        BorderFactory.createEmptyBorder(
                                15, 20, 15, 20)));

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(7, 10, 7, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // ---------------- LABELS ----------------

        residentLabel =
                new JLabel("Resident:");

        categoryLabel =
                new JLabel("Category:");

        descriptionLabel =
                new JLabel("Description:");

        dateLabel =
                new JLabel("Complaint Date:");

        statusLabel =
                new JLabel("Status:");

        residentLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        categoryLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        descriptionLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        dateLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        statusLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        // ---------------- FIELDS ----------------

        residentField =
                new JTextField();

        residentField.setEditable(false);

        categoryCombo =
                new JComboBox<>(
                        new String[] {
                                "Food",
                                "Room",
                                "Cleanliness",
                                "Maintenance",
                                "Water",
                                "Electricity",
                                "Other"
                        });

        descriptionArea =
                new JTextArea(3, 20);

        descriptionArea.setLineWrap(true);

        descriptionArea.setWrapStyleWord(true);

        JScrollPane descriptionScroll =
                new JScrollPane(descriptionArea);

        dateField =
                new JTextField();

        statusCombo =
                new JComboBox<>(
                        new String[] {
                                "PENDING",
                                "IN PROGRESS",
                                "RESOLVED",
                                "REJECTED"
                        });

        // ---------------- FIRST ROW ----------------

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        formPanel.add(
                residentLabel,
                gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        formPanel.add(
                residentField,
                gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;

        formPanel.add(
                categoryLabel,
                gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;

        formPanel.add(
                categoryCombo,
                gbc);

        // ---------------- SECOND ROW ----------------

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        formPanel.add(
                descriptionLabel,
                gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1;

        formPanel.add(
                descriptionScroll,
                gbc);

        // Reset grid width

        gbc.gridwidth = 1;

        // ---------------- THIRD ROW ----------------

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        formPanel.add(
                dateLabel,
                gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        formPanel.add(
                dateField,
                gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;

        formPanel.add(
                statusLabel,
                gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;

        formPanel.add(
                statusCombo,
                gbc);

        // ---------------- BUTTONS ----------------

        updateButton =
                new JButton("Update Complaint");

        deleteButton =
                new JButton("Delete Complaint");

        clearButton =
                new JButton("Clear");

        updateButton.setBackground(blue);
        updateButton.setForeground(Color.WHITE);

        deleteButton.setBackground(
                new Color(180, 60, 60));

        deleteButton.setForeground(Color.WHITE);

        clearButton.setBackground(
                new Color(100, 110, 120));

        clearButton.setForeground(Color.WHITE);

        updateButton.setFont(
                new Font("Arial", Font.BOLD, 13));

        deleteButton.setFont(
                new Font("Arial", Font.BOLD, 13));

        clearButton.setFont(
                new Font("Arial", Font.BOLD, 13));

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;

        formPanel.add(
                buttonPanel,
                gbc);

        gbc.gridwidth = 1;

        // ---------------- SEARCH PANEL ----------------

        JPanel searchPanel =
                new JPanel(new BorderLayout(10, 10));

        searchPanel.setBackground(Color.WHITE);

        searchPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 0, 10, 0));

        JLabel searchLabel =
                new JLabel("Search Resident:");

        searchLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        searchField =
                new JTextField();

        searchButton =
                new JButton("Search");

        viewButton =
                new JButton("View All");

        searchButton.setBackground(blue);
        searchButton.setForeground(Color.WHITE);

        viewButton.setBackground(blue);
        viewButton.setForeground(Color.WHITE);

        JPanel searchInputPanel =
                new JPanel(new BorderLayout(8, 0));

        searchInputPanel.setBackground(Color.WHITE);

        searchInputPanel.add(
                searchLabel,
                BorderLayout.WEST);

        searchInputPanel.add(
                searchField,
                BorderLayout.CENTER);

        JPanel searchButtonPanel =
                new JPanel();

        searchButtonPanel.setBackground(Color.WHITE);

        searchButtonPanel.add(searchButton);
        searchButtonPanel.add(viewButton);

        searchPanel.add(
                searchInputPanel,
                BorderLayout.CENTER);

        searchPanel.add(
                searchButtonPanel,
                BorderLayout.EAST);

        // ---------------- TABLE ----------------

        model =
                new DefaultTableModel();

        model.setColumnIdentifiers(
                new String[] {
                        "ID",
                        "Resident",
                        "Category",
                        "Description",
                        "Date",
                        "Status"
                });

        table =
                new JTable(model);

        table.setRowHeight(28);

        table.setFont(
                new Font("Arial", Font.PLAIN, 13));

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13));

        table.setAutoResizeMode(
                JTable.AUTO_RESIZE_LAST_COLUMN);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 215, 220)));

        // ---------------- LOWER PANEL ----------------

        JPanel lowerPanel =
                new JPanel(new BorderLayout());

        lowerPanel.setBackground(
                lightBackground);

        lowerPanel.add(
                searchPanel,
                BorderLayout.NORTH);

        lowerPanel.add(
                scrollPane,
                BorderLayout.CENTER);

        // ---------------- CONTENT PANEL ----------------

        JPanel contentPanel =
                new JPanel(new BorderLayout(15, 10));

        contentPanel.setBackground(
                lightBackground);

        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 20, 20));

        contentPanel.add(
                formPanel,
                BorderLayout.NORTH);

        contentPanel.add(
                lowerPanel,
                BorderLayout.CENTER);

        // ---------------- MAIN ----------------

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH);

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER);

        add(mainPanel);

        // ---------------- LOAD COMPLAINTS ----------------

        loadComplaints();

        // ---------------- UPDATE ----------------

        updateButton.addActionListener(e -> {

            int selectedRow =
                    table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a complaint");

                return;
            }

            try {

                int complaintId =
                        Integer.parseInt(
                                model.getValueAt(
                                        selectedRow,
                                        0
                                ).toString());

                String category =
                        categoryCombo
                                .getSelectedItem()
                                .toString();

                String description =
                        descriptionArea
                                .getText()
                                .trim();

                String date =
                        dateField
                                .getText()
                                .trim();

                String status =
                        statusCombo
                                .getSelectedItem()
                                .toString();

                if (description.isEmpty()
                        || date.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please fill all fields");

                    return;
                }

                String sql =
                        "UPDATE complaints SET " +
                        "category=?, " +
                        "description=?, " +
                        "complaint_date=?, " +
                        "status=? " +
                        "WHERE complaint_id=?";

                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                statement.setString(
                        1,
                        category);

                statement.setString(
                        2,
                        description);

                statement.setDate(
                        3,
                        java.sql.Date.valueOf(date));

                statement.setString(
                        4,
                        status);

                statement.setInt(
                        5,
                        complaintId);

                statement.executeUpdate();

                statement.close();
                connection.close();

                JOptionPane.showMessageDialog(
                        this,
                        "Complaint Updated Successfully");

                loadComplaints();

                clearFields();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Update Error: "
                                + ex.getMessage());

                ex.printStackTrace();
            }
        });

        // ---------------- DELETE ----------------

        deleteButton.addActionListener(e -> {

            int selectedRow =
                    table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a complaint");

                return;
            }

            try {

                int complaintId =
                        Integer.parseInt(
                                model.getValueAt(
                                        selectedRow,
                                        0
                                ).toString());

                String sql =
                        "DELETE FROM complaints " +
                        "WHERE complaint_id=?";

                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                statement.setInt(
                        1,
                        complaintId);

                statement.executeUpdate();

                statement.close();
                connection.close();

                JOptionPane.showMessageDialog(
                        this,
                        "Complaint Deleted Successfully");

                loadComplaints();

                clearFields();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Delete Error: "
                                + ex.getMessage());

                ex.printStackTrace();
            }
        });

        // ---------------- SEARCH ----------------

        searchButton.addActionListener(e -> {

            String search =
                    searchField
                            .getText()
                            .trim();

            if (search.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter resident name");

                return;
            }

            searchComplaints(search);
        });

        // ---------------- VIEW ALL ----------------

        viewButton.addActionListener(e -> {

            searchField.setText("");

            loadComplaints();
        });

        // ---------------- CLEAR ----------------

        clearButton.addActionListener(e -> {

            clearFields();

            searchField.setText("");

            loadComplaints();
        });

        // ---------------- TABLE SELECTION ----------------

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    int selectedRow =
                            table.getSelectedRow();

                    if (selectedRow != -1) {

                        residentField.setText(
                                model.getValueAt(
                                        selectedRow,
                                        1
                                ).toString());

                        categoryCombo.setSelectedItem(
                                model.getValueAt(
                                        selectedRow,
                                        2
                                ).toString());

                        descriptionArea.setText(
                                model.getValueAt(
                                        selectedRow,
                                        3
                                ).toString());

                        dateField.setText(
                                model.getValueAt(
                                        selectedRow,
                                        4
                                ).toString());

                        statusCombo.setSelectedItem(
                                model.getValueAt(
                                        selectedRow,
                                        5
                                ).toString());
                    }
                });
    }

    // =========================================================
    // LOAD ALL COMPLAINTS
    // =========================================================

    private void loadComplaints() {

        model.setRowCount(0);

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "SELECT c.complaint_id, " +
                    "r.name, " +
                    "c.category, " +
                    "c.description, " +
                    "c.complaint_date, " +
                    "c.status " +

                    "FROM complaints c " +

                    "JOIN residents r " +
                    "ON c.resident_id = r.resident_id " +

                    "ORDER BY c.complaint_id DESC";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                model.addRow(
                        new Object[] {

                                resultSet.getInt(
                                        "complaint_id"),

                                resultSet.getString(
                                        "name"),

                                resultSet.getString(
                                        "category"),

                                resultSet.getString(
                                        "description"),

                                resultSet.getDate(
                                        "complaint_date"),

                                resultSet.getString(
                                        "status")
                        });
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load complaints");

            ex.printStackTrace();
        }
    }

    // =========================================================
    // SEARCH COMPLAINTS
    // =========================================================

    private void searchComplaints(
            String search) {

        model.setRowCount(0);

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "SELECT c.complaint_id, " +
                    "r.name, " +
                    "c.category, " +
                    "c.description, " +
                    "c.complaint_date, " +
                    "c.status " +

                    "FROM complaints c " +

                    "JOIN residents r " +
                    "ON c.resident_id = r.resident_id " +

                    "WHERE r.name LIKE ? " +

                    "ORDER BY c.complaint_id DESC";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(
                    1,
                    "%" + search + "%");

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                model.addRow(
                        new Object[] {

                                resultSet.getInt(
                                        "complaint_id"),

                                resultSet.getString(
                                        "name"),

                                resultSet.getString(
                                        "category"),

                                resultSet.getString(
                                        "description"),

                                resultSet.getDate(
                                        "complaint_date"),

                                resultSet.getString(
                                        "status")
                        });
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search Error: "
                            + ex.getMessage());

            ex.printStackTrace();
        }
    }

    // =========================================================
    // CLEAR FIELDS
    // =========================================================

    private void clearFields() {

        residentField.setText("");

        categoryCombo.setSelectedIndex(0);

        descriptionArea.setText("");

        dateField.setText("");

        statusCombo.setSelectedIndex(0);

        table.clearSelection();
    }

    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args) {

        ComplaintManagement complaintManagement =
                new ComplaintManagement();

        complaintManagement.setVisible(true);
    }
}