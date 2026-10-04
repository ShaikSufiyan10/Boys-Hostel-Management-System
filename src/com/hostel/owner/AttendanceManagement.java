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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.hostel.database.DBConnection;

public class AttendanceManagement extends JFrame {

    JLabel typeLabel;
    JLabel personLabel;
    JLabel dateLabel;
    JLabel statusLabel;

    JComboBox<String> typeCombo;
    JComboBox<String> personCombo;
    JTextField dateField;
    JComboBox<String> statusCombo;

    JButton markButton;
    JButton deleteButton;
    JButton searchButton;
    JButton clearButton;
    JButton viewButton;

    JTextField searchField;

    JTable table;
    DefaultTableModel model;

    public AttendanceManagement() {

        // ---------------- WINDOW ----------------

        setTitle("Attendance Management");
        setSize(1000, 650);
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
                BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel titleLabel = new JLabel("ATTENDANCE MANAGEMENT");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24));

        JLabel subtitleLabel = new JLabel(
                "Manage Resident and Worker Attendance");
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setFont(
                new Font("Arial", Font.PLAIN, 13));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // ---------------- FORM PANEL ----------------

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 225, 230)),
                        BorderFactory.createEmptyBorder(
                                20, 25, 20, 25)));

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 10, 8, 10);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels

        typeLabel = new JLabel("Attendance For:");
        personLabel = new JLabel("Person:");
        dateLabel = new JLabel("Date:");
        statusLabel = new JLabel("Status:");

        typeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        personLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Components

        typeCombo = new JComboBox<>(
                new String[] {
                        "Resident",
                        "Worker"
                });

        personCombo = new JComboBox<>();

        dateField = new JTextField();

        statusCombo = new JComboBox<>(
                new String[] {
                        "Present",
                        "Absent"
                });

        // First row

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        formPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        formPanel.add(typeCombo, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;

        formPanel.add(personLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;

        formPanel.add(personCombo, gbc);

        // Second row

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        formPanel.add(dateField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;

        formPanel.add(statusLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;

        formPanel.add(statusCombo, gbc);

        // ---------------- BUTTONS ----------------

        markButton = new JButton("Mark Attendance");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        markButton.setBackground(blue);
        markButton.setForeground(Color.WHITE);

        deleteButton.setBackground(
                new Color(180, 60, 60));
        deleteButton.setForeground(Color.WHITE);

        clearButton.setBackground(
                new Color(100, 110, 120));
        clearButton.setForeground(Color.WHITE);

        markButton.setFont(
                new Font("Arial", Font.BOLD, 13));

        deleteButton.setFont(
                new Font("Arial", Font.BOLD, 13));

        clearButton.setFont(
                new Font("Arial", Font.BOLD, 13));

        JPanel buttonPanel = new JPanel();

        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(markButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;

        formPanel.add(buttonPanel, gbc);

        // ---------------- SEARCH PANEL ----------------

        JPanel searchPanel = new JPanel(
                new BorderLayout(10, 10));

        searchPanel.setBackground(Color.WHITE);

        searchPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 0, 10, 0));

        JLabel searchLabel =
                new JLabel("Search Person:");

        searchLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        searchField = new JTextField();

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

        model = new DefaultTableModel();

        model.setColumnIdentifiers(
                new String[] {
                        "Attendance ID",
                        "Type",
                        "Person",
                        "Date",
                        "Status"
                });

        table = new JTable(model);

        table.setRowHeight(28);
        table.setFont(
                new Font("Arial", Font.PLAIN, 13));

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 215, 220)));

        // ---------------- LOWER PANEL ----------------

        JPanel lowerPanel =
                new JPanel(new BorderLayout());

        lowerPanel.setBackground(lightBackground);

        lowerPanel.add(
                searchPanel,
                BorderLayout.NORTH);

        lowerPanel.add(
                scrollPane,
                BorderLayout.CENTER);

        // ---------------- CONTENT PANEL ----------------

        JPanel contentPanel =
                new JPanel(new BorderLayout(15, 10));

        contentPanel.setBackground(lightBackground);

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

        // ---------------- LOAD PEOPLE ----------------

        loadPeople();

        // ---------------- TYPE CHANGE ----------------

        typeCombo.addActionListener(e -> {
            loadPeople();
        });

        // ---------------- LOAD ATTENDANCE ----------------

        loadAttendance();

        // ---------------- MARK ATTENDANCE ----------------

        markButton.addActionListener(e -> {

            if (personCombo.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a person");

                return;
            }

            String type =
                    typeCombo.getSelectedItem().toString();

            String person =
                    personCombo.getSelectedItem().toString();

            String date =
                    dateField.getText().trim();

            String status =
                    statusCombo.getSelectedItem().toString();

            if (date.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter date");

                return;
            }

            try {

                Connection connection =
                        DBConnection.getConnection();

                int residentId = 0;
                int workerId = 0;

                // Get selected person's ID

                if (type.equals("Resident")) {

                    residentId =
                            getResidentId(person);

                } else {

                    workerId =
                            getWorkerId(person);
                }

                // Insert attendance

                String sql =
                        "INSERT INTO attendance " +
                        "(resident_id, worker_id, " +
                        "attendance_date, status) " +
                        "VALUES (?, ?, ?, ?)";

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                if (residentId > 0) {

                    statement.setInt(
                            1,
                            residentId);

                } else {

                    statement.setNull(
                            1,
                            java.sql.Types.INTEGER);
                }

                if (workerId > 0) {

                    statement.setInt(
                            2,
                            workerId);

                } else {

                    statement.setNull(
                            2,
                            java.sql.Types.INTEGER);
                }

                statement.setDate(
                        3,
                        java.sql.Date.valueOf(date));

                statement.setString(
                        4,
                        status);

                statement.executeUpdate();

                statement.close();
                connection.close();

                JOptionPane.showMessageDialog(
                        this,
                        "Attendance Marked Successfully");

                loadAttendance();

                clearFields();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Attendance Error: "
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
                        "Please select attendance");

                return;
            }

            try {

                int attendanceId =
                        Integer.parseInt(
                                model.getValueAt(
                                        selectedRow,
                                        0
                                ).toString());

                String sql =
                        "DELETE FROM attendance " +
                        "WHERE attendance_id=?";

                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                statement.setInt(
                        1,
                        attendanceId);

                statement.executeUpdate();

                statement.close();
                connection.close();

                JOptionPane.showMessageDialog(
                        this,
                        "Attendance Deleted Successfully");

                loadAttendance();

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
                    searchField.getText().trim();

            if (search.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter person name");

                return;
            }

            searchAttendance(search);
        });

        // ---------------- VIEW ALL ----------------

        viewButton.addActionListener(e -> {

            searchField.setText("");

            loadAttendance();
        });

        // ---------------- CLEAR ----------------

        clearButton.addActionListener(e -> {

            clearFields();

            searchField.setText("");

            loadAttendance();
        });
    }

    // =========================================================
    // LOAD RESIDENTS OR WORKERS
    // =========================================================

    private void loadPeople() {

        personCombo.removeAllItems();

        try {

            Connection connection =
                    DBConnection.getConnection();

            String type =
                    typeCombo.getSelectedItem()
                            .toString();

            String sql;

            if (type.equals("Resident")) {

                sql =
                        "SELECT name FROM residents " +
                        "ORDER BY name";

            } else {

                sql =
                        "SELECT name FROM workers " +
                        "ORDER BY name";
            }

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                personCombo.addItem(
                        resultSet.getString("name"));
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load people");

            ex.printStackTrace();
        }
    }

    // =========================================================
    // GET RESIDENT ID
    // =========================================================

    private int getResidentId(
            String name) throws Exception {

        String sql =
                "SELECT resident_id " +
                "FROM residents " +
                "WHERE name=?";

        Connection connection =
                DBConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql);

        statement.setString(
                1,
                name);

        ResultSet resultSet =
                statement.executeQuery();

        int id = 0;

        if (resultSet.next()) {

            id =
                    resultSet.getInt(
                            "resident_id");
        }

        resultSet.close();
        statement.close();
        connection.close();

        return id;
    }

    // =========================================================
    // GET WORKER ID
    // =========================================================

    private int getWorkerId(
            String name) throws Exception {

        String sql =
                "SELECT worker_id " +
                "FROM workers " +
                "WHERE name=?";

        Connection connection =
                DBConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql);

        statement.setString(
                1,
                name);

        ResultSet resultSet =
                statement.executeQuery();

        int id = 0;

        if (resultSet.next()) {

            id =
                    resultSet.getInt(
                            "worker_id");
        }

        resultSet.close();
        statement.close();
        connection.close();

        return id;
    }

    // =========================================================
    // LOAD ALL ATTENDANCE
    // =========================================================

    private void loadAttendance() {

        model.setRowCount(0);

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "SELECT a.attendance_id, " +

                    "CASE " +
                    "WHEN a.resident_id IS NOT NULL " +
                    "THEN 'Resident' " +
                    "ELSE 'Worker' END AS type, " +

                    "CASE " +
                    "WHEN a.resident_id IS NOT NULL " +
                    "THEN r.name " +
                    "ELSE w.name END AS person, " +

                    "a.attendance_date, " +
                    "a.status " +

                    "FROM attendance a " +

                    "LEFT JOIN residents r " +
                    "ON a.resident_id = r.resident_id " +

                    "LEFT JOIN workers w " +
                    "ON a.worker_id = w.worker_id " +

                    "ORDER BY a.attendance_id";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                model.addRow(
                        new Object[] {

                                resultSet.getInt(
                                        "attendance_id"),

                                resultSet.getString(
                                        "type"),

                                resultSet.getString(
                                        "person"),

                                resultSet.getDate(
                                        "attendance_date"),

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
                    "Unable to load attendance");

            ex.printStackTrace();
        }
    }

    // =========================================================
    // SEARCH ATTENDANCE
    // =========================================================

    private void searchAttendance(
            String search) {

        model.setRowCount(0);

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "SELECT a.attendance_id, " +

                    "CASE " +
                    "WHEN a.resident_id IS NOT NULL " +
                    "THEN 'Resident' " +
                    "ELSE 'Worker' END AS type, " +

                    "CASE " +
                    "WHEN a.resident_id IS NOT NULL " +
                    "THEN r.name " +
                    "ELSE w.name END AS person, " +

                    "a.attendance_date, " +
                    "a.status " +

                    "FROM attendance a " +

                    "LEFT JOIN residents r " +
                    "ON a.resident_id = r.resident_id " +

                    "LEFT JOIN workers w " +
                    "ON a.worker_id = w.worker_id " +

                    "WHERE r.name LIKE ? " +
                    "OR w.name LIKE ?";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(
                    1,
                    "%" + search + "%");

            statement.setString(
                    2,
                    "%" + search + "%");

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                model.addRow(
                        new Object[] {

                                resultSet.getInt(
                                        "attendance_id"),

                                resultSet.getString(
                                        "type"),

                                resultSet.getString(
                                        "person"),

                                resultSet.getDate(
                                        "attendance_date"),

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

        if (personCombo.getItemCount() > 0) {

            personCombo.setSelectedIndex(0);
        }

        dateField.setText("");

        statusCombo.setSelectedIndex(0);

        table.clearSelection();
    }

    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args) {

        AttendanceManagement attendanceManagement =
                new AttendanceManagement();

        attendanceManagement.setVisible(true);
    }
}