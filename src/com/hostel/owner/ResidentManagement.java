package com.hostel.owner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.Date;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.hostel.database.DBConnection;

public class ResidentManagement extends JFrame {

    JLabel nameLabel;
    JLabel mobileLabel;
    JLabel emailLabel;
    JLabel usernameLabel;
    JLabel passwordLabel;
    JLabel genderLabel;
    JLabel ageLabel;
    JLabel addressLabel;
    JLabel joiningDateLabel;

    JTextField nameField;
    JTextField mobileField;
    JTextField emailField;
    JTextField usernameField;
    JTextField passwordField;
    JTextField ageField;
    JTextField addressField;
    JTextField joiningDateField;

    JTextField searchField;

    JComboBox<String> genderBox;

    JButton addButton;
    JButton updateButton;
    JButton deleteButton;
    JButton searchButton;
    JButton clearButton;
    JButton viewButton;

    JTable residentTable;
    DefaultTableModel tableModel;

    public ResidentManagement() {

        setTitle("Resident Management");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        Color navy = new Color(25, 55, 90);
        Color background = new Color(245, 247, 250);
        Color blue = new Color(40, 100, 160);

        // =========================
        // MAIN PANEL
        // =========================

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(background);

        // =========================
        // HEADER
        // =========================

        JPanel headerPanel =
                new JPanel(new BorderLayout());

        headerPanel.setBackground(navy);

        headerPanel.setPreferredSize(
                new Dimension(1200, 75)
        );

        JLabel titleLabel =
                new JLabel(
                        "RESIDENT MANAGEMENT",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 26)
        );

        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(
                titleLabel,
                BorderLayout.CENTER
        );

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel =
                new JPanel(new GridBagLayout());

        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 215, 220)
                        ),
                        "Resident Details"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(7, 12, 7, 12);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Column widths
        gbc.weightx = 0;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                createLabel("Name"),
                gbc
        );

        nameField = new JTextField();
        setFieldSize(nameField);

        gbc.gridx = 1;
        gbc.weightx = 1;

        formPanel.add(nameField, gbc);

        // Mobile
        gbc.gridx = 2;
        gbc.weightx = 0;

        formPanel.add(
                createLabel("Mobile"),
                gbc
        );

        mobileField = new JTextField();
        setFieldSize(mobileField);

        gbc.gridx = 3;
        gbc.weightx = 1;

        formPanel.add(mobileField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        formPanel.add(
                createLabel("Email"),
                gbc
        );

        emailField = new JTextField();
        setFieldSize(emailField);

        gbc.gridx = 1;
        gbc.weightx = 1;

        formPanel.add(emailField, gbc);

        // Username
        gbc.gridx = 2;
        gbc.weightx = 0;

        formPanel.add(
                createLabel("Username"),
                gbc
        );

        usernameField = new JTextField();
        setFieldSize(usernameField);

        gbc.gridx = 3;
        gbc.weightx = 1;

        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        formPanel.add(
                createLabel("Password"),
                gbc
        );

        passwordField = new JTextField();
        setFieldSize(passwordField);

        gbc.gridx = 1;
        gbc.weightx = 1;

        formPanel.add(passwordField, gbc);

        // Gender
        gbc.gridx = 2;
        gbc.weightx = 0;

        formPanel.add(
                createLabel("Gender"),
                gbc
        );

        genderBox =
                new JComboBox<>(
                        new String[]{
                                "Male",
                                "Female",
                                "Other"
                        }
                );

        genderBox.setPreferredSize(
                new Dimension(250, 32)
        );

        gbc.gridx = 3;
        gbc.weightx = 1;

        formPanel.add(genderBox, gbc);

        // Age
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;

        formPanel.add(
                createLabel("Age"),
                gbc
        );

        ageField = new JTextField();
        setFieldSize(ageField);

        gbc.gridx = 1;
        gbc.weightx = 1;

        formPanel.add(ageField, gbc);

        // Address
        gbc.gridx = 2;
        gbc.weightx = 0;

        formPanel.add(
                createLabel("Address"),
                gbc
        );

        addressField = new JTextField();
        setFieldSize(addressField);

        gbc.gridx = 3;
        gbc.weightx = 1;

        formPanel.add(addressField, gbc);

        // Joining Date
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;

        formPanel.add(
                createLabel("Joining Date"),
                gbc
        );

        joiningDateField = new JTextField();
        joiningDateField.setPreferredSize(
                new Dimension(250, 32)
        );

        joiningDateField.setToolTipText(
                "YYYY-MM-DD"
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        formPanel.add(
                joiningDateField,
                gbc
        );

        // =========================
        // BUTTONS
        // =========================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                12,
                                5
                        )
                );

        buttonPanel.setBackground(Color.WHITE);

        addButton = new JButton("ADD");
        updateButton = new JButton("UPDATE");
        deleteButton = new JButton("DELETE");
        clearButton = new JButton("CLEAR");

        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);
        styleButton(clearButton);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        formPanel.add(buttonPanel, gbc);

        // =========================
        // SEARCH PANEL
        // =========================

        JPanel searchPanel =
                new JPanel(new BorderLayout(10, 0));

        searchPanel.setBackground(background);

        searchPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 15, 10, 15
                )
        );

        JLabel searchLabel =
                new JLabel("Search Resident:");

        searchLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        searchField = new JTextField();

        searchButton =
                new JButton("SEARCH");

        viewButton =
                new JButton("VIEW ALL");

        styleSmallButton(searchButton);
        styleSmallButton(viewButton);

        searchPanel.add(
                searchLabel,
                BorderLayout.WEST
        );

        searchPanel.add(
                searchField,
                BorderLayout.CENTER
        );

        JPanel searchButtons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                5,
                                0
                        )
                );

        searchButtons.setBackground(background);

        searchButtons.add(searchButton);
        searchButtons.add(viewButton);

        searchPanel.add(
                searchButtons,
                BorderLayout.EAST
        );

        // =========================
        // TABLE
        // =========================

        String[] columns = {
                "ID",
                "Name",
                "Mobile",
                "Email",
                "Username",
                "Gender",
                "Age",
                "Address",
                "Joining Date"
        };

        tableModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        residentTable =
                new JTable(tableModel);

        residentTable.setRowHeight(28);

        residentTable.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        residentTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        residentTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        residentTable.setAutoCreateRowSorter(true);

        // Important: allows wide table
        residentTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        // Column widths
        int[] widths = {
                60, 150, 120, 200, 130,
                90, 60, 200, 120
        };

        for (int i = 0; i < widths.length; i++) {
            residentTable.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(widths[i]);
        }

        JScrollPane tableScrollPane =
                new JScrollPane(residentTable);

        tableScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Resident List"
                )
        );

        // =========================
        // LOWER PANEL
        // =========================

        JPanel lowerPanel =
                new JPanel(new BorderLayout());

        lowerPanel.setBackground(background);

        lowerPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        lowerPanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        // =========================
        // CONTENT
        // =========================

        JPanel contentPanel =
                new JPanel(new BorderLayout());

        contentPanel.setBackground(background);

        contentPanel.add(
                formPanel,
                BorderLayout.NORTH
        );

        contentPanel.add(
                lowerPanel,
                BorderLayout.CENTER
        );

        // =========================
        // MAIN LAYOUT
        // =========================

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // =========================
        // BUTTON ACTIONS
        // =========================

        addButton.addActionListener(e ->
                addResident()
        );

        updateButton.addActionListener(e ->
                updateResident()
        );

        deleteButton.addActionListener(e ->
                deleteResident()
        );

        searchButton.addActionListener(e -> {

            String text =
                    searchField.getText().trim();

            if (text.isEmpty()) {
                loadResidents();
            } else {
                searchResident(text);
            }
        });

        viewButton.addActionListener(e -> {

            searchField.setText("");
            loadResidents();
        });

        clearButton.addActionListener(e ->
                clearFields()
        );

        residentTable
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int row =
                                residentTable
                                        .getSelectedRow();

                        if (row != -1) {

                            int modelRow =
                                    residentTable
                                            .convertRowIndexToModel(
                                                    row
                                            );

                            loadSelectedResident(
                                    modelRow
                            );
                        }
                    }
                });

        loadResidents();
    }

    // =========================
    // ADD RESIDENT
    // =========================

    private void addResident() {

        String name =
                nameField.getText().trim();

        String mobile =
                mobileField.getText().trim();

        String email =
                emailField.getText().trim();

        String username =
                usernameField.getText().trim();

        String password =
                passwordField.getText().trim();

        String gender =
                genderBox.getSelectedItem().toString();

        String age =
                ageField.getText().trim();

        String address =
                addressField.getText().trim();

        String joiningDate =
                joiningDateField.getText().trim();

        if (name.isEmpty()
                || mobile.isEmpty()
                || username.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill Name, Mobile, Username and Password."
            );

            return;
        }

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "INSERT INTO residents " +
                    "(name, mobile, email, username, password, " +
                    "gender, age, address, joining_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, name);
            statement.setString(2, mobile);
            statement.setString(3, email);
            statement.setString(4, username);
            statement.setString(5, password);
            statement.setString(6, gender);
            statement.setInt(7, Integer.parseInt(age));
            statement.setString(8, address);
            statement.setDate(
                    9,
                    Date.valueOf(joiningDate)
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Resident added successfully."
            );

            statement.close();
            connection.close();

            clearFields();
            loadResidents();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Age must be a number."
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Joining Date must be YYYY-MM-DD."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    // =========================
    // UPDATE RESIDENT
    // =========================

    private void updateResident() {

        int row =
                residentTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a resident."
            );

            return;
        }

        int modelRow =
                residentTable.convertRowIndexToModel(row);

        int residentId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        String sql =
                "UPDATE residents SET " +
                "name=?, mobile=?, email=?, username=?, " +
                "password=?, gender=?, age=?, address=?, " +
                "joining_date=? WHERE resident_id=?";

        try {

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(
                    1,
                    nameField.getText().trim()
            );

            statement.setString(
                    2,
                    mobileField.getText().trim()
            );

            statement.setString(
                    3,
                    emailField.getText().trim()
            );

            statement.setString(
                    4,
                    usernameField.getText().trim()
            );

            statement.setString(
                    5,
                    passwordField.getText().trim()
            );

            statement.setString(
                    6,
                    genderBox.getSelectedItem().toString()
            );

            statement.setInt(
                    7,
                    Integer.parseInt(
                            ageField.getText().trim()
                    )
            );

            statement.setString(
                    8,
                    addressField.getText().trim()
            );

            statement.setDate(
                    9,
                    Date.valueOf(
                            joiningDateField
                                    .getText()
                                    .trim()
                    )
            );

            statement.setInt(
                    10,
                    residentId
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Resident updated successfully."
            );

            statement.close();
            connection.close();

            clearFields();
            loadResidents();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    // =========================
    // DELETE RESIDENT
    // =========================

    private void deleteResident() {

        int row =
                residentTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a resident."
            );

            return;
        }

        int modelRow =
                residentTable.convertRowIndexToModel(row);

        int residentId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                modelRow,
                                0
                        ).toString()
                );

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this resident?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "DELETE FROM residents " +
                    "WHERE resident_id=?";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(
                    1,
                    residentId
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Resident deleted successfully."
            );

            statement.close();
            connection.close();

            clearFields();
            loadResidents();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage()
            );

            ex.printStackTrace();
        }
    }

    // =========================
    // LOAD RESIDENTS
    // =========================

    public void loadResidents() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT * FROM residents " +
                "ORDER BY resident_id DESC";

        try {

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                tableModel.addRow(
                        new Object[] {

                                resultSet.getInt(
                                        "resident_id"
                                ),

                                resultSet.getString(
                                        "name"
                                ),

                                resultSet.getString(
                                        "mobile"
                                ),

                                resultSet.getString(
                                        "email"
                                ),

                                resultSet.getString(
                                        "username"
                                ),

                                resultSet.getString(
                                        "gender"
                                ),

                                resultSet.getInt(
                                        "age"
                                ),

                                resultSet.getString(
                                        "address"
                                ),

                                resultSet.getDate(
                                        "joining_date"
                                )
                        }
                );
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading residents: "
                            + ex.getMessage()
            );
        }
    }

    // =========================
    // SEARCH
    // =========================

    public void searchResident(
            String searchText
    ) {

        tableModel.setRowCount(0);

        String sql =
                "SELECT * FROM residents " +
                "WHERE name LIKE ? " +
                "OR mobile LIKE ? " +
                "OR username LIKE ? " +
                "ORDER BY resident_id DESC";

        try {

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            String value =
                    "%" + searchText + "%";

            statement.setString(1, value);
            statement.setString(2, value);
            statement.setString(3, value);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                tableModel.addRow(
                        new Object[] {

                                resultSet.getInt("resident_id"),
                                resultSet.getString("name"),
                                resultSet.getString("mobile"),
                                resultSet.getString("email"),
                                resultSet.getString("username"),
                                resultSet.getString("gender"),
                                resultSet.getInt("age"),
                                resultSet.getString("address"),
                                resultSet.getDate("joining_date")
                        }
                );
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search error: "
                            + ex.getMessage()
            );
        }
    }

    // =========================
    // LOAD SELECTED RESIDENT
    // =========================

    private void loadSelectedResident(
            int row
    ) {

        nameField.setText(
                getTableValue(row, 1)
        );

        mobileField.setText(
                getTableValue(row, 2)
        );

        emailField.setText(
                getTableValue(row, 3)
        );

        usernameField.setText(
                getTableValue(row, 4)
        );

        genderBox.setSelectedItem(
                getTableValue(row, 5)
        );

        ageField.setText(
                getTableValue(row, 6)
        );

        addressField.setText(
                getTableValue(row, 7)
        );

        joiningDateField.setText(
                getTableValue(row, 8)
        );

        int residentId =
                Integer.parseInt(
                        getTableValue(row, 0)
                );

        loadPassword(residentId);
    }

    // =========================
    // LOAD PASSWORD
    // =========================

    private void loadPassword(
            int residentId
    ) {

        String sql =
                "SELECT password FROM residents " +
                "WHERE resident_id=?";

        try {

            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(1, residentId);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                passwordField.setText(
                        resultSet.getString("password")
                );
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading password: "
                            + ex.getMessage()
            );
        }
    }

    // =========================
    // GET TABLE VALUE
    // =========================

    private String getTableValue(
            int row,
            int column
    ) {

        Object value =
                tableModel.getValueAt(
                        row,
                        column
                );

        return value == null
                ? ""
                : value.toString();
    }

    // =========================
    // CLEAR
    // =========================

    public void clearFields() {

        nameField.setText("");
        mobileField.setText("");
        emailField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        ageField.setText("");
        addressField.setText("");
        joiningDateField.setText("");
        searchField.setText("");

        genderBox.setSelectedIndex(0);

        residentTable.clearSelection();
    }

    // =========================
    // FIELD SIZE
    // =========================

    private void setFieldSize(
            JTextField field
    ) {

        field.setPreferredSize(
                new Dimension(250, 32)
        );
    }

    // =========================
    // LABEL
    // =========================

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setPreferredSize(
                new Dimension(110, 30)
        );

        return label;
    }

    // =========================
    // BUTTON STYLE
    // =========================

    private void styleButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(Color.WHITE);

        button.setBackground(
                new Color(40, 100, 160)
        );

        button.setFocusPainted(false);

        button.setPreferredSize(
                new Dimension(115, 38)
        );
    }

    private void styleSmallButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(Color.WHITE);

        button.setBackground(
                new Color(40, 100, 160)
        );

        button.setFocusPainted(false);

        button.setPreferredSize(
                new Dimension(105, 32)
        );
    }

    // =========================
    // MAIN
    // =========================

    public static void main(
            String[] args
    ) {

        ResidentManagement page =
                new ResidentManagement();

        page.setVisible(true);
    }
}