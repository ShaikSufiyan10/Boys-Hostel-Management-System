package com.hostel.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.hostel.database.DBConnection;
import com.hostel.resident.ResidentDashboard;

public class ResidentLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    // Cohesive Modern Enterprise Palette
    private static final Color COLOR_PRIMARY = new Color(24, 43, 73);      // Deep Navy
    private static final Color COLOR_ACCENT = new Color(37, 99, 235);      // Royal Blue
    private static final Color COLOR_ACCENT_HOVER = new Color(29, 78, 216);
    private static final Color COLOR_BG = new Color(248, 250, 252);        // Slate-50 Canvas
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_BORDER = new Color(226, 232, 240);
    private static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);
    private static final Color COLOR_INPUT_BG = new Color(248, 250, 252);

    public ResidentLogin() {
        setTitle("Boys Hostel Management System - Resident Login");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main 2-Panel Split Canvas
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(createLeftBrandPanel(), BorderLayout.WEST);
        rootPanel.add(createRightFormPanel(), BorderLayout.CENTER);

        add(rootPanel);
        setupEvents();
    }

    // -------------------------------------------------------------
    // LEFT PANEL: Visual Identity & Resident Context
    // -------------------------------------------------------------
    private JPanel createLeftBrandPanel() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(320, 520));
        leftPanel.setBackground(COLOR_PRIMARY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 30, 8, 30);

        JLabel tagLabel = new JLabel("STUDENT & RESIDENT ACCESS");
        tagLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tagLabel.setForeground(new Color(147, 197, 253));

        JLabel titleLabel = new JLabel("<html><b>RESIDENT</b><br>PORTAL</html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JLabel descLabel = new JLabel(
            "<html>Access room allocations, mess schedules, fee payments, and raise maintenance complaints.</html>"
        );
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(203, 213, 225));

        JLabel securityBadge = new JLabel("<html>🔒 Student Self-Service Hub</html>");
        securityBadge.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        securityBadge.setForeground(new Color(148, 163, 184));

        gbc.gridy = 0;
        leftPanel.add(tagLabel, gbc);
        gbc.gridy = 1;
        leftPanel.add(titleLabel, gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(12, 30, 40, 30);
        leftPanel.add(descLabel, gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(40, 30, 8, 30);
        leftPanel.add(securityBadge, gbc);

        return leftPanel;
    }

    // -------------------------------------------------------------
    // RIGHT PANEL: Floating Form Card
    // -------------------------------------------------------------
    private JPanel createRightFormPanel() {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(COLOR_BG);

        // Elevated Rounded Card Box
        JPanel cardPanel = new JPanel() {
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
        cardPanel.setOpaque(false);
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setPreferredSize(new Dimension(420, 430));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 32, 20, 32));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        // Card Title & Subtitle
        JLabel welcomeTitle = new JLabel("Sign In as Resident", SwingConstants.LEFT);
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeTitle.setForeground(COLOR_PRIMARY);

        JLabel welcomeSubtitle = new JLabel("Enter your student credentials below", SwingConstants.LEFT);
        welcomeSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        welcomeSubtitle.setForeground(COLOR_TEXT_MUTED);

        // Form Fields
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(COLOR_PRIMARY);

        usernameField = new JTextField();
        styleInputField(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(COLOR_PRIMARY);

        passwordField = new JPasswordField();
        styleInputField(passwordField);

        // Actions
        loginButton = new JButton("Log In to Portal");
        backButton = new JButton("← Back to Main Menu");

        stylePrimaryButton(loginButton, COLOR_ACCENT, COLOR_ACCENT_HOVER);
        styleTextLinkButton(backButton);

        // Add to Card with GridBag layout
        c.gridy = 0;
        c.insets = new Insets(0, 0, 2, 0);
        cardPanel.add(welcomeTitle, c);

        c.gridy = 1;
        c.insets = new Insets(0, 0, 18, 0);
        cardPanel.add(welcomeSubtitle, c);

        c.gridy = 2;
        c.insets = new Insets(0, 0, 4, 0);
        cardPanel.add(usernameLabel, c);

        c.gridy = 3;
        c.insets = new Insets(0, 0, 12, 0);
        cardPanel.add(usernameField, c);

        c.gridy = 4;
        c.insets = new Insets(0, 0, 4, 0);
        cardPanel.add(passwordLabel, c);

        c.gridy = 5;
        c.insets = new Insets(0, 0, 20, 0);
        cardPanel.add(passwordField, c);

        c.gridy = 6;
        c.insets = new Insets(0, 0, 10, 0);
        cardPanel.add(loginButton, c);

        c.gridy = 7;
        c.insets = new Insets(0, 0, 0, 0);
        cardPanel.add(backButton, c);

        rightPanel.add(cardPanel);
        return rightPanel;
    }

    // -------------------------------------------------------------
    // STYLING HELPERS
    // -------------------------------------------------------------
    private void styleInputField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(320, 40));
        field.setBackground(COLOR_INPUT_BG);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        // Focus ring styling
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

    private void stylePrimaryButton(JButton btn, Color baseColor, Color hoverColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(baseColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(320, 42));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });
    }

    private void styleTextLinkButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(COLOR_TEXT_MUTED);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(COLOR_PRIMARY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(COLOR_TEXT_MUTED);
            }
        });
    }

    // -------------------------------------------------------------
    // EVENT & DATABASE LOGIC
    // -------------------------------------------------------------
    private void setupEvents() {
        loginButton.addActionListener(e -> handleLogin());
        
        // Enter key in password triggers login
        passwordField.addActionListener(e -> handleLogin());

        backButton.addActionListener(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
            dispose();
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter both username and password.",
                "Login",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = "SELECT * FROM residents WHERE username = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Update user session
                    Session.setUser(
                        resultSet.getInt("resident_id"),
                        resultSet.getString("name"),
                        "RESIDENT"
                    );

                    JOptionPane.showMessageDialog(
                        this,
                        "Resident Login Successful!",
                        "Login",
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    ResidentDashboard residentDashboard = new ResidentDashboard();
                    residentDashboard.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Database Error: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ResidentLogin().setVisible(true);
        });
    }
}