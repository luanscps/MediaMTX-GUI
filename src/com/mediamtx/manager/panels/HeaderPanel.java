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
        setBackground(Theme.BG_HEADER);
        setBorder(new EmptyBorder(10, 16, 10, 16));

        // Logo + titulo
        JLabel title = new JLabel("  MediaMTX GUI");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        title.setForeground(new Color(56, 189, 248)); // sky-400
        title.setIcon(new ImageIcon(
            com.mediamtx.manager.AppIcon.get().getScaledInstance(26, 26, Image.SCALE_SMOOTH)));

        // Badge de status
        lblStatus = new JLabel("\u25cf  PARADO");
        lblStatus.setFont(Theme.FONT_BOLD);
        lblStatus.setForeground(new Color(248, 113, 113));
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(248, 113, 113), 1, true),
            new EmptyBorder(3, 12, 3, 12)));

        // Botoes
        JButton btnStart   = styledHeaderBtn("\u25b6  Iniciar",   new Color(34, 197, 94),  Color.WHITE);
        JButton btnStop    = styledHeaderBtn("\u25a0  Parar",     new Color(239, 68,  68),  Color.WHITE);
        JButton btnRestart = styledHeaderBtn("\u21ba  Reiniciar", new Color(251, 191, 36),  new Color(30, 41, 59));

        btnStart.addActionListener(e -> service.start());
        btnStop.addActionListener(e -> service.stop());
        btnRestart.addActionListener(e -> service.restart());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(lblStatus);
        right.add(Box.createHorizontalStrut(16));
        right.add(btnStart);
        right.add(btnStop);
        right.add(btnRestart);

        add(left,  BorderLayout.WEST);
        add(right, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(51, 65, 85));
        add(sep, BorderLayout.SOUTH);
    }

    private JButton styledHeaderBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(fg);
        b.setFont(Theme.FONT_BOLD);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(118, 32));
        return b;
    }

    public void updateStatus(boolean running) {
        Color c = running ? new Color(34, 197, 94) : new Color(248, 113, 113);
        lblStatus.setText(running ? "\u25cf  RODANDO" : "\u25cf  PARADO");
        lblStatus.setForeground(c);
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(c, 1, true),
            new EmptyBorder(3, 12, 3, 12)));
    }
}
