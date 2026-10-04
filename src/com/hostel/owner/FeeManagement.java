package com.hostel.owner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.hostel.database.DBConnection;

public class FeeManagement extends JFrame {

    // =========================================
    // LABELS
    // =========================================

    JLabel residentLabel;
    JLabel monthLabel;
    JLabel amountLabel;
    JLabel dueDateLabel;

    // =========================================
    // FIELDS
    // =========================================

    JComboBox<String> residentCombo;

    JTextField monthField;
    JTextField amountField;
    JTextField dueDateField;

    // =========================================
    // BUTTONS
    // =========================================

    JButton addButton;
    JButton updateButton;
    JButton deleteButton;
    JButton searchButton;
    JButton clearButton;
    JButton viewButton;

    // =========================================
    // TABLE
    // =========================================

    JTable table;
    DefaultTableModel model;

    // =========================================
    // CONSTRUCTOR
    // =========================================

    public FeeManagement() {

        // Window properties
        setTitle(
                "Fee Management - Boys Hostel Management System"
        );

        setSize(1000, 650);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);

        // =========================================
        // MAIN PANEL
        // =========================================

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(
                new Color(245, 247, 250)
        );

        // =========================================
        // HEADER
        // =========================================

        JPanel headerPanel =
                new JPanel(new BorderLayout());

        headerPanel.setBackground(
                new Color(25, 55, 90)
        );

        headerPanel.setPreferredSize(
                new Dimension(1000, 80)
        );

        JLabel titleLabel =
                new JLabel(
                        "FEE MANAGEMENT",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        titleLabel.setForeground(
                Color.WHITE
        );

        headerPanel.add(
                titleLabel,
                BorderLayout.CENTER
        );

        // =========================================
        // FORM PANEL
        // =========================================

        JPanel formPanel =
                new JPanel(new GridBagLayout());

        formPanel.setBackground(
                Color.WHITE
        );

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 215, 220)
                        ),
                        "Fee Details"
                )
        );

        formPanel.setPreferredSize(
                new Dimension(1000, 220)
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        10,
                        20,
                        10,
                        20
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // =========================================
        // RESIDENT
        // =========================================

        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                createLabel("Resident"),
                gbc
        );

        residentCombo =
                new JComboBox<>();

        residentCombo.setPreferredSize(
                new Dimension(230, 32)
        );

        gbc.gridx = 1;

        formPanel.add(
                residentCombo,
                gbc
        );

        // =========================================
        // MONTH
        // =========================================

        gbc.gridx = 2;

        formPanel.add(
                createLabel("Month"),
                gbc
        );

        monthField =
                new JTextField();

        monthField.setPreferredSize(
                new Dimension(230, 32)
        );

        gbc.gridx = 3;

        formPanel.add(
                monthField,
                gbc
        );

        // =========================================
        // FEE AMOUNT
        // =========================================

        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(
                createLabel("Fee Amount"),
                gbc
        );

        amountField =
                new JTextField();

        amountField.setPreferredSize(
                new Dimension(230, 32)
        );

        gbc.gridx = 1;

        formPanel.add(
                amountField,
                gbc
        );

        // =========================================
        // DUE DATE
        // =========================================

        gbc.gridx = 2;

        formPanel.add(
                createLabel("Due Date"),
                gbc
        );

        dueDateField =
                new JTextField();

        dueDateField.setPreferredSize(
                new Dimension(230, 32)
        );

        gbc.gridx = 3;

        formPanel.add(
                dueDateField,
                gbc
        );

        // =========================================
        // BUTTON PANEL
        // =========================================

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.setBackground(
                Color.WHITE
        );

        addButton =
                new JButton("ADD FEE");

        updateButton =
                new JButton("UPDATE");

        deleteButton =
                new JButton("DELETE");

        clearButton =
                new JButton("CLEAR");

        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);
        styleButton(clearButton);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 2;

        gbc.gridwidth = 4;

        gbc.anchor =
                GridBagConstraints.CENTER;

        formPanel.add(
                buttonPanel,
                gbc
        );

        // =========================================
        // SEARCH PANEL
        // =========================================

        JPanel searchPanel =
                new JPanel(
                        new BorderLayout(10, 0)
                );

        searchPanel.setBackground(
                new Color(245, 247, 250)
        );

        searchPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );

        JLabel searchLabel =
                new JLabel(
                        "Search Resident:"
                );

        searchLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        JTextField searchField =
                new JTextField();

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

        JPanel searchButtonPanel =
                new JPanel();

        searchButtonPanel.setBackground(
                new Color(245, 247, 250)
        );

        searchButtonPanel.add(
                searchButton
        );

        searchButtonPanel.add(
                viewButton
        );

        searchPanel.add(
                searchButtonPanel,
                BorderLayout.EAST
        );

        // =========================================
        // TABLE
        // =========================================

        model =
                new DefaultTableModel(
                        new String[] {
                                "Fee ID",
                                "Resident",
                                "Month",
                                "Amount",
                                "Due Date",
                                "Status"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        table =
                new JTable(model);

        table.setRowHeight(30);

        table.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        table.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14
                        )
                );

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Fee List"
                )
        );

        // =========================================
        // LOWER PANEL
        // =========================================

        JPanel lowerPanel =
                new JPanel(
                        new BorderLayout()
                );

        lowerPanel.setBackground(
                new Color(245, 247, 250)
        );

        lowerPanel.add(
                searchPanel,
                BorderLayout.NORTH
        );

        lowerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =========================================
        // CONTENT PANEL
        // =========================================

        JPanel contentPanel =
                new JPanel(
                        new BorderLayout()
                );

        contentPanel.setBackground(
                new Color(245, 247, 250)
        );

        contentPanel.add(
                formPanel,
                BorderLayout.NORTH
        );

        contentPanel.add(
                lowerPanel,
                BorderLayout.CENTER
        );

        // =========================================
        // ADD TO MAIN PANEL
        // =========================================

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // =========================================
        // LOAD RESIDENTS
        // =========================================

        loadResidents();

        // =========================================
        // LOAD FEES
        // =========================================

        loadFees();

        // =========================================
        // ADD FEE
        // =========================================

        addButton.addActionListener(e -> {

            if (residentCombo.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a resident.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String residentName =
                    residentCombo
                            .getSelectedItem()
                            .toString();

            String month =
                    monthField
                            .getText()
                            .trim();

            String amount =
                    amountField
                            .getText()
                            .trim();

            String dueDate =
                    dueDateField
                            .getText()
                            .trim();

            if (month.isEmpty()
                    || amount.isEmpty()
                    || dueDate.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            try {

                int residentId =
                        getResidentId(
                                residentName
                        );

                String sql =
                        "INSERT INTO fees "
                        + "(resident_id, month, fee_amount, "
                        + "due_date, status) "
                        + "VALUES (?, ?, ?, ?, 'PENDING')";

                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                statement.setInt(
                        1,
                        residentId
                );

                statement.setString(
                        2,
                        month
                );

                statement.setDouble(
                        3,
                        Double.parseDouble(amount)
                );

                statement.setDate(
                        4,
                        java.sql.Date.valueOf(
                                dueDate
                        )
                );

                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Fee added successfully."
                );

                statement.close();
                connection.close();

                loadFees();
                clearFields();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: "
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        // =========================================
        // UPDATE FEE
        // =========================================

        updateButton.addActionListener(e -> {

            int selectedRow =
                    table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a fee from the table.",
                        "Select Fee",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            try {

                int feeId =
                        Integer.parseInt(
                                model.getValueAt(
                                        selectedRow,
                                        0
                                ).toString()
                        );

                if (residentCombo.getSelectedItem() == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please select a resident."
                    );

                    return;
                }

                String residentName =
                        residentCombo
                                .getSelectedItem()
                                .toString();

                int residentId =
                        getResidentId(
                                residentName
                        );

                String month =
                        monthField
                                .getText()
                                .trim();

                String amount =
                        amountField
                                .getText()
                                .trim();

                String dueDate =
                        dueDateField
                                .getText()
                                .trim();

                if (month.isEmpty()
                        || amount.isEmpty()
                        || dueDate.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please fill all fields."
                    );

                    return;
                }

                String sql =
                        "UPDATE fees SET "
                        + "resident_id=?, "
                        + "month=?, "
                        + "fee_amount=?, "
                        + "due_date=? "
                        + "WHERE fee_id=?";

                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                statement.setInt(
                        1,
                        residentId
                );

                statement.setString(
                        2,
                        month
                );

                statement.setDouble(
                        3,
                        Double.parseDouble(
                                amount
                        )
                );

                statement.setDate(
                        4,
                        java.sql.Date.valueOf(
                                dueDate
                        )
                );

                statement.setInt(
                        5,
                        feeId
                );

                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Fee updated successfully."
                );

                statement.close();
                connection.close();

                loadFees();
                clearFields();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: "
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        // =========================================
        // DELETE FEE
        // =========================================

        deleteButton.addActionListener(e -> {

            int selectedRow =
                    table.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a fee.",
                        "Select Fee",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete this fee?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice !=
                    JOptionPane.YES_OPTION) {

                return;
            }

            try {

                int feeId =
                        Integer.parseInt(
                                model.getValueAt(
                                        selectedRow,
                                        0
                                ).toString()
                        );

                String sql =
                        "DELETE FROM fees "
                        + "WHERE fee_id=?";

                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                statement.setInt(
                        1,
                        feeId
                );

                statement.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Fee deleted successfully."
                );

                statement.close();
                connection.close();

                loadFees();
                clearFields();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error: "
                                + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        // =========================================
        // SEARCH
        // =========================================

        searchButton.addActionListener(e -> {

            String search =
                    searchField
                            .getText()
                            .trim();

            if (search.isEmpty()) {

                loadFees();

            } else {

                searchFee(search);
            }
        });

        // =========================================
        // VIEW ALL
        // =========================================

        viewButton.addActionListener(e -> {

            searchField.setText("");

            loadFees();
        });

        // =========================================
        // CLEAR
        // =========================================

        clearButton.addActionListener(e -> {

            clearFields();
        });

        // =========================================
        // TABLE SELECTION
        // =========================================

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int selectedRow =
                                table.getSelectedRow();

                        if (selectedRow != -1) {

                            Object resident =
                                    model.getValueAt(
                                            selectedRow,
                                            1
                                    );

                            Object month =
                                    model.getValueAt(
                                            selectedRow,
                                            2
                                    );

                            Object amount =
                                    model.getValueAt(
                                            selectedRow,
                                            3
                                    );

                            Object dueDate =
                                    model.getValueAt(
                                            selectedRow,
                                            4
                                    );

                            if (resident != null) {

                                residentCombo
                                        .setSelectedItem(
                                                resident.toString()
                                        );
                            }

                            if (month != null) {

                                monthField.setText(
                                        month.toString()
                                );
                            }

                            if (amount != null) {

                                amountField.setText(
                                        amount.toString()
                                );
                            }

                            if (dueDate != null) {

                                dueDateField.setText(
                                        dueDate.toString()
                                );
                            }
                        }
                    }
                });
    }

    // =========================================
    // CREATE LABEL
    // =========================================

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

        label.setForeground(
                new Color(40, 50, 60)
        );

        return label;
    }

    // =========================================
    // BUTTON STYLE
    // =========================================

    private void styleButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                new Color(40, 100, 160)
        );

        button.setFocusPainted(false);

        button.setPreferredSize(
                new Dimension(
                        115,
                        38
                )
        );
    }

    // =========================================
    // SMALL BUTTON STYLE
    // =========================================

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

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                new Color(40, 100, 160)
        );

        button.setFocusPainted(false);

        button.setPreferredSize(
                new Dimension(
                        110,
                        34
                )
        );
    }

    // =========================================
    // LOAD RESIDENTS
    // =========================================

    private void loadResidents() {

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "SELECT name "
                    + "FROM residents "
                    + "ORDER BY name";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            residentCombo.removeAllItems();

            while (resultSet.next()) {

                residentCombo.addItem(
                        resultSet.getString(
                                "name"
                        )
                );
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load residents.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =========================================
    // GET RESIDENT ID
    // =========================================

    private int getResidentId(
            String residentName
    ) throws Exception {

        String sql =
                "SELECT resident_id "
                + "FROM residents "
                + "WHERE name=?";

        Connection connection =
                DBConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql);

        statement.setString(
                1,
                residentName
        );

        ResultSet resultSet =
                statement.executeQuery();

        int id = 0;

        if (resultSet.next()) {

            id =
                    resultSet.getInt(
                            "resident_id"
                    );
        }

        resultSet.close();
        statement.close();
        connection.close();

        return id;
    }

    // =========================================
    // LOAD ALL FEES
    // =========================================

    private void loadFees() {

        model.setRowCount(0);

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "SELECT f.fee_id, "
                    + "r.name, "
                    + "f.month, "
                    + "f.fee_amount, "
                    + "f.due_date, "
                    + "f.status "
                    + "FROM fees f "
                    + "JOIN residents r "
                    + "ON f.resident_id = r.resident_id "
                    + "ORDER BY f.fee_id DESC";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                model.addRow(
                        new Object[] {

                                resultSet.getInt(
                                        "fee_id"
                                ),

                                resultSet.getString(
                                        "name"
                                ),

                                resultSet.getString(
                                        "month"
                                ),

                                resultSet.getDouble(
                                        "fee_amount"
                                ),

                                resultSet.getDate(
                                        "due_date"
                                ),

                                resultSet.getString(
                                        "status"
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
                    "Unable to load fees.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =========================================
    // SEARCH FEE
    // =========================================

    private void searchFee(
            String search
    ) {

        model.setRowCount(0);

        try {

            Connection connection =
                    DBConnection.getConnection();

            String sql =
                    "SELECT f.fee_id, "
                    + "r.name, "
                    + "f.month, "
                    + "f.fee_amount, "
                    + "f.due_date, "
                    + "f.status "
                    + "FROM fees f "
                    + "JOIN residents r "
                    + "ON f.resident_id = r.resident_id "
                    + "WHERE r.name LIKE ? "
                    + "ORDER BY f.fee_id DESC";

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(
                    1,
                    "%" + search + "%"
            );

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                model.addRow(
                        new Object[] {

                                resultSet.getInt(
                                        "fee_id"
                                ),

                                resultSet.getString(
                                        "name"
                                ),

                                resultSet.getString(
                                        "month"
                                ),

                                resultSet.getDouble(
                                        "fee_amount"
                                ),

                                resultSet.getDate(
                                        "due_date"
                                ),

                                resultSet.getString(
                                        "status"
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
                    "Search Error: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =========================================
    // CLEAR FIELDS
    // =========================================

    private void clearFields() {

        if (residentCombo.getItemCount() > 0) {

            residentCombo.setSelectedIndex(0);
        }

        monthField.setText("");

        amountField.setText("");

        dueDateField.setText("");

        table.clearSelection();
    }

    // =========================================
    // MAIN METHOD
    // =========================================

    public static void main(
            String[] args
    ) {

        FeeManagement feeManagement =
                new FeeManagement();

        feeManagement.setVisible(true);
    }
}