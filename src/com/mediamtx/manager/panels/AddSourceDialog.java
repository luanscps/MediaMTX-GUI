package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class AddSourceDialog extends JDialog {

    public interface OnInsertCallback {
        void onInsert(String pathName, String yamlBlock);
    }

    private final OnInsertCallback callback;
    private JTextField tfPath;
    private JTextField tfUrl;
    private JTextField tfIp;
    private JComboBox<String> cbType;
    private JTextArea  taPreview;
    private JLabel     lblIp;
    private JButton    btnWebCtrl;

    private static final String[] TYPES = {
        "RTMP push  (câmera / OBS → servidor)",
        "RTSP pull  (servidor puxa câmera IP)",
        "SRT push   (encoder → servidor)",
        "SRT pull   (servidor puxa SRT)",
        "FFmpeg runOnDemand  (webcam / arquivo)",
        "Câmera Android — CAMSTREAMER-BR"
    };

    public AddSourceDialog(Window owner, OnInsertCallback callback) {
        super(owner, "➕  Adicionar Fonte de Retransmissão", ModalityType.APPLICATION_MODAL);
        this.callback = callback;
        setSize(560, 450);
        setResizable(false);
        setLocationRelativeTo(owner);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(20, 24, 16, 24));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;

        // Linha 0: path name
        g.gridx = 0; g.gridy = 0; g.weightx = 0.28;
        panel.add(lbl("Nome do Path:"), g);
        g.gridx = 1; g.weightx = 0.72;
        tfPath = field("ex: cam1");
        panel.add(tfPath, g);

        // Linha 1: tipo
        g.gridx = 0; g.gridy = 1; g.weightx = 0.28;
        panel.add(lbl("Tipo de Fonte:"), g);
        g.gridx = 1; g.weightx = 0.72;
        cbType = new JComboBox<>(TYPES);
        cbType.setFont(Theme.FONT_MEDIUM);
        cbType.setBackground(new Color(15, 23, 42));
        cbType.setForeground(Theme.TEXT);
        panel.add(cbType, g);

        // Linha 2: URL
        g.gridx = 0; g.gridy = 2; g.weightx = 0.28;
        panel.add(lbl("URL / Source:"), g);
        g.gridx = 1; g.weightx = 0.72;
        tfUrl = field("rtsp://192.168.1.X:8554/live");
        panel.add(tfUrl, g);

        // Linha 3: IP Android
        g.gridx = 0; g.gridy = 3; g.weightx = 0.28;
        lblIp = lbl("IP do Android:");
        panel.add(lblIp, g);
        g.gridx = 1; g.weightx = 0.72;
        tfIp = field("192.168.1.X");
        panel.add(tfIp, g);
        lblIp.setVisible(false);
        tfIp.setVisible(false);

        // Linha 4: preview YAML
        g.gridx = 0; g.gridy = 4; g.weightx = 0.28;
        panel.add(lbl("Preview YAML:"), g);
        g.gridx = 1; g.weightx = 0.72;
        taPreview = new JTextArea(6, 32);
        taPreview.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        taPreview.setBackground(new Color(10, 15, 30));
        taPreview.setForeground(new Color(134, 239, 172));
        taPreview.setCaretColor(Color.WHITE);
        taPreview.setEditable(false);
        taPreview.setBorder(new EmptyBorder(8, 10, 8, 10));
        JScrollPane ps = new JScrollPane(taPreview);
        ps.setBorder(BorderFactory.createLineBorder(Theme.BORDER_MED));
        panel.add(ps, g);

        // Linha 5: botões
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2; g.weightx = 1.0;
        g.insets = new Insets(14, 4, 4, 4);
        panel.add(buildButtonRow(), g);

        add(panel);

        KeyAdapter kl = new KeyAdapter() {
            public void keyReleased(KeyEvent e) { updatePreview(); }
        };
        tfPath.addKeyListener(kl);
        tfUrl.addKeyListener(kl);
        tfIp.addKeyListener(kl);
        cbType.addActionListener(e -> updatePreview());
        updatePreview();
    }

    private JPanel buildButtonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        row.setBackground(Theme.BG);

        btnWebCtrl = btn("\uD83D\uDD17 Abrir WebControl Android", new Color(99, 102, 241));
        btnWebCtrl.setVisible(false);
        btnWebCtrl.addActionListener(e -> {
            String ip = tfIp.getText().trim().isEmpty() ? "localhost" : tfIp.getText().trim();
            AppWindow.openBrowser("http://" + ip + ":8080");
        });

        JButton btnCancel = btn("Cancelar", new Color(100, 116, 139));
        btnCancel.addActionListener(e -> dispose());

        JButton btnInsert = btn("✔  Inserir no YAML", new Color(14, 165, 233));
        btnInsert.addActionListener(e -> {
            String name = tfPath.getText().trim();
            String yaml = taPreview.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Informe o nome do path.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (callback != null) callback.onInsert(name, yaml);
            dispose();
        });

        row.add(btnWebCtrl);
        row.add(btnCancel);
        row.add(btnInsert);
        return row;
    }

    private void updatePreview() {
        int    idx       = cbType.getSelectedIndex();
        String path      = tfPath.getText().trim().isEmpty() ? "meu_path" : tfPath.getText().trim();
        String url       = tfUrl.getText().trim();
        boolean isAndroid = (idx == 5);

        lblIp.setVisible(isAndroid);
        tfIp.setVisible(isAndroid);
        tfUrl.setEnabled(idx == 1 || idx == 3 || idx == 4);
        if (btnWebCtrl != null) btnWebCtrl.setVisible(isAndroid);

        String yaml;
        switch (idx) {
            case 0:
                yaml = "paths:\n  " + path + ":\n"
                     + "    # Publisher envia para:\n"
                     + "    # rtmp://SEU_HOST:1935/" + path;
                break;
            case 1:
                yaml = "paths:\n  " + path + ":\n"
                     + "    source: " + (url.isEmpty() ? "rtsp://IP:554/stream" : url) + "\n"
                     + "    sourceOnDemand: false";
                break;
            case 2:
                yaml = "paths:\n  " + path + ":\n"
                     + "    # Publisher envia para:\n"
                     + "    # srt://SEU_HOST:8890?streamid=publish:" + path + "&pkt_size=1316";
                break;
            case 3:
                yaml = "paths:\n  " + path + ":\n"
                     + "    source: " + (url.isEmpty() ? "srt://IP:8890?streamid=request:" + path : url);
                break;
            case 4:
                String src = url.isEmpty() ? "/dev/video0" : url;
                yaml = "paths:\n  " + path + ":\n"
                     + "    runOnDemand: >-\n"
                     + "      ffmpeg -i " + src + "\n"
                     + "      -c:v libx264 -preset ultrafast -b:v 1500k -g 30\n"
                     + "      -c:a aac -b:a 128k\n"
                     + "      -f rtsp rtsp://localhost:8554/$MTX_PATH\n"
                     + "    runOnDemandRestart: yes";
                break;
            case 5:
                String ip = tfIp.getText().trim().isEmpty() ? "192.168.1.X" : tfIp.getText().trim();
                yaml = "paths:\n  " + path + ":\n"
                     + "    source: rtsp://" + ip + ":8554/live\n"
                     + "    # WebControl do app: http://" + ip + ":8080";
                break;
            default:
                yaml = "";
        }
        taPreview.setText(yaml);
        revalidate();
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_MEDIUM);
        l.setForeground(Theme.TEXT);
        return l;
    }

    private JTextField field(String tip) {
        JTextField tf = new JTextField(22);
        tf.setFont(Theme.FONT_MEDIUM);
        tf.setBackground(new Color(15, 23, 42));
        tf.setForeground(Theme.TEXT);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_MED),
            new EmptyBorder(4, 8, 4, 8)));
        tf.setToolTipText(tip);
        return tf;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(Theme.FONT_MEDIUM);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        return b;
    }
}