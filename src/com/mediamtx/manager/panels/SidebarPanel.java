package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SidebarPanel extends JPanel {

    private final JLabel valStatus;

    public SidebarPanel(MediaMTXService service, AppWindow win) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.BG_SIDEBAR);
        setPreferredSize(new Dimension(200, 0));
        setBorder(new EmptyBorder(16, 12, 16, 12));

        addSection("SERVIDOR");
        valStatus = infoRow("Status", "Parado");
        infoRow("Config", "mediamtx.yml");

        addDivider();
        addSection("PROTOCOLOS");
        infoRow("RTSP",   ":8554");
        infoRow("RTMP",   ":1935");
        infoRow("HLS",    ":8888");
        infoRow("WebRTC", ":8889");
        infoRow("SRT",    ":8890");

        addDivider();
        addSection("API REST");
        JLabel apiLink = new JLabel("<html><a href=''>localhost:9997</a></html>");
        apiLink.setFont(Theme.FONT_SMALL);
        apiLink.setForeground(Theme.ACCENT);
        apiLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        apiLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                AppWindow.openBrowser("http://localhost:9997");
            }
        });
        apiLink.setAlignmentX(LEFT_ALIGNMENT);
        add(apiLink);

        add(Box.createVerticalGlue());

        JLabel credit = new JLabel("<html><center><small>by <b>Luan Silva</b></small></center></html>");
        credit.setFont(Theme.FONT_SMALL);
        credit.setForeground(Theme.TEXT_MUTED);
        credit.setAlignmentX(CENTER_ALIGNMENT);
        add(credit);
    }

    private void addSection(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        add(lbl);
        add(Box.createVerticalStrut(4));
    }

    private JLabel infoRow(String key, String val) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        row.setAlignmentX(LEFT_ALIGNMENT);
        JLabel k = new JLabel(key); k.setFont(Theme.FONT_SMALL); k.setForeground(Theme.TEXT_MUTED);
        JLabel v = new JLabel(val); v.setFont(Theme.FONT_BOLD);  v.setForeground(Theme.TEXT);
        row.add(k, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        add(row);
        add(Box.createVerticalStrut(3));
        return v;
    }

    private void addDivider() {
        add(Box.createVerticalStrut(10));
        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        add(sep);
        add(Box.createVerticalStrut(10));
    }

    public void updateStatus(boolean running) {
        valStatus.setText(running ? "Rodando" : "Parado");
        valStatus.setForeground(running ? Theme.SUCCESS : Theme.DANGER);
    }
}
