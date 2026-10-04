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

public class OwnerDashboard extends JFrame {

    // =============================================================
    // COHESIVE ENTERPRISE COLOR PALETTE
    // =============================================================
    private static final Color COLOR_PRIMARY        = new Color(24, 43, 73);    // Deep Navy
    private static final Color COLOR_BG             = new Color(248, 250, 252); // Slate-50 Canvas
    private static final Color COLOR_CARD           = Color.WHITE;
    private static final Color COLOR_BORDER         = new Color(226, 232, 240); // Slate-200
    private static final Color COLOR_TEXT_MAIN      = new Color(15, 23, 42);    // Slate-900
    private static final Color COLOR_TEXT_MUTED     = new Color(100, 116, 139); // Slate-500
    private static final Color COLOR_TILE_HOVER     = new Color(239, 246, 255); // Blue-50
    private static final Color COLOR_TILE_BORDER_HOVER = new Color(191, 219, 254);
    private static final Color COLOR_DANGER         = new Color(225, 29, 72);   // Rose-600
    private static final Color COLOR_SECONDARY_BG   = new Color(241, 245, 249); // Slate-100

    // Quick Stats Badges
    private JLabel statResidents;
    private JLabel statRooms;
    private JLabel statAvailableBeds;
    private JLabel statPendingTasks;

    public OwnerDashboard() {
        setTitle("Boys Hostel Management System - Owner Dashboard");
        setSize(1260, 860);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Root Canvas
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createDashboardContent(), BorderLayout.CENTER);

        add(rootPanel);
        loadLiveMetrics();
    }

    // -------------------------------------------------------------
    // 1. HEADER APP BAR
    // -------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(1260, 75));
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // Left Branding Info
        JPanel leftBrand = new JPanel(new BorderLayout(0, 3));
        leftBrand.setOpaque(false);

        JLabel tag = new JLabel("ENTERPRISE ADMINISTRATION CONSOLE");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setForeground(new Color(147, 197, 253));

        JLabel title = new JLabel("Boys Hostel Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(Color.WHITE);

        leftBrand.add(tag, BorderLayout.NORTH);
        leftBrand.add(title, BorderLayout.CENTER);

        // Right Profile & Logout
        JPanel rightCluster = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 18));
        rightCluster.setOpaque(false);

        String ownerName = "Administrator";
        try {
            String sessName = Session.getUserName();
            if (sessName != null && !sessName.trim().isEmpty()) {
                ownerName = sessName;
            }
        } catch (Throwable ignored) {}

        JLabel userPill = new JLabel("👤 " + ownerName + " (Owner)") {
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

        // Real-Time Stats Row
        content.add(createStatMetricsRow());
        content.add(Box.createVerticalStrut(20));

        // Group 1: Occupancy & Personnel (Already Completed & Working)
        content.add(createSectionGroup(
            "Occupancy & Resident Administration",
            new ModuleDef[] {
                new ModuleDef("👥", "Residents", "Manage student profiles & KYC", () -> openModule("com.hostel.owner.ResidentManagement", "Resident Management")),
                new ModuleDef("👷", "Workers", "Staff directory, roles & payroll", () -> new WorkerManagement().setVisible(true)),
                new ModuleDef("🚪", "Rooms", "Floors, categories & bed sizes", () -> new RoomManagement().setVisible(true)),
                new ModuleDef("🛏️", "Beds", "Bed allocation & vacate status", () -> new BedManagement().setVisible(true))
            }
        ));
        content.add(Box.createVerticalStrut(18));

        // Group 2: Financial & Billings
        content.add(createSectionGroup(
            "Finance & Financial Auditing",
            new ModuleDef[] {
                new ModuleDef("💳", "Fees", "Fee structures & dues setup", () -> openModule("com.hostel.owner.FeeManagement", "Fee Management")),
                new ModuleDef("💵", "Payments", "Verify receipts & transactions", () -> openModule("com.hostel.owner.PaymentManagement", "Payment Management")),
                new ModuleDef("📉", "Expenses", "Hostel outflows, food & utilities", () -> new ExpenseManagement().setVisible(true)),
                new ModuleDef("📊", "Reports", "Comprehensive analytics & exports", () -> openModule("com.hostel.owner.Reports", "Reports"))
            }
        ));
        content.add(Box.createVerticalStrut(18));

        // Group 3: Operations & Welfare
        content.add(createSectionGroup(
            "Hostel Operations & Resident Support",
            new ModuleDef[] {
                new ModuleDef("📅", "Attendance", "Daily attendance & head counts", () -> openModule("com.hostel.owner.AttendanceManagement", "Attendance Management")),
                new ModuleDef("🚶", "In / Out Log", "Gate register & entry tracking", () -> new InOutManagement().setVisible(true)),
                new ModuleDef("🛠️", "Maintenance", "Repair work orders & technicians", () -> new MaintenanceManagement().setVisible(true)),
                new ModuleDef("💬", "Feedback", "Quality ratings & student remarks", () -> new FeedbackManagement().setVisible(true))
            }
        ));
        content.add(Box.createVerticalStrut(18));

        // Group 4: Grievance Redressal
        content.add(createSectionGroup(
            "Grievances & Grievance Redressal",
            new ModuleDef[] {
                new ModuleDef("🚨", "Complaints", "Review, assign & resolve resident grievances", () -> openModule("com.hostel.owner.ComplaintManagement", "Complaint Management"))
            }
        ));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.getViewport().setBackground(COLOR_BG);
        return scroll;
    }

    // -------------------------------------------------------------
    // DYNAMIC MODULE LAUNCHER (Prevents Compilation Crashes)
    // -------------------------------------------------------------
    private void openModule(String className, String title) {
        try {
            Class<?> clazz = Class.forName(className);
            JFrame frame = (JFrame) clazz.getDeclaredConstructor().newInstance();
            frame.setVisible(true);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(
                this,
                title + " module is being prepared and will be available shortly.",
                title,
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Unable to open " + title + ": " + e.getMessage(),
                "Module Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------
    // 3. STAT METRICS ROW
    // -------------------------------------------------------------
    private JPanel createStatMetricsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));

        statResidents = new JLabel("...");
        statRooms = new JLabel("...");
        statAvailableBeds = new JLabel("...");
        statPendingTasks = new JLabel("...");

        row.add(createMiniStatCard("Total Residents", statResidents, new Color(37, 99, 235)));
        row.add(createMiniStatCard("Total Rooms", statRooms, new Color(13, 148, 136)));
        row.add(createMiniStatCard("Available Beds", statAvailableBeds, new Color(16, 185, 129)));
        row.add(createMiniStatCard("Pending Maintenance", statPendingTasks, new Color(225, 29, 72)));

        return row;
    }

    private JPanel createMiniStatCard(String title, JLabel valLabel, Color accentColor) {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 4));
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLbl.setForeground(COLOR_TEXT_MUTED);

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLabel.setForeground(accentColor);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valLabel, BorderLayout.CENTER);
        return card;
    }

    // -------------------------------------------------------------
    // 4. SECTION GROUPS & INTERACTIVE TILES
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

        // Icon + Name
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);

        JLabel icon = new JLabel(mod.icon);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        JLabel title = new JLabel(mod.name);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_PRIMARY);

        top.add(icon);
        top.add(title);

        JLabel desc = new JLabel(mod.description);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(COLOR_TEXT_MUTED);

        tile.add(top, BorderLayout.NORTH);
        tile.add(desc, BorderLayout.SOUTH);
        return tile;
    }

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
    // LIVE KPI ASYNC DB QUERY
    // -------------------------------------------------------------
    private void loadLiveMetrics() {
        new Thread(() -> {
            int residents = 0;
            int rooms = 0;
            int availBeds = 0;
            int pending = 0;

            try (Connection conn = DBConnection.getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM residents");
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) residents = rs.getInt(1);
                }

                try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM rooms");
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) rooms = rs.getInt(1);
                }

                try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM beds WHERE status='AVAILABLE'");
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) availBeds = rs.getInt(1);
                }

                try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM maintenance WHERE status='PENDING'");
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) pending = rs.getInt(1);
                }
            } catch (Exception ignored) {}

            final int r = residents, rm = rooms, b = availBeds, p = pending;
            SwingUtilities.invokeLater(() -> {
                statResidents.setText(String.valueOf(r));
                statRooms.setText(String.valueOf(rm));
                statAvailableBeds.setText(String.valueOf(b));
                statPendingTasks.setText(String.valueOf(p));
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
            new OwnerDashboard().setVisible(true);
        });
    }
}