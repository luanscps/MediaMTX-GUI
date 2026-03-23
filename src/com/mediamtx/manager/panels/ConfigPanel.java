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

        // Editor inicializado PRIMEIRO
        editor = new JTextArea(service.loadConfigContent());
        editor.setFont(Theme.FONT_MONO);
        editor.setBackground(new Color(15, 23, 42));   // fundo escuro no editor de codigo
        editor.setForeground(new Color(134, 239, 172)); // verde claro
        editor.setCaretColor(new Color(56, 189, 248));
        editor.setTabSize(2);
        editor.setBorder(new EmptyBorder(10, 12, 10, 12));

        JScrollPane scroll = new JScrollPane(editor);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_MED));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);

        JButton btnLoad    = Theme.primaryButton("\uD83D\uDCC2  Carregar");
        JButton btnSave    = Theme.successButton("\uD83D\uDCBE  Salvar");
        JButton btnDefault = Theme.warningButton("\u21BA  Config Padr\u00e3o");

        btnLoad.addActionListener(e -> editor.setText(service.loadConfigContent()));
        btnSave.addActionListener(e -> {
            service.saveConfigContent(editor.getText());
            JOptionPane.showMessageDialog(this, "Configura\u00e7\u00e3o salva!", "OK", JOptionPane.INFORMATION_MESSAGE);
        });
        btnDefault.addActionListener(e -> editor.setText(service.defaultYaml()));

        toolbar.add(btnLoad);
        toolbar.add(btnSave);
        toolbar.add(btnDefault);

        JLabel hint = new JLabel("  Edite o YAML e salve antes de iniciar o servidor");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.TEXT_DIM);
        toolbar.add(hint);

        add(toolbar, BorderLayout.NORTH);
        add(scroll,  BorderLayout.CENTER);
    }
    public void appendYaml(String block) {
        String current  = editor.getText().stripTrailing();
        String toAppend = block.replaceFirst("^paths:\\s*\\n", "");
        editor.setText(current + "\n\n" + toAppend);
        editor.setCaretPosition(editor.getDocument().getLength());
    }
}
