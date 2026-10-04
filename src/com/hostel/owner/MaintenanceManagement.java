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
import java.text.SimpleDateFormat;
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

public class MaintenanceManagement extends JFrame {

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
    private static final Color COLOR_WARNING        = new Color(217, 119, 6);   // Amber-600
    private static final Color COLOR_SECONDARY_BG   = new Color(241, 245, 249); // Slate-100
    private static final Color COLOR_SECONDARY_TEXT = new Color(51, 65, 85);

    // Form Controls
    private JComboBox<String> roomCombo;
    private JTextField issueField;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> workerCombo;
    private JTextField costField;
    private JTextField dateField;
    private JComboBox<String> statusCombo;

    // Action Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    // Search Controls
    private JTextField searchField;
    private JButton searchButton;
    private JButton viewButton;

    // KPI Badge
    private JLabel summaryBadge;

    // Table
    private JTable table;
    private DefaultTableModel model;

    public MaintenanceManagement() {
        setTitle("Boys Hostel Management System - Maintenance & Repairs");
        setSize(1240, 840);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Root Canvas
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createMainContentArea(), BorderLayout.CENTER);

        add(rootPanel);

        initEvents();
        loadRooms();
        loadWorkers();
        loadMaintenance();
        setDefaultDate();
    }

    // -------------------------------------------------------------
    // 1. TOP APP BAR
    // -------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(1240, 72));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // Left Branding Info
        JPanel leftBrand = new JPanel(new BorderLayout(0, 4));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("OPERATIONS & ASSET CARE");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Hostel Maintenance & Work Orders");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        // Right Status Pill
        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Work Orders Active") {
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
    // 2. MAIN CONTENT WRAPPER
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

        JLabel sectionTitle = new JLabel("Work Order Specifications");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(COLOR_TEXT_MAIN);
        card.add(sectionTitle, BorderLayout.NORTH);

        // Grid Layout (2 Rows of 4 balanced columns)
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.weightx = 0.25;

        // Row 0: Room, Issue, Category, Assigned Worker
        roomCombo = new JComboBox<>();
        styleComboBox(roomCombo);
        gbc.gridx = 0; gbc.gridy = 0;
        grid.add(createFieldBlock("Room Number *", roomCombo), gbc);

        issueField = new JTextField();
        styleInputField(issueField);
        gbc.gridx = 1; gbc.gridy = 0;
        grid.add(createFieldBlock("Issue Description *", issueField), gbc);

        String[] categories = {"Electrical", "Plumbing", "Cleaning", "Fan / Lighting", "Furniture", "AC", "Carpentry", "Other"};
        categoryCombo = new JComboBox<>(categories);
        styleComboBox(categoryCombo);
        gbc.gridx = 2; gbc.gridy = 0;
        grid.add(createFieldBlock("Category *", categoryCombo), gbc);

        workerCombo = new JComboBox<>();
        styleComboBox(workerCombo);
        gbc.gridx = 3; gbc.gridy = 0;
        grid.add(createFieldBlock("Assigned Worker *", workerCombo), gbc);

        // Row 1: Cost, Maintenance Date, Status
        costField = new JTextField();
        styleInputField(costField);
        gbc.gridx = 0; gbc.gridy = 1;
        grid.add(createFieldBlock("Repair Cost (₹) *", costField), gbc);

        dateField = new JTextField();
        dateField.setToolTipText("Format: YYYY-MM-DD");
        styleInputField(dateField);
        gbc.gridx = 1; gbc.gridy = 1;
        grid.add(createFieldBlock("Maintenance Date *", dateField), gbc);

        String[] statuses = {"PENDING", "IN PROGRESS", "COMPLETED"};
        statusCombo = new JComboBox<>(statuses);
        styleComboBox(statusCombo);
        gbc.gridx = 2; gbc.gridy = 1;
        gbc.gridwidth = 2;
        grid.add(createFieldBlock("Ticket Status *", statusCombo), gbc);

        card.add(grid, BorderLayout.CENTER);

        // Action Toolbar
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        clearButton = new JButton("Clear Form");
        deleteButton = new JButton("Delete Ticket");
        updateButton = new JButton("Save Updates");
        addButton = new JButton("+ Add Maintenance");

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

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout(14, 0));
        toolbar.setOpaque(false);

        // Search Box
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBox.setOpaque(false);

        JLabel searchLbl = new JLabel("Filter Room:");
        searchLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLbl.setForeground(COLOR_TEXT_MAIN);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 36));
        styleInputField(searchField);

        searchButton = new JButton("Search");
        viewButton = new JButton("Reset Table");

        styleSmallButton(searchButton, COLOR_ACCENT, COLOR_ACCENT_HOVER);
        styleSmallOutlineButton(viewButton);

        searchBox.add(searchLbl);
        searchBox.add(searchField);
        searchBox.add(searchButton);
        searchBox.add(viewButton);

        // Summary Metric Pill
        JPanel metricBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        metricBox.setOpaque(false);

        summaryBadge = new JLabel("Orders: 0 | Pending: 0 | Total Cost: ₹0.00") {
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
        summaryBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        summaryBadge.setForeground(COLOR_PRIMARY);
        summaryBadge.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        metricBox.add(summaryBadge);

        toolbar.add(searchBox, BorderLayout.WEST);
        toolbar.add(metricBox, BorderLayout.EAST);
        card.add(toolbar, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"ID", "Room", "Issue", "Category", "Worker", "Cost (₹)", "Date", "Status"};
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

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(230);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(130);

        // Standard Text Cells Renderer
        DefaultTableCellRenderer standardRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                if (!isSel) {
                    comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return comp;
            }
        };

        for (int i = 0; i < 7; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(standardRenderer);
        }

        // Status Badge Renderer
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                String status = (val == null) ? "" : val.toString();
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (!isSel) {
                    lbl.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }

                if ("COMPLETED".equalsIgnoreCase(status)) {
                    lbl.setForeground(new Color(5, 150, 105)); // Emerald Green
                    lbl.setText("● Completed");
                } else if ("IN PROGRESS".equalsIgnoreCase(status)) {
                    lbl.setForeground(COLOR_WARNING); // Amber
                    lbl.setText("● In Progress");
                } else {
                    lbl.setForeground(COLOR_DANGER); // Rose Red
                    lbl.setText("● Pending");
                }
                return lbl;
            }
        });

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
        btn.setPreferredSize(new Dimension(165, 40));

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
        btn.setPreferredSize(new Dimension(130, 40));

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
        addButton.addActionListener(e -> addMaintenance());
        updateButton.addActionListener(e -> updateMaintenance());
        deleteButton.addActionListener(e -> deleteMaintenance());
        clearButton.addActionListener(e -> {
            clearFields();
            setDefaultDate();
        });

        searchButton.addActionListener(e -> {
            String search = searchField.getText().trim();
            if (search.isEmpty()) {
                loadMaintenance();
            } else {
                searchMaintenance(search);
            }
        });

        searchField.addActionListener(e -> searchButton.doClick());

        viewButton.addActionListener(e -> {
            searchField.setText("");
            loadMaintenance();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    roomCombo.setSelectedItem(model.getValueAt(selectedRow, 1).toString());
                    issueField.setText(model.getValueAt(selectedRow, 2).toString());
                    categoryCombo.setSelectedItem(model.getValueAt(selectedRow, 3).toString());
                    workerCombo.setSelectedItem(model.getValueAt(selectedRow, 4).toString());
                    costField.setText(model.getValueAt(selectedRow, 5).toString());
                    dateField.setText(model.getValueAt(selectedRow, 6).toString());
                    statusCombo.setSelectedItem(model.getValueAt(selectedRow, 7).toString());
                }
            }
        });
    }

    private void setDefaultDate() {
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
    }

    private void loadRooms() {
        String sql = "SELECT room_number FROM rooms ORDER BY room_number";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            roomCombo.removeAllItems();
            while (resultSet.next()) {
                roomCombo.addItem(resultSet.getString("room_number"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load rooms: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadWorkers() {
        String sql = "SELECT name FROM workers ORDER BY name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            workerCombo.removeAllItems();
            while (resultSet.next()) {
                workerCombo.addItem(resultSet.getString("name"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load workers: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getRoomId(String roomNumber) throws Exception {
        String sql = "SELECT room_id FROM rooms WHERE room_number=?";
        int id = 0;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, roomNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("room_id");
                }
            }
        }
        return id;
    }

    private int getWorkerId(String workerName) throws Exception {
        String sql = "SELECT worker_id FROM workers WHERE name=?";
        int id = 0;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, workerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("worker_id");
                }
            }
        }
        return id;
    }

    private void addMaintenance() {
        if (roomCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a room.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (workerCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a worker.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String room = roomCombo.getSelectedItem().toString();
        String issue = issueField.getText().trim();
        String category = categoryCombo.getSelectedItem().toString();
        String worker = workerCombo.getSelectedItem().toString();
        String cost = costField.getText().trim();
        String date = dateField.getText().trim();
        String status = statusCombo.getSelectedItem().toString();

        if (issue.isEmpty() || cost.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO maintenance (room_id, issue, category, worker_id, cost, maintenance_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            int roomId = getRoomId(room);
            int workerId = getWorkerId(worker);

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, roomId);
                statement.setString(2, issue);
                statement.setString(3, category);
                statement.setInt(4, workerId);
                statement.setDouble(5, Double.parseDouble(cost));
                statement.setDate(6, Date.valueOf(date));
                statement.setString(7, status);

                statement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Maintenance ticket created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                setDefaultDate();
                loadMaintenance();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cost must be a valid numerical value.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Date must follow YYYY-MM-DD format.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateMaintenance() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a maintenance record to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maintenanceId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        String room = roomCombo.getSelectedItem().toString();
        String issue = issueField.getText().trim();
        String category = categoryCombo.getSelectedItem().toString();
        String worker = workerCombo.getSelectedItem().toString();
        String cost = costField.getText().trim();
        String date = dateField.getText().trim();
        String status = statusCombo.getSelectedItem().toString();

        if (issue.isEmpty() || cost.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE maintenance SET room_id=?, issue=?, category=?, worker_id=?, cost=?, maintenance_date=?, status=? WHERE maintenance_id=?";

        try {
            int roomId = getRoomId(room);
            int workerId = getWorkerId(worker);

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, roomId);
                statement.setString(2, issue);
                statement.setString(3, category);
                statement.setInt(4, workerId);
                statement.setDouble(5, Double.parseDouble(cost));
                statement.setDate(6, Date.valueOf(date));
                statement.setString(7, status);
                statement.setInt(8, maintenanceId);

                statement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Maintenance ticket updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                setDefaultDate();
                loadMaintenance();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cost must be a valid numerical value.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Date must follow YYYY-MM-DD format.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteMaintenance() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a maintenance record to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maintenanceId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete maintenance ticket #" + maintenanceId + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM maintenance WHERE maintenance_id=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, maintenanceId);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Maintenance ticket deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            setDefaultDate();
            loadMaintenance();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void loadMaintenance() {
        model.setRowCount(0);
        int total = 0, pending = 0;
        double totalCost = 0.0;

        String sql = "SELECT m.maintenance_id, r.room_number, m.issue, m.category, "
                   + "w.name AS worker_name, m.cost, m.maintenance_date, m.status "
                   + "FROM maintenance m "
                   + "LEFT JOIN rooms r ON m.room_id = r.room_id "
                   + "LEFT JOIN workers w ON m.worker_id = w.worker_id "
                   + "ORDER BY m.maintenance_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                total++;
                String status = resultSet.getString("status");
                if ("PENDING".equalsIgnoreCase(status)) {
                    pending++;
                }

                double cost = resultSet.getDouble("cost");
                totalCost += cost;

                model.addRow(new Object[] {
                    resultSet.getInt("maintenance_id"),
                    resultSet.getString("room_number"),
                    resultSet.getString("issue"),
                    resultSet.getString("category"),
                    resultSet.getString("worker_name"),
                    cost,
                    resultSet.getDate("maintenance_date"),
                    status
                });
            }

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            summaryBadge.setText(String.format("Orders: %d | Pending: %d | Total Cost: %s", total, pending, currencyFormat.format(totalCost)));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load maintenance records: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void searchMaintenance(String search) {
        model.setRowCount(0);
        int total = 0, pending = 0;
        double totalCost = 0.0;

        String sql = "SELECT m.maintenance_id, r.room_number, m.issue, m.category, "
                   + "w.name AS worker_name, m.cost, m.maintenance_date, m.status "
                   + "FROM maintenance m "
                   + "LEFT JOIN rooms r ON m.room_id = r.room_id "
                   + "LEFT JOIN workers w ON m.worker_id = w.worker_id "
                   + "WHERE r.room_number LIKE ? "
                   + "ORDER BY m.maintenance_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + search + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    total++;
                    String status = resultSet.getString("status");
                    if ("PENDING".equalsIgnoreCase(status)) {
                        pending++;
                    }

                    double cost = resultSet.getDouble("cost");
                    totalCost += cost;

                    model.addRow(new Object[] {
                        resultSet.getInt("maintenance_id"),
                        resultSet.getString("room_number"),
                        resultSet.getString("issue"),
                        resultSet.getString("category"),
                        resultSet.getString("worker_name"),
                        cost,
                        resultSet.getDate("maintenance_date"),
                        status
                    });
                }
            }

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            summaryBadge.setText(String.format("Filtered: %d | Pending: %d | Total: %s", total, pending, currencyFormat.format(totalCost)));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        if (roomCombo.getItemCount() > 0) roomCombo.setSelectedIndex(0);
        issueField.setText("");
        categoryCombo.setSelectedIndex(0);
        if (workerCombo.getItemCount() > 0) workerCombo.setSelectedIndex(0);
        costField.setText("");
        dateField.setText("");
        statusCombo.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MaintenanceManagement().setVisible(true);
        });
    }
}