package com.hostel.resident;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.hostel.database.DBConnection;
import com.hostel.login.Session;

public class MyRoom extends JFrame {

    // =============================================================
    // COHESIVE ENTERPRISE COLOR PALETTE
    // =============================================================
    private static final Color COLOR_PRIMARY        = new Color(24, 43, 73);    // Deep Navy
    private static final Color COLOR_ACCENT         = new Color(37, 99, 235);   // Royal Blue
    private static final Color COLOR_BG             = new Color(248, 250, 252); // Slate-50 Canvas
    private static final Color COLOR_CARD           = Color.WHITE;
    private static final Color COLOR_BORDER         = new Color(226, 232, 240); // Slate-200
    private static final Color COLOR_TEXT_MAIN      = new Color(15, 23, 42);    // Slate-900
    private static final Color COLOR_TEXT_MUTED     = new Color(100, 116, 139); // Slate-500
    private static final Color COLOR_INPUT_BG       = new Color(248, 250, 252);
    private static final Color COLOR_SECONDARY_BG   = new Color(241, 245, 249);
    private static final Color COLOR_SECONDARY_TEXT = new Color(51, 65, 85);

    // Summary Header Labels
    private JLabel roomHeaderLabel;
    private JLabel floorHeaderLabel;
    private JLabel bedHeaderLabel;

    // Detail Fields
    private JTextField roomNumberField;
    private JTextField floorField;
    private JTextField roomTypeField;
    private JTextField capacityField;
    private JTextField bedNumberField;
    private JTextField bedStatusField;

    // Roommates Table
    private JTable roommatesTable;
    private DefaultTableModel roommatesModel;

    private JButton closeButton;

    public MyRoom() {
        setTitle("Boys Hostel Management System - My Room");
        setSize(920, 780);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createMainContentArea(), BorderLayout.CENTER);

        add(rootPanel);

        initEvents();
        loadRoom();
    }

    // -------------------------------------------------------------
    // 1. TOP APP BAR
    // -------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(920, 72));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        JPanel leftBrand = new JPanel(new BorderLayout(0, 3));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("RESIDENTIAL ACCOMMODATION");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Room Allocation & Bed Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Accommodation Active") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 58, 95));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
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
    // 2. MAIN CONTENT SCROLLER
    // -------------------------------------------------------------
    private JScrollPane createMainContentArea() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(COLOR_BG);
        container.setBorder(BorderFactory.createEmptyBorder(18, 24, 24, 24));

        // Top Summary Card
        container.add(createRoomSummaryCard());
        container.add(Box.createVerticalStrut(16));

        // Details Form Card
        container.add(createRoomDetailsCard());
        container.add(Box.createVerticalStrut(16));

        // Roommates Table Card
        container.add(createRoommatesCard());
        container.add(Box.createVerticalStrut(16));

        // Bottom Action
        JPanel bottomAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomAction.setOpaque(false);

        closeButton = new JButton("Close Window");
        styleOutlineButton(closeButton);
        bottomAction.add(closeButton);

        container.add(bottomAction);

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getViewport().setBackground(COLOR_BG);
        return scroll;
    }

    // -------------------------------------------------------------
    // 3. ROOM SUMMARY OVERVIEW CARD
    // -------------------------------------------------------------
    private JPanel createRoomSummaryCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));

        // Left Icon Badge
        JLabel iconBadge = new JLabel("🚪", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(239, 246, 255));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(191, 219, 254));
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconBadge.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        iconBadge.setPreferredSize(new Dimension(56, 56));

        // Middle Room Header Info
        JPanel textInfo = new JPanel(new BorderLayout(0, 3));
        textInfo.setOpaque(false);

        roomHeaderLabel = new JLabel("Room Loading...");
        roomHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        roomHeaderLabel.setForeground(COLOR_PRIMARY);

        floorHeaderLabel = new JLabel("Floor Assignment: Checking...");
        floorHeaderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        floorHeaderLabel.setForeground(COLOR_TEXT_MUTED);

        textInfo.add(roomHeaderLabel, BorderLayout.NORTH);
        textInfo.add(floorHeaderLabel, BorderLayout.CENTER);

        // Right Bed Badge
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        right.setOpaque(false);

        bedHeaderLabel = new JLabel("Bed: Not Assigned");
        bedHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bedHeaderLabel.setForeground(COLOR_ACCENT);
        bedHeaderLabel.setBackground(new Color(239, 246, 255));
        bedHeaderLabel.setOpaque(true);
        bedHeaderLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254), 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        right.add(bedHeaderLabel);

        card.add(iconBadge, BorderLayout.WEST);
        card.add(textInfo, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    // -------------------------------------------------------------
    // 4. ROOM DETAILS SPECIFICATION CARD
    // -------------------------------------------------------------
    private JPanel createRoomDetailsCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Room & Bed Specifications");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_TEXT_MAIN);
        card.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.weightx = 0.5;

        // Row 0: Room Number & Floor
        roomNumberField = createReadOnlyField();
        gbc.gridx = 0; gbc.gridy = 0;
        grid.add(createFieldBlock("Room Number", roomNumberField), gbc);

        floorField = createReadOnlyField();
        gbc.gridx = 1; gbc.gridy = 0;
        grid.add(createFieldBlock("Floor Level", floorField), gbc);

        // Row 1: Room Type & Capacity
        roomTypeField = createReadOnlyField();
        gbc.gridx = 0; gbc.gridy = 1;
        grid.add(createFieldBlock("Room Category", roomTypeField), gbc);

        capacityField = createReadOnlyField();
        gbc.gridx = 1; gbc.gridy = 1;
        grid.add(createFieldBlock("Bed Capacity", capacityField), gbc);

        // Row 2: Bed Number & Status
        bedNumberField = createReadOnlyField();
        gbc.gridx = 0; gbc.gridy = 2;
        grid.add(createFieldBlock("Your Bed Identifier", bedNumberField), gbc);

        bedStatusField = createReadOnlyField();
        gbc.gridx = 1; gbc.gridy = 2;
        grid.add(createFieldBlock("Bed Status", bedStatusField), gbc);

        card.add(grid, BorderLayout.CENTER);
        return card;
    }

    // -------------------------------------------------------------
    // 5. ROOMMATES TABLE CARD
    // -------------------------------------------------------------
    private JPanel createRoommatesCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Room Beds & Assigned Residents");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(COLOR_TEXT_MAIN);
        card.add(title, BorderLayout.NORTH);

        String[] columns = {"Bed Identifier", "Resident Occupant", "Status"};
        roommatesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        roommatesTable = new JTable(roommatesModel);
        roommatesTable.setRowHeight(34);
        roommatesTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roommatesTable.setForeground(COLOR_TEXT_MAIN);
        roommatesTable.setGridColor(new Color(241, 245, 249));
        roommatesTable.setShowHorizontalLines(true);
        roommatesTable.setShowVerticalLines(false);

        // Header Styling
        roommatesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        roommatesTable.getTableHeader().setBackground(new Color(241, 245, 249));
        roommatesTable.getTableHeader().setForeground(COLOR_TEXT_MAIN);
        roommatesTable.getTableHeader().setPreferredSize(new Dimension(0, 36));

        // Center Alignment
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        roommatesTable.getColumnModel().getColumn(0).setCellRenderer(center);
        roommatesTable.getColumnModel().getColumn(2).setCellRenderer(center);

        JScrollPane scrollPane = new JScrollPane(roommatesTable);
        scrollPane.setPreferredSize(new Dimension(800, 130));
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

    private JTextField createReadOnlyField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(320, 38));
        field.setEditable(false);
        field.setBackground(COLOR_INPUT_BG);
        field.setForeground(COLOR_TEXT_MAIN);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        return field;
    }

    private void styleOutlineButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(COLOR_SECONDARY_TEXT);
        btn.setBackground(COLOR_CARD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 38));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_SECONDARY_BG); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_CARD); }
        });
    }

    private String getFriendlyFloor(int floor) {
        switch (floor) {
            case 0:  return "Ground Floor";
            case 1:  return "1st Floor";
            case 2:  return "2nd Floor";
            case 3:  return "3rd Floor";
            case 4:  return "4th Floor";
            case 5:  return "5th Floor";
            default: return floor + "th Floor";
        }
    }

    // -------------------------------------------------------------
    // LOGIC & DATA LOADING
    // -------------------------------------------------------------
    private void initEvents() {
        closeButton.addActionListener(e -> dispose());
    }

    private void loadRoom() {
        int residentId = 0;
        try {
            residentId = Session.getUserId();
        } catch (Throwable ignored) {}

        String sql = "SELECT r.room_id, r.room_number, r.floor, r.room_type, r.capacity, "
                   + "b.bed_number, b.status "
                   + "FROM beds b "
                   + "JOIN rooms r ON b.room_id = r.room_id "
                   + "WHERE b.resident_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, residentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int roomId = resultSet.getInt("room_id");
                    String roomNum = resultSet.getString("room_number");
                    int floorNum = resultSet.getInt("floor");
                    String floorName = getFriendlyFloor(floorNum);
                    String roomType = resultSet.getString("room_type");
                    int capacity = resultSet.getInt("capacity");
                    String bedNum = resultSet.getString("bed_number");
                    String bedStatus = resultSet.getString("status");

                    // Set Summary Header
                    roomHeaderLabel.setText("Room #" + roomNum);
                    floorHeaderLabel.setText(floorName + "  •  " + roomType + " (" + capacity + " Beds)");
                    bedHeaderLabel.setText("🛏️ Assigned Bed: " + bedNum);

                    // Set Form Fields
                    roomNumberField.setText(roomNum);
                    floorField.setText(floorName);
                    roomTypeField.setText(roomType);
                    capacityField.setText(capacity + " Persons");
                    bedNumberField.setText(bedNum);
                    bedStatusField.setText(bedStatus != null ? bedStatus : "OCCUPIED");

                    // Load Roommates in same room
                    loadRoommates(connection, roomId, residentId);
                } else {
                    roomHeaderLabel.setText("No Room Allocated");
                    floorHeaderLabel.setText("Please contact the warden for room & bed allocation.");
                    bedHeaderLabel.setText("Unassigned");

                    roomNumberField.setText("N/A");
                    floorField.setText("N/A");
                    roomTypeField.setText("N/A");
                    capacityField.setText("N/A");
                    bedNumberField.setText("N/A");
                    bedStatusField.setText("N/A");

                    JOptionPane.showMessageDialog(
                        this,
                        "You have not been assigned to a room yet. Please contact hostel administration.",
                        "Room Assignment",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Unable to load room details: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private void loadRoommates(Connection conn, int roomId, int currentResidentId) {
        roommatesModel.setRowCount(0);
        String sql = "SELECT b.bed_number, COALESCE(r.name, 'Vacant') AS resident_name, b.status, b.resident_id "
                   + "FROM beds b "
                   + "LEFT JOIN residents r ON b.resident_id = r.resident_id "
                   + "WHERE b.room_id = ? "
                   + "ORDER BY b.bed_number";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String bed = rs.getString("bed_number");
                    String resName = rs.getString("resident_name");
                    int rId = rs.getInt("resident_id");
                    String status = rs.getString("status");

                    if (rId == currentResidentId) {
                        resName += " (You)";
                    }

                    roommatesModel.addRow(new Object[]{bed, resName, status});
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MyRoom().setVisible(true);
        });
    }
}