package com.mediamtx.manager.panels;

import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConfigPanel extends JPanel {

    private final JTextArea       editor;
    private final MediaMTXService service;

    public ConfigPanel(MediaMTXService service) {
        this.service = service;
        setLayout(new MigLayout("insets 16, gap 8", "[grow]", "[][grow]"));
        setBackground(Theme.BG);

        // ── Toolbar ───────────────────────────────────────────────────────
        JPanel toolbar = new JPanel(new MigLayout("insets 0, gap 8", "[][][]push[]", "[]"));
        toolbar.setOpaque(false);

        JButton btnLoad    = Theme.primaryButton("\uD83D\uDCC2  Carregar");
        JButton btnSave    = Theme.successButton("\uD83D\uDCBE  Salvar");
        JButton btnDefault = Theme.warningButton("\u21BA  Config Padrao");

        JLabel hint = new JLabel("  Voce pode editar manualmente ou usar a aba Assistente para gerar automaticamente");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.TEXT_DIM);

        toolbar.add(btnLoad);
        toolbar.add(btnSave);
        toolbar.add(btnDefault);
        toolbar.add(hint);

        // ── Editor YAML ───────────────────────────────────────────────────
        editor = new JTextArea(service.loadConfigContent());
        editor.setFont(Theme.FONT_MONO);
        editor.setBackground(Theme.BG_INPUT);
        editor.setForeground(Theme.TEXT_WHITE);
        editor.setCaretColor(Theme.ACCENT);
        editor.setSelectedTextColor(Color.WHITE);
        editor.setSelectionColor(Theme.BG_SELECT);
        editor.setTabSize(2);
        editor.setBorder(new EmptyBorder(12, 14, 12, 14));

        JScrollPane scroll = new JScrollPane(editor);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_MED));

        btnLoad.addActionListener(e -> editor.setText(service.loadConfigContent()));
        btnSave.addActionListener(e -> {
            service.saveConfigContent(editor.getText());
            JOptionPane.showMessageDialog(this,
                "Configuracao salva!", "OK", JOptionPane.INFORMATION_MESSAGE);
        });
        btnDefault.addActionListener(e -> editor.setText(service.defaultYaml()));

        add(toolbar, "growx, wrap");
        add(scroll,  "grow");
    }

    /** Chamado pelo ConfigWizardPanel apos gerar o YAML */
    public void setYaml(String yaml) {
        editor.setText(yaml);
        editor.setCaretPosition(0);
    }

    /** Adiciona bloco de path ao final do YAML atual */
    public void appendYaml(String block) {
        String current  = editor.getText().stripTrailing();
        String toAppend = block.replaceFirst("^paths:\\\\s*\\\\n", "");
        editor.setText(current + "\n\n" + toAppend);
        editor.setCaretPosition(editor.getDocument().getLength());
    }
}
