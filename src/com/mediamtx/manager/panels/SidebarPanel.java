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
        setBackground(Theme.BG_HEADER);   // BG_SIDEBAR renomeado para BG_HEADER no novo Theme
        setPreferredSize(new Dimension(200, 0));
        setBorder(new EmptyBorder(18, 14, 18, 14));

        addSection("SERVIDOR");
        valStatus = infoRow("Status", "Parado", new Color(248, 113, 113));
        infoRow("Config", "mediamtx.yml", Theme.SIDEBAR_TEXT);

        addDivider();
        addSection("PROTOCOLOS");
        infoRow("RTSP",   ":8554", new Color( 56, 189, 248));
        infoRow("RTMP",   ":1935", new Color( 99, 179, 237));
        infoRow("HLS",    ":8888", new Color(251, 191,  36));
        infoRow("WebRTC", ":8889", new Color( 52, 211, 153));
        infoRow("SRT",    ":8890", new Color(192, 132, 252));

        addDivider();
        addSection("API REST");
        JLabel apiLink = new JLabel("<html><a href='#' style='color:#38bdf8'>localhost:9997</a></html>");
        apiLink.setFont(Theme.FONT_SMALL);
        apiLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        apiLink.setAlignmentX(LEFT_ALIGNMENT);
        apiLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                AppWindow.openBrowser("http://localhost:9997");
            }
        });
        add(apiLink);

        addDivider();
        addSection("CAMERA APP");
        JLabel camLink = new JLabel("<html><a href='#' style='color:#34d399'>camera2api-brSS</a></html>");
        camLink.setFont(Theme.FONT_SMALL);
        camLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        camLink.setAlignmentX(LEFT_ALIGNMENT);
        camLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                AppWindow.openBrowser("https://github.com/luanscps/camera2api-brSS/tree/v4-ui");
            }
        });
        add(camLink);

        add(Box.createVerticalGlue());

        JLabel credit = new JLabel("<html><center>by <b style='color:#38bdf8'>Luan Silva</b></center></html>");
        credit.setFont(Theme.FONT_SMALL);
        credit.setForeground(Theme.SIDEBAR_TEXT_MUTED);
        credit.setAlignmentX(CENTER_ALIGNMENT);
        add(credit);
    }

    private void addSection(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 9));
        lbl.setForeground(Theme.TEXT_DIM);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        add(lbl);
        add(Box.createVerticalStrut(5));
    }

    private JLabel infoRow(String key, String val, Color valColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        row.setAlignmentX(LEFT_ALIGNMENT);
        JLabel k = new JLabel(key); k.setFont(Theme.FONT_SMALL); k.setForeground(Theme.TEXT_MUTED);
        JLabel v = new JLabel(val); v.setFont(Theme.FONT_BOLD);  v.setForeground(valColor);
        row.add(k, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        add(row);
        add(Box.createVerticalStrut(3));
        return v;
    }

    private void addDivider() {
        add(Box.createVerticalStrut(10));
        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.TEXT_DIM);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        add(sep);
        add(Box.createVerticalStrut(10));
    }

    public void updateStatus(boolean running) {
        valStatus.setText(running ? "Rodando" : "Parado");
        valStatus.setForeground(running ? new Color(52, 211, 153) : new Color(248, 113, 113));
    }
}
