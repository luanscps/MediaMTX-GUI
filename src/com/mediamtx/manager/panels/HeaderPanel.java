package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private final JLabel lblStatus;
    private final JLabel lblVersion;

    public HeaderPanel(MediaMTXService service, AppWindow win) {
        setLayout(new MigLayout("insets 10 16 10 16, gap 0", "[grow][]", "[][]"));
        setBackground(Theme.BG_HEADER);

        JLabel title = new JLabel("  MediaMTX GUI");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        title.setForeground(new Color(56, 189, 248));
        title.setIcon(new ImageIcon(
            com.mediamtx.manager.AppIcon.get().getScaledInstance(26, 26, Image.SCALE_SMOOTH)));

        lblVersion = new JLabel("  versao desconhecida");
        lblVersion.setFont(Theme.FONT_SMALL);
        lblVersion.setForeground(Theme.TEXT_DIM);

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleRow.setOpaque(false);
        titleRow.add(title);
        titleRow.add(lblVersion);

        service.setVersionConsumer(version -> {
            lblVersion.setText("  " + version);
            lblVersion.setForeground(new Color(56, 189, 248));
        });
        service.detectVersion();

        lblStatus = new JLabel("●  PARADO");
        lblStatus.setFont(Theme.FONT_BOLD);
        lblStatus.setForeground(Theme.DANGER);
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.DANGER, 1, true),
            new EmptyBorder(3, 12, 3, 12)));

        JButton btnStart   = headerBtn("▶  Iniciar",   Theme.SUCCESS, Color.WHITE);
        JButton btnStop    = headerBtn("■  Parar",     Theme.DANGER,  Color.WHITE);
        JButton btnRestart = headerBtn("↺  Reiniciar", Theme.WARNING, Theme.BG_HEADER);

        btnStart.addActionListener(e -> service.start());
        btnStop.addActionListener(e -> service.stop());
        btnRestart.addActionListener(e -> service.restart());

        JPanel right = new JPanel(new MigLayout("insets 0, gap 8", "[][][][][]", "[]"));
        right.setOpaque(false);
        right.add(lblStatus);
        right.add(new JSeparator(JSeparator.VERTICAL), "growy, gapleft 8, gapright 8");
        right.add(btnStart);
        right.add(btnStop);
        right.add(btnRestart);

        add(titleRow, "growx");
        add(right, "wrap");

        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.TEXT_DIM);
        add(sep, "span 2, growx, gapy 4 0");
    }

    private JButton headerBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(Theme.FONT_BOLD);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(118, 32));
        return b;
    }

    public void updateStatus(boolean running) {
        Color c = running ? Theme.SUCCESS : Theme.DANGER;
        lblStatus.setText(running ? "●  RODANDO" : "●  PARADO");
        lblStatus.setForeground(c);
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(c, 1, true),
            new EmptyBorder(3, 12, 3, 12)));
    }
}
