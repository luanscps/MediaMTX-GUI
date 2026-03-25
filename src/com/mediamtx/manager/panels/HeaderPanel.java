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

        // ── Logo + titulo ─────────────────────────────────────────────────
        JLabel title = new JLabel("  MediaMTX GUI");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        title.setForeground(new Color(56, 189, 248));
        title.setIcon(new ImageIcon(
            com.mediamtx.manager.AppIcon.get().getScaledInstance(26, 26, Image.SCALE_SMOOTH)));

        // ── Badge de versao do binario ────────────────────────────────────
        lblVersion = new JLabel("versao desconhecida");
        lblVersion.setFont(Theme.FONT_SMALL);
        lblVersion.setForeground(Theme.TEXT_DIM);
        lblVersion.setBorder(new EmptyBorder(0, 8, 0, 0));

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleRow.setOpaque(false);
        titleRow.add(title);
        titleRow.add(lblVersion);

        // Registra consumer de versao no service
        service.setVersionConsumer(version -> {
            lblVersion.setText("  " + version);
            lblVersion.setForeground(new Color(56, 189, 248));
        });
        // Tenta detectar versao ja na abertura
        service.detectVersion();

        // ── Badge de status ───────────────────────────────────────────────
        lblStatus = new JLabel("\u25cf  PARADO");
        lblStatus.setFont(Theme.FONT_BOLD);
        lblStatus.setForeground(Theme.DANGER);
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.DANGER, 1, true),
            new EmptyBorder(3, 12, 3, 12)));

        // ── Botoes ────────────────────────────────────────────────────────
        JButton btnStart   = headerBtn("\u25b6  Iniciar",   Theme.SUCCESS, Color.WHITE);
        JButton btnStop    = headerBtn("\u25a0  Parar",     Theme.DANGER,  Color.WHITE);
        JButton btnRestart = headerBtn("\u21ba  Reiniciar", Theme.WARNING, Theme.BG_HEADER);

        btnStart.addActionListener(e -> service.start());
        btnStop.addActionListener(e -> service.stop());
        btnRestart.addActionListener(e -> service.restart());

        // ── Painel direito ────────────────────────────────────────────────
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
        lblStatus.setText(running ? "\u25cf  RODANDO" : "\u25cf  PARADO");
        lblStatus.setForeground(c);
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(c, 1, true),
            new EmptyBorder(3, 12, 3, 12)));
    }
}
