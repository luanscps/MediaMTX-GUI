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

        JLabel title = new JLabel("Gerenciamento de Paths (Streams)");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        add(title, BorderLayout.NORTH);

        String[] cols = {"Path / Nome do Stream", "Fonte (source)", "Gravacao", "Autenticacao"};
        Object[][] data = {
            {"all_others", "(qualquer publisher)", "Herdada", "Nenhuma"},
            {"cam1",       "rpiCamera / ffmpeg",    "Sim",     "Nenhuma"},
            {"live",       "publisher externo",     "Nao",     "Nenhuma"},
        };
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(Theme.FONT_MEDIUM); table.setForeground(Theme.TEXT);
        table.setBackground(Theme.BG_CARD); table.setGridColor(Theme.BORDER);
        table.setRowHeight(28); table.setSelectionBackground(new Color(79,195,247,60));
        table.getTableHeader().setFont(Theme.FONT_BOLD);
        table.getTableHeader().setBackground(Theme.BG_SIDEBAR);
        table.getTableHeader().setForeground(Theme.TEXT_MUTED);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        add(scroll, BorderLayout.CENTER);

        JLabel info = new JLabel("<html>Os paths sao configurados no <b>mediamtx.yml</b>. Edite na aba <b>Config YAML</b> para adicionar novos streams.</html>");
        info.setFont(Theme.FONT_SMALL); info.setForeground(Theme.TEXT_MUTED);
        info.setBorder(new EmptyBorder(8,0,0,0));
        add(info, BorderLayout.SOUTH);
    }
}
