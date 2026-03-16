package com.mediamtx.manager.panels;

import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class PathsPanel extends JPanel {

    public PathsPanel(MediaMTXService service) {
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("\uD83D\uDD17  Gerenciamento de Paths (Streams)");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        add(title, BorderLayout.NORTH);

        String[] cols = {"Path / Nome do Stream", "Fonte (source)", "Grava\u00e7\u00e3o", "Autentica\u00e7\u00e3o"};
        Object[][] data = {
            {"all_others",  "(qualquer publisher)",       "Herdada", "Nenhuma"},
            {"cam_android", "rtsp://IP_DEVICE:8554/live",  "Sim",    "Nenhuma"},
            {"live",        "publisher externo (OBS)",     "N\u00e3o", "Nenhuma"},
            {"cam1",        "ffmpeg / device local",       "Sim",    "Nenhuma"},
        };
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(Theme.FONT_MEDIUM);
        table.setForeground(Theme.TEXT);
        table.setBackground(Theme.BG_CARD);
        table.setGridColor(Theme.BORDER);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(14, 165, 233, 40));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(Theme.FONT_BOLD);
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(Theme.TEXT_DIM);

        // Alterna cor das linhas
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setForeground(Theme.TEXT);
                setBackground(sel ? new Color(14, 165, 233, 40)
                    : (r % 2 == 0 ? Theme.BG_CARD : new Color(248, 250, 252)));
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_MED));
        add(scroll, BorderLayout.CENTER);

        JLabel info = new JLabel("<html>Os paths s\u00e3o configurados no <b>mediamtx.yml</b>. " +
            "Edite na aba <b>\u2699 Config YAML</b> para adicionar novos streams.<br>" +
            "Para integrar com o <b>camera2api-brSS</b>, use <code>source: rtsp://IP_DEVICE:8554/live</code> no path desejado.</html>");
        info.setFont(Theme.FONT_SMALL);
        info.setForeground(Theme.TEXT_DIM);
        info.setBorder(new EmptyBorder(10, 0, 0, 0));
        add(info, BorderLayout.SOUTH);
    }
}
