package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private final JLabel lblStatus;

    public HeaderPanel(MediaMTXService service, AppWindow win) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_SIDEBAR);
        setBorder(new EmptyBorder(10, 16, 10, 16));

        JLabel title = new JLabel("  MediaMTX GUI");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        title.setIcon(new ImageIcon(com.mediamtx.manager.AppIcon.get().getScaledInstance(28, 28, Image.SCALE_SMOOTH)));

        lblStatus = new JLabel("\u25cf PARADO");
        lblStatus.setFont(Theme.FONT_BOLD);
        lblStatus.setForeground(Theme.DANGER);
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.DANGER, 1, true),
            new EmptyBorder(3, 10, 3, 10)));

        JButton btnStart   = Theme.primaryButton("\u25b6  Iniciar");
        JButton btnStop    = Theme.dangerButton("\u25a0  Parar");
        JButton btnRestart = new JButton("\u21ba  Reiniciar");
        btnRestart.setFont(Theme.FONT_BOLD);
        btnRestart.setForeground(Theme.WARNING);
        btnRestart.setBackground(Theme.BG_CARD);
        btnRestart.setFocusPainted(false);
        btnRestart.setBorderPainted(false);
        btnRestart.setOpaque(true);

        btnStart.setPreferredSize(new Dimension(120, 34));
        btnStop.setPreferredSize(new Dimension(100, 34));
        btnRestart.setPreferredSize(new Dimension(120, 34));

        btnStart.addActionListener(e -> service.start());
        btnStop.addActionListener(e -> service.stop());
        btnRestart.addActionListener(e -> service.restart());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(lblStatus);
        right.add(Box.createHorizontalStrut(12));
        right.add(btnStart);
        right.add(btnStop);
        right.add(btnRestart);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);
        add(sep, BorderLayout.SOUTH);
    }

    public void updateStatus(boolean running) {
        if (running) {
            lblStatus.setText("\u25cf RODANDO");
            lblStatus.setForeground(Theme.SUCCESS);
            lblStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.SUCCESS, 1, true),
                new EmptyBorder(3, 10, 3, 10)));
        } else {
            lblStatus.setText("\u25cf PARADO");
            lblStatus.setForeground(Theme.DANGER);
            lblStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.DANGER, 1, true),
                new EmptyBorder(3, 10, 3, 10)));
        }
    }
}
