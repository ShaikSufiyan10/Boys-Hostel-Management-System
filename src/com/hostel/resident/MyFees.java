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
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
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
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.hostel.database.DBConnection;
import com.hostel.login.Session;

public class MyFees extends JFrame {

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
    private static final Color COLOR_SECONDARY_BG   = new Color(241, 245, 249);
    private static final Color COLOR_SECONDARY_TEXT = new Color(51, 65, 85);

    // Status Colors
    private static final Color COLOR_SUCCESS        = new Color(5, 150, 105);   // Emerald-600
    private static final Color COLOR_DANGER         = new Color(225, 29, 72);   // Rose-600

    // Metric Badges
    private JLabel totalPendingBadge;
    private JLabel totalPaidBadge;

    // Table
    private JTable feeTable;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton refreshButton;
    private JButton closeButton;

    public MyFees() {
        setTitle("Boys Hostel Management System - My Fee Invoices");
        setSize(960, 760);
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
        loadFees();
    }

    // -------------------------------------------------------------
    // 1. TOP APP BAR
    // -------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(960, 72));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        JPanel leftBrand = new JPanel(new BorderLayout(0, 3));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("RESIDENT FINANCIAL SERVICES");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("My Fee Invoices & Payment Ledger");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Ledger Synchronized") {
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
    // 2. MAIN CONTENT WRAPPER
    // -------------------------------------------------------------
    private JScrollPane createMainContentArea() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(COLOR_BG);
        container.setBorder(BorderFactory.createEmptyBorder(18, 24, 24, 24));

        // Top Financial Overview Card
        container.add(createSummaryOverviewCard());
        container.add(Box.createVerticalStrut(16));

        // Invoices Table Card
        container.add(createTableCard());
        container.add(Box.createVerticalStrut(16));

        // Footer Actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footer.setOpaque(false);

        refreshButton = new JButton("↻ Refresh Ledger");
        stylePrimaryButton(refreshButton);

        closeButton = new JButton("Close Window");
        styleOutlineButton(closeButton);

        footer.add(refreshButton);
        footer.add(closeButton);
        container.add(footer);

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getViewport().setBackground(COLOR_BG);
        return scroll;
    }

    // -------------------------------------------------------------
    // 3. TOP FINANCIAL OVERVIEW CARD
    // -------------------------------------------------------------
    private JPanel createSummaryOverviewCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));

        // Left Icon
        JLabel iconBadge = new JLabel("💳", SwingConstants.CENTER) {
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

        // Description Info
        JPanel textInfo = new JPanel(new BorderLayout(0, 3));
        textInfo.setOpaque(false);

        JLabel mainLabel = new JLabel("Account Statement & Billing");
        mainLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainLabel.setForeground(COLOR_PRIMARY);

        JLabel subLabel = new JLabel("Monthly room charges, mess subscriptions, and official dues.");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(COLOR_TEXT_MUTED);

        textInfo.add(mainLabel, BorderLayout.NORTH);
        textInfo.add(subLabel, BorderLayout.CENTER);

        // Right Financial Summary Badges
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        right.setOpaque(false);

        totalPaidBadge = createStatusPill("Paid: ₹0.00", new Color(236, 253, 245), COLOR_SUCCESS, new Color(167, 243, 208));
        totalPendingBadge = createStatusPill("Pending Dues: ₹0.00", new Color(255, 241, 242), COLOR_DANGER, new Color(254, 205, 211));

        right.add(totalPaidBadge);
        right.add(totalPendingBadge);

        card.add(iconBadge, BorderLayout.WEST);
        card.add(textInfo, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private JLabel createStatusPill(String text, Color bg, Color fg, Color border) {
        JLabel pill = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(border);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pill.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pill.setForeground(fg);
        pill.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return pill;
    }

    // -------------------------------------------------------------
    // 4. INVOICES TABLE CARD
    // -------------------------------------------------------------
    private JPanel createTableCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 18, 20));

        JLabel title = new JLabel("Invoice Statements");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_TEXT_MAIN);
        card.add(title, BorderLayout.NORTH);

        String[] columns = {"Invoice ID", "Billing Period / Month", "Billed Amount (₹)", "Due Date", "Settlement Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        feeTable = new JTable(tableModel);
        feeTable.setRowHeight(38);
        feeTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feeTable.setForeground(COLOR_TEXT_MAIN);
        feeTable.setSelectionBackground(new Color(224, 238, 255));
        feeTable.setSelectionForeground(COLOR_PRIMARY);
        feeTable.setGridColor(new Color(241, 245, 249));
        feeTable.setShowHorizontalLines(true);
        feeTable.setShowVerticalLines(false);
        feeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feeTable.setAutoCreateRowSorter(true);
        feeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Header Styling
        feeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        feeTable.getTableHeader().setBackground(new Color(241, 245, 249));
        feeTable.getTableHeader().setForeground(COLOR_TEXT_MAIN);
        feeTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        feeTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        feeTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        feeTable.getColumnModel().getColumn(1).setPreferredWidth(210);
        feeTable.getColumnModel().getColumn(2).setPreferredWidth(160);
        feeTable.getColumnModel().getColumn(3).setPreferredWidth(160);
        feeTable.getColumnModel().getColumn(4).setPreferredWidth(180);

        // Standard Text Cells Renderer
        DefaultTableCellRenderer standardRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                Component comp = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                if (!isSel) {
                    comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                return comp;
            }
        };

        for (int i = 0; i < 4; i++) {
            feeTable.getColumnModel().getColumn(i).setCellRenderer(standardRenderer);
        }

        // Status Badge Renderer for Settlement Status
        feeTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                String status = (val == null) ? "" : val.toString();
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (!isSel) {
                    lbl.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }

                if ("PAID".equalsIgnoreCase(status)) {
                    lbl.setForeground(COLOR_SUCCESS);
                    lbl.setText("● Paid in Full");
                } else {
                    lbl.setForeground(COLOR_DANGER);
                    lbl.setText("● Payment Pending");
                }
                return lbl;
            }
        });

        JScrollPane scrollPane = new JScrollPane(feeTable);
        scrollPane.setPreferredSize(new Dimension(880, 330));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    // -------------------------------------------------------------
    // STYLING HELPERS
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
        btn.setPreferredSize(new Dimension(170, 38));

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
        btn.setPreferredSize(new Dimension(135, 38));

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
        refreshButton.addActionListener(e -> loadFees());
        closeButton.addActionListener(e -> dispose());
    }

    private void loadFees() {
        tableModel.setRowCount(0);
        int residentId = 0;
        try {
            residentId = Session.getUserId();
        } catch (Throwable ignored) {}

        String sql = "SELECT fee_id, month, fee_amount, due_date, status "
                   + "FROM fees "
                   + "WHERE resident_id = ? "
                   + "ORDER BY due_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, residentId);

            double totalPaid = 0.0;
            double totalPending = 0.0;
            int count = 0;

            NumberFormat cur = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    count++;
                    double amount = resultSet.getDouble("fee_amount");
                    String status = resultSet.getString("status");

                    if ("PAID".equalsIgnoreCase(status)) {
                        totalPaid += amount;
                    } else {
                        totalPending += amount;
                    }

                    tableModel.addRow(new Object[] {
                        resultSet.getInt("fee_id"),
                        resultSet.getString("month"),
                        cur.format(amount),
                        resultSet.getDate("due_date"),
                        status
                    });
                }
            }

            totalPaidBadge.setText("Paid: " + cur.format(totalPaid));
            totalPendingBadge.setText("Pending Dues: " + cur.format(totalPending));

            if (count == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "No fee statements found for your account.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Unable to load fee statements: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MyFees().setVisible(true);
        });
    }
}