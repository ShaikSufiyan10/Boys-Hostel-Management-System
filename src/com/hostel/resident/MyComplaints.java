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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.hostel.database.DBConnection;
import com.hostel.login.Session;

public class MyComplaints extends JFrame {

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
    private static final Color COLOR_SECONDARY_BG   = new Color(241, 245, 249);
    private static final Color COLOR_SECONDARY_TEXT = new Color(51, 65, 85);

    // Status Colors
    private static final Color COLOR_SUCCESS        = new Color(5, 150, 105);   // Emerald-600
    private static final Color COLOR_DANGER         = new Color(225, 29, 72);   // Rose-600
    private static final Color COLOR_WARNING        = new Color(217, 119, 6);   // Amber-600

    // Form Controls
    private JComboBox<String> categoryCombo;
    private JTextArea descriptionArea;

    // Action Buttons
    private JButton submitButton;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton closeButton;

    // Table & Model
    private JTable complaintTable;
    private DefaultTableModel tableModel;

    // KPI Badges
    private JLabel totalBadge;
    private JLabel pendingBadge;
    private JLabel resolvedBadge;

    public MyComplaints() {
        setTitle("Boys Hostel Management System - My Grievance Portal");
        setSize(1180, 840);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createMainContentArea(), BorderLayout.CENTER);

        add(rootPanel);

        initEvents();
        loadComplaints();
    }

    // -------------------------------------------------------------
    // 1. TOP APP BAR
    // -------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(1180, 72));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        JPanel leftBrand = new JPanel(new BorderLayout(0, 3));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("RESIDENT SUPPORT & GRIEVANCE DESK");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("My Complaint Log & Resolution Status");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Support Desk Connected") {
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

        container.add(createFormCard());
        container.add(Box.createVerticalStrut(16));

        container.add(createTableCard());

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getViewport().setBackground(COLOR_BG);
        return scroll;
    }

    // -------------------------------------------------------------
    // 3. TOP FORM CARD
    // -------------------------------------------------------------
    private JPanel createFormCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 16, 22));

        JLabel sectionTitle = new JLabel("Lodge a Grievance or Maintenance Request");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(COLOR_TEXT_MAIN);
        card.add(sectionTitle, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);

        // Row 0: Category
        String[] categories = {
            "Room Maintenance", "Electrical & Lighting", "Plumbing & Water",
            "Food & Mess Hygiene", "Cleanliness & Housekeeping", "Furniture Repair", "Other / General"
        };
        categoryCombo = new JComboBox<>(categories);
        styleComboBox(categoryCombo);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        grid.add(createFieldBlock("Issue Category *", categoryCombo), gbc);

        // Row 1: Description
        descriptionArea = new JTextArea(4, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionArea.setBackground(COLOR_INPUT_BG);
        descriptionArea.setForeground(COLOR_TEXT_MAIN);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        descriptionArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                descriptionArea.setBackground(Color.WHITE);
            }
            @Override
            public void focusLost(FocusEvent e) {
                descriptionArea.setBackground(COLOR_INPUT_BG);
            }
        });

        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setPreferredSize(new Dimension(800, 85));
        descScroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0;
        grid.add(createFieldBlock("Detailed Description of the Issue *", descScroll), gbc);

        card.add(grid, BorderLayout.CENTER);

        // Action Toolbar
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        submitButton = new JButton("+ Submit Complaint");
        stylePrimaryButton(submitButton);

        actions.add(submitButton);
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

        JPanel toolbar = new JPanel(new BorderLayout(14, 0));
        toolbar.setOpaque(false);

        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftActions.setOpaque(false);

        JLabel tableTitle = new JLabel("Your Complaint History");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableTitle.setForeground(COLOR_TEXT_MAIN);

        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Withdraw Complaint");
        closeButton = new JButton("Close");

        styleSmallOutlineButton(refreshButton);
        styleSmallDangerButton(deleteButton);
        styleSmallOutlineButton(closeButton);

        leftActions.add(tableTitle);
        leftActions.add(refreshButton);
        leftActions.add(deleteButton);
        leftActions.add(closeButton);

        // Right KPI Badges
        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightBadge.setOpaque(false);

        totalBadge = createStatusPill("Total: 0", COLOR_SECONDARY_BG, COLOR_SECONDARY_TEXT, COLOR_BORDER);
        pendingBadge = createStatusPill("Pending: 0", new Color(255, 241, 242), COLOR_DANGER, new Color(254, 205, 211));
        resolvedBadge = createStatusPill("Resolved: 0", new Color(236, 253, 245), COLOR_SUCCESS, new Color(167, 243, 208));

        rightBadge.add(totalBadge);
        rightBadge.add(pendingBadge);
        rightBadge.add(resolvedBadge);

        toolbar.add(leftActions, BorderLayout.WEST);
        toolbar.add(rightBadge, BorderLayout.EAST);
        card.add(toolbar, BorderLayout.NORTH);

        String[] columns = {"ID", "Category", "Issue Description", "Lodged Date", "Resolution Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        complaintTable = new JTable(tableModel);
        complaintTable.setRowHeight(38);
        complaintTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        complaintTable.setForeground(COLOR_TEXT_MAIN);
        complaintTable.setSelectionBackground(new Color(224, 238, 255));
        complaintTable.setSelectionForeground(COLOR_PRIMARY);
        complaintTable.setGridColor(new Color(241, 245, 249));
        complaintTable.setShowHorizontalLines(true);
        complaintTable.setShowVerticalLines(false);
        complaintTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        complaintTable.setAutoCreateRowSorter(true);
        complaintTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        complaintTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        complaintTable.getTableHeader().setBackground(new Color(241, 245, 249));
        complaintTable.getTableHeader().setForeground(COLOR_TEXT_MAIN);
        complaintTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        complaintTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        complaintTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        complaintTable.getColumnModel().getColumn(1).setPreferredWidth(170);
        complaintTable.getColumnModel().getColumn(2).setPreferredWidth(450);
        complaintTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        complaintTable.getColumnModel().getColumn(4).setPreferredWidth(150);

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

        for (int i = 0; i < 4; i++) {
            complaintTable.getColumnModel().getColumn(i).setCellRenderer(standardRenderer);
        }

        // Status Badge Renderer
        complaintTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                String status = (val == null) ? "" : val.toString();
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (!isSel) {
                    lbl.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }

                if ("RESOLVED".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status)) {
                    lbl.setForeground(COLOR_SUCCESS);
                    lbl.setText("● Resolved");
                } else if ("IN PROGRESS".equalsIgnoreCase(status)) {
                    lbl.setForeground(COLOR_WARNING);
                    lbl.setText("● In Progress");
                } else {
                    lbl.setForeground(COLOR_DANGER);
                    lbl.setText("● Pending Review");
                }
                return lbl;
            }
        });

        JScrollPane scrollPane = new JScrollPane(complaintTable);
        scrollPane.setPreferredSize(new Dimension(1080, 240));
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
        btn.setPreferredSize(new Dimension(185, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_ACCENT_HOVER); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_ACCENT); }
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
        btn.setPreferredSize(new Dimension(95, 34));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_SECONDARY_BG); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_CARD); }
        });
    }

    private void styleSmallDangerButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(COLOR_DANGER);
        btn.setBackground(new Color(255, 241, 242));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(254, 205, 211), 1));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 34));

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

    // -------------------------------------------------------------
    // LOGIC & DATABASE INTERACTIONS
    // -------------------------------------------------------------
    private void initEvents() {
        submitButton.addActionListener(e -> submitComplaint());
        refreshButton.addActionListener(e -> loadComplaints());
        deleteButton.addActionListener(e -> deleteComplaint());
        closeButton.addActionListener(e -> dispose());
    }

    /**
     * Resolves a valid resident ID for submitting or loading complaints.
     * Prevents Foreign Key violations during direct standalone testing.
     */
    private int getValidResidentId() {
        int residentId = 0;
        try {
            residentId = Session.getUserId();
        } catch (Throwable ignored) {}

        if (residentId > 0) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT resident_id FROM residents WHERE resident_id = ?")) {
                ps.setInt(1, residentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return residentId;
                }
            } catch (Exception ignored) {}
        }

        // Fallback for direct testing: pick the first resident registered in the database
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT resident_id FROM residents ORDER BY resident_id ASC LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("resident_id");
            }
        } catch (Exception ignored) {}

        return 0;
    }

    private void submitComplaint() {
        String category = categoryCombo.getSelectedItem().toString();
        String description = descriptionArea.getText().trim();

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please describe the problem or grievance.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int residentId = getValidResidentId();
        if (residentId <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "No registered resident record found in database.\nPlease register a resident before lodging complaints.",
                "Resident Not Found",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = "INSERT INTO complaints (resident_id, category, description, complaint_date, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, residentId);
            statement.setString(2, category);
            statement.setString(3, description);
            statement.setDate(4, new Date(System.currentTimeMillis()));
            statement.setString(5, "PENDING");

            int result = statement.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Complaint lodged successfully. Administration has been notified.", "Success", JOptionPane.INFORMATION_MESSAGE);
                descriptionArea.setText("");
                categoryCombo.setSelectedIndex(0);
                loadComplaints();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to lodge complaint: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadComplaints() {
        tableModel.setRowCount(0);
        int residentId = getValidResidentId();

        if (residentId <= 0) {
            totalBadge.setText("Total: 0");
            pendingBadge.setText("Pending: 0");
            resolvedBadge.setText("Resolved: 0");
            return;
        }

        String sql = "SELECT complaint_id, category, description, complaint_date, status "
                   + "FROM complaints "
                   + "WHERE resident_id = ? "
                   + "ORDER BY complaint_date DESC, complaint_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, residentId);

            int total = 0;
            int pending = 0;
            int resolved = 0;

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    total++;
                    String status = resultSet.getString("status");

                    if ("RESOLVED".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status)) {
                        resolved++;
                    } else if ("PENDING".equalsIgnoreCase(status)) {
                        pending++;
                    }

                    tableModel.addRow(new Object[] {
                        resultSet.getInt("complaint_id"),
                        resultSet.getString("category"),
                        resultSet.getString("description"),
                        resultSet.getDate("complaint_date"),
                        status
                    });
                }
            }

            totalBadge.setText("Total: " + total);
            pendingBadge.setText("Pending: " + pending);
            resolvedBadge.setText("Resolved: " + resolved);

            if (total == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "You have no logged grievances on file.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load complaints: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteComplaint() {
        int selectedRow = complaintTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a complaint to withdraw.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = complaintTable.convertRowIndexToModel(selectedRow);
        int complaintId = (int) tableModel.getValueAt(modelRow, 0);

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to withdraw complaint #" + complaintId + "?",
            "Confirm Withdrawal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) return;

        int residentId = getValidResidentId();
        String sql = "DELETE FROM complaints WHERE complaint_id = ? AND resident_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, complaintId);
            statement.setInt(2, residentId);

            int result = statement.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Complaint withdrawn successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadComplaints();
            } else {
                JOptionPane.showMessageDialog(this, "Complaint could not be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to delete complaint: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MyComplaints().setVisible(true);
        });
    }
}