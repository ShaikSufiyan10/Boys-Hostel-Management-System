package com.hostel.login;

import com.hostel.owner.OwnerDashboard;
import com.hostel.resident.ResidentDashboard;
import com.hostel.database.DBConnection;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JRadioButton ownerRadio;
    private JRadioButton residentRadio;

    private JButton loginButton;
    private JButton exitButton;

    private JLabel selectedRoleLabel;

    // ============================================================
    // COLORS
    // ============================================================

    private static final Color COLOR_PRIMARY =
            new Color(24, 43, 73);

    private static final Color COLOR_ACCENT =
            new Color(37, 99, 235);

    private static final Color COLOR_ACCENT_HOVER =
            new Color(29, 78, 216);

    private static final Color COLOR_SECONDARY_BG =
            new Color(241, 245, 249);

    private static final Color COLOR_SECONDARY_HOVER =
            new Color(226, 232, 240);

    private static final Color COLOR_BG =
            new Color(248, 250, 252);

    private static final Color COLOR_CARD =
            Color.WHITE;

    private static final Color COLOR_TEXT_MUTED =
            new Color(100, 116, 139);

    private static final Color COLOR_DANGER =
            new Color(225, 29, 72);

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public LoginPage() {

        setTitle("Boys Hostel Management System");

        setSize(850, 560);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);

        JPanel rootPanel = new JPanel(new BorderLayout());

        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(
                createLeftBrandPanel(),
                BorderLayout.WEST
        );

        rootPanel.add(
                createRightActionPanel(),
                BorderLayout.CENTER
        );

        add(rootPanel);

        setupEvents();
    }

    // ============================================================
    // LEFT BRAND PANEL
    // ============================================================

    private JPanel createLeftBrandPanel() {

        JPanel leftPanel =
                new JPanel(new GridBagLayout());

        leftPanel.setPreferredSize(
                new Dimension(320, 560)
        );

        leftPanel.setBackground(COLOR_PRIMARY);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(8, 30, 8, 30);

        // --------------------------------------------------------
        // PORTAL ACCESS
        // --------------------------------------------------------

        JLabel tagLabel =
                new JLabel("PORTAL ACCESS");

        tagLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        tagLabel.setForeground(
                new Color(147, 197, 253)
        );

        // --------------------------------------------------------
        // TITLE
        // --------------------------------------------------------

        JLabel titleLabel =
                new JLabel(
                        "<html><b>BOYS HOSTEL</b>"
                        + "<br>MANAGEMENT</html>"
                );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        titleLabel.setForeground(
                Color.WHITE
        );

        // --------------------------------------------------------
        // DESCRIPTION
        // --------------------------------------------------------

        JLabel descLabel =
                new JLabel(
                        "<html>"
                        + "Secure access for hostel "
                        + "administration and residents."
                        + "<br><br>"
                        + "Manage rooms, residents, workers,"
                        + "<br>"
                        + "fees, feedback and more."
                        + "</html>"
                );

        descLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        descLabel.setForeground(
                new Color(203, 213, 225)
        );

        // --------------------------------------------------------
        // COPYRIGHT
        // --------------------------------------------------------

        JLabel copyrightLabel =
                new JLabel(
                        "© 2026 Hostel System • v2.4"
                );

        copyrightLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        copyrightLabel.setForeground(
                new Color(148, 163, 184)
        );

        // --------------------------------------------------------
        // ADD COMPONENTS
        // --------------------------------------------------------

        gbc.gridy = 0;

        leftPanel.add(
                tagLabel,
                gbc
        );

        gbc.gridy = 1;

        leftPanel.add(
                titleLabel,
                gbc
        );

        gbc.gridy = 2;

        gbc.insets =
                new Insets(12, 30, 40, 30);

        leftPanel.add(
                descLabel,
                gbc
        );

        gbc.gridy = 3;

        gbc.insets =
                new Insets(80, 30, 8, 30);

        leftPanel.add(
                copyrightLabel,
                gbc
        );

        return leftPanel;
    }

    // ============================================================
    // RIGHT ACTION PANEL
    // ============================================================

    private JPanel createRightActionPanel() {

        JPanel rightPanel =
                new JPanel(new GridBagLayout());

        rightPanel.setBackground(COLOR_BG);

        // --------------------------------------------------------
        // CARD
        // --------------------------------------------------------

        JPanel cardPanel =
                new JPanel() {

                    @Override
                    protected void paintComponent(Graphics g) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(COLOR_CARD);

                        g2.fillRoundRect(
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                16,
                                16
                        );

                        g2.setColor(
                                new Color(226, 232, 240)
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

        cardPanel.setOpaque(false);

        cardPanel.setLayout(
                new GridBagLayout()
        );

        cardPanel.setPreferredSize(
                new Dimension(390, 490)
        );

        cardPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        28,
                        20,
                        28
                )
        );

        GridBagConstraints c =
                new GridBagConstraints();

        c.gridx = 0;

        c.fill =
                GridBagConstraints.HORIZONTAL;

        c.insets =
                new Insets(5, 0, 5, 0);

        // ========================================================
        // WELCOME TITLE
        // ========================================================

        JLabel welcomeTitle =
                new JLabel(
                        "Welcome Back",
                        SwingConstants.CENTER
                );

        welcomeTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        welcomeTitle.setForeground(
                COLOR_PRIMARY
        );

        // ========================================================
        // SUBTITLE
        // ========================================================

        JLabel welcomeSubtitle =
                new JLabel(
                        "Select your account type and login",
                        SwingConstants.CENTER
                );

        welcomeSubtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        welcomeSubtitle.setForeground(
                COLOR_TEXT_MUTED
        );

        // ========================================================
        // LOGIN AS LABEL
        // ========================================================

        JLabel loginAsLabel =
                new JLabel("Login As");

        loginAsLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        loginAsLabel.setForeground(
                COLOR_PRIMARY
        );

        // ========================================================
        // RADIO BUTTONS
        // ========================================================

        ownerRadio =
                new JRadioButton(
                        "Owner / Admin"
                );

        residentRadio =
                new JRadioButton(
                        "Resident"
                );

        ownerRadio.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        residentRadio.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        ownerRadio.setForeground(
                COLOR_PRIMARY
        );

        residentRadio.setForeground(
                COLOR_PRIMARY
        );

        ownerRadio.setBackground(
                COLOR_CARD
        );

        residentRadio.setBackground(
                COLOR_CARD
        );

        ownerRadio.setFocusPainted(false);

        residentRadio.setFocusPainted(false);

        // Owner selected by default
        ownerRadio.setSelected(true);

        ButtonGroup roleGroup =
                new ButtonGroup();

        roleGroup.add(ownerRadio);

        roleGroup.add(residentRadio);

        // --------------------------------------------------------
        // RADIO PANEL
        // --------------------------------------------------------

        JPanel rolePanel =
                new JPanel();

        rolePanel.setBackground(
                COLOR_CARD
        );

        rolePanel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints roleC =
                new GridBagConstraints();

        roleC.gridy = 0;

        roleC.gridx = 0;

        roleC.anchor =
                GridBagConstraints.WEST;

        roleC.insets =
                new Insets(0, 0, 0, 15);

        rolePanel.add(
                ownerRadio,
                roleC
        );

        roleC.gridx = 1;

        roleC.insets =
                new Insets(0, 0, 0, 0);

        rolePanel.add(
                residentRadio,
                roleC
        );

        // ========================================================
        // SELECTED ROLE LABEL
        // ========================================================

        selectedRoleLabel =
                new JLabel(
                        "Selected: Owner / Admin",
                        SwingConstants.CENTER
                );

        selectedRoleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        selectedRoleLabel.setForeground(
                COLOR_ACCENT
        );

        // ========================================================
        // USERNAME LABEL
        // ========================================================

        JLabel usernameLabel =
                new JLabel("Username");

        usernameLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        usernameLabel.setForeground(
                COLOR_PRIMARY
        );

        // ========================================================
        // USERNAME FIELD
        // ========================================================

        usernameField =
                new JTextField();

        usernameField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        usernameField.setPreferredSize(
                new Dimension(300, 40)
        );

        usernameField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(203, 213, 225)
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        // ========================================================
        // PASSWORD LABEL
        // ========================================================

        JLabel passwordLabel =
                new JLabel("Password");

        passwordLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        passwordLabel.setForeground(
                COLOR_PRIMARY
        );

        // ========================================================
        // PASSWORD FIELD
        // ========================================================

        passwordField =
                new JPasswordField();

        passwordField.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        passwordField.setPreferredSize(
                new Dimension(300, 40)
        );

        passwordField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(203, 213, 225)
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        // ========================================================
        // LOGIN BUTTON
        // ========================================================

        loginButton =
                new JButton("Login");

        stylePrimaryButton(
                loginButton,
                COLOR_ACCENT,
                COLOR_ACCENT_HOVER
        );

        // ========================================================
        // EXIT BUTTON
        // ========================================================

        exitButton =
                new JButton("Exit Application");

        styleTextLinkButton(
                exitButton
        );

        // ========================================================
        // PLACE COMPONENTS
        // ========================================================

        c.gridy = 0;

        c.insets =
                new Insets(3, 0, 2, 0);

        cardPanel.add(
                welcomeTitle,
                c
        );

        c.gridy = 1;

        c.insets =
                new Insets(0, 0, 8, 0);

        cardPanel.add(
                welcomeSubtitle,
                c
        );

        // Login As
        c.gridy = 2;

        c.insets =
                new Insets(5, 0, 2, 0);

        cardPanel.add(
                loginAsLabel,
                c
        );

        // Radio buttons
        c.gridy = 3;

        c.insets =
                new Insets(2, 0, 2, 0);

        cardPanel.add(
                rolePanel,
                c
        );

        // Selected role
        c.gridy = 4;

        c.insets =
                new Insets(0, 0, 8, 0);

        cardPanel.add(
                selectedRoleLabel,
                c
        );

        // Username label
        c.gridy = 5;

        c.insets =
                new Insets(3, 0, 2, 0);

        cardPanel.add(
                usernameLabel,
                c
        );

        // Username
        c.gridy = 6;

        c.insets =
                new Insets(2, 0, 6, 0);

        cardPanel.add(
                usernameField,
                c
        );

        // Password label
        c.gridy = 7;

        c.insets =
                new Insets(3, 0, 2, 0);

        cardPanel.add(
                passwordLabel,
                c
        );

        // Password
        c.gridy = 8;

        c.insets =
                new Insets(2, 0, 10, 0);

        cardPanel.add(
                passwordField,
                c
        );

        // Login
        c.gridy = 9;

        c.insets =
                new Insets(3, 0, 6, 0);

        cardPanel.add(
                loginButton,
                c
        );

        // Exit
        c.gridy = 10;

        c.insets =
                new Insets(3, 0, 0, 0);

        cardPanel.add(
                exitButton,
                c
        );

        rightPanel.add(
                cardPanel
        );

        return rightPanel;
    }

    // ============================================================
    // PRIMARY BUTTON STYLE
    // ============================================================

    private void stylePrimaryButton(
            JButton btn,
            Color baseColor,
            Color hoverColor) {

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        btn.setForeground(
                Color.WHITE
        );

        btn.setBackground(
                baseColor
        );

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setOpaque(true);

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setPreferredSize(
                new Dimension(300, 42)
        );

        btn.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        btn.setBackground(
                                hoverColor
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        btn.setBackground(
                                baseColor
                        );
                    }
                }
        );
    }

    // ============================================================
    // EXIT BUTTON STYLE
    // ============================================================

    private void styleTextLinkButton(
            JButton btn) {

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        btn.setForeground(
                COLOR_TEXT_MUTED
        );

        btn.setContentAreaFilled(false);

        btn.setBorderPainted(false);

        btn.setFocusPainted(false);

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        btn.setForeground(
                                COLOR_DANGER
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        btn.setForeground(
                                COLOR_TEXT_MUTED
                        );
                    }
                }
        );
    }

    // ============================================================
    // EVENTS
    // ============================================================

    private void setupEvents() {

        // Owner selected
        ownerRadio.addActionListener(
                e -> {

                    selectedRoleLabel.setText(
                            "Selected: Owner / Admin"
                    );

                    selectedRoleLabel.setForeground(
                            COLOR_ACCENT
                    );

                    usernameField.requestFocus();
                }
        );

        // Resident selected
        residentRadio.addActionListener(
                e -> {

                    selectedRoleLabel.setText(
                            "Selected: Resident"
                    );

                    selectedRoleLabel.setForeground(
                            new Color(16, 185, 129)
                    );

                    usernameField.requestFocus();
                }
        );

        // Login button
        loginButton.addActionListener(
                e -> performLogin()
        );

        // Exit
        exitButton.addActionListener(
                e -> System.exit(0)
        );

        // Press Enter in password field
        passwordField.addActionListener(
                e -> performLogin()
        );
    }

    // ============================================================
    // PERFORM LOGIN
    // ============================================================

    private void performLogin() {

        if (ownerRadio.isSelected()) {

            ownerLogin();

        } else {

            residentLogin();
        }
    }

    // ============================================================
    // OWNER LOGIN
    // ============================================================

    private void ownerLogin() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (username.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String sql =
                "SELECT owner_id, username "
                + "FROM owner_login "
                + "WHERE username = ? AND password = ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    username
            );

            ps.setString(
                    2,
                    password
            );

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                int ownerId =
                        rs.getInt("owner_id");

                String ownerUsername =
                        rs.getString("username");

                // Store OWNER session
                Session.setUser(
                        ownerId,
                        ownerUsername,
                        "OWNER"
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Owner / Admin Login Successful!",
                        "Login Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                OwnerDashboard dashboard =
                        new OwnerDashboard();

                dashboard.setVisible(true);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Owner / Admin Username or Password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                passwordField.setText("");
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                    + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // ============================================================
    // RESIDENT LOGIN
    // ============================================================

    private void residentLogin() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (username.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String sql =
                "SELECT resident_id, name, username "
                + "FROM residents "
                + "WHERE username = ? AND password = ?";

        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    username
            );

            ps.setString(
                    2,
                    password
            );

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                int residentId =
                        rs.getInt("resident_id");

                String residentName =
                        rs.getString("name");

                // Store RESIDENT session
                Session.setUser(
                        residentId,
                        residentName,
                        "RESIDENT"
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome "
                        + residentName
                        + "!\nResident Login Successful.",
                        "Login Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                ResidentDashboard dashboard =
                        new ResidentDashboard();

                dashboard.setVisible(true);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Resident Username or Password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                passwordField.setText("");
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error:\n"
                    + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }

    // ============================================================
    // MAIN METHOD
    // ============================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    LoginPage loginPage =
                            new LoginPage();

                    loginPage.setVisible(true);
                }
        );
    }
}