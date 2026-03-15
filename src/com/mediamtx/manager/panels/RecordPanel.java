package com.mediamtx.manager.panels;

import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class RecordPanel extends JPanel {

    private final JCheckBox chkRecord;
    private final JTextField txtPath;
    private final JComboBox<String> cmbFormat;
    private final JTextField txtSegment;
    private final JTextField txtDelete;

    public RecordPanel(MediaMTXService service) {
        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Configuracoes de Gravacao");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.BG_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true), new EmptyBorder(20,20,20,20)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8); g.anchor = GridBagConstraints.WEST;

        chkRecord = new JCheckBox("Ativar gravacao automatica de streams");
        chkRecord.setFont(Theme.FONT_BOLD); chkRecord.setForeground(Theme.TEXT); chkRecord.setOpaque(false);
        g.gridx=0; g.gridy=0; g.gridwidth=3; form.add(chkRecord, g); g.gridwidth=1;

        g.gridx=0; g.gridy=1; form.add(lbl("Diretorio de saida:"), g);
        txtPath = field("./recordings/%path/%Y-%m-%d_%H-%M-%S-%f", 36); g.gridx=1; form.add(txtPath, g);
        JButton btnBrowse = new JButton("...");
        btnBrowse.setFont(Theme.FONT_BOLD); btnBrowse.setBackground(Theme.BG); btnBrowse.setForeground(Theme.ACCENT);
        btnBrowse.setBorderPainted(false); btnBrowse.setFocusPainted(false);
        btnBrowse.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(); fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                txtPath.setText(fc.getSelectedFile().getAbsolutePath() + "/%path/%Y-%m-%d_%H-%M-%S-%f");
        });
        g.gridx=2; form.add(btnBrowse, g);

        g.gridx=0; g.gridy=2; form.add(lbl("Formato:"), g);
        cmbFormat = new JComboBox<>(new String[]{"fmp4","mpegts"}); cmbFormat.setFont(Theme.FONT_MEDIUM);
        g.gridx=1; form.add(cmbFormat, g);

        g.gridx=0; g.gridy=3; form.add(lbl("Duracao do segmento:"), g);
        txtSegment = field("1h", 10); g.gridx=1; form.add(txtSegment, g);

        g.gridx=0; g.gridy=4; form.add(lbl("Deletar apos:"), g);
        txtDelete = field("24h", 10); g.gridx=1; form.add(txtDelete, g);

        JLabel vars = new JLabel("<html><i><small>Variaveis: %path=nome do stream | %Y/%m/%d=data | %H/%M/%S=hora</small></i></html>");
        vars.setForeground(Theme.TEXT_MUTED); g.gridx=0; g.gridy=5; g.gridwidth=3; form.add(vars, g);

        JButton btnApply = Theme.primaryButton("Gerar trecho YAML");
        btnApply.setPreferredSize(new Dimension(200, 36));
        g.gridx=0; g.gridy=6; g.gridwidth=2; form.add(btnApply, g);
        btnApply.addActionListener(e -> {
            String yaml = "record: " + (chkRecord.isSelected() ? "yes" : "no") + "\n" +
                "recordPath: " + txtPath.getText() + "\n" +
                "recordFormat: " + cmbFormat.getSelectedItem() + "\n" +
                "recordSegmentDuration: " + txtSegment.getText() + "\n" +
                "recordDeleteAfter: " + txtDelete.getText() + "\n";
            JTextArea ta = new JTextArea(yaml); ta.setFont(Theme.FONT_MONO); ta.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Trecho YAML", JOptionPane.INFORMATION_MESSAGE);
        });

        add(form, BorderLayout.CENTER);
    }

    private JLabel lbl(String t) { JLabel l=new JLabel(t); l.setFont(Theme.FONT_MEDIUM); l.setForeground(Theme.TEXT); return l; }
    private JTextField field(String v, int c) {
        JTextField f=new JTextField(v,c); f.setFont(Theme.FONT_MONO);
        f.setBackground(new Color(20,20,32)); f.setForeground(Theme.TEXT); f.setCaretColor(Theme.ACCENT);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.BORDER), new EmptyBorder(4,6,4,6)));
        return f;
    }
}
