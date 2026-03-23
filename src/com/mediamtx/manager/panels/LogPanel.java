package com.mediamtx.manager.panels;

import com.mediamtx.manager.theme.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogPanel extends JPanel {

    private final JTextArea area;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LogPanel() {
        setLayout(new MigLayout("insets 12, gap 8", "[grow]", "[][grow]"));
        setBackground(Theme.BG);

        JPanel toolbar = new JPanel(new MigLayout("insets 0, gap 8", "[][][]push", "[]"));
        toolbar.setOpaque(false);

        JButton btnClear  = Theme.warningButton("Limpar");
        JButton btnCopy   = Theme.primaryButton("Copiar tudo");
        JLabel  hint      = new JLabel("Log em tempo real do processo MediaMTX");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.TEXT_DIM);

        toolbar.add(btnClear);
        toolbar.add(btnCopy);
        toolbar.add(hint);

        area = new JTextArea();
        area.setEditable(false);
        area.setFont(Theme.FONT_MONO);
        area.setBackground(new Color(15, 23, 42));
        area.setForeground(new Color(203, 213, 225));
        area.setCaretColor(Color.WHITE);
        area.setBorder(new EmptyBorder(10, 12, 10, 12));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_MED));

        btnClear.addActionListener(e -> area.setText(""));
        btnCopy.addActionListener(e -> {
            area.selectAll();
            area.copy();
            area.select(0, 0);
        });

        add(toolbar, "growx, wrap");
        add(scroll,  "grow");
    }

    public void append(String msg) {
        String ts = LocalTime.now().format(FMT);
        area.append("[" + ts + "] " + msg + "\n");
        area.setCaretPosition(area.getDocument().getLength());
    }
}
