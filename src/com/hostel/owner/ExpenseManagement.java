package com.hostel.owner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Locale;

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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.hostel.database.DBConnection;

public class ExpenseManagement extends JFrame {

    // =============================================================
    // COHESIVE ENTERPRISE COLOR PALETTE
    // =============================================================
    private static final Color COLOR_PRIMARY        = new Color(24, 43, 73);    // Deep Navy
    private static final Color COLOR_ACCENT         = new Color(37, 99, 235);   // Royal Blue
    private static final Color COLOR_ACCENT_HOVER   = new Color(29, 78, 216);
    private static final Color COLOR_BG             = new Color(248, 250, 252); // Slate-50 Canvas
    private static final Color COLOR_CARD           = Color.WHITE;
    private static final Color COLOR_BORDER         = new Color(226, 232, 240); // Slate-200
    private static final Color COLOR_TEXT_MAIN      = new Color(15, 23, 42);    // Slate-900
    private static final Color COLOR_TEXT_MUTED     = new Color(100, 116, 139); // Slate-500
    private static final Color COLOR_INPUT_BG       = new Color(248, 250, 252);

    // Action Colors
    private static final Color COLOR_DANGER         = new Color(225, 29, 72);   // Rose-600
    private static final Color COLOR_SECONDARY_BG   = new Color(241, 245, 249); // Slate-100
    private static final Color COLOR_SECONDARY_TEXT = new Color(51, 65, 85);

    // Form Controls
    private JComboBox<String> categoryCombo;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField searchField;

    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;
    private JButton viewButton;

    // Total Metric Display
    private JLabel totalExpenseLabel;

    // Table
    private JTable table;
    private DefaultTableModel model;

    public ExpenseManagement() {
        setTitle("Boys Hostel Management System - Expense Tracking");
        setSize(1180, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main Layout Canvas
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createMainContentArea(), BorderLayout.CENTER);

        add(rootPanel);

        initEvents();
        loadExpenses();
    }

    // -------------------------------------------------------------
    // 1. TOP APP BAR
    // -------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(1180, 72));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // Left Branding
        JPanel leftBrand = new JPanel(new BorderLayout(0, 4));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("FINANCIAL ADMINISTRATION");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Expense & Outflow Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        // Right Online Status Badge
        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Accounting Active") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 58, 95));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(new Color(52, 211, 153));
        badge.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        rightBadge.add(badge);

        header.add(leftBrand, BorderLayout.WEST);
        header.add(rightBadge, BorderLayout.EAST);
        return header;
    }

    // -------------------------------------------------------------
    // 2. MAIN CONTENT AREA
    // -------------------------------------------------------------
    private JPanel createMainContentArea() {
        JPanel container = new JPanel(new BorderLayout(0, 16));
        container.setBackground(COLOR_BG);
        container.setBorder(BorderFactory.createEmptyBorder(16, 24, 20, 24));

        container.add(createFormCard(), BorderLayout.NORTH);
        container.add(createTableCard(), BorderLayout.CENTER);

        return container;
    }

    // -------------------------------------------------------------
    // 3. TOP FORM CARD
    // -------------------------------------------------------------
    private JPanel createFormCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 16, 22));

        JLabel sectionTitle = new JLabel("Record & Modify Outflow Details");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(COLOR_TEXT_MAIN);
        card.add(sectionTitle, BorderLayout.NORTH);

        // 2-Column Form Grid
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.weightx = 0.5;

        // Row 0: Category & Description
        String[] categories = {
            "Food & Mess", "Electricity Bill", "Water Supply", 
            "Worker Salary", "Maintenance & Repairs", "Cleaning Supplies", 
            "Property Rent", "Other / Miscellaneous"
        };
        categoryCombo = new JComboBox<>(categories);
        styleComboBox(categoryCombo);
        gbc.gridx = 0; gbc.gridy = 0;
        grid.add(createFieldBlock("Expense Category *", categoryCombo), gbc);

        descriptionField = new JTextField();
        styleInputField(descriptionField);
        gbc.gridx = 1; gbc.gridy = 0;
        grid.add(createFieldBlock("Description / Vendor Details *", descriptionField), gbc);

        // Row 1: Amount & Date
        amountField = new JTextField();
        styleInputField(amountField);
        gbc.gridx = 0; gbc.gridy = 1;
        grid.add(createFieldBlock("Amount (₹) *", amountField), gbc);

        dateField = new JTextField();
        dateField.setToolTipText("Format: YYYY-MM-DD");
        styleInputField(dateField);
        gbc.gridx = 1; gbc.gridy = 1;
        grid.add(createFieldBlock("Expense Date (YYYY-MM-DD) *", dateField), gbc);

        card.add(grid, BorderLayout.CENTER);

        // Action Toolbar
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        clearButton = new JButton("Clear Form");
        deleteButton = new JButton("Delete Expense");
        updateButton = new JButton("Save Updates");
        addButton = new JButton("+ Add Expense");

        styleOutlineButton(clearButton);
        styleDangerButton(deleteButton);
        styleSecondaryActionButton(updateButton);
        stylePrimaryButton(addButton);

        actions.add(clearButton);
        actions.add(deleteButton);
        actions.add(updateButton);
        actions.add(addButton);

        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    // -------------------------------------------------------------
    // 4. BOTTOM TABLE CARD
    // -------------------------------------------------------------
    private JPanel createTableCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 18, 20));

        // Toolbar: Search on Left, KPI Badge on Right
        JPanel toolbar = new JPanel(new BorderLayout(14, 0));
        toolbar.setOpaque(false);

        // Search Controls
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBox.setOpaque(false);

        JLabel searchLbl = new JLabel("Filter Category:");
        searchLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLbl.setForeground(COLOR_TEXT_MAIN);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(240, 36));
        styleInputField(searchField);

        searchButton = new JButton("Filter");
        viewButton = new JButton("Reset Table");
        styleSmallButton(searchButton, COLOR_ACCENT, COLOR_ACCENT_HOVER);
        styleSmallOutlineButton(viewButton);

        searchBox.add(searchLbl);
        searchBox.add(searchField);
        searchBox.add(searchButton);
        searchBox.add(viewButton);

        // Real-Time Total Metric Pill
        JPanel metricBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        metricBox.setOpaque(false);

        totalExpenseLabel = new JLabel("Total Outflow: ₹0.00") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_SECONDARY_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        totalExpenseLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalExpenseLabel.setForeground(COLOR_PRIMARY);
        totalExpenseLabel.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        metricBox.add(totalExpenseLabel);

        toolbar.add(searchBox, BorderLayout.WEST);
        toolbar.add(metricBox, BorderLayout.EAST);
        card.add(toolbar, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"ID", "Category", "Description", "Amount (₹)", "Expense Date"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(COLOR_TEXT_MAIN);
        table.setSelectionBackground(new Color(224, 238, 255));
        table.setSelectionForeground(COLOR_PRIMARY);
        table.setGridColor(new Color(241, 245, 249));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Header Styling
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(COLOR_TEXT_MAIN);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        // Column Widths
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(340);
        table.getColumnModel().getColumn(3).setPreferredWidth(140);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);

        // Row Striping & Padding
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(table, val, isSel, hasFocus, r, c);
                if (!isSel) {
                    comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return comp;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    // -------------------------------------------------------------
    // STYLING HELPERS & CARD CONTAINER
    // -------------------------------------------------------------
    private JPanel createCardPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
    }

    private JPanel createFieldBlock(String labelText, Component input) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(COLOR_TEXT_MUTED);

        panel.add(label, BorderLayout.NORTH);
        panel.add(input, BorderLayout.CENTER);
        return panel;
    }

    private void styleInputField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 38));
        field.setBackground(COLOR_INPUT_BG);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBackground(Color.WHITE);
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACCENT, 1),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBackground(COLOR_INPUT_BG);
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BORDER, 1),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
        });
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        box.setPreferredSize(new Dimension(box.getPreferredSize().width, 38));
        box.setBackground(COLOR_INPUT_BG);
        box.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        box.setFocusable(false);
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_ACCENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(145, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_ACCENT_HOVER); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_ACCENT); }
        });
    }

    private void styleSecondaryActionButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(51, 65, 85)); // Slate-700
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(135, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(30, 41, 59)); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(51, 65, 85)); }
        });
    }

    private void styleDangerButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(COLOR_DANGER);
        btn.setBackground(new Color(255, 241, 242));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(254, 205, 211), 1));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(135, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_DANGER);
                btn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(255, 241, 242));
                btn.setForeground(COLOR_DANGER);
            }
        });
    }

    private void styleOutlineButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(COLOR_SECONDARY_TEXT);
        btn.setBackground(COLOR_CARD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_SECONDARY_BG); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_CARD); }
        });
    }

    private void styleSmallButton(JButton btn, Color base, Color hover) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(base);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(85, 36));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(base); }
        });
    }

    private void styleSmallOutlineButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(COLOR_SECONDARY_TEXT);
        btn.setBackground(COLOR_CARD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 36));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_SECONDARY_BG); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_CARD); }
        });
    }

    // -------------------------------------------------------------
    // LOGIC & DATABASE INTERACTIONS
    // -------------------------------------------------------------
    private void initEvents() {
        addButton.addActionListener(e -> addExpense());
        updateButton.addActionListener(e -> updateExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        clearButton.addActionListener(e -> clearFields());

        searchButton.addActionListener(e -> {
            String text = searchField.getText().trim();
            if (text.isEmpty()) {
                loadExpenses();
            } else {
                searchExpenses(text);
            }
        });

        searchField.addActionListener(e -> searchButton.doClick());

        viewButton.addActionListener(e -> {
            searchField.setText("");
            loadExpenses();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    categoryCombo.setSelectedItem(model.getValueAt(selectedRow, 1).toString());
                    descriptionField.setText(model.getValueAt(selectedRow, 2).toString());
                    amountField.setText(model.getValueAt(selectedRow, 3).toString());
                    dateField.setText(model.getValueAt(selectedRow, 4).toString());
                }
            }
        });
    }

    private void addExpense() {
        String category = categoryCombo.getSelectedItem().toString();
        String description = descriptionField.getText().trim();
        String amount = amountField.getText().trim();
        String date = dateField.getText().trim();

        if (description.isEmpty() || amount.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO expenses (category, description, amount, expense_date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, category);
            statement.setString(2, description);
            statement.setDouble(3, Double.parseDouble(amount));
            statement.setDate(4, Date.valueOf(date));

            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Expense recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadExpenses();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid numerical value.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Date must follow YYYY-MM-DD format.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding expense: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateExpense() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense row to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int expenseId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        String category = categoryCombo.getSelectedItem().toString();
        String description = descriptionField.getText().trim();
        String amount = amountField.getText().trim();
        String date = dateField.getText().trim();

        if (description.isEmpty() || amount.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE expenses SET category=?, description=?, amount=?, expense_date=? WHERE expense_id=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, category);
            statement.setString(2, description);
            statement.setDouble(3, Double.parseDouble(amount));
            statement.setDate(4, Date.valueOf(date));
            statement.setInt(5, expenseId);

            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Expense record updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadExpenses();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid numerical value.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Date must follow YYYY-MM-DD format.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteExpense() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int expenseId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this expense record?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM expenses WHERE expense_id=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, expenseId);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Expense record deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadExpenses();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void loadExpenses() {
        model.setRowCount(0);
        double totalSum = 0.0;
        String sql = "SELECT * FROM expenses ORDER BY expense_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                double amt = resultSet.getDouble("amount");
                totalSum += amt;

                model.addRow(new Object[] {
                    resultSet.getInt("expense_id"),
                    resultSet.getString("category"),
                    resultSet.getString("description"),
                    amt,
                    resultSet.getDate("expense_date")
                });
            }
            updateTotalPill(totalSum);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load expenses: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void searchExpenses(String search) {
        model.setRowCount(0);
        double totalSum = 0.0;
        String sql = "SELECT * FROM expenses WHERE category LIKE ? OR description LIKE ? ORDER BY expense_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String term = "%" + search + "%";
            statement.setString(1, term);
            statement.setString(2, term);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    double amt = resultSet.getDouble("amount");
                    totalSum += amt;

                    model.addRow(new Object[] {
                        resultSet.getInt("expense_id"),
                        resultSet.getString("category"),
                        resultSet.getString("description"),
                        amt,
                        resultSet.getDate("expense_date")
                    });
                }
            }
            updateTotalPill(totalSum);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateTotalPill(double total) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        totalExpenseLabel.setText("Total Outflow: " + currencyFormat.format(total));
    }

    private void clearFields() {
        categoryCombo.setSelectedIndex(0);
        descriptionField.setText("");
        amountField.setText("");
        dateField.setText("");
        searchField.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExpenseManagement().setVisible(true);
        });
    }
}