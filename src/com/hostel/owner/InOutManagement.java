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
import java.sql.Time;
import java.text.SimpleDateFormat;

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

public class InOutManagement extends JFrame {

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
    private JComboBox<String> residentCombo;
    private JTextField dateField;
    private JTextField timeField;
    private JComboBox<String> typeCombo;

    // Action Buttons
    private JButton recordButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton nowButton;

    // Search Controls
    private JTextField searchField;
    private JButton searchButton;
    private JButton viewButton;

    // KPI Summary Badge
    private JLabel movementSummaryBadge;

    // Table
    private JTable table;
    private DefaultTableModel model;

    public InOutManagement() {
        setTitle("Boys Hostel Management System - Resident In/Out Tracking");
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
        loadResidents();
        loadRecords();
        setNowTimestamp();
    }

    // -------------------------------------------------------------
    // 1. TOP APP BAR
    // -------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(1180, 72));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // Left Branding Info
        JPanel leftBrand = new JPanel(new BorderLayout(0, 4));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("GATE & SECURITY ADMINISTRATION");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Resident Entry / Exit Log");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        // Right Status Pill
        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Gate Registry Active") {
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

        JLabel sectionTitle = new JLabel("Log Resident Movement");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(COLOR_TEXT_MAIN);
        card.add(sectionTitle, BorderLayout.NORTH);

        // Form Fields Grid
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.weightx = 0.5;

        // Row 0: Resident & Movement Type
        residentCombo = new JComboBox<>();
        styleComboBox(residentCombo);
        gbc.gridx = 0; gbc.gridy = 0;
        grid.add(createFieldBlock("Select Resident *", residentCombo), gbc);

        typeCombo = new JComboBox<>(new String[]{"OUT", "IN"});
        styleComboBox(typeCombo);
        gbc.gridx = 1; gbc.gridy = 0;
        grid.add(createFieldBlock("Movement Direction *", typeCombo), gbc);

        // Row 1: Date & Time
        dateField = new JTextField();
        dateField.setToolTipText("Format: YYYY-MM-DD");
        styleInputField(dateField);
        gbc.gridx = 0; gbc.gridy = 1;
        grid.add(createFieldBlock("Record Date (YYYY-MM-DD) *", dateField), gbc);

        // Time with "Now" shortcut button
        JPanel timeWrapper = new JPanel(new BorderLayout(8, 0));
        timeWrapper.setOpaque(false);

        timeField = new JTextField();
        timeField.setToolTipText("Format: HH:mm:ss");
        styleInputField(timeField);

        nowButton = new JButton("🕒 Now");
        styleSmallOutlineButton(nowButton);
        nowButton.setPreferredSize(new Dimension(85, 38));

        timeWrapper.add(timeField, BorderLayout.CENTER);
        timeWrapper.add(nowButton, BorderLayout.EAST);

        gbc.gridx = 1; gbc.gridy = 1;
        grid.add(createFieldBlock("Record Time (HH:mm:ss) *", timeWrapper), gbc);

        card.add(grid, BorderLayout.CENTER);

        // Action Toolbar
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        clearButton = new JButton("Clear Fields");
        deleteButton = new JButton("Delete Record");
        recordButton = new JButton("+ Record Entry / Exit");

        styleOutlineButton(clearButton);
        styleDangerButton(deleteButton);
        stylePrimaryButton(recordButton);

        actions.add(clearButton);
        actions.add(deleteButton);
        actions.add(recordButton);

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

        // Search Controls
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBox.setOpaque(false);

        JLabel searchLbl = new JLabel("Filter Resident:");
        searchLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLbl.setForeground(COLOR_TEXT_MAIN);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(240, 36));
        styleInputField(searchField);

        searchButton = new JButton("Search");
        viewButton = new JButton("Reset Table");

        styleSmallButton(searchButton, COLOR_ACCENT, COLOR_ACCENT_HOVER);
        styleSmallOutlineButton(viewButton);

        searchBox.add(searchLbl);
        searchBox.add(searchField);
        searchBox.add(searchButton);
        searchBox.add(viewButton);

        // Movement Stats Badge
        JPanel metricBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        metricBox.setOpaque(false);

        movementSummaryBadge = new JLabel("Total Logs: 0 | OUT: 0 | IN: 0") {
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
        movementSummaryBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        movementSummaryBadge.setForeground(COLOR_PRIMARY);
        movementSummaryBadge.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        metricBox.add(movementSummaryBadge);

        toolbar.add(searchBox, BorderLayout.WEST);
        toolbar.add(metricBox, BorderLayout.EAST);
        card.add(toolbar, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"ID", "Resident", "Date", "Time", "Movement Type"};
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

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(260);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(160);
        table.getColumnModel().getColumn(4).setPreferredWidth(160);

        // Standard Text Cells Renderer
        DefaultTableCellRenderer standardRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                if (!isSel) {
                    comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return comp;
            }
        };

        for (int i = 0; i < 4; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(standardRenderer);
        }

        // Status Badge for Movement Direction
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                String type = (val == null) ? "" : val.toString();
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (!isSel) {
                    lbl.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }

                if ("IN".equalsIgnoreCase(type)) {
                    lbl.setForeground(new Color(5, 150, 105)); // Emerald Green
                    lbl.setText("● In (Returned)");
                } else {
                    lbl.setForeground(new Color(225, 29, 72)); // Rose Red
                    lbl.setText("● Out (Exit)");
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
        btn.setPreferredSize(new Dimension(175, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_ACCENT_HOVER); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_ACCENT); }
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
        btn.setPreferredSize(new Dimension(115, 40));

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
        recordButton.addActionListener(e -> recordMovement());
        deleteButton.addActionListener(e -> deleteMovement());
        clearButton.addActionListener(e -> {
            clearFields();
            setNowTimestamp();
        });

        nowButton.addActionListener(e -> setNowTimestamp());

        searchButton.addActionListener(e -> {
            String search = searchField.getText().trim();
            if (search.isEmpty()) {
                loadRecords();
            } else {
                searchRecords(search);
            }
        });

        searchField.addActionListener(e -> searchButton.doClick());

        viewButton.addActionListener(e -> {
            searchField.setText("");
            loadRecords();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    residentCombo.setSelectedItem(model.getValueAt(selectedRow, 1).toString());
                    dateField.setText(model.getValueAt(selectedRow, 2).toString());
                    timeField.setText(model.getValueAt(selectedRow, 3).toString());
                    typeCombo.setSelectedItem(model.getValueAt(selectedRow, 4).toString());
                }
            }
        });
    }

    private void setNowTimestamp() {
        long currentMillis = System.currentTimeMillis();
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(currentMillis)));
        timeField.setText(new SimpleDateFormat("HH:mm:ss").format(new java.util.Date(currentMillis)));
    }

    private void loadResidents() {
        String sql = "SELECT name FROM residents ORDER BY name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            residentCombo.removeAllItems();
            while (resultSet.next()) {
                residentCombo.addItem(resultSet.getString("name"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load residents: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getResidentId(String name) throws Exception {
        String sql = "SELECT resident_id FROM residents WHERE name=?";
        int id = 0;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("resident_id");
                }
            }
        }
        return id;
    }

    private void recordMovement() {
        if (residentCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a resident.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String resident = residentCombo.getSelectedItem().toString();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String type = typeCombo.getSelectedItem().toString();

        if (date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Date and Time.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int residentId = getResidentId(resident);
            if (residentId == 0) {
                JOptionPane.showMessageDialog(this, "Resident not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO in_out (resident_id, record_date, record_time, type) VALUES (?, ?, ?, ?)";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, residentId);
                statement.setDate(2, Date.valueOf(date));
                statement.setTime(3, Time.valueOf(time));
                statement.setString(4, type);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Resident " + type + " movement recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                setNowTimestamp();
                loadRecords();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date/Time format. Use YYYY-MM-DD and HH:mm:ss", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteMovement() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        String resident = model.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete movement log for " + resident + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM in_out WHERE in_out_id=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Movement record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            setNowTimestamp();
            loadRecords();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void loadRecords() {
        model.setRowCount(0);
        int total = 0, countIn = 0, countOut = 0;

        String sql = "SELECT io.in_out_id, r.name, io.record_date, io.record_time, io.type "
                   + "FROM in_out io "
                   + "JOIN residents r ON io.resident_id = r.resident_id "
                   + "ORDER BY io.in_out_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                total++;
                String type = resultSet.getString("type");
                if ("IN".equalsIgnoreCase(type)) {
                    countIn++;
                } else {
                    countOut++;
                }

                model.addRow(new Object[] {
                    resultSet.getInt("in_out_id"),
                    resultSet.getString("name"),
                    resultSet.getDate("record_date"),
                    resultSet.getTime("record_time"),
                    type
                });
            }

            movementSummaryBadge.setText(String.format("Total Logs: %d | OUT: %d | IN: %d", total, countOut, countIn));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load records: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void searchRecords(String search) {
        model.setRowCount(0);
        int total = 0, countIn = 0, countOut = 0;

        String sql = "SELECT io.in_out_id, r.name, io.record_date, io.record_time, io.type "
                   + "FROM in_out io "
                   + "JOIN residents r ON io.resident_id = r.resident_id "
                   + "WHERE r.name LIKE ? "
                   + "ORDER BY io.in_out_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + search + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    total++;
                    String type = resultSet.getString("type");
                    if ("IN".equalsIgnoreCase(type)) {
                        countIn++;
                    } else {
                        countOut++;
                    }

                    model.addRow(new Object[] {
                        resultSet.getInt("in_out_id"),
                        resultSet.getString("name"),
                        resultSet.getDate("record_date"),
                        resultSet.getTime("record_time"),
                        type
                    });
                }
            }

            movementSummaryBadge.setText(String.format("Filtered: %d | OUT: %d | IN: %d", total, countOut, countIn));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        if (residentCombo.getItemCount() > 0) residentCombo.setSelectedIndex(0);
        dateField.setText("");
        timeField.setText("");
        typeCombo.setSelectedIndex(0);
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InOutManagement().setVisible(true);
        });
    }
}