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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.hostel.database.DBConnection;
import com.hostel.login.Session;

public class MyProfile extends JFrame {

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

    // Profile Card Header Labels
    private JLabel avatarLabel;
    private JLabel nameHeaderLabel;
    private JLabel roleHeaderLabel;

    // Fields
    private JTextField nameField;
    private JTextField mobileField;
    private JTextField emailField;
    private JTextField usernameField;
    private JTextField genderField;
    private JTextField ageField;
    private JTextField addressField;
    private JTextField joiningDateField;

    private JButton closeButton;

    public MyProfile() {
        setTitle("Boys Hostel Management System - My Profile");
        setSize(850, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Canvas
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createMainContentArea(), BorderLayout.CENTER);

        add(rootPanel);

        initEvents();
        loadProfile();
    }

    // -------------------------------------------------------------
    // 1. TOP APP BAR
    // -------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(850, 72));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // Left Branding Info
        JPanel leftBrand = new JPanel(new BorderLayout(0, 3));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("RESIDENT ACCOUNT SETTINGS");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Personal Profile Dossier");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        // Right Verification Pill
        JPanel rightBadge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightBadge.setOpaque(false);

        JLabel badge = new JLabel("● Verified Resident") {
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
        container.setBorder(BorderFactory.createEmptyBorder(20, 28, 28, 28));

        // Profile Identity Header Card
        container.add(createIdentitySummaryCard());
        container.add(Box.createVerticalStrut(18));

        // Profile Details Form Card
        container.add(createProfileFormCard());
        container.add(Box.createVerticalStrut(18));

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
    // 3. IDENTITY SUMMARY CARD
    // -------------------------------------------------------------
    private JPanel createIdentitySummaryCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));

        // Left Avatar Icon
        avatarLabel = new JLabel("👤", SwingConstants.CENTER) {
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
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        avatarLabel.setPreferredSize(new Dimension(56, 56));

        // Middle Text Info
        JPanel textInfo = new JPanel(new BorderLayout(0, 3));
        textInfo.setOpaque(false);

        nameHeaderLabel = new JLabel("Resident Name");
        nameHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameHeaderLabel.setForeground(COLOR_PRIMARY);

        roleHeaderLabel = new JLabel("Official Hostel Resident Account");
        roleHeaderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleHeaderLabel.setForeground(COLOR_TEXT_MUTED);

        textInfo.add(nameHeaderLabel, BorderLayout.NORTH);
        textInfo.add(roleHeaderLabel, BorderLayout.CENTER);

        // Right ID Status Badge
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
        right.setOpaque(false);

        JLabel statusPill = new JLabel("Active Student Profile");
        statusPill.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusPill.setForeground(COLOR_ACCENT);
        statusPill.setBackground(new Color(239, 246, 255));
        statusPill.setOpaque(true);
        statusPill.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254), 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        right.add(statusPill);

        card.add(avatarLabel, BorderLayout.WEST);
        card.add(textInfo, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    // -------------------------------------------------------------
    // 4. PROFILE FORM CARD
    // -------------------------------------------------------------
    private JPanel createProfileFormCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 16));
        card.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Registered Credentials & Contact Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_TEXT_MAIN);
        card.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.weightx = 0.5;

        // Row 0: Full Name & System Username
        nameField = createReadOnlyField();
        gbc.gridx = 0; gbc.gridy = 0;
        grid.add(createFieldBlock("Full Name", nameField), gbc);

        usernameField = createReadOnlyField();
        gbc.gridx = 1; gbc.gridy = 0;
        grid.add(createFieldBlock("System Username", usernameField), gbc);

        // Row 1: Mobile & Email
        mobileField = createReadOnlyField();
        gbc.gridx = 0; gbc.gridy = 1;
        grid.add(createFieldBlock("Mobile Contact", mobileField), gbc);

        emailField = createReadOnlyField();
        gbc.gridx = 1; gbc.gridy = 1;
        grid.add(createFieldBlock("Email Address", emailField), gbc);

        // Row 2: Gender & Age
        genderField = createReadOnlyField();
        gbc.gridx = 0; gbc.gridy = 2;
        grid.add(createFieldBlock("Gender", genderField), gbc);

        ageField = createReadOnlyField();
        gbc.gridx = 1; gbc.gridy = 2;
        grid.add(createFieldBlock("Age", ageField), gbc);

        // Row 3: Joining Date & Permanent Address
        joiningDateField = createReadOnlyField();
        gbc.gridx = 0; gbc.gridy = 3;
        grid.add(createFieldBlock("Joining Date", joiningDateField), gbc);

        addressField = createReadOnlyField();
        gbc.gridx = 1; gbc.gridy = 3;
        grid.add(createFieldBlock("Permanent Address", addressField), gbc);

        card.add(grid, BorderLayout.CENTER);
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

    // -------------------------------------------------------------
    // LOGIC & EVENTS
    // -------------------------------------------------------------
    private void initEvents() {
        closeButton.addActionListener(e -> dispose());
    }

    private void loadProfile() {
        int residentId = 0;
        try {
            residentId = Session.getUserId();
        } catch (Throwable ignored) {}

        String sql = "SELECT * FROM residents WHERE resident_id=?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, residentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");

                    nameField.setText(name);
                    nameHeaderLabel.setText(name);

                    usernameField.setText(username);
                    roleHeaderLabel.setText("Username: @" + username + "  •  ID #" + resultSet.getInt("resident_id"));

                    mobileField.setText(resultSet.getString("mobile"));
                    emailField.setText(resultSet.getString("email"));
                    genderField.setText(resultSet.getString("gender"));
                    ageField.setText(String.valueOf(resultSet.getInt("age")));
                    addressField.setText(resultSet.getString("address"));

                    if (resultSet.getDate("joining_date") != null) {
                        joiningDateField.setText(resultSet.getDate("joining_date").toString());
                    } else {
                        joiningDateField.setText("N/A");
                    }
                } else {
                    nameHeaderLabel.setText("Resident Record Not Found");
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Unable to load profile data: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MyProfile().setVisible(true);
        });
    }
}