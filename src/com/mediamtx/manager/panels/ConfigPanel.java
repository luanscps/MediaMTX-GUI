package com.mediamtx.manager.panels;

import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.util.regex.Pattern;

public class ConfigPanel extends JPanel {

    private final JTextArea       editor;
    private final MediaMTXService service;

    public ConfigPanel(MediaMTXService service) {
        this.service = service;
        setLayout(new MigLayout("insets 16, gap 8", "[grow]", "[][grow]"));
        setBackground(Theme.BG);

        JPanel toolbar = new JPanel(new MigLayout("insets 0, gap 8", "[][][][]push[]", "[]"));
        toolbar.setOpaque(false);

        JButton btnLoad     = Theme.primaryButton("📂  Carregar");
        JButton btnSave     = Theme.successButton("💾  Salvar");
        JButton btnDefault  = Theme.warningButton("↺  Config Padrao");
        JButton btnValidate = makeValidateBtn();

        JLabel hint = new JLabel("  Edite manualmente ou use a aba Assistente para gerar automaticamente");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.TEXT_DIM);

        toolbar.add(btnLoad);
        toolbar.add(btnSave);
        toolbar.add(btnDefault);
        toolbar.add(btnValidate);
        toolbar.add(hint);

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

        btnLoad.addActionListener(e -> {
            editor.getHighlighter().removeAllHighlights();
            editor.setText(service.loadConfigContent());
        });
        btnSave.addActionListener(e -> {
            if (validateYaml(true)) {
                service.saveConfigContent(editor.getText());
                JOptionPane.showMessageDialog(this,
                    "Configuracao salva!", "OK", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnDefault.addActionListener(e -> {
            editor.getHighlighter().removeAllHighlights();
            editor.setText(service.defaultYaml());
        });
        btnValidate.addActionListener(e -> validateYaml(false));

        add(toolbar, "growx, wrap");
        add(scroll,  "grow");
    }

    private JButton makeValidateBtn() {
        JButton b = new JButton("✓  Validar YAML");
        b.setFont(Theme.FONT_BOLD);
        b.setBackground(new Color(99, 102, 241));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private boolean validateYaml(boolean silent) {
        editor.getHighlighter().removeAllHighlights();
        String[] lines = editor.getText().split("\n", -1);

        Pattern tabInLine    = Pattern.compile("^\t");
        Pattern invalidIndent = Pattern.compile("^( {1}| {3}| {5}| {7}| {9})\\S");
        Pattern colonNoSpace  = Pattern.compile("^\\s*[^#\\s][^:]*:[^\\s\\/]");

        for (int i = 0; i < lines.length; i++) {
            String line    = lines[i];
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

            if (tabInLine.matcher(line).find()) {
                highlightLine(i + 1);
                showError("Erro na linha " + (i + 1) + ":\nYAML nao aceita TAB para indentacao. Use espacos.\n\nLinha: "
                    + line.substring(0, Math.min(line.length(), 60)));
                return false;
            }

            if (invalidIndent.matcher(line).find()) {
                highlightLine(i + 1);
                showError("Aviso na linha " + (i + 1) + ":\nIndentacao com numero impar de espacos.\nYAML usa multiplos de 2 espacos.\n\nLinha: "
                    + line.substring(0, Math.min(line.length(), 60)));
                return false;
            }

            if (colonNoSpace.matcher(line).find()
                    && !trimmed.startsWith("-")
                    && !trimmed.contains("://")
                    && !trimmed.contains(":\\\\")) {
                highlightLine(i + 1);
                showError("Possivel erro na linha " + (i + 1) + ":\nEncontrado ':' sem espaco apos a chave.\nCorreto: 'chave: valor'\n\nLinha: "
                    + line.substring(0, Math.min(line.length(), 60)));
                return false;
            }
        }

        if (!silent) {
            JOptionPane.showMessageDialog(this,
                "YAML valido! Nenhum erro encontrado.",
                "Validacao OK", JOptionPane.INFORMATION_MESSAGE);
        }
        return true;
    }

    private void highlightLine(int lineNumber) {
        try {
            int start = editor.getLineStartOffset(lineNumber - 1);
            int end   = editor.getLineEndOffset(lineNumber - 1);
            editor.getHighlighter().addHighlight(start, end,
                new DefaultHighlighter.DefaultHighlightPainter(new Color(239, 68, 68, 90)));
            editor.setCaretPosition(start);
        } catch (BadLocationException ignored) {}
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "YAML invalido", JOptionPane.ERROR_MESSAGE);
    }

    public void setYaml(String yaml) {
        editor.getHighlighter().removeAllHighlights();
        editor.setText(yaml);
        editor.setCaretPosition(0);
    }

    public void appendYaml(String block) {
        String current  = editor.getText().stripTrailing();
        String toAppend = block.replaceFirst("^paths:\\s*\n", "");
        editor.setText(current + "\n\n" + toAppend);
        editor.setCaretPosition(editor.getDocument().getLength());
    }
}
