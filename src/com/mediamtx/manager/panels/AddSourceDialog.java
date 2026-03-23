package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.theme.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class AddSourceDialog extends JDialog {

    public interface OnInsertCallback {
        void onInsert(String pathName, String yamlBlock);
    }

    private final OnInsertCallback callback;
    private JTextField   tfPath;
    private JTextField   tfUrl;
    private JTextField   tfIp;
    private JTextField   tfPort;
    private JTextField   tfUser;
    private JTextField   tfPass;
    private JComboBox<String> cbType;
    private JTextArea    taPreview;
    private JLabel       lblIp, lblPort, lblUser, lblPass;
    private JButton      btnWebCtrl;

    private static final String[] TYPES = {
        "RTMP push  (câmera / OBS → servidor)",
        "RTSP pull  (servidor puxa câmera IP)",
        "SRT push   (encoder → servidor)",
        "SRT pull   (servidor puxa SRT)",
        "FFmpeg runOnDemand  (webcam / arquivo)",
        "Câmera Android — CAMSTREAMER-BR",
        "HLS pull   (servidor puxa stream HLS)",
        "UDP/MPEG-TS  (encoder FFmpeg → servidor)",
        "RTMP pull  (servidor puxa de RTMP externo)",
        "RTSP push passivo  (câmera envia diretamente)",
        "WebRTC WHIP  (browser / OBS → servidor)",
        "Reolink / Dahua NVR  (RTSP com autenticação)"
    };

    public AddSourceDialog(Window owner, OnInsertCallback callback) {
        super(owner, "➕  Adicionar Fonte de Retransmissão", ModalityType.APPLICATION_MODAL);
        this.callback = callback;
        setSize(620, 560);
        setResizable(false);
        setLocationRelativeTo(owner);
        buildUI();
    }

    private void buildUI() {
        // MigLayout: 2 colunas — label fixa 140px, campo cresce
        JPanel panel = new JPanel(new MigLayout(
            "insets 24 28 20 28, gap 10 8",
            "[140px][grow]",
            "[][]8[][]8[][]8[][]8[][]8[grow][]"
        ));
        panel.setBackground(Theme.BG);

        // ── Nome do Path ─────────────────────────────────────────────────
        panel.add(lbl("Nome do Path:"));
        tfPath = field("ex: cam1");
        panel.add(tfPath, "growx, wrap");

        // ── Tipo de fonte ────────────────────────────────────────────────
        panel.add(lbl("Tipo de Fonte:"));
        cbType = new JComboBox<>(TYPES);
        panel.add(cbType, "growx, wrap");

        // ── URL ──────────────────────────────────────────────────────────
        panel.add(lbl("URL / Source:"));
        tfUrl = field("rtsp://192.168.1.X:554/stream");
        panel.add(tfUrl, "growx, wrap");

        // ── IP (Android / NVR) ───────────────────────────────────────────
        lblIp = lbl("IP do dispositivo:");
        panel.add(lblIp);
        tfIp = field("192.168.1.X");
        panel.add(tfIp, "growx, wrap");
        lblIp.setVisible(false); tfIp.setVisible(false);

        // ── Porta (NVR) ───────────────────────────────────────────────────
        lblPort = lbl("Porta:");
        panel.add(lblPort);
        tfPort = field("554");
        panel.add(tfPort, "growx, wrap");
        lblPort.setVisible(false); tfPort.setVisible(false);

        // ── Usuário (NVR) ─────────────────────────────────────────────────
        lblUser = lbl("Usuário:");
        panel.add(lblUser);
        tfUser = field("admin");
        panel.add(tfUser, "growx, wrap");
        lblUser.setVisible(false); tfUser.setVisible(false);

        // ── Senha (NVR) ───────────────────────────────────────────────────
        lblPass = lbl("Senha:");
        panel.add(lblPass);
        tfPass = field("senha");
        panel.add(tfPass, "growx, wrap");
        lblPass.setVisible(false); tfPass.setVisible(false);

        // ── Preview YAML ──────────────────────────────────────────────────
        panel.add(lbl("Preview YAML:"), "aligny top");
        taPreview = new JTextArea(7, 40);
        taPreview.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        taPreview.setBackground(new Color(10, 15, 30));
        taPreview.setForeground(new Color(134, 239, 172));
        taPreview.setCaretColor(Color.WHITE);
        taPreview.setEditable(false);
        taPreview.setBorder(new EmptyBorder(8, 10, 8, 10));
        JScrollPane ps = new JScrollPane(taPreview);
        ps.setBorder(BorderFactory.createLineBorder(Theme.BORDER_MED));
        panel.add(ps, "growx, growy, wrap");

        // ── Botões ────────────────────────────────────────────────────────
        panel.add(buildButtonRow(), "span 2, growx, gaptop 10");

        add(panel);

        KeyAdapter kl = new KeyAdapter() {
            public void keyReleased(KeyEvent e) { updatePreview(); }
        };
        tfPath.addKeyListener(kl); tfUrl.addKeyListener(kl);
        tfIp.addKeyListener(kl);   tfPort.addKeyListener(kl);
        tfUser.addKeyListener(kl); tfPass.addKeyListener(kl);
        cbType.addActionListener(e -> updatePreview());
        updatePreview();
    }

    private JPanel buildButtonRow() {
        JPanel row = new JPanel(new MigLayout("insets 0, gap 8", "[][][][]push", "[]"));
        row.setOpaque(false);

        btnWebCtrl = btn("\uD83D\uDD17 WebControl Android", new Color(99, 102, 241));
        btnWebCtrl.setVisible(false);
        btnWebCtrl.addActionListener(e -> {
            String ip = tfIp.getText().trim().isEmpty() ? "localhost" : tfIp.getText().trim();
            AppWindow.openBrowser("http://" + ip + ":8080");
        });

        JButton btnCancel = btn("Cancelar", new Color(71, 85, 105));
        btnCancel.addActionListener(e -> dispose());

        JButton btnInsert = btn("✔  Inserir no YAML", Theme.ACCENT);
        btnInsert.addActionListener(e -> {
            String name = tfPath.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Informe o nome do path.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (callback != null) callback.onInsert(name, taPreview.getText().trim());
            dispose();
        });

        row.add(btnWebCtrl);
        row.add(new JPanel(){{ setOpaque(false); }}, "push");
        row.add(btnCancel);
        row.add(btnInsert);
        return row;
    }

    private void updatePreview() {
        int    idx    = cbType.getSelectedIndex();
        String path   = tfPath.getText().trim().isEmpty() ? "meu_path" : tfPath.getText().trim();
        String url    = tfUrl.getText().trim();
        String ip     = tfIp.getText().trim().isEmpty()   ? "192.168.1.X" : tfIp.getText().trim();
        String port   = tfPort.getText().trim().isEmpty()  ? "554"         : tfPort.getText().trim();
        String user   = tfUser.getText().trim().isEmpty()  ? "admin"       : tfUser.getText().trim();
        String pass   = tfPass.getText().trim().isEmpty()  ? "senha"       : tfPass.getText().trim();

        boolean isAndroid = (idx == 5);
        boolean isNvr     = (idx == 11);

        lblIp.setVisible(isAndroid || isNvr);   tfIp.setVisible(isAndroid || isNvr);
        lblPort.setVisible(isNvr);               tfPort.setVisible(isNvr);
        lblUser.setVisible(isNvr);               tfUser.setVisible(isNvr);
        lblPass.setVisible(isNvr);               tfPass.setVisible(isNvr);
        if (btnWebCtrl != null) btnWebCtrl.setVisible(isAndroid);
        tfUrl.setEnabled(idx == 1 || idx == 3 || idx == 4 || idx == 6 || idx == 7 || idx == 8 || idx == 10);

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
                String src4 = url.isEmpty() ? "/dev/video0" : url;
                yaml = "paths:\n  " + path + ":\n"
                     + "    runOnDemand: >-\n"
                     + "      ffmpeg -i " + src4 + "\n"
                     + "      -c:v libx264 -preset ultrafast -b:v 1500k -g 30\n"
                     + "      -c:a aac -b:a 128k\n"
                     + "      -f rtsp rtsp://localhost:8554/$MTX_PATH\n"
                     + "    runOnDemandRestart: yes";
                break;
            case 5:
                yaml = "paths:\n  " + path + ":\n"
                     + "    source: rtsp://" + ip + ":8554/live\n"
                     + "    # WebControl do app: http://" + ip + ":8080";
                break;
            case 6:
                yaml = "paths:\n  " + path + ":\n"
                     + "    source: " + (url.isEmpty() ? "http://IP/stream.m3u8" : url) + "\n"
                     + "    sourceOnDemand: false";
                break;
            case 7:
                String src7 = url.isEmpty() ? "udp://0.0.0.0:8890" : url;
                yaml = "paths:\n  " + path + ":\n"
                     + "    runOnInit: >-\n"
                     + "      ffmpeg -i " + src7 + "\n"
                     + "      -c copy -f rtsp rtsp://localhost:8554/" + path + "\n"
                     + "    runOnInitRestart: yes";
                break;
            case 8:
                yaml = "paths:\n  " + path + ":\n"
                     + "    source: " + (url.isEmpty() ? "rtmp://IP:1935/live/" + path : url) + "\n"
                     + "    sourceOnDemand: false";
                break;
            case 9:
                yaml = "paths:\n  " + path + ":\n"
                     + "    # Câmera/encoder envia diretamente para:\n"
                     + "    # rtsp://SEU_HOST:8554/" + path + "\n"
                     + "    # Nenhuma configuração extra necessária";
                break;
            case 10:
                yaml = "paths:\n  " + path + ":\n"
                     + "    source: " + (url.isEmpty() ? "whip://SEU_HOST:8889/" + path : url) + "\n"
                     + "    # OBS WHIP endpoint: http://SEU_HOST:8889/" + path + "/whip";
                break;
            case 11:
                yaml = "paths:\n  " + path + ":\n"
                     + "    source: rtsp://" + user + ":" + pass + "@" + ip + ":" + port
                     + "/cam/realmonitor?channel=1&subtype=0\n"
                     + "    sourceOnDemand: false\n"
                     + "    # Ajuste channel= e subtype= conforme modelo do NVR";
                break;
            default: yaml = "";
        }
        taPreview.setText(yaml);
        revalidate();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_MEDIUM);
        return l;
    }

    private JTextField field(String tip) {
        JTextField tf = new JTextField();
        tf.setToolTipText(tip);
        return tf;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(Theme.FONT_MEDIUM);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
