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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.DocumentFilter;

import com.hostel.database.DBConnection;

public class FeedbackManagement extends JFrame {

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

    // =============================================================
    // INSPECTOR FIELDS
    // =============================================================

    private JTextField residentField;      // editable: user can TYPE resident name
    private JTextField foodField;          // editable: user can TYPE 1-5
    private JTextField cleanlinessField;   // editable: user can TYPE 1-5
    private JTextField maintenanceField;   // editable: user can TYPE 1-5
    private JTextField staffField;         // editable: user can TYPE 1-5
    private JTextField overallField;       // editable: user can TYPE 1-5
    private JTextArea commentArea;         // editable: user can TYPE/edit remarks
    private JTextField dateField;          // read-only (comes from DB)

    // Filter Fields
    private JTextField searchField;
    private JTextField filterDateField;
    private JButton calendarButton;

    // Buttons
    private JButton deleteButton;
    private JButton searchButton;
    private JButton clearButton;
    private JButton viewButton;
    private JButton listButton;

    // KPI Badge
    private JLabel avgRatingBadge;

    // Table
    private JTable table;
    private DefaultTableModel model;

    public FeedbackManagement() {

        setTitle("Boys Hostel Management System - Feedback & Ratings");
        setSize(1320, 860);
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
        header.setPreferredSize(new Dimension(1320, 72));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        JPanel leftBrand = new JPanel(new BorderLayout(0, 4));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("QUALITY & RESIDENT RELATIONS");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Resident Feedback & Service Ratings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Feedback Monitor Active") {
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

        container.add(createInspectorCard(), BorderLayout.NORTH);
        container.add(createTableCard(), BorderLayout.CENTER);

        return container;
    }

    // -------------------------------------------------------------
    // 3. TOP INSPECTOR CARD
    // -------------------------------------------------------------

    private JPanel createInspectorCard() {

        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        JLabel sectionTitle = new JLabel("Selected Review Inspector");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionTitle.setForeground(COLOR_TEXT_MAIN);
        card.add(sectionTitle, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 8, 4, 8);

        // Row 0: Resident (EDITABLE) & Date (read-only from DB)
        residentField = new JTextField();
        styleInputField(residentField);          // editable, user can TYPE the name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        grid.add(createFieldBlock("Resident Name", residentField), gbc);

        dateField = new JTextField();
        styleReadOnlyField(dateField);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5;
        grid.add(createFieldBlock("Feedback Submission Date", dateField), gbc);

        // Row 1: Score fields - normal editable JTextFields, only 1-5 accepted
        JPanel scoresRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        scoresRow.setOpaque(false);

        foodField = createScoreInputField();
        cleanlinessField = createScoreInputField();
        maintenanceField = createScoreInputField();
        staffField = createScoreInputField();
        overallField = createScoreInputField();

        scoresRow.add(createScoreBlock("Food", foodField));
        scoresRow.add(createScoreBlock("Cleanliness", cleanlinessField));
        scoresRow.add(createScoreBlock("Maintenance", maintenanceField));
        scoresRow.add(createScoreBlock("Staff", staffField));
        scoresRow.add(createScoreBlock("Overall Score", overallField));

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 1.0;
        grid.add(scoresRow, gbc);

        // Row 2: Comment Remarks - EDITABLE
        commentArea = new JTextArea();
        commentArea.setEditable(true);           // user can TYPE/edit remarks
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        commentArea.setBackground(Color.WHITE);
        commentArea.setForeground(COLOR_TEXT_MAIN);
        commentArea.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setPreferredSize(new Dimension(600, 52));
        commentScroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 1.0;
        grid.add(createFieldBlock("Resident Remarks / Comments", commentScroll), gbc);

        card.add(grid, BorderLayout.CENTER);

        // Action Toolbar
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        clearButton = new JButton("Clear Inspector");
        deleteButton = new JButton("Delete Review");

        styleOutlineButton(clearButton);
        styleDangerButton(deleteButton);

        actions.add(clearButton);
        actions.add(deleteButton);

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

        // Search Controls Area
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBox.setOpaque(false);

        JLabel searchLbl = new JLabel("Filter Resident:");
        searchLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLbl.setForeground(COLOR_TEXT_MAIN);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(190, 36));
        styleInputField(searchField);

        JLabel dateLbl = new JLabel("Date:");
        dateLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dateLbl.setForeground(COLOR_TEXT_MAIN);

        filterDateField = new JTextField();
        filterDateField.setEditable(false);
        filterDateField.setToolTipText("Pick feedback date");
        filterDateField.setPreferredSize(new Dimension(115, 36));
        styleInputField(filterDateField);

        calendarButton = new JButton("\uD83D\uDCC5");   // calendar emoji
        styleCalendarButton(calendarButton);

        searchButton = new JButton("Search");
        viewButton = new JButton("Reset Table");
        listButton = new JButton("List All");

        styleSmallButton(searchButton, COLOR_ACCENT, COLOR_ACCENT_HOVER);
        styleSmallOutlineButton(viewButton);
        styleSmallOutlineButton(listButton);

        searchBox.add(searchLbl);
        searchBox.add(searchField);
        searchBox.add(dateLbl);
        searchBox.add(filterDateField);
        searchBox.add(calendarButton);
        searchBox.add(searchButton);
        searchBox.add(viewButton);
        searchBox.add(listButton);

        // Real-Time Average Score KPI Badge
        JPanel metricBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        metricBox.setOpaque(false);

        avgRatingBadge = new JLabel("Average Overall Score: 0.0 \u2605") {
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
        avgRatingBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        avgRatingBadge.setForeground(COLOR_PRIMARY);
        avgRatingBadge.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));

        metricBox.add(avgRatingBadge);

        toolbar.add(searchBox, BorderLayout.WEST);
        toolbar.add(metricBox, BorderLayout.EAST);

        card.add(toolbar, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {
            "ID", "Resident", "Food", "Cleanliness",
            "Maintenance", "Staff", "Overall", "Comment", "Date"
        };

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
        table.setAutoCreateRowSorter(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Header Styling
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(COLOR_TEXT_MAIN);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        int[] widths = {60, 180, 95, 115, 120, 90, 95, 330, 120};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Standard Text Renderer
        DefaultTableCellRenderer standardRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                Component comp = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                if (!isSel) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return comp;
            }
        };

        table.getColumnModel().getColumn(0).setCellRenderer(standardRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(standardRenderer);
        table.getColumnModel().getColumn(7).setCellRenderer(standardRenderer);
        table.getColumnModel().getColumn(8).setCellRenderer(standardRenderer);

        // Rating Badge Renderer (table only - inspector fields stay plain text)
        DefaultTableCellRenderer ratingRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                if (!isSel) {
                    lbl.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 252, 255));
                }
                int score = 0;
                try {
                    score = Integer.parseInt(val == null ? "0" : val.toString());
                } catch (Exception ignored) {}
                if (score >= 4) {
                    lbl.setForeground(new Color(5, 150, 105));   // Green-600
                } else if (score == 3) {
                    lbl.setForeground(new Color(217, 119, 6));   // Amber-600
                } else {
                    lbl.setForeground(new Color(225, 29, 72));   // Rose-600
                }
                lbl.setText(score + " \u2605");
                return lbl;
            }
        };

        for (int i = 2; i <= 6; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(ratingRenderer);
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

    private JPanel createScoreBlock(String title, JTextField field) {

        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(COLOR_TEXT_MUTED);

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a plain editable JTextField for a 1-5 rating.
     * A DocumentFilter guarantees only a single digit 1-5 (or empty) can ever
     * exist in the field, so users can freely TYPE while invalid input is blocked.
     */
    private JTextField createScoreInputField() {

        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tf.setHorizontalAlignment(SwingConstants.CENTER);
        tf.setPreferredSize(new Dimension(85, 34));
        tf.setEditable(true);
        tf.setFocusable(true);
        tf.setBackground(Color.WHITE);
        tf.setForeground(COLOR_PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));

        attachRatingFilter(tf);
        return tf;
    }

    /**
     * Restricts document content to: empty, or a single character '1'..'5'.
     */
    private void attachRatingFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws javax.swing.text.BadLocationException {
                replace(fb, offset, 0, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws javax.swing.text.BadLocationException {

                String safeText = (text == null) ? "" : text;
                String current = fb.getDocument().getText(0, fb.getDocument().getLength());

                StringBuilder sb = new StringBuilder(current);
                sb.replace(offset, offset + length, safeText);
                String proposed = sb.toString();

                if (proposed.isEmpty()
                        || (proposed.length() == 1 && proposed.charAt(0) >= '1' && proposed.charAt(0) <= '5')) {
                    super.replace(fb, offset, length, text, attrs);
                }
                // otherwise: silently reject the keystroke
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length)
                    throws javax.swing.text.BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    private void styleReadOnlyField(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 36));
        field.setEditable(false);
        field.setBackground(COLOR_INPUT_BG);
        field.setForeground(COLOR_TEXT_MAIN);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleInputField(JTextField field) {

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 36));
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

    private void styleCalendarButton(JButton button) {

        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        button.setForeground(COLOR_ACCENT);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        button.setPreferredSize(new Dimension(45, 36));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleDangerButton(JButton btn) {

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(COLOR_DANGER);
        btn.setBackground(new Color(255, 241, 242));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(254, 205, 211), 1));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(135, 38));

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
        btn.setPreferredSize(new Dimension(125, 38));

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
        btn.setPreferredSize(new Dimension(95, 36));

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

        deleteButton.addActionListener(e -> deleteFeedback());
        clearButton.addActionListener(e -> clearFields());
        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> searchButton.doClick());
        calendarButton.addActionListener(e -> showCalendar());

        viewButton.addActionListener(e -> {
            searchField.setText("");
            filterDateField.setText("");
            loadFeedback();
        });

        listButton.addActionListener(e -> {
            searchField.setText("");
            filterDateField.setText("");
            loadFeedback();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    loadSelectedFeedback(modelRow);
                }
            }
        });
    }

    private void performSearch() {

        String resident = searchField.getText().trim();
        String date = filterDateField.getText().trim();

        if (resident.isEmpty() && date.isEmpty()) {
            loadFeedback();
        } else {
            searchFeedback(resident, date);
        }
    }

    public void loadFeedback() {

        model.setRowCount(0);

        int totalRows = 0;
        double sumOverall = 0.0;

        String sql = "SELECT f.feedback_id, r.name, f.food_rating, f.cleanliness_rating, "
                   + "f.maintenance_rating, f.staff_rating, f.overall_rating, f.comment, f.feedback_date "
                   + "FROM feedback f "
                   + "JOIN residents r ON f.resident_id = r.resident_id "
                   + "ORDER BY f.feedback_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                totalRows++;
                int overall = resultSet.getInt("overall_rating");
                sumOverall += overall;
                addFeedbackRow(resultSet);
            }

            updateAverage(totalRows, sumOverall);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load feedback: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void searchFeedback(String resident, String selectedDate) {

        model.setRowCount(0);

        int totalRows = 0;
        double sumOverall = 0.0;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT f.feedback_id, r.name, f.food_rating, f.cleanliness_rating, ")
           .append("f.maintenance_rating, f.staff_rating, f.overall_rating, f.comment, f.feedback_date ")
           .append("FROM feedback f ")
           .append("JOIN residents r ON f.resident_id = r.resident_id ")
           .append("WHERE 1=1 ");

        if (!resident.isEmpty()) {
            sql.append("AND r.name LIKE ? ");
        }
        if (!selectedDate.isEmpty()) {
            sql.append("AND DATE(f.feedback_date) = ? ");
        }
        sql.append("ORDER BY f.feedback_id DESC");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            int parameter = 1;

            if (!resident.isEmpty()) {
                statement.setString(parameter++, "%" + resident + "%");
            }
            if (!selectedDate.isEmpty()) {
                statement.setString(parameter, selectedDate);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    totalRows++;
                    int overall = resultSet.getInt("overall_rating");
                    sumOverall += overall;
                    addFeedbackRow(resultSet);
                }
            }

            updateAverage(totalRows, sumOverall);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void addFeedbackRow(ResultSet resultSet) throws Exception {

        model.addRow(new Object[] {
            resultSet.getInt("feedback_id"),
            resultSet.getString("name"),
            resultSet.getInt("food_rating"),
            resultSet.getInt("cleanliness_rating"),
            resultSet.getInt("maintenance_rating"),
            resultSet.getInt("staff_rating"),
            resultSet.getInt("overall_rating"),
            resultSet.getString("comment"),
            resultSet.getDate("feedback_date")
        });
    }

    private void updateAverage(int totalRows, double sumOverall) {

        double avg = totalRows > 0 ? sumOverall / totalRows : 0.0;
        avgRatingBadge.setText(String.format("Average Overall Score: %.1f \u2605", avg));
    }

    private void loadSelectedFeedback(int modelRow) {

        residentField.setText(getTableValue(modelRow, 1));

        // Plain numbers only, so the user can immediately edit/retype them.
        foodField.setText(extractPlainScore(getTableValue(modelRow, 2)));
        cleanlinessField.setText(extractPlainScore(getTableValue(modelRow, 3)));
        maintenanceField.setText(extractPlainScore(getTableValue(modelRow, 4)));
        staffField.setText(extractPlainScore(getTableValue(modelRow, 5)));
        overallField.setText(extractPlainScore(getTableValue(modelRow, 6)));

        commentArea.setText(getTableValue(modelRow, 7));
        dateField.setText(getTableValue(modelRow, 8));
    }

    /**
     * Returns the raw numeric value from a table cell (strips " ★" decoration
     * and guards against null/blank). Returns "" when there is no valid 1-5 value.
     */
    private String extractPlainScore(String value) {

        if (value == null) {
            return "";
        }

        String cleaned = value.replace("\u2605", "").trim();

        if (cleaned.isEmpty()) {
            return "";
        }

        try {
            int score = Integer.parseInt(cleaned);
            if (score >= 1 && score <= 5) {
                return String.valueOf(score);
            }
        } catch (NumberFormatException ignored) {
        }

        return "";
    }

    private void showCalendar() {

        Calendar calendar = Calendar.getInstance();

        String currentDate = filterDateField.getText().trim();
        if (!currentDate.isEmpty()) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                calendar.setTime(format.parse(currentDate));
            } catch (Exception ignored) {}
        }

        JDialog dialog = new JDialog(this, "Select Feedback Date", true);
        dialog.setSize(380, 390);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);

        JButton previousButton = new JButton("\u25C0");   // ◀
        JButton nextButton = new JButton("\u25B6");       // ▶
        JLabel monthLabel = new JLabel("", SwingConstants.CENTER);

        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        monthLabel.setForeground(Color.WHITE);

        styleCalendarNavigationButton(previousButton);
        styleCalendarNavigationButton(nextButton);

        header.add(previousButton, BorderLayout.WEST);
        header.add(monthLabel, BorderLayout.CENTER);
        header.add(nextButton, BorderLayout.EAST);

        dialog.add(header, BorderLayout.NORTH);

        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(Color.WHITE);

        JPanel daysHeader = new JPanel(new GridLayout(1, 7));
        daysHeader.setBackground(new Color(241, 245, 249));

        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : dayNames) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(COLOR_TEXT_MAIN);
            daysHeader.add(label);
        }

        calendarPanel.add(daysHeader, BorderLayout.NORTH);

        JPanel daysPanel = new JPanel(new GridLayout(6, 7, 2, 2));
        daysPanel.setBackground(Color.WHITE);

        calendarPanel.add(daysPanel, BorderLayout.CENTER);
        dialog.add(calendarPanel, BorderLayout.CENTER);

        Runnable updateCalendar = () -> {

            daysPanel.removeAll();

            monthLabel.setText(new SimpleDateFormat("MMMM yyyy").format(calendar.getTime()));

            Calendar firstDay = (Calendar) calendar.clone();
            firstDay.set(Calendar.DAY_OF_MONTH, 1);

            int firstPosition = firstDay.get(Calendar.DAY_OF_WEEK) - 1;
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 0; i < firstPosition; i++) {
                daysPanel.add(new JLabel());
            }

            for (int day = 1; day <= daysInMonth; day++) {

                final int selectedDay = day;

                JButton dayButton = new JButton(String.valueOf(day));
                dayButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                dayButton.setFocusPainted(false);
                dayButton.setBorderPainted(false);
                dayButton.setBackground(Color.WHITE);
                dayButton.setForeground(COLOR_TEXT_MAIN);
                dayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                if (calendar.get(Calendar.DAY_OF_MONTH) == day) {
                    dayButton.setBackground(new Color(219, 234, 254));
                    dayButton.setForeground(COLOR_ACCENT);
                    dayButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
                }

                dayButton.addActionListener(e -> {
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    filterDateField.setText(format.format(calendar.getTime()));
                    dialog.dispose();
                });

                daysPanel.add(dayButton);
            }

            int total = firstPosition + daysInMonth;
            for (int i = total; i < 42; i++) {
                daysPanel.add(new JLabel());
            }

            daysPanel.revalidate();
            daysPanel.repaint();
        };

        previousButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar.run();
        });

        nextButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar.run();
        });

        updateCalendar.run();

        dialog.setVisible(true);
    }

    private void styleCalendarNavigationButton(JButton button) {

        button.setForeground(Color.WHITE);
        button.setBackground(COLOR_PRIMARY);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void deleteFeedback() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a feedback entry to delete.",
                    "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int feedbackId = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
        String resident = model.getValueAt(modelRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete feedback from " + resident + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM feedback WHERE feedback_id=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, feedbackId);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Feedback deleted successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            loadFeedback();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private String getTableValue(int row, int column) {

        Object value = model.getValueAt(row, column);
        return value == null ? "" : value.toString();
    }

    private void clearFields() {

        residentField.setText("");

        foodField.setText("");
        cleanlinessField.setText("");
        maintenanceField.setText("");
        staffField.setText("");
        overallField.setText("");

        commentArea.setText("");
        dateField.setText("");

        searchField.setText("");
        filterDateField.setText("");

        table.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new FeedbackManagement().setVisible(true);
        });
    }
}