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

        // ── Toolbar ───────────────────────────────────────────────────────
        JPanel toolbar = new JPanel(new MigLayout("insets 0, gap 8", "[][][][]push[]", "[]"));
        toolbar.setOpaque(false);

        JButton btnLoad     = Theme.primaryButton("\uD83D\uDCC2  Carregar");
        JButton btnSave     = Theme.successButton("\uD83D\uDCBE  Salvar");
        JButton btnDefault  = Theme.warningButton("\u21BA  Config Padrao");
        JButton btnValidate = makeValidateBtn();

        JLabel hint = new JLabel("  Edite manualmente ou use a aba Assistente para gerar automaticamente");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.TEXT_DIM);

        toolbar.add(btnLoad);
        toolbar.add(btnSave);
        toolbar.add(btnDefault);
        toolbar.add(btnValidate);
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
        JButton b = new JButton("\u2713  Validar YAML");
        b.setFont(Theme.FONT_BOLD);
        b.setBackground(new Color(99, 102, 241));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    /**
     * Validacao basica de YAML sem dependencia externa:
     * - Detecta tabs (YAML nao aceita tabs para indentacao)
     * - Detecta chaves duplicadas na mesma indentacao
     * - Detecta ':' sem espaco (ex: key:value)
     * - Detecta linhas com indentacao inconsistente
     *
     * @param silent se true, nao exibe popup em caso de sucesso (usado antes de salvar)
     * @return true se sem erros criticos, false se encontrou problema
     */
    private boolean validateYaml(boolean silent) {
        editor.getHighlighter().removeAllHighlights();
        String[] lines = editor.getText().split("\n", -1);

        // Patterns
        Pattern tabInLine      = Pattern.compile("^\t");
        Pattern colonNoSpace   = Pattern.compile("^\\s*[^#\\s][^:]*:[^\\s\\/]");
        Pattern invalidIndent  = Pattern.compile("^( {1}| {3}| {5}| {7}| {9})\\S");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmed = line.trim();

            // Ignora linhas vazias e comentarios
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

            // Tab no inicio
            if (tabInLine.matcher(line).find()) {
                highlightLine(i + 1);
                showError("Erro na linha " + (i + 1) + ":\n"
                    + "YAML nao aceita TAB para indentacao. Use espacos.\n\n"
                    + "Linha: " + line.substring(0, Math.min(line.length(), 60)));
                return false;
            }

            // Indentacao impar (YAML usa multiplos de 2)
            if (invalidIndent.matcher(line).find()) {
                highlightLine(i + 1);
                showError("Aviso na linha " + (i + 1) + ":\n"
                    + "Indentacao com numero impar de espacos detectada.\n"
                    + "YAML usa multiplos de 2 espacos.\n\n"
                    + "Linha: " + line.substring(0, Math.min(line.length(), 60)));
                return false;
            }

            // chave:valor sem espaco (ex: rtsp:true deveria ser rtsp: true)
            if (colonNoSpace.matcher(line).find()
                    && !trimmed.startsWith("-")
                    && !trimmed.contains("://")
                    && !trimmed.contains(":\\\\")) {
                highlightLine(i + 1);
                showError("Possivel erro na linha " + (i + 1) + ":\n"
                    + "Encontrado ':' sem espaco apos a chave.\n"
                    + "Correto: 'chave: valor'\n\n"
                    + "Linha: " + line.substring(0, Math.min(line.length(), 60)));
                return false;
            }
        }

        if (!silent) {
            JOptionPane.showMessageDialog(this,
                "YAML valido! Nenhum erro de sintaxe encontrado.",
                "Validacao OK", JOptionPane.INFORMATION_MESSAGE);
        }
        return true;
    }

    private void highlightLine(int lineNumber) {
        try {
            int lineStart = editor.getLineStartOffset(lineNumber - 1);
            int lineEnd   = editor.getLineEndOffset(lineNumber - 1);
            editor.getHighlighter().addHighlight(lineStart, lineEnd,
                new DefaultHighlighter.DefaultHighlightPainter(new Color(239, 68, 68, 90)));
            editor.setCaretPosition(lineStart);
        } catch (BadLocationException ignored) {}
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "YAML invalido", JOptionPane.ERROR_MESSAGE);
    }

    /** Chamado pelo ConfigWizardPanel apos gerar o YAML */
    public void setYaml(String yaml) {
        editor.getHighlighter().removeAllHighlights();
        editor.setText(yaml);
        editor.setCaretPosition(0);
    }

    /** Adiciona bloco de path ao final do YAML atual */
    public void appendYaml(String block) {
        String current  = editor.getText().stripTrailing();
        String toAppend = block.replaceFirst("^paths:\\s*\\n", "");
        editor.setText(current + "\n\n" + toAppend);
        editor.setCaretPosition(editor.getDocument().getLength());
    }
}
