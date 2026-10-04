package com.hostel.resident;

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
import javax.swing.SwingUtilities;

import com.hostel.database.DBConnection;
import com.hostel.login.LoginPage;
import com.hostel.login.Session;

public class ResidentDashboard extends JFrame {

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
    private static final Color COLOR_TILE_HOVER     = new Color(239, 246, 255); // Blue-50
    private static final Color COLOR_TILE_BORDER_HOVER = new Color(191, 219, 254);
    private static final Color COLOR_DANGER         = new Color(225, 29, 72);   // Rose-600
    private static final Color COLOR_SECONDARY_BG   = new Color(241, 245, 249); // Slate-100

    // Personal Summary Badges
    private JLabel roomBedBadge;
    private JLabel feeStatusBadge;

    public ResidentDashboard() {
        setTitle("Boys Hostel Management System - Resident Portal");
        setSize(1180, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createDashboardContent(), BorderLayout.CENTER);

        add(rootPanel);

        loadResidentSummary();
    }

    // -------------------------------------------------------------
    // 1. TOP APP BAR
    // -------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(1180, 75));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // Left Branding Info
        JPanel leftBrand = new JPanel(new BorderLayout(0, 3));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("STUDENT & RESIDENT PORTAL");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Boys Hostel Self-Service Hub");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        // Right Profile & Logout
        JPanel rightCluster = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 18));
        rightCluster.setOpaque(false);

        String residentName = "Resident";
        try {
            String name = Session.getUserName();
            if (name != null && !name.trim().isEmpty()) {
                residentName = name;
            }
        } catch (Throwable ignored) {}

        JLabel userPill = new JLabel("🎓 " + residentName + " (Resident)") {
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
        userPill.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userPill.setForeground(Color.WHITE);
        userPill.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        JButton logoutBtn = new JButton("Log Out");
        styleHeaderLogoutButton(logoutBtn);
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Session.logout();
                } catch (Throwable ignored) {}
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        rightCluster.add(userPill);
        rightCluster.add(logoutBtn);

        header.add(leftBrand, BorderLayout.WEST);
        header.add(rightCluster, BorderLayout.EAST);
        return header;
    }

    // -------------------------------------------------------------
    // 2. MAIN SCROLLABLE DASHBOARD
    // -------------------------------------------------------------
    private JScrollPane createDashboardContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(COLOR_BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 28, 28, 28));

        // Top Personal Status Card
        content.add(createPersonalStatusCard());
        content.add(Box.createVerticalStrut(20));

        // Group 1: Personal Accommodations
        content.add(createSectionGroup(
            "Accommodation & My Account",
            new ModuleDef[] {
                new ModuleDef("👤", "My Profile", "View contact details, KYC & parents' info", () -> openModule("com.hostel.resident.MyProfile", "My Profile")),
                new ModuleDef("🚪", "My Room", "Room assignment, floor & bed number", () -> openModule("com.hostel.resident.MyRoom", "My Room")),
                new ModuleDef("💳", "My Fees", "Invoice dues, fee breakdown & payment history", () -> openModule("com.hostel.resident.MyFees", "My Fees")),
                new ModuleDef("📅", "Attendance", "Personal daily check-in & attendance history", () -> openModule("com.hostel.resident.MyAttendance", "My Attendance"))
            }
        ));
        content.add(Box.createVerticalStrut(18));

        // Group 2: Gate & Services
        content.add(createSectionGroup(
            "Security, Services & Welfare",
            new ModuleDef[] {
                new ModuleDef("🚶", "In / Out Gate Log", "View your check-in & check-out logs", () -> openModule("com.hostel.resident.MyInOut", "In / Out Log")),
                new ModuleDef("🚨", "Complaints", "Lodge a grievance or maintenance issue", () -> openModule("com.hostel.resident.MyComplaints", "Complaints")),
                new ModuleDef("💬", "Feedback", "Rate food, cleanliness, staff & facility quality", () -> openModule("com.hostel.resident.MyFeedback", "Feedback & Rating"))
            }
        ));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getViewport().setBackground(COLOR_BG);
        return scroll;
    }

    // -------------------------------------------------------------
    // 3. TOP PERSONAL STATUS CARD
    // -------------------------------------------------------------
    private JPanel createPersonalStatusCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 85));

        JPanel left = new JPanel(new BorderLayout(0, 3));
        left.setOpaque(false);

        String residentName = "Resident";
        try {
            String name = Session.getUserName();
            if (name != null) residentName = name;
        } catch (Throwable ignored) {}

        JLabel welcome = new JLabel("Welcome, " + residentName);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcome.setForeground(COLOR_PRIMARY);

        JLabel sub = new JLabel("Here is an overview of your current hostel occupancy and status.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_TEXT_MUTED);

        left.add(welcome, BorderLayout.NORTH);
        left.add(sub, BorderLayout.CENTER);

        // Right Quick Status Badges
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        right.setOpaque(false);

        roomBedBadge = createStatusPill("🛏️ Room: Loading...", new Color(239, 246, 255), COLOR_ACCENT, new Color(191, 219, 254));
        feeStatusBadge = createStatusPill("💳 Fees: Checking...", COLOR_SECONDARY_BG, COLOR_TEXT_MUTED, COLOR_BORDER);

        right.add(roomBedBadge);
        right.add(feeStatusBadge);

        card.add(left, BorderLayout.WEST);
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
    // 4. SECTION GROUPS & INTERACTIVE MODULE TILES
    // -------------------------------------------------------------
    private JPanel createSectionGroup(String sectionTitle, ModuleDef[] modules) {
        JPanel group = createCardPanel();
        group.setLayout(new BorderLayout(0, 14));
        group.setBorder(BorderFactory.createEmptyBorder(16, 20, 18, 20));

        JLabel lbl = new JLabel(sectionTitle);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(COLOR_TEXT_MAIN);
        group.add(lbl, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, Math.min(modules.length, 4), 14, 14));
        grid.setOpaque(false);

        for (ModuleDef mod : modules) {
            grid.add(createInteractiveTile(mod));
        }

        group.add(grid, BorderLayout.CENTER);
        return group;
    }

    private JPanel createInteractiveTile(ModuleDef mod) {
        JPanel tile = new JPanel() {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mod.action.run();
                    }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? COLOR_TILE_HOVER : COLOR_SECONDARY_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(hovered ? COLOR_TILE_BORDER_HOVER : COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        tile.setLayout(new BorderLayout(0, 4));
        tile.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        tile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tile.setPreferredSize(new Dimension(240, 85));

        // Top Line: Icon + Title
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);

        JLabel icon = new JLabel(mod.icon);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        JLabel title = new JLabel(mod.name);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_PRIMARY);

        top.add(icon);
        top.add(title);

        // Subtitle Description
        JLabel desc = new JLabel(mod.description);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(COLOR_TEXT_MUTED);

        tile.add(top, BorderLayout.NORTH);
        tile.add(desc, BorderLayout.SOUTH);
        return tile;
    }

    // -------------------------------------------------------------
    // DYNAMIC MODULE LAUNCHER
    // -------------------------------------------------------------
    private void openModule(String className, String title) {
        try {
            Class<?> clazz = Class.forName(className);
            JFrame frame = (JFrame) clazz.getDeclaredConstructor().newInstance();
            frame.setVisible(true);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(
                this,
                title + " screen is being configured and will be available shortly.",
                title,
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Unable to open " + title + ": " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
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

    private void styleHeaderLogoutButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_DANGER);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 32));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(190, 18, 60)); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_DANGER); }
        });
    }

    // -------------------------------------------------------------
    // ASYNC RESIDENT STATUS LOADER
    // -------------------------------------------------------------
    private void loadResidentSummary() {
        new Thread(() -> {
            String roomBedInfo = "🛏️ Bed: Unallocated";
            String feeInfo = "💳 Fees: Up to date";
            Color feeColor = new Color(5, 150, 105);

            int residentId = 0;
            try {
                residentId = Session.getUserId();
            } catch (Throwable ignored) {}

            if (residentId > 0) {
                try (Connection conn = DBConnection.getConnection()) {
                    // Query Room & Bed
                    String bedSql = "SELECT r.room_number, b.bed_number FROM beds b "
                                  + "JOIN rooms r ON b.room_id = r.room_id WHERE b.resident_id=?";
                    try (PreparedStatement ps = conn.prepareStatement(bedSql)) {
                        ps.setInt(1, residentId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                roomBedInfo = "🛏️ Room " + rs.getString("room_number") + " (Bed " + rs.getString("bed_number") + ")";
                            }
                        }
                    }

                    // Query Pending Fees
                    String feeSql = "SELECT COALESCE(SUM(fee_amount),0) FROM fees WHERE resident_id=? AND status='PENDING'";
                    try (PreparedStatement ps = conn.prepareStatement(feeSql)) {
                        ps.setInt(1, residentId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                double pending = rs.getDouble(1);
                                if (pending > 0) {
                                    feeInfo = String.format("💳 Dues: ₹%.2f Pending", pending);
                                    feeColor = COLOR_DANGER;
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {}
            }

            final String finalRoom = roomBedInfo;
            final String finalFee = feeInfo;
            final Color finalFeeColor = feeColor;

            SwingUtilities.invokeLater(() -> {
                roomBedBadge.setText(finalRoom);
                feeStatusBadge.setText(finalFee);
                feeStatusBadge.setForeground(finalFeeColor);
            });
        }).start();
    }

    private static class ModuleDef {
        final String icon;
        final String name;
        final String description;
        final Runnable action;

        ModuleDef(String icon, String name, String description, Runnable action) {
            this.icon = icon;
            this.name = name;
            this.description = description;
            this.action = action;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ResidentDashboard().setVisible(true);
        });
    }
}