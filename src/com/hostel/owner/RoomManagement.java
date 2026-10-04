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

public class RoomManagement extends JFrame {

    // =============================================================
    // COHESIVE MODERN ENTERPRISE COLOR PALETTE
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
    private static final Color COLOR_DANGER_HOVER   = new Color(190, 18, 60);
    private static final Color COLOR_SECONDARY_BG   = new Color(241, 245, 249); // Slate-100
    private static final Color COLOR_SECONDARY_TEXT = new Color(51, 65, 85);

    // Form Controls
    private JTextField roomNumberField;
    private JTextField searchField;
    private JComboBox<String> floorComboBox;
    private JComboBox<String> roomTypeComboBox;
    private JComboBox<String> capacityComboBox;
    private JComboBox<String> floorFilterComboBox;

    // Action Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;
    private JButton viewButton;
    private JButton floorListButton;

    // Data Table
    private JTable roomTable;
    private DefaultTableModel tableModel;

    public RoomManagement() {
        setTitle("Boys Hostel Management System - Room Administration");
        setSize(1240, 820);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Root Layout Canvas
        JPanel rootPanel = new JPanel(new BorderLayout(0, 0));
        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createMainContentArea(), BorderLayout.CENTER);

        add(rootPanel);

        // Event Hookups & Initial Load
        initEvents();
        updateRoomDetails();
        loadRooms();
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

        JLabel tag = new JLabel("HOSTEL ADMINISTRATION");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Room Inventory & Allocation");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        // Right Status Pill
        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● System Online") {
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
        badge.setForeground(new Color(52, 211, 153)); // Soft green
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
        JPanel container = new JPanel(new BorderLayout(0, 18));
        container.setBackground(COLOR_BG);
        container.setBorder(BorderFactory.createEmptyBorder(18, 24, 20, 24));

        container.add(createFormCard(), BorderLayout.NORTH);
        container.add(createTableCard(), BorderLayout.CENTER);

        return container;
    }

    // -------------------------------------------------------------
    // 3. TOP FORM CARD (Input Fields & Main Actions)
    // -------------------------------------------------------------
    private JPanel createFormCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 16));
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        // Card Title
        JLabel sectionTitle = new JLabel("Room Configuration & Details");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(COLOR_TEXT_MAIN);
        card.add(sectionTitle, BorderLayout.NORTH);

        // Grid of Input Fields
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.weightx = 0.5;

        // Row 1: Room Number & Floor
        gbc.gridx = 0; gbc.gridy = 0;
        grid.add(createFieldBlock("Room Number", roomNumberField = new JTextField()), gbc);

        String[] floors = {"Ground Floor", "1st Floor", "2nd Floor", "3rd Floor", "4th Floor", "5th Floor"};
        floorComboBox = new JComboBox<>(floors);
        styleComboBox(floorComboBox);
        gbc.gridx = 1; gbc.gridy = 0;
        grid.add(createFieldBlock("Floor Assignment", floorComboBox), gbc);

        // Row 2: Room Type & Capacity
        roomTypeComboBox = new JComboBox<>();
        styleComboBox(roomTypeComboBox);
        gbc.gridx = 0; gbc.gridy = 1;
        grid.add(createFieldBlock("Room Type", roomTypeComboBox), gbc);

        capacityComboBox = new JComboBox<>();
        styleComboBox(capacityComboBox);
        gbc.gridx = 1; gbc.gridy = 1;
        grid.add(createFieldBlock("Bed Capacity", capacityComboBox), gbc);

        styleInputField(roomNumberField);
        card.add(grid, BorderLayout.CENTER);

        // Actions Row
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        clearButton = new JButton("Clear Form");
        deleteButton = new JButton("Delete Room");
        updateButton = new JButton("Save Updates");
        addButton = new JButton("+ Add New Room");

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
    // 4. BOTTOM TABLE CARD (Search Filters & Interactive Table)
    // -------------------------------------------------------------
    private JPanel createTableCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 18, 20));

        // Filter Bar (Search Box + Floor Filter Dropdown)
        JPanel filterBar = new JPanel(new BorderLayout(14, 0));
        filterBar.setOpaque(false);

        // Search Block
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBox.setOpaque(false);

        JLabel searchLbl = new JLabel("Search:");
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

        // Floor Filter Block
        JPanel floorBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        floorBox.setOpaque(false);

        JLabel filterLbl = new JLabel("Floor Filter:");
        filterLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        filterLbl.setForeground(COLOR_TEXT_MAIN);

        String[] floorFilters = {"All Floors", "Ground Floor", "1st Floor", "2nd Floor", "3rd Floor", "4th Floor", "5th Floor"};
        floorFilterComboBox = new JComboBox<>(floorFilters);
        floorFilterComboBox.setPreferredSize(new Dimension(170, 36));
        styleComboBox(floorFilterComboBox);

        floorListButton = new JButton("Filter");
        styleSmallButton(floorListButton, COLOR_PRIMARY, new Color(40, 68, 110));

        floorBox.add(filterLbl);
        floorBox.add(floorFilterComboBox);
        floorBox.add(floorListButton);

        filterBar.add(searchBox, BorderLayout.WEST);
        filterBar.add(floorBox, BorderLayout.EAST);
        card.add(filterBar, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"ID", "Room Number", "Floor", "Room Type", "Capacity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        roomTable = new JTable(tableModel);
        roomTable.setRowHeight(40);
        roomTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roomTable.setForeground(COLOR_TEXT_MAIN);
        roomTable.setSelectionBackground(new Color(224, 238, 255));
        roomTable.setSelectionForeground(COLOR_PRIMARY);
        roomTable.setGridColor(new Color(241, 245, 249));
        roomTable.setShowHorizontalLines(true);
        roomTable.setShowVerticalLines(false);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Modern Header Styling
        roomTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        roomTable.getTableHeader().setBackground(new Color(241, 245, 249));
        roomTable.getTableHeader().setForeground(COLOR_TEXT_MAIN);
        roomTable.getTableHeader().setPreferredSize(new Dimension(0, 42));
        roomTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        // Cell Alignment & Alternate Striping
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
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
        centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < roomTable.getColumnCount(); i++) {
            roomTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Adjust specific column widths
        roomTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        roomTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        roomTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        roomTable.getColumnModel().getColumn(3).setPreferredWidth(220);
        roomTable.getColumnModel().getColumn(4).setPreferredWidth(140);

        JScrollPane scrollPane = new JScrollPane(roomTable);
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
        btn.setBackground(new Color(255, 241, 242)); // Light rose
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(254, 205, 211), 1));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(125, 40));

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
    // LOGIC & EVENTS
    // -------------------------------------------------------------
    private void initEvents() {
        floorComboBox.addActionListener(e -> updateRoomDetails());
        addButton.addActionListener(e -> addRoom());
        updateButton.addActionListener(e -> updateRoom());
        deleteButton.addActionListener(e -> deleteRoom());
        clearButton.addActionListener(e -> clearFields());

        searchButton.addActionListener(e -> {
            String roomNumber = searchField.getText().trim();
            if (roomNumber.isEmpty()) {
                loadRooms();
            } else {
                searchRoom(roomNumber);
            }
        });

        // Pressing Enter in search field
        searchField.addActionListener(e -> searchButton.doClick());

        viewButton.addActionListener(e -> {
            searchField.setText("");
            floorFilterComboBox.setSelectedIndex(0);
            loadRooms();
        });

        floorListButton.addActionListener(e -> listRoomsByFloor());

        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = roomTable.getSelectedRow();
                if (selectedRow != -1) {
                    roomNumberField.setText(getTableValue(selectedRow, 1));
                    int floor = Integer.parseInt(getTableValue(selectedRow, 2));
                    floorComboBox.setSelectedIndex(floor);
                    updateRoomDetails();
                }
            }
        });
    }

    private void updateRoomDetails() {
        int floor = floorComboBox.getSelectedIndex();
        roomTypeComboBox.removeAllItems();
        capacityComboBox.removeAllItems();

        switch (floor) {
            case 0:
                roomTypeComboBox.addItem("Single");
                capacityComboBox.addItem("1");
                break;
            case 1:
            case 2:
                roomTypeComboBox.addItem("2 Sharing");
                capacityComboBox.addItem("2");
                break;
            case 3:
                roomTypeComboBox.addItem("3 Sharing");
                capacityComboBox.addItem("3");
                break;
            case 4:
                roomTypeComboBox.addItem("4 Sharing");
                capacityComboBox.addItem("4");
                break;
            case 5:
                roomTypeComboBox.addItem("5 Sharing");
                capacityComboBox.addItem("5");
                break;
            default:
                break;
        }
    }

    private void addRoom() {
        String roomNumber = roomNumberField.getText().trim();
        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid room number.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int floor = floorComboBox.getSelectedIndex();
        String roomType = roomTypeComboBox.getSelectedItem().toString();
        int capacity = Integer.parseInt(capacityComboBox.getSelectedItem().toString());

        String sql = "INSERT INTO rooms (room_number, floor, room_type, capacity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomNumber);
            stmt.setInt(2, floor);
            stmt.setString(3, roomType);
            stmt.setInt(4, capacity);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Room added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadRooms();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int roomId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String roomNumber = roomNumberField.getText().trim();
        if (roomNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter room number.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int floor = floorComboBox.getSelectedIndex();
        String roomType = roomTypeComboBox.getSelectedItem().toString();
        int capacity = Integer.parseInt(capacityComboBox.getSelectedItem().toString());

        String sql = "UPDATE rooms SET room_number=?, floor=?, room_type=?, capacity=? WHERE room_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomNumber);
            stmt.setInt(2, floor);
            stmt.setString(3, roomType);
            stmt.setInt(4, capacity);
            stmt.setInt(5, roomId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Room updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadRooms();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int roomId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete room #" + getTableValue(selectedRow, 1) + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM rooms WHERE room_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Room removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadRooms();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Cannot delete room: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void searchRoom(String roomNumber) {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM rooms WHERE room_number LIKE ? ORDER BY room_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + roomNumber + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    addTableRow(rs);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listRoomsByFloor() {
        int selected = floorFilterComboBox.getSelectedIndex();
        if (selected == 0) {
            loadRooms();
            return;
        }

        int floor = selected - 1;
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM rooms WHERE floor=? ORDER BY room_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, floor);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    addTableRow(rs);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading floor rooms: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadRooms() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM rooms ORDER BY room_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                addTableRow(rs);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTableRow(ResultSet rs) throws Exception {
        Object[] row = {
            rs.getInt("room_id"),
            rs.getString("room_number"),
            rs.getInt("floor"),
            rs.getString("room_type"),
            rs.getInt("capacity")
        };
        tableModel.addRow(row);
    }

    private String getTableValue(int row, int col) {
        Object val = tableModel.getValueAt(row, col);
        return (val == null) ? "" : val.toString();
    }

    public void clearFields() {
        roomNumberField.setText("");
        floorComboBox.setSelectedIndex(0);
        updateRoomDetails();
        searchField.setText("");
        floorFilterComboBox.setSelectedIndex(0);
        roomTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RoomManagement().setVisible(true);
        });
    }
}