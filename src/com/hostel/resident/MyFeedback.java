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

public class MyFeedback extends JFrame {

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
    private static final Color COLOR_DANGER         = new Color(225, 29, 72);   // Rose-600

    // Rating Selectors
    private JComboBox<String> foodCombo;
    private JComboBox<String> cleanlinessCombo;
    private JComboBox<String> maintenanceCombo;
    private JComboBox<String> staffCombo;
    private JComboBox<String> overallCombo;

    private JTextArea commentArea;

    // Table & Model
    private JTable feedbackTable;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton submitButton;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton closeButton;

    // KPI Badge
    private JLabel summaryBadge;

    public MyFeedback() {
        setTitle("Boys Hostel Management System - My Feedback");
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
        loadFeedback();
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

        JLabel tag = new JLabel("RESIDENT WELFARE & SERVICE RATINGS");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("My Experience & Feedback History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Feedback Channel Open") {
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
    // 3. TOP FORM CARD (Rating Form)
    // -------------------------------------------------------------
    private JPanel createFormCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 14));
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 16, 22));

        JLabel sectionTitle = new JLabel("Submit Quality & Service Feedback");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(COLOR_TEXT_MAIN);
        card.add(sectionTitle, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);

        // Safe standard options without unrendered unicode characters
        String[] ratingOptions = {
            "5 - Excellent",
            "4 - Good",
            "3 - Average",
            "2 - Poor",
            "1 - Very Poor"
        };

        foodCombo = new JComboBox<>(ratingOptions);
        cleanlinessCombo = new JComboBox<>(ratingOptions);
        maintenanceCombo = new JComboBox<>(ratingOptions);

        styleComboBox(foodCombo);
        styleComboBox(cleanlinessCombo);
        styleComboBox(maintenanceCombo);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.33;
        grid.add(createFieldBlock("Food & Mess Quality", foodCombo), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.33;
        grid.add(createFieldBlock("Cleanliness & Hygiene", cleanlinessCombo), gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.33;
        grid.add(createFieldBlock("Room & Bath Maintenance", maintenanceCombo), gbc);

        staffCombo = new JComboBox<>(ratingOptions);
        overallCombo = new JComboBox<>(ratingOptions);

        styleComboBox(staffCombo);
        styleComboBox(overallCombo);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.33;
        grid.add(createFieldBlock("Hostel Staff Behavior", staffCombo), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 0.66;
        grid.add(createFieldBlock("Overall Experience Rating", overallCombo), gbc);

        commentArea = new JTextArea(3, 30);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        commentArea.setBackground(COLOR_INPUT_BG);
        commentArea.setForeground(COLOR_TEXT_MAIN);
        commentArea.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        commentArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                commentArea.setBackground(Color.WHITE);
            }
            @Override
            public void focusLost(FocusEvent e) {
                commentArea.setBackground(COLOR_INPUT_BG);
            }
        });

        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setPreferredSize(new Dimension(800, 65));
        commentScroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.weightx = 1.0;
        grid.add(createFieldBlock("Detailed Comments / Suggestions (Optional)", commentScroll), gbc);

        card.add(grid, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        submitButton = new JButton("Submit Feedback");
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

        JLabel tableTitle = new JLabel("Your Previous Submissions");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableTitle.setForeground(COLOR_TEXT_MAIN);

        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete Entry");
        closeButton = new JButton("Close");

        styleSmallOutlineButton(refreshButton);
        styleSmallDangerButton(deleteButton);
        styleSmallOutlineButton(closeButton);

        leftActions.add(tableTitle);
        leftActions.add(refreshButton);
        leftActions.add(deleteButton);
        leftActions.add(closeButton);

        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightBadge.setOpaque(false);

        summaryBadge = new JLabel("Total Submissions: 0 | Avg: 0.0 / 5") {
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
        rightBadge.add(summaryBadge);

        toolbar.add(leftActions, BorderLayout.WEST);
        toolbar.add(rightBadge, BorderLayout.EAST);
        card.add(toolbar, BorderLayout.NORTH);

        String[] columns = {"ID", "Food", "Cleanliness", "Maintenance", "Staff", "Overall", "Remarks", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        feedbackTable = new JTable(tableModel);
        feedbackTable.setRowHeight(38);
        feedbackTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feedbackTable.setForeground(COLOR_TEXT_MAIN);
        feedbackTable.setSelectionBackground(new Color(224, 238, 255));
        feedbackTable.setSelectionForeground(COLOR_PRIMARY);
        feedbackTable.setGridColor(new Color(241, 245, 249));
        feedbackTable.setShowHorizontalLines(true);
        feedbackTable.setShowVerticalLines(false);
        feedbackTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feedbackTable.setAutoCreateRowSorter(true);
        feedbackTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        feedbackTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        feedbackTable.getTableHeader().setBackground(new Color(241, 245, 249));
        feedbackTable.getTableHeader().setForeground(COLOR_TEXT_MAIN);
        feedbackTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        feedbackTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        int[] widths = {60, 95, 115, 120, 90, 95, 340, 120};
        for (int i = 0; i < widths.length; i++) {
            feedbackTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

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

        feedbackTable.getColumnModel().getColumn(0).setCellRenderer(standardRenderer);
        feedbackTable.getColumnModel().getColumn(6).setCellRenderer(standardRenderer);
        feedbackTable.getColumnModel().getColumn(7).setCellRenderer(standardRenderer);

        // Safe Score Renderer (e.g., "4 / 5")
        DefaultTableCellRenderer ratingRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, r, c);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (!isSel) {
                    lbl.setBackground(r % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }

                int score = 0;
                try {
                    score = Integer.parseInt(val == null ? "0" : val.toString());
                } catch (Exception ignored) {}

                if (score >= 4) {
                    lbl.setForeground(new Color(5, 150, 105)); // Green-600
                } else if (score == 3) {
                    lbl.setForeground(new Color(217, 119, 6)); // Amber-600
                } else {
                    lbl.setForeground(COLOR_DANGER);            // Rose-600
                }

                lbl.setText(score + " / 5");
                return lbl;
            }
        };

        for (int i = 1; i <= 5; i++) {
            feedbackTable.getColumnModel().getColumn(i).setCellRenderer(ratingRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        scrollPane.setPreferredSize(new Dimension(1080, 240));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));

        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // -------------------------------------------------------------
    // STYLING HELPERS & CARD CONTAINERS
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
        btn.setPreferredSize(new Dimension(110, 34));

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
        submitButton.addActionListener(e -> submitFeedback());
        refreshButton.addActionListener(e -> loadFeedback());
        deleteButton.addActionListener(e -> deleteFeedback());
        closeButton.addActionListener(e -> dispose());
    }

    private int extractScore(JComboBox<String> combo) {
        String item = (String) combo.getSelectedItem();
        if (item == null) return 5;
        try {
            return Integer.parseInt(item.substring(0, 1));
        } catch (Exception e) {
            return 5;
        }
    }

    /**
     * Resolves a valid resident ID for submitting or loading feedback.
     * Prevents Foreign Key violation errors when testing screens directly.
     */
    private int getValidResidentId() {
        int residentId = 0;
        try {
            residentId = Session.getUserId();
        } catch (Throwable ignored) {}

        // If a session exists, check if resident_id exists in 'residents' table
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

    private void submitFeedback() {
        int residentId = getValidResidentId();

        if (residentId <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "No registered resident record found in the database.\nPlease add a resident profile before submitting feedback.",
                "Resident Not Found",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int foodRating = extractScore(foodCombo);
        int cleanlinessRating = extractScore(cleanlinessCombo);
        int maintenanceRating = extractScore(maintenanceCombo);
        int staffRating = extractScore(staffCombo);
        int overallRating = extractScore(overallCombo);
        String comment = commentArea.getText().trim();

        String sql = "INSERT INTO feedback "
                   + "(resident_id, food_rating, cleanliness_rating, maintenance_rating, "
                   + "staff_rating, overall_rating, comment, feedback_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, residentId);
            statement.setInt(2, foodRating);
            statement.setInt(3, cleanlinessRating);
            statement.setInt(4, maintenanceRating);
            statement.setInt(5, staffRating);
            statement.setInt(6, overallRating);
            statement.setString(7, comment);
            statement.setDate(8, new Date(System.currentTimeMillis()));

            int result = statement.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Thank you! Your feedback has been recorded.", "Feedback Submitted", JOptionPane.INFORMATION_MESSAGE);
                commentArea.setText("");
                foodCombo.setSelectedIndex(0);
                cleanlinessCombo.setSelectedIndex(0);
                maintenanceCombo.setSelectedIndex(0);
                staffCombo.setSelectedIndex(0);
                overallCombo.setSelectedIndex(0);

                loadFeedback();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to submit feedback: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadFeedback() {
        tableModel.setRowCount(0);
        int residentId = getValidResidentId();

        int totalCount = 0;
        double sumOverall = 0.0;

        String sql = "SELECT feedback_id, food_rating, cleanliness_rating, maintenance_rating, "
                   + "staff_rating, overall_rating, comment, feedback_date "
                   + "FROM feedback "
                   + "WHERE resident_id = ? "
                   + "ORDER BY feedback_date DESC, feedback_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, residentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    totalCount++;
                    int overall = resultSet.getInt("overall_rating");
                    sumOverall += overall;

                    tableModel.addRow(new Object[] {
                        resultSet.getInt("feedback_id"),
                        resultSet.getInt("food_rating"),
                        resultSet.getInt("cleanliness_rating"),
                        resultSet.getInt("maintenance_rating"),
                        resultSet.getInt("staff_rating"),
                        overall,
                        resultSet.getString("comment"),
                        resultSet.getDate("feedback_date")
                    });
                }
            }

            double avg = totalCount > 0 ? (sumOverall / totalCount) : 0.0;
            summaryBadge.setText(String.format("Total Submissions: %d | Avg: %.1f / 5", totalCount, avg));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load feedback: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteFeedback() {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a feedback entry from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = feedbackTable.convertRowIndexToModel(selectedRow);
        int feedbackId = (int) tableModel.getValueAt(modelRow, 0);

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this feedback record?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) return;

        int residentId = getValidResidentId();

        String sql = "DELETE FROM feedback WHERE feedback_id = ? AND resident_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, feedbackId);
            statement.setInt(2, residentId);

            int result = statement.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Feedback record deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadFeedback();
            } else {
                JOptionPane.showMessageDialog(this, "Feedback could not be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to delete feedback: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MyFeedback().setVisible(true);
        });
    }
}