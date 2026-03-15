package com.mediamtx.manager.panels;

import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConfigPanel extends JPanel {

    private final JTextArea editor;
    private final MediaMTXService service;

    public ConfigPanel(MediaMTXService service) {
        this.service = service;
        setLayout(new BorderLayout(8, 8));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);

        JButton btnLoad    = Theme.primaryButton("Carregar");
        JButton btnSave    = Theme.primaryButton("Salvar");
        JButton btnDefault = new JButton("Config Padrao");
        btnDefault.setFont(Theme.FONT_BOLD);
        btnDefault.setForeground(Theme.WARNING);
        btnDefault.setBackground(Theme.BG_CARD);
        btnDefault.setFocusPainted(false);
        btnDefault.setBorderPainted(false);
        btnDefault.setOpaque(true);

        btnLoad.addActionListener(e -> editor.setText(service.loadConfigContent()));
        btnSave.addActionListener(e -> {
            service.saveConfigContent(editor.getText());
            JOptionPane.showMessageDialog(this, "Configuracao salva!", "OK", JOptionPane.INFORMATION_MESSAGE);
        });
        btnDefault.addActionListener(e -> editor.setText(service.defaultYaml()));

        toolbar.add(btnLoad); toolbar.add(btnSave); toolbar.add(btnDefault);
        JLabel hint = new JLabel("  Edite o YAML e salve antes de iniciar o servidor");
        hint.setFont(Theme.FONT_SMALL); hint.setForeground(Theme.TEXT_MUTED);
        toolbar.add(hint);

        editor = new JTextArea(service.loadConfigContent());
        editor.setFont(Theme.FONT_MONO);
        editor.setBackground(new Color(20, 20, 32));
        editor.setForeground(new Color(180, 220, 180));
        editor.setCaretColor(Theme.ACCENT);
        editor.setTabSize(2);
        editor.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(editor);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));

        add(toolbar, BorderLayout.NORTH);
        add(scroll,  BorderLayout.CENTER);
    }
}
