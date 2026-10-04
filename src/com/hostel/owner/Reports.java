package com.hostel.owner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.hostel.database.DBConnection;

public class Reports extends JFrame {

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
    private static final Color COLOR_SECONDARY_BG   = new Color(241, 245, 249); // Slate-100
    private static final Color COLOR_SECONDARY_TEXT = new Color(51, 65, 85);

    // Accent Status Colors
    private static final Color COLOR_SUCCESS        = new Color(5, 150, 105);   // Emerald-600
    private static final Color COLOR_WARNING        = new Color(217, 119, 6);   // Amber-600
    private static final Color COLOR_DANGER         = new Color(225, 29, 72);   // Rose-600

    // Metric Value Labels
    private JLabel residentsVal;
    private JLabel workersVal;
    private JLabel roomsVal;
    private JLabel bedsVal;
    private JLabel occupiedBedsVal;
    private JLabel availableBedsVal;
    private JLabel collectionVal;
    private JLabel pendingFeesVal;
    private JLabel paidFeesVal;
    private JLabel expensesVal;
    private JLabel maintenanceVal;
    private JLabel complaintsVal;
    private JLabel pendingComplaintsVal;
    private JLabel ratingVal;

    // Report Text Area
    private JTextArea reportArea;

    // Action Buttons
    private JButton refreshButton;
    private JButton copyButton;
    private JButton closeButton;

    public Reports() {
        setTitle("Boys Hostel Management System - Executive Analytics & Reports");
        setSize(1260, 880);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createMainContentArea(), BorderLayout.CENTER);

        add(rootPanel);

        initEvents();
        loadReport();
    }

    // -------------------------------------------------------------
    // 1. TOP APP BAR
    // -------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(1260, 72));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // Left Branding Info
        JPanel leftBrand = new JPanel(new BorderLayout(0, 4));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("EXECUTIVE AUDIT & PERFORMANCE SUMMARY");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Hostel Operations & Financial Dossier");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        // Right Sync Status
        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Report Synchronized") {
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
    // 2. MAIN CONTENT SCROLLER
    // -------------------------------------------------------------
    private JScrollPane createMainContentArea() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(COLOR_BG);
        container.setBorder(BorderFactory.createEmptyBorder(18, 24, 24, 24));

        // 1. KPI Metric Dashboard Grid
        container.add(createKpiSection());
        container.add(Box.createVerticalStrut(18));

        // 2. Comprehensive Text Dossier Card
        container.add(createDossierCard());
        container.add(Box.createVerticalStrut(14));

        // 3. Footer Action Toolbar
        container.add(createFooterToolbar());

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getViewport().setBackground(COLOR_BG);
        return scroll;
    }

    // -------------------------------------------------------------
    // 3. KPI METRIC SECTION
    // -------------------------------------------------------------
    private JPanel createKpiSection() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 18, 20));

        JLabel title = new JLabel("Key Performance Indicators");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_TEXT_MAIN);
        card.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 5, 12, 12));
        grid.setOpaque(false);

        // Occupancy metrics
        residentsVal = new JLabel("0");
        workersVal = new JLabel("0");
        roomsVal = new JLabel("0");
        bedsVal = new JLabel("0");
        occupiedBedsVal = new JLabel("0");
        availableBedsVal = new JLabel("0");

        // Financial metrics
        collectionVal = new JLabel("₹0.00");
        paidFeesVal = new JLabel("₹0.00");
        pendingFeesVal = new JLabel("₹0.00");
        expensesVal = new JLabel("₹0.00");
        maintenanceVal = new JLabel("₹0.00");

        // Welfare metrics
        complaintsVal = new JLabel("0");
        pendingComplaintsVal = new JLabel("0");
        ratingVal = new JLabel("0.00 ★");

        grid.add(createMiniStatTile("Total Residents", residentsVal, COLOR_ACCENT));
        grid.add(createMiniStatTile("Active Staff", workersVal, COLOR_SECONDARY_TEXT));
        grid.add(createMiniStatTile("Total Rooms", roomsVal, COLOR_PRIMARY));
        grid.add(createMiniStatTile("Total Beds", bedsVal, COLOR_PRIMARY));
        grid.add(createMiniStatTile("Occupied Beds", occupiedBedsVal, COLOR_WARNING));

        grid.add(createMiniStatTile("Available Beds", availableBedsVal, COLOR_SUCCESS));
        grid.add(createMiniStatTile("Fee Collections", collectionVal, COLOR_SUCCESS));
        grid.add(createMiniStatTile("Paid Fee Revenue", paidFeesVal, COLOR_SUCCESS));
        grid.add(createMiniStatTile("Outstanding Dues", pendingFeesVal, COLOR_DANGER));
        grid.add(createMiniStatTile("Operating Expenses", expensesVal, COLOR_DANGER));

        grid.add(createMiniStatTile("Maintenance Costs", maintenanceVal, COLOR_WARNING));
        grid.add(createMiniStatTile("Total Complaints", complaintsVal, COLOR_SECONDARY_TEXT));
        grid.add(createMiniStatTile("Pending Grievances", pendingComplaintsVal, COLOR_DANGER));
        grid.add(createMiniStatTile("Average Rating", ratingVal, COLOR_ACCENT));
        grid.add(createQuickTimestampTile());

        card.add(grid, BorderLayout.CENTER);
        return card;
    }

    private JPanel createMiniStatTile(String labelText, JLabel valueLabel, Color valueColor) {
        JPanel tile = new JPanel(new BorderLayout(0, 4));
        tile.setBackground(COLOR_SECONDARY_BG);
        tile.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(COLOR_TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        valueLabel.setForeground(valueColor);

        tile.add(lbl, BorderLayout.NORTH);
        tile.add(valueLabel, BorderLayout.CENTER);
        return tile;
    }

    private JPanel createQuickTimestampTile() {
        JPanel tile = new JPanel(new BorderLayout(0, 4));
        tile.setBackground(new Color(239, 246, 255));
        tile.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel lbl = new JLabel("Audit Timestamp");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(COLOR_ACCENT);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, HH:mm");
        JLabel val = new JLabel(sdf.format(new Date()));
        val.setFont(new Font("Segoe UI", Font.BOLD, 14));
        val.setForeground(COLOR_PRIMARY);

        tile.add(lbl, BorderLayout.NORTH);
        tile.add(val, BorderLayout.CENTER);
        return tile;
    }

    // -------------------------------------------------------------
    // 4. DETAILED REPORT DOSSIER CARD
    // -------------------------------------------------------------
    private JPanel createDossierCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 18, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Formatted Executive Dossier");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_TEXT_MAIN);

        copyButton = new JButton("📋 Copy Report to Clipboard");
        styleOutlineButton(copyButton);

        header.add(title, BorderLayout.WEST);
        header.add(copyButton, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        reportArea.setBackground(new Color(248, 250, 252));
        reportArea.setForeground(COLOR_TEXT_MAIN);
        reportArea.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(1160, 290));
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));

        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // -------------------------------------------------------------
    // 5. FOOTER TOOLBAR
    // -------------------------------------------------------------
    private JPanel createFooterToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        toolbar.setOpaque(false);

        refreshButton = new JButton("↻ Refresh Live Analytics");
        stylePrimaryButton(refreshButton);

        closeButton = new JButton("Close Window");
        styleOutlineButton(closeButton);

        toolbar.add(refreshButton);
        toolbar.add(closeButton);
        return toolbar;
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

    private void stylePrimaryButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_ACCENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(190, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_ACCENT_HOVER); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_ACCENT); }
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
        btn.setPreferredSize(new Dimension(200, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_SECONDARY_BG); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_CARD); }
        });
    }

    // -------------------------------------------------------------
    // LOGIC & DATABASE DATA FETCH
    // -------------------------------------------------------------
    private void initEvents() {
        refreshButton.addActionListener(e -> loadReport());
        closeButton.addActionListener(e -> dispose());

        copyButton.addActionListener(e -> {
            String text = reportArea.getText();
            if (text != null && !text.isEmpty()) {
                StringSelection selection = new StringSelection(text);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                JOptionPane.showMessageDialog(this, "Full report copied to clipboard!", "Copied", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void loadReport() {
        NumberFormat cur = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, hh:mm a");
        String reportDate = sdf.format(new Date());

        try (Connection conn = DBConnection.getConnection()) {

            // Counts
            int totalResidents = queryCount(conn, "SELECT COUNT(*) FROM residents");
            int totalWorkers   = queryCount(conn, "SELECT COUNT(*) FROM workers");
            int totalRooms     = queryCount(conn, "SELECT COUNT(*) FROM rooms");
            int totalBeds      = queryCount(conn, "SELECT COUNT(*) FROM beds");
            int occupiedBeds   = queryCount(conn, "SELECT COUNT(*) FROM beds WHERE status='OCCUPIED'");
            int availableBeds  = queryCount(conn, "SELECT COUNT(*) FROM beds WHERE status='AVAILABLE'");
            int totalComplaints= queryCount(conn, "SELECT COUNT(*) FROM complaints");
            int pendingComplaints = queryCount(conn, "SELECT COUNT(*) FROM complaints WHERE status='PENDING'");

            // Amounts
            double pendingFees = queryAmount(conn, "SELECT COALESCE(SUM(fee_amount),0) FROM fees WHERE status='PENDING'");
            double paidFees    = queryAmount(conn, "SELECT COALESCE(SUM(fee_amount),0) FROM fees WHERE status='PAID'");
            double collection  = queryAmount(conn, "SELECT COALESCE(SUM(amount),0) FROM payments");
            double expenses    = queryAmount(conn, "SELECT COALESCE(SUM(amount),0) FROM expenses");
            double maintenance = queryAmount(conn, "SELECT COALESCE(SUM(cost),0) FROM maintenance");
            double averageRating = queryAmount(conn, "SELECT COALESCE(AVG(overall_rating),0) FROM feedback");

            double netCashflow = collection - (expenses + maintenance);
            double occupancyRate = (totalBeds > 0) ? ((double) occupiedBeds / totalBeds) * 100.0 : 0.0;

            // Update KPI Tiles
            residentsVal.setText(String.valueOf(totalResidents));
            workersVal.setText(String.valueOf(totalWorkers));
            roomsVal.setText(String.valueOf(totalRooms));
            bedsVal.setText(String.valueOf(totalBeds));
            occupiedBedsVal.setText(String.valueOf(occupiedBeds));
            availableBedsVal.setText(String.valueOf(availableBeds));

            collectionVal.setText(cur.format(collection));
            paidFeesVal.setText(cur.format(paidFees));
            pendingFeesVal.setText(cur.format(pendingFees));
            expensesVal.setText(cur.format(expenses));
            maintenanceVal.setText(cur.format(maintenance));

            complaintsVal.setText(String.valueOf(totalComplaints));
            pendingComplaintsVal.setText(String.valueOf(pendingComplaints));
            ratingVal.setText(String.format("%.2f ★", averageRating));

            // Generate Structured Monospace Dossier
            StringBuilder sb = new StringBuilder();
            sb.append("========================================================================================\n");
            sb.append("                    BOYS HOSTEL MANAGEMENT SYSTEM - EXECUTIVE AUDIT                     \n");
            sb.append("========================================================================================\n");
            sb.append(String.format("Generated On         : %s\n", reportDate));
            sb.append("Audit Scope          : Comprehensive Operations, Occupancy & Financials\n");
            sb.append("----------------------------------------------------------------------------------------\n\n");

            sb.append("[1] OCCUPANCY & INFRASTRUCTURE\n");
            sb.append(String.format("    %-24s : %-12d %-24s : %d\n", "Total Residents", totalResidents, "Active Workers", totalWorkers));
            sb.append(String.format("    %-24s : %-12d %-24s : %d\n", "Total Rooms", totalRooms, "Total Bed Inventory", totalBeds));
            sb.append(String.format("    %-24s : %-12d %-24s : %d\n", "Occupied Beds", occupiedBeds, "Vacant Beds", availableBeds));
            sb.append(String.format("    %-24s : %.1f%%\n\n", "Occupancy Ratio", occupancyRate));

            sb.append("[2] FINANCIAL HEALTH & REVENUE RECOVERY\n");
            sb.append(String.format("    %-24s : %s\n", "Total Payments Received", cur.format(collection)));
            sb.append(String.format("    %-24s : %s\n", "Confirmed Fee Revenue", cur.format(paidFees)));
            sb.append(String.format("    %-24s : %s\n", "Uncollected / Due Fees", cur.format(pendingFees)));
            sb.append(String.format("    %-24s : %s\n", "Operating Outflows", cur.format(expenses)));
            sb.append(String.format("    %-24s : %s\n", "Maintenance & Repairs", cur.format(maintenance)));
            sb.append(String.format("    %-24s : %s\n\n", "Net Operational Surplus", cur.format(netCashflow)));

            sb.append("[3] RESIDENT WELFARE & FACILITY FEEDBACK\n");
            sb.append(String.format("    %-24s : %-12d %-24s : %d\n", "Total Complaints Filed", totalComplaints, "Unresolved Grievances", pendingComplaints));
            sb.append(String.format("    %-24s : %.2f / 5.00 ★\n\n", "Overall Resident Rating", averageRating));

            sb.append("========================================================================================\n");
            sb.append("                                    END OF AUDIT DOSSIER                                \n");
            sb.append("========================================================================================\n");

            reportArea.setText(sb.toString());
            reportArea.setCaretPosition(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to generate hostel report: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private int queryCount(Connection conn, String sql) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private double queryAmount(Connection conn, String sql) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Reports().setVisible(true);
        });
    }
}