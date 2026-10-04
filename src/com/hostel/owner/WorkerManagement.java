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
import java.sql.Date;
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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.hostel.database.DBConnection;

public class WorkerManagement extends JFrame {

    // =============================================================
    // COLORS
    // =============================================================

    private static final Color COLOR_PRIMARY =
            new Color(24, 43, 73);

    private static final Color COLOR_ACCENT =
            new Color(37, 99, 235);

    private static final Color COLOR_ACCENT_HOVER =
            new Color(29, 78, 216);

    private static final Color COLOR_BG =
            new Color(248, 250, 252);

    private static final Color COLOR_CARD =
            Color.WHITE;

    private static final Color COLOR_BORDER =
            new Color(226, 232, 240);

    private static final Color COLOR_TEXT_MAIN =
            new Color(15, 23, 42);

    private static final Color COLOR_TEXT_MUTED =
            new Color(100, 116, 139);

    private static final Color COLOR_INPUT_BG =
            new Color(248, 250, 252);

    private static final Color COLOR_DANGER =
            new Color(225, 29, 72);

    private static final Color COLOR_SECONDARY_BG =
            new Color(241, 245, 249);

    private static final Color COLOR_SECONDARY_TEXT =
            new Color(51, 65, 85);

    // =============================================================
    // FORM FIELDS
    // =============================================================

    private JTextField nameField;
    private JTextField mobileField;
    private JTextField emailField;
    private JTextField jobRoleField;
    private JTextField salaryField;
    private JTextField joiningDateField;
    private JTextField addressField;
    private JTextField usernameField;

    private JPasswordField passwordField;

    private JTextField searchField;

    // =============================================================
    // CALENDAR BUTTON
    // =============================================================

    private JButton calendarButton;

    // =============================================================
    // BUTTONS
    // =============================================================

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;
    private JButton viewButton;

    // =============================================================
    // TABLE
    // =============================================================

    private JTable workerTable;

    private DefaultTableModel tableModel;

    // =============================================================
    // CONSTRUCTOR
    // =============================================================

    public WorkerManagement() {

        setTitle(
                "Boys Hostel Management System - Staff Administration"
        );

        setSize(
                1260,
                850
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(true);

        // =========================================================
        // ROOT PANEL
        // =========================================================

        JPanel rootPanel =
                new JPanel(
                        new BorderLayout()
                );

        rootPanel.setBackground(
                COLOR_BG
        );

        rootPanel.add(
                createHeaderPanel(),
                BorderLayout.NORTH
        );

        rootPanel.add(
                createMainContentArea(),
                BorderLayout.CENTER
        );

        add(rootPanel);

        // =========================================================
        // EVENTS
        // =========================================================

        initEvents();

        // =========================================================
        // LOAD DATA
        // =========================================================

        loadWorkers();
    }

    // =============================================================
    // 1. HEADER
    // =============================================================

    private JPanel createHeaderPanel() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                COLOR_PRIMARY
        );

        header.setPreferredSize(
                new Dimension(
                        1260,
                        72
                )
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        28,
                        0,
                        28
                )
        );

        // =========================================================
        // LEFT BRAND
        // =========================================================

        JPanel leftBrand =
                new JPanel(
                        new BorderLayout(
                                0,
                                4
                        )
                );

        leftBrand.setOpaque(false);

        JLabel tag =
                new JLabel(
                        "HOSTEL ADMINISTRATION"
                );

        tag.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        tag.setForeground(
                new Color(
                        147,
                        197,
                        253
                )
        );

        JLabel title =
                new JLabel(
                        "Staff & Worker Directory"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        title.setForeground(
                Color.WHITE
        );

        leftBrand.add(
                tag,
                BorderLayout.NORTH
        );

        leftBrand.add(
                title,
                BorderLayout.CENTER
        );

        // =========================================================
        // RIGHT BADGE
        // =========================================================

        JPanel rightBadge =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                16
                        )
                );

        rightBadge.setOpaque(false);

        JLabel badge =
                new JLabel(
                        "● Directory Active"
                ) {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(
                                new Color(
                                        30,
                                        58,
                                        95
                                )
                        );

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                14,
                                14
                        );

                        g2.dispose();

                        super.paintComponent(g);
                    }
                };

        badge.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        badge.setForeground(
                new Color(
                        52,
                        211,
                        153
                )
        );

        badge.setBorder(
                BorderFactory.createEmptyBorder(
                        6,
                        14,
                        6,
                        14
                )
        );

        rightBadge.add(badge);

        header.add(
                leftBrand,
                BorderLayout.WEST
        );

        header.add(
                rightBadge,
                BorderLayout.EAST
        );

        return header;
    }

    // =============================================================
    // 2. MAIN CONTENT
    // =============================================================

    private JPanel createMainContentArea() {

        JPanel container =
                new JPanel(
                        new BorderLayout(
                                0,
                                16
                        )
                );

        container.setBackground(
                COLOR_BG
        );

        container.setBorder(
                BorderFactory.createEmptyBorder(
                        16,
                        24,
                        20,
                        24
                )
        );

        container.add(
                createFormCard(),
                BorderLayout.NORTH
        );

        container.add(
                createTableCard(),
                BorderLayout.CENTER
        );

        return container;
    }

    // =============================================================
    // 3. FORM CARD
    // =============================================================

    private JPanel createFormCard() {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        0,
                        14
                )
        );

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        16,
                        22,
                        16,
                        22
                )
        );

        JLabel sectionTitle =
                new JLabel(
                        "Worker Profile & Credentials"
                );

        sectionTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        sectionTitle.setForeground(
                COLOR_TEXT_MAIN
        );

        card.add(
                sectionTitle,
                BorderLayout.NORTH
        );

        // =========================================================
        // GRID
        // =========================================================

        JPanel grid =
                new JPanel(
                        new GridBagLayout()
                );

        grid.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(
                        4,
                        8,
                        4,
                        8
                );

        gbc.weightx =
                0.33;

        // =========================================================
        // ROW 0
        // =========================================================

        gbc.gridx = 0;
        gbc.gridy = 0;

        nameField =
                new JTextField();

        grid.add(
                createFieldBlock(
                        "Full Name *",
                        nameField
                ),
                gbc
        );

        gbc.gridx = 1;

        mobileField =
                new JTextField();

        grid.add(
                createFieldBlock(
                        "Mobile Number *",
                        mobileField
                ),
                gbc
        );

        gbc.gridx = 2;

        emailField =
                new JTextField();

        grid.add(
                createFieldBlock(
                        "Email Address",
                        emailField
                ),
                gbc
        );

        // =========================================================
        // ROW 1
        // =========================================================

        gbc.gridx = 0;
        gbc.gridy = 1;

        jobRoleField =
                new JTextField();

        grid.add(
                createFieldBlock(
                        "Job Role *",
                        jobRoleField
                ),
                gbc
        );

        gbc.gridx = 1;

        salaryField =
                new JTextField();

        grid.add(
                createFieldBlock(
                        "Salary (₹) *",
                        salaryField
                ),
                gbc
        );

        // =========================================================
        // JOINING DATE
        // =========================================================

        gbc.gridx = 2;

        joiningDateField =
                new JTextField();

        joiningDateField.setToolTipText(
                "Select joining date"
        );

        calendarButton =
                new JButton("📅");

        styleCalendarButton(
                calendarButton
        );

        JPanel datePanel =
                new JPanel(
                        new BorderLayout(
                                5,
                                0
                        )
                );

        datePanel.setOpaque(false);

        datePanel.add(
                joiningDateField,
                BorderLayout.CENTER
        );

        datePanel.add(
                calendarButton,
                BorderLayout.EAST
        );

        grid.add(
                createFieldBlock(
                        "Joining Date *",
                        datePanel
                ),
                gbc
        );

        // =========================================================
        // ROW 2
        // =========================================================

        gbc.gridx = 0;
        gbc.gridy = 2;

        addressField =
                new JTextField();

        grid.add(
                createFieldBlock(
                        "Residential Address",
                        addressField
                ),
                gbc
        );

        gbc.gridx = 1;

        usernameField =
                new JTextField();

        grid.add(
                createFieldBlock(
                        "Username",
                        usernameField
                ),
                gbc
        );

        gbc.gridx = 2;

        passwordField =
                new JPasswordField();

        grid.add(
                createFieldBlock(
                        "Account Password",
                        passwordField
                ),
                gbc
        );

        // =========================================================
        // STYLE INPUTS
        // =========================================================

        styleInputField(nameField);
        styleInputField(mobileField);
        styleInputField(emailField);
        styleInputField(jobRoleField);
        styleInputField(salaryField);
        styleInputField(joiningDateField);
        styleInputField(addressField);
        styleInputField(usernameField);
        styleInputField(passwordField);

        card.add(
                grid,
                BorderLayout.CENTER
        );

        // =========================================================
        // ACTION BUTTONS
        // =========================================================

        JPanel actions =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        actions.setOpaque(false);

        clearButton =
                new JButton(
                        "Clear Form"
                );

        deleteButton =
                new JButton(
                        "Delete Worker"
                );

        updateButton =
                new JButton(
                        "Save Updates"
                );

        addButton =
                new JButton(
                        "+ Add New Worker"
                );

        styleOutlineButton(
                clearButton
        );

        styleDangerButton(
                deleteButton
        );

        styleSecondaryActionButton(
                updateButton
        );

        stylePrimaryButton(
                addButton
        );

        actions.add(clearButton);
        actions.add(deleteButton);
        actions.add(updateButton);
        actions.add(addButton);

        card.add(
                actions,
                BorderLayout.SOUTH
        );

        return card;
    }

    // =============================================================
    // 4. TABLE CARD
    // =============================================================

    private JPanel createTableCard() {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        16,
                        20,
                        18,
                        20
                )
        );

        // =========================================================
        // SEARCH BAR
        // =========================================================

        JPanel filterBar =
                new JPanel(
                        new BorderLayout(
                                14,
                                0
                        )
                );

        filterBar.setOpaque(false);

        JPanel searchBox =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                0
                        )
                );

        searchBox.setOpaque(false);

        JLabel searchLbl =
                new JLabel(
                        "Search Directory:"
                );

        searchLbl.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        searchLbl.setForeground(
                COLOR_TEXT_MAIN
        );

        searchField =
                new JTextField();

        searchField.setPreferredSize(
                new Dimension(
                        320,
                        36
                )
        );

        styleInputField(
                searchField
        );

        searchButton =
                new JButton(
                        "Search"
                );

        viewButton =
                new JButton(
                        "Reset Table"
                );

        styleSmallButton(
                searchButton,
                COLOR_ACCENT,
                COLOR_ACCENT_HOVER
        );

        styleSmallOutlineButton(
                viewButton
        );

        searchBox.add(searchLbl);
        searchBox.add(searchField);
        searchBox.add(searchButton);
        searchBox.add(viewButton);

        filterBar.add(
                searchBox,
                BorderLayout.WEST
        );

        card.add(
                filterBar,
                BorderLayout.NORTH
        );

        // =========================================================
        // TABLE
        // =========================================================

        String[] columns = {

                "ID",
                "Full Name",
                "Mobile",
                "Email",
                "Job Role",
                "Salary",
                "Joining Date",
                "Address",
                "Username"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int col
                    ) {

                        return false;
                    }
                };

        workerTable =
                new JTable(
                        tableModel
                );

        workerTable.setRowHeight(
                38
        );

        workerTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        workerTable.setForeground(
                COLOR_TEXT_MAIN
        );

        workerTable.setSelectionBackground(
                new Color(
                        224,
                        238,
                        255
                )
        );

        workerTable.setSelectionForeground(
                COLOR_PRIMARY
        );

        workerTable.setGridColor(
                new Color(
                        241,
                        245,
                        249
                )
        );

        workerTable.setShowHorizontalLines(
                true
        );

        workerTable.setShowVerticalLines(
                false
        );

        workerTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        workerTable.setAutoCreateRowSorter(
                true
        );

        workerTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        // =========================================================
        // HEADER
        // =========================================================

        workerTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

        workerTable
                .getTableHeader()
                .setBackground(
                        new Color(
                                241,
                                245,
                                249
                        )
                );

        workerTable
                .getTableHeader()
                .setForeground(
                        COLOR_TEXT_MAIN
                );

        workerTable
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                40
                        )
                );

        workerTable
                .getTableHeader()
                .setBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                COLOR_BORDER
                        )
                );

        // =========================================================
        // COLUMN WIDTHS
        // =========================================================

        int[] widths = {

                60,
                170,
                130,
                190,
                140,
                110,
                120,
                200,
                130
        };

        for (
                int i = 0;
                i < widths.length;
                i++
        ) {

            workerTable
                    .getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                            widths[i]
                    );
        }

        // =========================================================
        // TABLE STRIPING
        // =========================================================

        DefaultTableCellRenderer customRenderer =
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object val,
                            boolean isSel,
                            boolean hasFocus,
                            int row,
                            int column
                    ) {

                        Component comp =
                                super.getTableCellRendererComponent(
                                        table,
                                        val,
                                        isSel,
                                        hasFocus,
                                        row,
                                        column
                                );

                        if (!isSel) {

                            comp.setBackground(
                                    row % 2 == 0
                                            ? Color.WHITE
                                            : new Color(
                                                    250,
                                                    252,
                                                    255
                                            )
                            );
                        }

                        setBorder(
                                BorderFactory.createEmptyBorder(
                                        0,
                                        10,
                                        0,
                                        10
                                )
                        );

                        return comp;
                    }
                };

        for (
                int i = 0;
                i < workerTable.getColumnCount();
                i++
        ) {

            workerTable
                    .getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(
                            customRenderer
                    );
        }

        // =========================================================
        // SCROLL PANE
        // =========================================================

        JScrollPane scrollPane =
                new JScrollPane(
                        workerTable
                );

        scrollPane
                .getViewport()
                .setBackground(
                        Color.WHITE
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        COLOR_BORDER,
                        1
                )
        );

        card.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return card;
    }

    // =============================================================
    // CARD PANEL
    // =============================================================

    private JPanel createCardPanel() {

        return new JPanel() {

            @Override
            protected void paintComponent(
                    Graphics g
            ) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(
                        COLOR_CARD
                );

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        16,
                        16
                );

                g2.setColor(
                        COLOR_BORDER
                );

                g2.drawRoundRect(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1,
                        16,
                        16
                );

                g2.dispose();

                super.paintComponent(g);
            }
        };
    }

    // =============================================================
    // FIELD BLOCK
    // =============================================================

    private JPanel createFieldBlock(
            String labelText,
            Component input
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                0,
                                4
                        )
                );

        panel.setOpaque(false);

        JLabel label =
                new JLabel(
                        labelText
                );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(
                COLOR_TEXT_MUTED
        );

        panel.add(
                label,
                BorderLayout.NORTH
        );

        panel.add(
                input,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =============================================================
    // INPUT STYLE
    // =============================================================

    private void styleInputField(
            JTextField field
    ) {

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        field.setPreferredSize(
                new Dimension(
                        field.getPreferredSize().width,
                        36
                )
        );

        field.setBackground(
                COLOR_INPUT_BG
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COLOR_BORDER,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        field.addFocusListener(
                new FocusAdapter() {

                    @Override
                    public void focusGained(
                            FocusEvent e
                    ) {

                        field.setBackground(
                                Color.WHITE
                        );

                        field.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                COLOR_ACCENT,
                                                1
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                5,
                                                10,
                                                5,
                                                10
                                        )
                                )
                        );
                    }

                    @Override
                    public void focusLost(
                            FocusEvent e
                    ) {

                        field.setBackground(
                                COLOR_INPUT_BG
                        );

                        field.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(
                                                COLOR_BORDER,
                                                1
                                        ),
                                        BorderFactory.createEmptyBorder(
                                                5,
                                                10,
                                                5,
                                                10
                                        )
                                )
                        );
                    }
                }
        );
    }

    // =============================================================
    // PRIMARY BUTTON
    // =============================================================

    private void stylePrimaryButton(
            JButton btn
    ) {

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btn.setForeground(
                Color.WHITE
        );

        btn.setBackground(
                COLOR_ACCENT
        );

        btn.setFocusPainted(
                false
        );

        btn.setBorderPainted(
                false
        );

        btn.setOpaque(
                true
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setPreferredSize(
                new Dimension(
                        155,
                        38
                )
        );

        btn.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                COLOR_ACCENT_HOVER
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                COLOR_ACCENT
                        );
                    }
                }
        );
    }

    // =============================================================
    // SECONDARY BUTTON
    // =============================================================

    private void styleSecondaryActionButton(
            JButton btn
    ) {

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btn.setForeground(
                Color.WHITE
        );

        btn.setBackground(
                new Color(
                        51,
                        65,
                        85
                )
        );

        btn.setFocusPainted(
                false
        );

        btn.setBorderPainted(
                false
        );

        btn.setOpaque(
                true
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setPreferredSize(
                new Dimension(
                        135,
                        38
                )
        );

        btn.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                new Color(
                                        30,
                                        41,
                                        59
                                )
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                new Color(
                                        51,
                                        65,
                                        85
                                )
                        );
                    }
                }
        );
    }

    // =============================================================
    // DELETE BUTTON
    // =============================================================

    private void styleDangerButton(
            JButton btn
    ) {

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btn.setForeground(
                COLOR_DANGER
        );

        btn.setBackground(
                new Color(
                        255,
                        241,
                        242
                )
        );

        btn.setFocusPainted(
                false
        );

        btn.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                254,
                                205,
                                211
                        ),
                        1
                )
        );

        btn.setOpaque(
                true
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setPreferredSize(
                new Dimension(
                        130,
                        38
                )
        );

        btn.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                COLOR_DANGER
                        );

                        btn.setForeground(
                                Color.WHITE
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                new Color(
                                        255,
                                        241,
                                        242
                                )
                        );

                        btn.setForeground(
                                COLOR_DANGER
                        );
                    }
                }
        );
    }

    // =============================================================
    // OUTLINE BUTTON
    // =============================================================

    private void styleOutlineButton(
            JButton btn
    ) {

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        btn.setForeground(
                COLOR_SECONDARY_TEXT
        );

        btn.setBackground(
                COLOR_CARD
        );

        btn.setFocusPainted(
                false
        );

        btn.setBorder(
                BorderFactory.createLineBorder(
                        COLOR_BORDER,
                        1
                )
        );

        btn.setOpaque(
                true
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setPreferredSize(
                new Dimension(
                        110,
                        38
                )
        );

        btn.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                COLOR_SECONDARY_BG
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                COLOR_CARD
                        );
                    }
                }
        );
    }

    // =============================================================
    // SMALL BUTTON
    // =============================================================

    private void styleSmallButton(
            JButton btn,
            Color base,
            Color hover
    ) {

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        btn.setForeground(
                Color.WHITE
        );

        btn.setBackground(
                base
        );

        btn.setFocusPainted(
                false
        );

        btn.setBorderPainted(
                false
        );

        btn.setOpaque(
                true
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setPreferredSize(
                new Dimension(
                        85,
                        36
                )
        );

        btn.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                hover
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                base
                        );
                    }
                }
        );
    }

    // =============================================================
    // SMALL OUTLINE BUTTON
    // =============================================================

    private void styleSmallOutlineButton(
            JButton btn
    ) {

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        btn.setForeground(
                COLOR_SECONDARY_TEXT
        );

        btn.setBackground(
                COLOR_CARD
        );

        btn.setFocusPainted(
                false
        );

        btn.setBorder(
                BorderFactory.createLineBorder(
                        COLOR_BORDER,
                        1
                )
        );

        btn.setOpaque(
                true
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setPreferredSize(
                new Dimension(
                        100,
                        36
                )
        );

        btn.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                COLOR_SECONDARY_BG
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        btn.setBackground(
                                COLOR_CARD
                        );
                    }
                }
        );
    }

    // =============================================================
    // CALENDAR BUTTON STYLE
    // =============================================================

    private void styleCalendarButton(
            JButton btn
    ) {

        btn.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        16
                )
        );

        btn.setForeground(
                COLOR_ACCENT
        );

        btn.setBackground(
                Color.WHITE
        );

        btn.setFocusPainted(
                false
        );

        btn.setBorder(
                BorderFactory.createLineBorder(
                        COLOR_BORDER,
                        1
                )
        );

        btn.setOpaque(
                true
        );

        btn.setPreferredSize(
                new Dimension(
                        48,
                        36
                )
        );

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =============================================================
    // EVENTS
    // =============================================================

    private void initEvents() {

        addButton.addActionListener(
                e -> addWorker()
        );

        updateButton.addActionListener(
                e -> updateWorker()
        );

        deleteButton.addActionListener(
                e -> deleteWorker()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        // =========================================================
        // CALENDAR
        // =========================================================

        calendarButton.addActionListener(
                e -> showCalendar()
        );

        // =========================================================
        // SEARCH
        // =========================================================

        searchButton.addActionListener(
                e -> {

                    String text =
                            searchField
                                    .getText()
                                    .trim();

                    if (text.isEmpty()) {

                        loadWorkers();

                    } else {

                        searchWorker(
                                text
                        );
                    }
                }
        );

        searchField.addActionListener(
                e -> searchButton.doClick()
        );

        // =========================================================
        // RESET
        // =========================================================

        viewButton.addActionListener(
                e -> {

                    searchField.setText("");

                    loadWorkers();
                }
        );

        // =========================================================
        // TABLE SELECTION
        // =========================================================

        workerTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (
                                    !e.getValueIsAdjusting()
                            ) {

                                int row =
                                        workerTable
                                                .getSelectedRow();

                                if (row != -1) {

                                    int modelRow =
                                            workerTable
                                                    .convertRowIndexToModel(
                                                            row
                                                    );

                                    loadSelectedWorker(
                                            modelRow
                                    );
                                }
                            }
                        }
                );
    }

    // =============================================================
    // CALENDAR POPUP
    // =============================================================

    private void showCalendar() {

        Calendar selectedCalendar =
                Calendar.getInstance();

        // =========================================================
        // IF DATE ALREADY EXISTS
        // =========================================================

        String existingDate =
                joiningDateField
                        .getText()
                        .trim();

        if (!existingDate.isEmpty()) {

            try {

                SimpleDateFormat format =
                        new SimpleDateFormat(
                                "yyyy-MM-dd"
                        );

                selectedCalendar.setTime(
                        format.parse(
                                existingDate
                        )
                );

            } catch (Exception ex) {

                selectedCalendar =
                        Calendar.getInstance();
            }
        }

        final Calendar calendar =
                selectedCalendar;

        JDialog dialog =
                new JDialog(
                        this,
                        "Select Joining Date",
                        true
                );

        dialog.setSize(
                380,
                390
        );

        dialog.setResizable(false);

        dialog.setLocationRelativeTo(
                this
        );

        dialog.setLayout(
                new BorderLayout()
        );

        // =========================================================
        // CALENDAR HEADER
        // =========================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                COLOR_PRIMARY
        );

        JButton previousButton =
                new JButton(
                        "◀"
                );

        JButton nextButton =
                new JButton(
                        "▶"
                );

        JLabel monthLabel =
                new JLabel(
                        "",
                        SwingConstants.CENTER
                );

        monthLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        monthLabel.setForeground(
                Color.WHITE
        );

        previousButton.setForeground(
                Color.WHITE
        );

        previousButton.setBackground(
                COLOR_PRIMARY
        );

        previousButton.setBorderPainted(
                false
        );

        previousButton.setFocusPainted(
                false
        );

        nextButton.setForeground(
                Color.WHITE
        );

        nextButton.setBackground(
                COLOR_PRIMARY
        );

        nextButton.setBorderPainted(
                false
        );

        nextButton.setFocusPainted(
                false
        );

        header.add(
                previousButton,
                BorderLayout.WEST
        );

        header.add(
                monthLabel,
                BorderLayout.CENTER
        );

        header.add(
                nextButton,
                BorderLayout.EAST
        );

        dialog.add(
                header,
                BorderLayout.NORTH
        );

        // =========================================================
        // CALENDAR CENTER
        // =========================================================

        JPanel calendarPanel =
                new JPanel(
                        new BorderLayout()
                );

        calendarPanel.setBackground(
                Color.WHITE
        );

        JPanel daysHeader =
                new JPanel(
                        new GridLayout(
                                1,
                                7
                        )
                );

        daysHeader.setBackground(
                new Color(
                        241,
                        245,
                        249
                )
        );

        String[] dayNames = {

                "Sun",
                "Mon",
                "Tue",
                "Wed",
                "Thu",
                "Fri",
                "Sat"
        };

        for (
                String day :
                dayNames
        ) {

            JLabel dayLabel =
                    new JLabel(
                            day,
                            SwingConstants.CENTER
                    );

            dayLabel.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            12
                    )
            );

            dayLabel.setForeground(
                    COLOR_TEXT_MAIN
            );

            daysHeader.add(
                    dayLabel
            );
        }

        calendarPanel.add(
                daysHeader,
                BorderLayout.NORTH
        );

        JPanel daysPanel =
                new JPanel(
                        new GridLayout(
                                6,
                                7,
                                2,
                                2
                        )
                );

        daysPanel.setBackground(
                Color.WHITE
        );

        calendarPanel.add(
                daysPanel,
                BorderLayout.CENTER
        );

        dialog.add(
                calendarPanel,
                BorderLayout.CENTER
        );

        // =========================================================
        // UPDATE CALENDAR
        // =========================================================

        Runnable updateCalendar =
                () -> {

                    daysPanel.removeAll();

                    int year =
                            calendar.get(
                                    Calendar.YEAR
                            );

                    int month =
                            calendar.get(
                                    Calendar.MONTH
                            );

                    monthLabel.setText(
                            new SimpleDateFormat(
                                    "MMMM yyyy"
                            ).format(
                                    calendar.getTime()
                            )
                    );

                    Calendar firstDay =
                            (Calendar)
                                    calendar.clone();

                    firstDay.set(
                            Calendar.DAY_OF_MONTH,
                            1
                    );

                    int firstDayPosition =
                            firstDay.get(
                                    Calendar.DAY_OF_WEEK
                            ) - 1;

                    int daysInMonth =
                            calendar.getActualMaximum(
                                    Calendar.DAY_OF_MONTH
                            );

                    // Empty spaces before first day
                    for (
                            int i = 0;
                            i < firstDayPosition;
                            i++
                    ) {

                        daysPanel.add(
                                new JLabel()
                        );
                    }

                    // =================================================
                    // DAYS
                    // =================================================

                    for (
                            int day = 1;
                            day <= daysInMonth;
                            day++
                    ) {

                        final int selectedDay =
                                day;

                        JButton dayButton =
                                new JButton(
                                        String.valueOf(
                                                day
                                        )
                                );

                        dayButton.setFont(
                                new Font(
                                        "Segoe UI",
                                        Font.PLAIN,
                                        13
                                )
                        );

                        dayButton.setFocusPainted(
                                false
                        );

                        dayButton.setBorderPainted(
                                false
                        );

                        dayButton.setBackground(
                                Color.WHITE
                        );

                        dayButton.setForeground(
                                COLOR_TEXT_MAIN
                        );

                        dayButton.setCursor(
                                new Cursor(
                                        Cursor.HAND_CURSOR
                                )
                        );

                        // Highlight current selected day
                        if (
                                calendar.get(
                                        Calendar.DAY_OF_MONTH
                                ) == day
                        ) {

                            dayButton.setBackground(
                                    new Color(
                                            219,
                                            234,
                                            254
                                    )
                            );

                            dayButton.setForeground(
                                    COLOR_ACCENT
                            );

                            dayButton.setFont(
                                    new Font(
                                            "Segoe UI",
                                            Font.BOLD,
                                            13
                                    )
                            );
                        }

                        dayButton.addActionListener(
                                e -> {

                                    calendar.set(
                                            Calendar.DAY_OF_MONTH,
                                            selectedDay
                                    );

                                    SimpleDateFormat format =
                                            new SimpleDateFormat(
                                                    "yyyy-MM-dd"
                                            );

                                    joiningDateField.setText(
                                            format.format(
                                                    calendar.getTime()
                                            )
                                    );

                                    dialog.dispose();
                                }
                        );

                        daysPanel.add(
                                dayButton
                        );
                    }

                    // Fill remaining cells
                    int totalCells =
                            firstDayPosition
                                    + daysInMonth;

                    for (
                            int i = totalCells;
                            i < 42;
                            i++
                    ) {

                        daysPanel.add(
                                new JLabel()
                        );
                    }

                    daysPanel.revalidate();

                    daysPanel.repaint();
                };

        // =========================================================
        // PREVIOUS MONTH
        // =========================================================

        previousButton.addActionListener(
                e -> {

                    calendar.add(
                            Calendar.MONTH,
                            -1
                    );

                    updateCalendar.run();
                }
        );

        // =========================================================
        // NEXT MONTH
        // =========================================================

        nextButton.addActionListener(
                e -> {

                    calendar.add(
                            Calendar.MONTH,
                            1
                    );

                    updateCalendar.run();
                }
        );

        // =========================================================
        // INITIAL CALENDAR
        // =========================================================

        updateCalendar.run();

        dialog.setVisible(true);
    }

    // =============================================================
    // ADD WORKER
    // =============================================================

    private void addWorker() {

        String name =
                nameField
                        .getText()
                        .trim();

        String mobile =
                mobileField
                        .getText()
                        .trim();

        String email =
                emailField
                        .getText()
                        .trim();

        String jobRole =
                jobRoleField
                        .getText()
                        .trim();

        String salary =
                salaryField
                        .getText()
                        .trim();

        String joiningDate =
                joiningDateField
                        .getText()
                        .trim();

        String address =
                addressField
                        .getText()
                        .trim();

        String username =
                usernameField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField
                                .getPassword()
                ).trim();

        // =========================================================
        // VALIDATION
        // =========================================================

        if (
                name.isEmpty()
                        ||
                mobile.isEmpty()
                        ||
                jobRole.isEmpty()
                        ||
                salary.isEmpty()
                        ||
                joiningDate.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all required fields "
                            + "(Name, Mobile, Job Role, Salary, and Joining Date).",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // =========================================================
        // SQL
        // =========================================================

        String sql =
                "INSERT INTO workers "
                        + "(name, mobile, email, job_role, salary, "
                        + "joining_date, address, username, password) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    name
            );

            statement.setString(
                    2,
                    mobile
            );

            statement.setString(
                    3,
                    email
            );

            statement.setString(
                    4,
                    jobRole
            );

            statement.setDouble(
                    5,
                    Double.parseDouble(
                            salary
                    )
            );

            statement.setDate(
                    6,
                    Date.valueOf(
                            joiningDate
                    )
            );

            statement.setString(
                    7,
                    address
            );

            statement.setString(
                    8,
                    username
            );

            statement.setString(
                    9,
                    password
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Worker registered successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();

            loadWorkers();

        } catch (
                NumberFormatException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Salary must be a valid numerical value.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (
                IllegalArgumentException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Joining Date.",
                    "Format Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (
                Exception ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error adding worker: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =============================================================
    // UPDATE WORKER
    // =============================================================

    private void updateWorker() {

        int row =
                workerTable
                        .getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a worker from the table to update.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                workerTable
                        .convertRowIndexToModel(
                                row
                        );

        int workerId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                )
                                .toString()
                );

        String sql =
                "UPDATE workers SET "
                        + "name=?, "
                        + "mobile=?, "
                        + "email=?, "
                        + "job_role=?, "
                        + "salary=?, "
                        + "joining_date=?, "
                        + "address=?, "
                        + "username=?, "
                        + "password=? "
                        + "WHERE worker_id=?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setString(
                    1,
                    nameField
                            .getText()
                            .trim()
            );

            statement.setString(
                    2,
                    mobileField
                            .getText()
                            .trim()
            );

            statement.setString(
                    3,
                    emailField
                            .getText()
                            .trim()
            );

            statement.setString(
                    4,
                    jobRoleField
                            .getText()
                            .trim()
            );

            statement.setDouble(
                    5,
                    Double.parseDouble(
                            salaryField
                                    .getText()
                                    .trim()
                    )
            );

            statement.setDate(
                    6,
                    Date.valueOf(
                            joiningDateField
                                    .getText()
                                    .trim()
                    )
            );

            statement.setString(
                    7,
                    addressField
                            .getText()
                            .trim()
            );

            statement.setString(
                    8,
                    usernameField
                            .getText()
                            .trim()
            );

            statement.setString(
                    9,
                    new String(
                            passwordField
                                    .getPassword()
                    ).trim()
            );

            statement.setInt(
                    10,
                    workerId
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Worker record updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();

            loadWorkers();

        } catch (
                NumberFormatException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Salary must be a valid number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (
                IllegalArgumentException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Joining Date must be selected using the calendar.",
                    "Format Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (
                Exception ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error updating worker: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =============================================================
    // DELETE WORKER
    // =============================================================

    private void deleteWorker() {

        int row =
                workerTable
                        .getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a worker to delete.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                workerTable
                        .convertRowIndexToModel(
                                row
                        );

        int workerId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                )
                                .toString()
                );

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete worker \""
                                + getTableValue(
                                        modelRow,
                                        1
                                )
                                + "\"?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (
                choice !=
                        JOptionPane.YES_OPTION
        ) {

            return;
        }

        String sql =
                "DELETE FROM workers "
                        + "WHERE worker_id=?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    workerId
            );

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Worker deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();

            loadWorkers();

        } catch (
                Exception ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cannot delete worker: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // =============================================================
    // LOAD WORKERS
    // =============================================================

    public void loadWorkers() {

        tableModel.setRowCount(0);

        String sql =
                "SELECT * FROM workers "
                        + "ORDER BY worker_id DESC";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        );

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (
                    resultSet.next()
            ) {

                tableModel.addRow(
                        new Object[] {

                                resultSet.getInt(
                                        "worker_id"
                                ),

                                resultSet.getString(
                                        "name"
                                ),

                                resultSet.getString(
                                        "mobile"
                                ),

                                resultSet.getString(
                                        "email"
                                ),

                                resultSet.getString(
                                        "job_role"
                                ),

                                resultSet.getDouble(
                                        "salary"
                                ),

                                resultSet.getDate(
                                        "joining_date"
                                ),

                                resultSet.getString(
                                        "address"
                                ),

                                resultSet.getString(
                                        "username"
                                )
                        }
                );
            }

        } catch (
                Exception ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading workers: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =============================================================
    // SEARCH WORKER
    // =============================================================

    public void searchWorker(
            String searchText
    ) {

        tableModel.setRowCount(0);

        String sql =
                "SELECT * FROM workers "
                        + "WHERE name LIKE ? "
                        + "OR mobile LIKE ? "
                        + "OR job_role LIKE ? "
                        + "OR username LIKE ? "
                        + "ORDER BY worker_id DESC";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            String value =
                    "%" + searchText + "%";

            statement.setString(
                    1,
                    value
            );

            statement.setString(
                    2,
                    value
            );

            statement.setString(
                    3,
                    value
            );

            statement.setString(
                    4,
                    value
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (
                        resultSet.next()
                ) {

                    tableModel.addRow(
                            new Object[] {

                                    resultSet.getInt(
                                            "worker_id"
                                    ),

                                    resultSet.getString(
                                            "name"
                                    ),

                                    resultSet.getString(
                                            "mobile"
                                    ),

                                    resultSet.getString(
                                            "email"
                                    ),

                                    resultSet.getString(
                                            "job_role"
                                    ),

                                    resultSet.getDouble(
                                            "salary"
                                    ),

                                    resultSet.getDate(
                                            "joining_date"
                                    ),

                                    resultSet.getString(
                                            "address"
                                    ),

                                    resultSet.getString(
                                            "username"
                                    )
                            }
                    );
                }
            }

        } catch (
                Exception ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search error: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =============================================================
    // LOAD SELECTED WORKER
    // =============================================================

    private void loadSelectedWorker(
            int row
    ) {

        nameField.setText(
                getTableValue(
                        row,
                        1
                )
        );

        mobileField.setText(
                getTableValue(
                        row,
                        2
                )
        );

        emailField.setText(
                getTableValue(
                        row,
                        3
                )
        );

        jobRoleField.setText(
                getTableValue(
                        row,
                        4
                )
        );

        salaryField.setText(
                getTableValue(
                        row,
                        5
                )
        );

        joiningDateField.setText(
                getTableValue(
                        row,
                        6
                )
        );

        addressField.setText(
                getTableValue(
                        row,
                        7
                )
        );

        usernameField.setText(
                getTableValue(
                        row,
                        8
                )
        );

        int workerId =
                Integer.parseInt(
                        getTableValue(
                                row,
                                0
                        )
                );

        loadPassword(
                workerId
        );
    }

    // =============================================================
    // LOAD PASSWORD
    // =============================================================

    private void loadPassword(
            int workerId
    ) {

        String sql =
                "SELECT password "
                        + "FROM workers "
                        + "WHERE worker_id=?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql
                        )
        ) {

            statement.setInt(
                    1,
                    workerId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (
                        resultSet.next()
                ) {

                    passwordField.setText(
                            resultSet.getString(
                                    "password"
                            )
                    );
                }
            }

        } catch (
                Exception ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading password: "
                            + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =============================================================
    // GET TABLE VALUE
    // =============================================================

    private String getTableValue(
            int row,
            int column
    ) {

        Object value =
                tableModel.getValueAt(
                        row,
                        column
                );

        return value == null
                ? ""
                : value.toString();
    }

    // =============================================================
    // CLEAR FIELDS
    // =============================================================

    public void clearFields() {

        nameField.setText("");

        mobileField.setText("");

        emailField.setText("");

        jobRoleField.setText("");

        salaryField.setText("");

        joiningDateField.setText("");

        addressField.setText("");

        usernameField.setText("");

        passwordField.setText("");

        searchField.setText("");

        workerTable.clearSelection();
    }

    // =============================================================
    // MAIN METHOD
    // =============================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    new WorkerManagement()
                            .setVisible(true);
                }
        );
    }
}