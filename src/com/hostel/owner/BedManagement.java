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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.hostel.database.DBConnection;

public class BedManagement extends JFrame {

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
    private JTextField bedNumberField;
    private JComboBox<String> roomBox;
    private JComboBox<String> residentBox;

    // Action Buttons
    private JButton addButton;
    private JButton assignButton;
    private JButton vacateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton viewButton;

    // Table & Model
    private JTable bedTable;
    private DefaultTableModel tableModel;

    // Occupancy KPI Pill
    private JLabel statsBadge;

    public BedManagement() {
        setTitle("Boys Hostel Management System - Bed Allocation & Inventory");
        setSize(1180, 800);
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
        loadResidents();
        loadBeds();
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

        JLabel tag = new JLabel("HOSTEL ADMINISTRATION");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Bed Inventory & Occupancy");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        // Right Status Pill
        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Inventory Sync Active") {
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
    // 3. TOP FORM CARD (Input Fields & Actions)
    // -------------------------------------------------------------
    private JPanel createFormCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 16, 22));

        JLabel sectionTitle = new JLabel("Bed Allocation & Management");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(COLOR_TEXT_MAIN);
        card.add(sectionTitle, BorderLayout.NORTH);

        // Form Fields Grid
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.weightx = 0.33;

        // Column 0: Room Selection
        roomBox = new JComboBox<>();
        styleComboBox(roomBox);
        gbc.gridx = 0; gbc.gridy = 0;
        grid.add(createFieldBlock("Room Number *", roomBox), gbc);

        // Column 1: Bed Number / Label
        bedNumberField = new JTextField();
        styleInputField(bedNumberField);
        gbc.gridx = 1; gbc.gridy = 0;
        grid.add(createFieldBlock("Bed Identifier (e.g., B-1) *", bedNumberField), gbc);

        // Column 2: Resident Assignment
        residentBox = new JComboBox<>();
        styleComboBox(residentBox);
        gbc.gridx = 2; gbc.gridy = 0;
        grid.add(createFieldBlock("Assign Resident (Optional)", residentBox), gbc);

        card.add(grid, BorderLayout.CENTER);

        // Actions Row
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        clearButton = new JButton("Clear Form");
        deleteButton = new JButton("Delete Bed");
        vacateButton = new JButton("Vacate Bed");
        assignButton = new JButton("Assign Resident");
        addButton = new JButton("+ Add Bed");

        styleOutlineButton(clearButton);
        styleDangerButton(deleteButton);
        styleWarningButton(vacateButton);
        styleSecondaryActionButton(assignButton);
        stylePrimaryButton(addButton);

        actions.add(clearButton);
        actions.add(deleteButton);
        actions.add(vacateButton);
        actions.add(assignButton);
        actions.add(addButton);

        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    // -------------------------------------------------------------
    // 4. BOTTOM TABLE CARD (Summary Toolbar + Inventory Table)
    // -------------------------------------------------------------
    private JPanel createTableCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 18, 20));

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout(14, 0));
        toolbar.setOpaque(false);

        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftControls.setOpaque(false);

        JLabel tableTitle = new JLabel("Bed Roster & Assignments");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(COLOR_TEXT_MAIN);

        viewButton = new JButton("Refresh Beds");
        styleSmallOutlineButton(viewButton);

        leftControls.add(tableTitle);
        leftControls.add(viewButton);

        // Real-Time Stats Pill
        JPanel rightMetrics = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightMetrics.setOpaque(false);

        statsBadge = new JLabel("Total: 0 | Occupied: 0 | Available: 0") {
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
        statsBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statsBadge.setForeground(COLOR_PRIMARY);
        statsBadge.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        rightMetrics.add(statsBadge);

        toolbar.add(leftControls, BorderLayout.WEST);
        toolbar.add(rightMetrics, BorderLayout.EAST);
        card.add(toolbar, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"Bed ID", "Room Number", "Bed Number", "Resident", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        bedTable = new JTable(tableModel);
        bedTable.setRowHeight(38);
        bedTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bedTable.setForeground(COLOR_TEXT_MAIN);
        bedTable.setSelectionBackground(new Color(224, 238, 255));
        bedTable.setSelectionForeground(COLOR_PRIMARY);
        bedTable.setGridColor(new Color(241, 245, 249));
        bedTable.setShowHorizontalLines(true);
        bedTable.setShowVerticalLines(false);
        bedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bedTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Header Styling
        bedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        bedTable.getTableHeader().setBackground(new Color(241, 245, 249));
        bedTable.getTableHeader().setForeground(COLOR_TEXT_MAIN);
        bedTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        bedTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        // Column Widths
        bedTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        bedTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        bedTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        bedTable.getColumnModel().getColumn(3).setPreferredWidth(260);
        bedTable.getColumnModel().getColumn(4).setPreferredWidth(140);

        // Custom Cell Renderer with Status Badge
        DefaultTableCellRenderer standardRenderer = new DefaultTableCellRenderer() {
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

        for (int i = 0; i < bedTable.getColumnCount() - 1; i++) {
            bedTable.getColumnModel().getColumn(i).setCellRenderer(standardRenderer);
        }

        // Status Badge Renderer
        bedTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, val, isSel, hasFocus, r, c);
                String status = (val == null) ? "" : val.toString();
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (!isSel) {
                    lbl.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }

                if ("OCCUPIED".equalsIgnoreCase(status)) {
                    lbl.setForeground(new Color(180, 83, 9)); // Amber-700
                    lbl.setText("● Occupied");
                } else {
                    lbl.setForeground(new Color(5, 150, 105)); // Green-600
                    lbl.setText("● Available");
                }
                return lbl;
            }
        });

        JScrollPane scrollPane = new JScrollPane(bedTable);
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
        btn.setPreferredSize(new Dimension(130, 40));

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
        btn.setPreferredSize(new Dimension(145, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(30, 41, 59)); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(51, 65, 85)); }
        });
    }

    private void styleWarningButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(COLOR_WARNING);
        btn.setBackground(new Color(254, 243, 199)); // Amber-100
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(253, 230, 138), 1));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_WARNING);
                btn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(254, 243, 199));
                btn.setForeground(COLOR_WARNING);
            }
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
        btn.setPreferredSize(new Dimension(120, 40));

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

    private void styleSmallOutlineButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(COLOR_SECONDARY_TEXT);
        btn.setBackground(COLOR_CARD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(115, 34));

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
        addButton.addActionListener(e -> addBed());
        assignButton.addActionListener(e -> assignBed());
        vacateButton.addActionListener(e -> vacateBed());
        deleteButton.addActionListener(e -> deleteBed());
        viewButton.addActionListener(e -> loadBeds());
        clearButton.addActionListener(e -> clearFields());

        bedTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = bedTable.getSelectedRow();
                if (selectedRow != -1) {
                    bedNumberField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    roomBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1).toString());

                    String resident = tableModel.getValueAt(selectedRow, 3).toString();
                    if (!resident.isEmpty() && !resident.equalsIgnoreCase("null")) {
                        residentBox.setSelectedItem(resident);
                    }
                }
            }
        });
    }

    private void addBed() {
        String bedNumber = bedNumberField.getText().trim();
        if (bedNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a bed number.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (roomBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a room.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String roomNumber = roomBox.getSelectedItem().toString();
        int roomId = getRoomId(roomNumber);
        if (roomId == 0) {
            JOptionPane.showMessageDialog(this, "Room not found in registry.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String checkSql = "SELECT bed_id FROM beds WHERE room_id=? AND bed_number=?";
        String insertSql = "INSERT INTO beds (room_id, bed_number, status) VALUES (?, ?, 'AVAILABLE')";

        try (Connection connection = DBConnection.getConnection()) {
            // Duplicate check
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, roomId);
                checkStmt.setString(2, bedNumber);
                try (ResultSet checkResult = checkStmt.executeQuery()) {
                    if (checkResult.next()) {
                        JOptionPane.showMessageDialog(this, "This bed number already exists in " + roomNumber + ".", "Duplicate Bed", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            // Insertion
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setInt(1, roomId);
                insertStmt.setString(2, bedNumber);
                int result = insertStmt.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Bed added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    loadBeds();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to add bed: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void assignBed() {
        int selectedRow = bedTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bed from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (residentBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a resident to assign.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String residentName = residentBox.getSelectedItem().toString();
        int residentId = getResidentId(residentName);
        if (residentId == 0) {
            JOptionPane.showMessageDialog(this, "Resident record not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bedId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String currentStatus = tableModel.getValueAt(selectedRow, 4).toString();

        if (currentStatus.equalsIgnoreCase("OCCUPIED")) {
            JOptionPane.showMessageDialog(this, "This bed is already occupied.", "Bed Occupied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String checkSql = "SELECT bed_id FROM beds WHERE resident_id=?";
        String updateSql = "UPDATE beds SET resident_id=?, status='OCCUPIED' WHERE bed_id=?";

        try (Connection connection = DBConnection.getConnection()) {
            // Check if resident already has another bed
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, residentId);
                try (ResultSet checkResult = checkStmt.executeQuery()) {
                    if (checkResult.next()) {
                        JOptionPane.showMessageDialog(this, "Resident \"" + residentName + "\" is already assigned to a bed.", "Resident Already Assigned", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            // Execute assignment
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setInt(1, residentId);
                updateStmt.setInt(2, bedId);
                int result = updateStmt.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Bed assigned successfully to " + residentName + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadBeds();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to assign bed: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void vacateBed() {
        int selectedRow = bedTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bed to vacate.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bedId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String residentName = tableModel.getValueAt(selectedRow, 3).toString();

        if (residentName.isEmpty() || residentName.equalsIgnoreCase("null")) {
            JOptionPane.showMessageDialog(this, "This bed is already vacant and available.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to vacate this bed from " + residentName + "?",
            "Confirm Vacate",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) return;

        String sql = "UPDATE beds SET resident_id=NULL, status='AVAILABLE' WHERE bed_id=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bedId);
            int result = statement.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Bed vacated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBeds();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to vacate bed: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteBed() {
        int selectedRow = bedTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bed to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bedId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String status = tableModel.getValueAt(selectedRow, 4).toString();

        if (status.equalsIgnoreCase("OCCUPIED")) {
            JOptionPane.showMessageDialog(this, "Cannot delete an occupied bed. Please vacate it first.", "Bed In Use", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove bed #" + tableModel.getValueAt(selectedRow, 2) + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM beds WHERE bed_id=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bedId);
            int result = statement.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Bed deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadBeds();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to delete bed: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void loadRooms() {
        roomBox.removeAllItems();
        String sql = "SELECT room_number FROM rooms ORDER BY room_number";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                roomBox.addItem(resultSet.getString("room_number"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load rooms: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadResidents() {
        residentBox.removeAllItems();
        String sql = "SELECT name FROM residents ORDER BY name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                residentBox.addItem(resultSet.getString("name"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load residents: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getRoomId(String roomNumber) {
        int roomId = 0;
        String sql = "SELECT room_id FROM rooms WHERE room_number=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, roomNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    roomId = resultSet.getInt("room_id");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return roomId;
    }

    public int getResidentId(String residentName) {
        int residentId = 0;
        String sql = "SELECT resident_id FROM residents WHERE name=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, residentName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    residentId = resultSet.getInt("resident_id");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return residentId;
    }

    public void loadBeds() {
        tableModel.setRowCount(0);
        int total = 0;
        int occupied = 0;

        String sql = "SELECT b.bed_id, r.room_number, b.bed_number, "
                   + "COALESCE(res.name, '') AS resident_name, b.status "
                   + "FROM beds b "
                   + "JOIN rooms r ON b.room_id = r.room_id "
                   + "LEFT JOIN residents res ON b.resident_id = res.resident_id "
                   + "ORDER BY r.room_number, b.bed_number";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                total++;
                String status = resultSet.getString("status");
                if ("OCCUPIED".equalsIgnoreCase(status)) {
                    occupied++;
                }

                tableModel.addRow(new Object[] {
                    resultSet.getInt("bed_id"),
                    resultSet.getString("room_number"),
                    resultSet.getString("bed_number"),
                    resultSet.getString("resident_name"),
                    status
                });
            }

            int available = total - occupied;
            statsBadge.setText(String.format("Total: %d  |  Occupied: %d  |  Available: %d", total, occupied, available));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load beds: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void clearFields() {
        bedNumberField.setText("");
        if (roomBox.getItemCount() > 0) roomBox.setSelectedIndex(0);
        if (residentBox.getItemCount() > 0) residentBox.setSelectedIndex(0);
        bedTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BedManagement().setVisible(true);
        });
    }
}