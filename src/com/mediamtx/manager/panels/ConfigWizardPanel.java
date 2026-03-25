package com.mediamtx.manager.panels;

import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.service.YamlPresetService;
import com.mediamtx.manager.service.YamlPresetService.*;
import com.mediamtx.manager.theme.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ConfigWizardPanel extends JPanel {

    private final MediaMTXService service;
    private final ConfigPanel     configPanel;

    // Listener para notificar mudança de porta da API
    public interface ApiPortListener {
        void onApiPortChanged(int port);
    }
    private ApiPortListener apiPortListener;
    public void setApiPortListener(ApiPortListener l) { this.apiPortListener = l; }

    // Geral
    private JComboBox<LogLevel> cbLog;
    private JCheckBox  chkLogFile;
    private JTextField tfLogFile, tfReadTimeout, tfWriteTimeout;
    private JSpinner   spWriteQueue, spUdpPayload;

    // Protocolos
    private JCheckBox  chkRtsp, chkRtmp, chkHls, chkWebrtc, chkSrt, chkApi;
    private JTextField tfPortRtsp, tfPortRtmp, tfPortHls, tfPortWebrtc, tfPortSrt, tfPortApi;

    // RTSP avancado
    private JComboBox<RtspEncryption> cbRtspEnc;
    private JCheckBox  chkRtspUdp, chkRtspMulticast;
    private JTextField tfPortRtp, tfPortRtcp, tfPortRtsps;

    // RTMP avancado
    private JComboBox<RtmpEncryption> cbRtmpEnc;
    private JTextField tfPortRtmps;

    // HLS avancado
    private JComboBox<HlsVariant> cbHlsVariant;
    private JSpinner   spHlsSegCount;
    private JTextField tfHlsSegDur, tfHlsPartDur, tfHlsDir;
    private JCheckBox  chkHlsAlways;

    // WebRTC avancado
    private JTextField tfPortWebrtcUDP, tfIceServer;
    private JCheckBox  chkWebrtcIce, chkWebrtcIPs;

    // Metricas / Playback
    private JCheckBox  chkMetrics, chkPlayback;
    private JTextField tfPortMetrics, tfPortPlayback;

    // Auth
    private JComboBox<AuthMode> cbAuth;
    private JTextField    tfUser, tfAuthHTTP, tfJwtJWKS, tfJwtKey;
    private JPasswordField tfPass;
    private JPanel pnlAuthSimples, pnlAuthHTTP, pnlAuthJWT;

    // Gravacao
    private JComboBox<RecordMode>   cbRecord;
    private JComboBox<RecordFormat> cbRecordFmt;
    private JComboBox<DeleteAfter>  cbDeleteAfter;
    private JTextField tfRecordPath, tfRecordPartDur;
    private JPanel pnlRecordOpts;

    // Qualidade
    private JComboBox<StreamQuality> cbQuality;

    // Path defaults
    private JCheckBox  chkSourceOnDemand, chkOverridePublisher, chkAbsTs;
    private JSpinner   spMaxReaders;
    private JTextField tfRunOnInit, tfRunOnDemand, tfRunOnReady, tfRunOnRead;
    private JTextField tfRunOnConnect, tfRunOnDisconnect;
    private JCheckBox  chkRunOnInitRestart, chkRunOnDemandRestart,
                       chkRunOnReadyRestart, chkRunOnReadRestart,
                       chkRunOnConnectRestart, chkRunOnDisconnectRestart;

    public ConfigWizardPanel(MediaMTXService service, ConfigPanel configPanel) {
        this.service     = service;
        this.configPanel = configPanel;
        setLayout(new MigLayout("insets 0", "[grow]", "[grow][]"));
        setBackground(Theme.BG);

        JScrollPane scroll = new JScrollPane(buildForm());
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll,        "grow, wrap");
        add(buildFooter(), "growx, h 52!");
    }

    public int getApiPort() {
        try { return Integer.parseInt(tfPortApi.getText().trim()); }
        catch (NumberFormatException e) { return 9997; }
    }

    private JPanel buildForm() {
        JPanel p = new JPanel(new MigLayout("insets 20, gap 0 14", "[grow]", "[]"));
        p.setBackground(Theme.BG);
        p.add(buildGeralCard(),        "growx, wrap");
        p.add(buildProtocolosCard(),   "growx, wrap");
        p.add(buildRtspCard(),         "growx, wrap");
        p.add(buildRtmpCard(),         "growx, wrap");
        p.add(buildHlsCard(),          "growx, wrap");
        p.add(buildWebrtcCard(),       "growx, wrap");
        p.add(buildServicosCard(),     "growx, wrap");
        p.add(buildAuthCard(),         "growx, wrap");
        p.add(buildGravacaoCard(),     "growx, wrap");
        p.add(buildQualidadeCard(),    "growx, wrap");
        p.add(buildPathDefaultsCard(), "growx, wrap");
        return p;
    }

    private static final String COL4 = "[160px][grow][160px][grow]";
    private static final String COL2 = "[160px][grow]";

    private JPanel makeCard(String title, Color accent) {
        JPanel card = new JPanel(new MigLayout("insets 0, gap 0", "[grow]", "[][grow]"));
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, accent),
            BorderFactory.createLineBorder(Theme.BORDER)));

        JLabel lbl = new JLabel("  " + title);
        lbl.setFont(Theme.FONT_BOLD);
        lbl.setForeground(accent);
        lbl.setBorder(new EmptyBorder(10, 10, 6, 10));
        lbl.setBackground(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 18));
        lbl.setOpaque(true);

        JPanel body = new JPanel(new MigLayout("insets 12 16 14 16, gap 10 8", COL4, ""));
        body.setBackground(Theme.BG_CARD);

        card.add(lbl,  "growx, wrap");
        card.add(body, "grow");
        return card;
    }

    private JPanel body(JPanel card) { return (JPanel) card.getComponent(1); }

    private JPanel buildGeralCard() {
        JPanel card = makeCard("Configuracoes Gerais", Theme.TEXT);
        JPanel b = body(card);

        b.add(lbl("Nivel de log:"));
        cbLog = new JComboBox<>(LogLevel.values());
        cbLog.setSelectedItem(LogLevel.INFO);
        b.add(cbLog, "growx");

        b.add(lbl("Read timeout:"));
        tfReadTimeout = tf("10s");
        b.add(tfReadTimeout, "growx, wrap");

        chkLogFile = chk("Salvar log em arquivo");
        b.add(chkLogFile, "span 2");

        b.add(lbl("Write timeout:"));
        tfWriteTimeout = tf("10s");
        b.add(tfWriteTimeout, "growx, wrap");

        b.add(lbl("Arquivo de log:"));
        tfLogFile = tf("mediamtx.log");
        b.add(tfLogFile, "growx");

        b.add(lbl("Write queue size:"));
        spWriteQueue = spinner(512, 64, 65536, 64);
        b.add(spWriteQueue, "growx, wrap");

        b.add(lbl("UDP max payload:"));
        spUdpPayload = spinner(1452, 512, 65507, 8);
        b.add(spUdpPayload, "growx, wrap");

        return card;
    }

    private JPanel buildProtocolosCard() {
        JPanel card = makeCard("Protocolos — Habilitar / Porta", Theme.ACCENT);
        JPanel b = body(card);
        b.setLayout(new MigLayout("insets 12 16 14 16, gap 10 8",
                                   "[180px][80px][180px][80px]", ""));

        chkRtsp   = chk("RTSP");   chkRtsp.setSelected(true);
        chkRtmp   = chk("RTMP");   chkRtmp.setSelected(true);
        chkHls    = chk("HLS");    chkHls.setSelected(true);
        chkWebrtc = chk("WebRTC"); chkWebrtc.setSelected(true);
        chkSrt    = chk("SRT");    chkSrt.setSelected(true);
        chkApi    = chk("API REST"); chkApi.setSelected(true);

        tfPortRtsp   = tf("8554");
        tfPortRtmp   = tf("1935");
        tfPortHls    = tf("8888");
        tfPortWebrtc = tf("8889");
        tfPortSrt    = tf("8890");
        tfPortApi    = tf("9997");

        // Notifica mudança de porta da API
        tfPortApi.addActionListener(e -> notifyApiPort());
        tfPortApi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) { notifyApiPort(); }
        });

        Color[] colors = {Theme.ACCENT, Theme.ACCENT2, Theme.WARNING,
                          Theme.SUCCESS, Theme.PURPLE, new Color(71,85,105)};
        JCheckBox[] cbs = {chkRtsp, chkRtmp, chkHls, chkWebrtc, chkSrt, chkApi};
        JTextField[] pts = {tfPortRtsp, tfPortRtmp, tfPortHls, tfPortWebrtc, tfPortSrt, tfPortApi};

        for (int i = 0; i < 6; i++) {
            cbs[i].setFont(Theme.FONT_BOLD);
            cbs[i].setForeground(colors[i]);
            b.add(cbs[i]);
            b.add(pts[i], "growx" + (i % 2 == 1 ? ", wrap" : ""));
        }
        return card;
    }

    private void notifyApiPort() {
        if (apiPortListener != null) {
            try {
                int port = Integer.parseInt(tfPortApi.getText().trim());
                apiPortListener.onApiPortChanged(port);
            } catch (NumberFormatException ignored) {}
        }
    }

    private JPanel buildRtspCard() {
        JPanel card = makeCard("RTSP — Configuracoes Avancadas", Theme.ACCENT);
        JPanel b = body(card);

        b.add(lbl("Criptografia:"));
        cbRtspEnc = new JComboBox<>(RtspEncryption.values());
        b.add(cbRtspEnc, "growx");
        b.add(lbl("Porta RTSPS (TLS):"));
        tfPortRtsps = tf("8322");
        b.add(tfPortRtsps, "growx, wrap");

        chkRtspUdp = chk("Habilitar UDP (RTP/RTCP)");
        chkRtspUdp.setSelected(true);
        b.add(chkRtspUdp, "span 2");
        b.add(lbl("Porta RTP:"));
        tfPortRtp = tf("8000");
        b.add(tfPortRtp, "growx, wrap");

        chkRtspMulticast = chk("Habilitar Multicast");
        b.add(chkRtspMulticast, "span 2");
        b.add(lbl("Porta RTCP:"));
        tfPortRtcp = tf("8001");
        b.add(tfPortRtcp, "growx, wrap");

        return card;
    }

    private JPanel buildRtmpCard() {
        JPanel card = makeCard("RTMP — Configuracoes Avancadas", Theme.ACCENT2);
        JPanel b = body(card);

        b.add(lbl("Criptografia RTMP:"));
        cbRtmpEnc = new JComboBox<>(RtmpEncryption.values());
        b.add(cbRtmpEnc, "growx");
        b.add(lbl("Porta RTMPS:"));
        tfPortRtmps = tf("1936");
        b.add(tfPortRtmps, "growx, wrap");

        return card;
    }

    private JPanel buildHlsCard() {
        JPanel card = makeCard("HLS — Configuracoes Avancadas", Theme.WARNING);
        JPanel b = body(card);

        b.add(lbl("Variante HLS:"));
        cbHlsVariant = new JComboBox<>(HlsVariant.values());
        b.add(cbHlsVariant, "span 3, growx, wrap");

        b.add(lbl("Segmentos em cache:"));
        spHlsSegCount = spinner(7, 1, 50, 1);
        b.add(spHlsSegCount, "growx");
        b.add(lbl("Duracao segmento:"));
        tfHlsSegDur = tf("1s");
        b.add(tfHlsSegDur, "growx, wrap");

        b.add(lbl("Duracao part (LL-HLS):"));
        tfHlsPartDur = tf("200ms");
        b.add(tfHlsPartDur, "growx");
        chkHlsAlways = chk("Gerar HLS sempre (alwaysRemux)");
        b.add(chkHlsAlways, "span 2, wrap");

        b.add(lbl("Diretorio HLS (vazio=RAM):"));
        tfHlsDir = tf("");
        b.add(tfHlsDir, "span 3, growx, wrap");

        return card;
    }

    private JPanel buildWebrtcCard() {
        JPanel card = makeCard("WebRTC — Configuracoes Avancadas", Theme.SUCCESS);
        JPanel b = body(card);

        b.add(lbl("Porta UDP local:"));
        tfPortWebrtcUDP = tf("8189");
        b.add(tfPortWebrtcUDP, "growx");
        chkWebrtcIPs = chk("Detectar IPs por interfaces");
        chkWebrtcIPs.setSelected(true);
        b.add(chkWebrtcIPs, "span 2, wrap");

        chkWebrtcIce = chk("ICE Server externo (STUN/TURN)");
        b.add(chkWebrtcIce, "span 2");
        b.add(lbl("ICE Server URL:"));
        tfIceServer = tf("stun:stun.l.google.com:19302");
        b.add(tfIceServer, "growx, wrap");
        tfIceServer.setEnabled(false);
        chkWebrtcIce.addActionListener(e -> tfIceServer.setEnabled(chkWebrtcIce.isSelected()));

        return card;
    }

    private JPanel buildServicosCard() {
        JPanel card = makeCard("Servicos Extras (Metrics / Playback)", new Color(71,85,105));
        JPanel b = body(card);

        chkMetrics = chk("Habilitar Metrics (Prometheus)");
        b.add(chkMetrics, "span 2");
        b.add(lbl("Porta Metrics:"));
        tfPortMetrics = tf("9998");
        b.add(tfPortMetrics, "growx, wrap");

        chkPlayback = chk("Habilitar Playback server");
        b.add(chkPlayback, "span 2");
        b.add(lbl("Porta Playback:"));
        tfPortPlayback = tf("9996");
        b.add(tfPortPlayback, "growx, wrap");

        return card;
    }

    private JPanel buildAuthCard() {
        JPanel card = makeCard("Autenticacao", Theme.DANGER);
        JPanel b = body(card);

        b.add(lbl("Metodo:"));
        cbAuth = new JComboBox<>(AuthMode.values());
        b.add(cbAuth, "span 3, growx, wrap");

        // Painel usuario+senha (com JPasswordField + toggle)
        pnlAuthSimples = subPanel(b, "span 4, growx, wrap");
        pnlAuthSimples.add(lbl("Usuario:"));
        tfUser = tf("admin");
        pnlAuthSimples.add(tfUser, "growx");

        pnlAuthSimples.add(lbl("Senha:"));
        JPanel passRow = new JPanel(new BorderLayout(4, 0));
        passRow.setOpaque(false);
        tfPass = new JPasswordField("senha123");
        tfPass.setFont(Theme.FONT_MEDIUM);
        JButton btnShow = new JButton("👁");
        btnShow.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        btnShow.setFocusPainted(false);
        btnShow.setBorderPainted(false);
        btnShow.setBackground(Theme.BG_CARD);
        btnShow.setForeground(Theme.TEXT_DIM);
        btnShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnShow.setPreferredSize(new Dimension(36, 28));
        btnShow.addActionListener(e -> {
            if (tfPass.getEchoChar() == 0) {
                tfPass.setEchoChar('●');
                btnShow.setText("👁");
            } else {
                tfPass.setEchoChar((char) 0);
                btnShow.setText("🙈");
            }
        });
        tfPass.setEchoChar('●');
        passRow.add(tfPass, BorderLayout.CENTER);
        passRow.add(btnShow, BorderLayout.EAST);
        pnlAuthSimples.add(passRow, "growx, wrap");

        // Painel HTTP externo
        pnlAuthHTTP = subPanel(b, "span 4, growx, wrap");
        pnlAuthHTTP.add(lbl("URL de autenticacao HTTP:"));
        tfAuthHTTP = tf("http://localhost:9000/auth");
        pnlAuthHTTP.add(tfAuthHTTP, "span 3, growx, wrap");

        // Painel JWT
        pnlAuthJWT = subPanel(b, "span 4, growx, wrap");
        pnlAuthJWT.add(lbl("JWKS URL:"));
        tfJwtJWKS = tf("http://localhost:9000/.well-known/jwks.json");
        pnlAuthJWT.add(tfJwtJWKS, "growx");
        pnlAuthJWT.add(lbl("Claim key:"));
        tfJwtKey = tf("mediamtx_permissions");
        pnlAuthJWT.add(tfJwtKey, "growx, wrap");

        hideAuthPanels();
        cbAuth.addActionListener(e -> {
            hideAuthPanels();
            AuthMode m = (AuthMode) cbAuth.getSelectedItem();
            if (m == AuthMode.SIMPLES)   pnlAuthSimples.setVisible(true);
            else if (m == AuthMode.HTTP) pnlAuthHTTP.setVisible(true);
            else if (m == AuthMode.JWT)  pnlAuthJWT.setVisible(true);
            b.revalidate(); b.repaint();
        });
        return card;
    }

    private void hideAuthPanels() {
        if (pnlAuthSimples != null) pnlAuthSimples.setVisible(false);
        if (pnlAuthHTTP    != null) pnlAuthHTTP.setVisible(false);
        if (pnlAuthJWT     != null) pnlAuthJWT.setVisible(false);
    }

    private JPanel buildGravacaoCard() {
        JPanel card = makeCard("Gravacao Local (Record)", Theme.ORANGE);
        JPanel b = body(card);

        b.add(lbl("Modo:"));
        cbRecord = new JComboBox<>(RecordMode.values());
        b.add(cbRecord, "span 3, growx, wrap");

        pnlRecordOpts = subPanel(b, "span 4, growx, wrap");
        pnlRecordOpts.add(lbl("Formato:"));
        cbRecordFmt = new JComboBox<>(RecordFormat.values());
        pnlRecordOpts.add(cbRecordFmt, "growx");
        pnlRecordOpts.add(lbl("Deletar apos:"));
        cbDeleteAfter = new JComboBox<>(DeleteAfter.values());
        cbDeleteAfter.setSelectedItem(DeleteAfter.D1);
        pnlRecordOpts.add(cbDeleteAfter, "growx, wrap");

        pnlRecordOpts.add(lbl("Pasta de gravacao:"));
        tfRecordPath = tf("./recordings/%path/%Y-%m-%d_%H-%M-%S-%f");
        pnlRecordOpts.add(tfRecordPath, "span 3, growx, wrap");

        pnlRecordOpts.add(lbl("Duracao da parte (RPO):"));
        tfRecordPartDur = tf("1s");
        pnlRecordOpts.add(tfRecordPartDur, "growx, wrap");

        pnlRecordOpts.setVisible(false);
        cbRecord.addActionListener(e -> {
            boolean on = cbRecord.getSelectedItem() != RecordMode.DESATIVADA;
            pnlRecordOpts.setVisible(on);
            b.revalidate(); b.repaint();
        });
        return card;
    }

    private JPanel buildQualidadeCard() {
        JPanel card = makeCard("Qualidade do Stream (referencia para paths)", Theme.TEAL);
        JPanel b = body(card);

        b.add(lbl("Preset de qualidade:"));
        cbQuality = new JComboBox<>(StreamQuality.values());
        cbQuality.setSelectedItem(StreamQuality.P720_MEDIA);
        b.add(cbQuality, "span 3, growx, wrap");

        JLabel detail = new JLabel(" ");
        detail.setFont(Theme.FONT_SMALL);
        detail.setForeground(Theme.TEXT_DIM);
        b.add(new JLabel(""), "");
        b.add(detail, "span 3, growx, wrap");

        Runnable update = () -> {
            StreamQuality q = (StreamQuality) cbQuality.getSelectedItem();
            if (q != null)
                detail.setText("Bitrate: " + q.bitrate
                    + "   Preset FFmpeg: " + q.preset
                    + "   GOP: " + q.gop
                    + "   Altura: " + q.height + "p");
        };
        cbQuality.addActionListener(e -> update.run());
        update.run();
        return card;
    }

    private JPanel buildPathDefaultsCard() {
        JPanel card = makeCard("Path Defaults (comportamento padrao de todos os paths)", Theme.PURPLE);
        JPanel b = body(card);

        chkSourceOnDemand    = chk("sourceOnDemand (puxa source so quando ha leitores)");
        chkOverridePublisher = chk("overridePublisher (permite substituir publisher ativo)");
        chkOverridePublisher.setSelected(true);
        chkAbsTs             = chk("useAbsoluteTimestamp");

        b.add(chkSourceOnDemand, "span 2");
        b.add(lbl("Max leitores (0=ilimitado):"));
        spMaxReaders = spinner(0, 0, 9999, 1);
        b.add(spMaxReaders, "growx, wrap");

        b.add(chkOverridePublisher, "span 2");
        b.add(chkAbsTs, "span 2, wrap");

        // Hooks
        JLabel sec = new JLabel("Hooks — comandos executados em eventos do path");
        sec.setFont(Theme.FONT_BOLD);
        sec.setForeground(Theme.TEXT_DIM);
        sec.setBorder(new EmptyBorder(8, 0, 4, 0));
        b.add(sec, "span 4, wrap");

        String[] hookNames = {
            "runOnInit", "runOnDemand", "runOnReady", "runOnRead",
            "runOnConnect", "runOnDisconnect"
        };
        JTextField[] tfs = new JTextField[hookNames.length];
        JCheckBox[]  cks = new JCheckBox[hookNames.length];
        for (int i = 0; i < hookNames.length; i++) {
            tfs[i] = tf("");
            cks[i] = chk("Restart");
            b.add(lbl(hookNames[i] + ":"));
            b.add(tfs[i], "growx");
            b.add(cks[i], "span 2, wrap");
        }
        tfRunOnInit               = tfs[0]; chkRunOnInitRestart         = cks[0];
        tfRunOnDemand             = tfs[1]; chkRunOnDemandRestart        = cks[1];
        tfRunOnReady              = tfs[2]; chkRunOnReadyRestart         = cks[2];
        tfRunOnRead               = tfs[3]; chkRunOnReadRestart          = cks[3];
        tfRunOnConnect            = tfs[4]; chkRunOnConnectRestart       = cks[4];
        tfRunOnDisconnect         = tfs[5]; chkRunOnDisconnectRestart    = cks[5];

        return card;
    }

    private JPanel buildFooter() {
        JPanel f = new JPanel(new MigLayout("insets 8 20 8 20, gap 10",
                                            "[]push[][]", "[]"));
        f.setBackground(Color.WHITE);
        f.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));

        JLabel hint = new JLabel("Configure acima e clique em Gerar + Aplicar para salvar o mediamtx.yml");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.TEXT_DIM);
        f.add(hint);

        JButton btnPreview = Theme.primaryButton("Previsualizar YAML");
        btnPreview.addActionListener(e -> previewYaml());
        f.add(btnPreview);

        JButton btnApply = Theme.successButton("Gerar + Aplicar");
        btnApply.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        btnApply.addActionListener(e -> applyYaml());
        f.add(btnApply);

        return f;
    }

    private Config buildConfig() {
        Config c = new Config();
        c.logLevel           = (LogLevel) cbLog.getSelectedItem();
        c.logToFile          = chkLogFile.isSelected();
        c.logFile            = tfLogFile.getText().trim();
        c.readTimeout        = tfReadTimeout.getText().trim();
        c.writeTimeout       = tfWriteTimeout.getText().trim();
        c.writeQueueSize     = (int) spWriteQueue.getValue();
        c.udpMaxPayloadSize  = (int) spUdpPayload.getValue();

        c.enableRtsp   = chkRtsp.isSelected();
        c.enableRtmp   = chkRtmp.isSelected();
        c.enableHls    = chkHls.isSelected();
        c.enableWebrtc = chkWebrtc.isSelected();
        c.enableSrt    = chkSrt.isSelected();
        c.enableApi    = chkApi.isSelected();
        c.portRtsp     = tfPortRtsp.getText().trim();
        c.portRtmp     = tfPortRtmp.getText().trim();
        c.portHls      = tfPortHls.getText().trim();
        c.portWebrtc   = tfPortWebrtc.getText().trim();
        c.portSrt      = tfPortSrt.getText().trim();
        c.portApi      = tfPortApi.getText().trim();

        c.rtspEncryption = (RtspEncryption) cbRtspEnc.getSelectedItem();
        c.rtspUdp        = chkRtspUdp.isSelected();
        c.rtspMulticast  = chkRtspMulticast.isSelected();
        c.portRtsps      = tfPortRtsps.getText().trim();
        c.portRtp        = tfPortRtp.getText().trim();
        c.portRtcp       = tfPortRtcp.getText().trim();

        c.rtmpEncryption = (RtmpEncryption) cbRtmpEnc.getSelectedItem();
        c.portRtmps      = tfPortRtmps.getText().trim();

        c.hlsVariant         = (HlsVariant) cbHlsVariant.getSelectedItem();
        c.hlsSegmentCount    = (int) spHlsSegCount.getValue();
        c.hlsSegmentDuration = tfHlsSegDur.getText().trim();
        c.hlsPartDuration    = tfHlsPartDur.getText().trim();
        c.hlsAlwaysRemux     = chkHlsAlways.isSelected();
        c.hlsDirectory       = tfHlsDir.getText().trim();

        c.portWebrtcUDP           = tfPortWebrtcUDP.getText().trim();
        c.webrtcIce               = chkWebrtcIce.isSelected();
        c.webrtcIceServer         = tfIceServer.getText().trim();
        c.webrtcIPsFromInterfaces = chkWebrtcIPs.isSelected();

        c.enableMetrics  = chkMetrics.isSelected();
        c.enablePlayback = chkPlayback.isSelected();
        c.portMetrics    = tfPortMetrics.getText().trim();
        c.portPlayback   = tfPortPlayback.getText().trim();

        c.authMode        = (AuthMode) cbAuth.getSelectedItem();
        c.authUser        = tfUser.getText().trim();
        c.authPass        = new String(tfPass.getPassword()).trim();
        c.authHTTPAddress = tfAuthHTTP.getText().trim();
        c.authJWTJWKS     = tfJwtJWKS.getText().trim();
        c.authJWTClaimKey = tfJwtKey.getText().trim();

        c.recordMode         = (RecordMode)   cbRecord.getSelectedItem();
        c.recordFormat       = (RecordFormat) cbRecordFmt.getSelectedItem();
        c.recordDeleteAfter  = (DeleteAfter)  cbDeleteAfter.getSelectedItem();
        c.recordPath         = tfRecordPath.getText().trim();
        c.recordPartDuration = tfRecordPartDur.getText().trim();

        c.sourceOnDemand       = chkSourceOnDemand.isSelected();
        c.overridePublisher    = chkOverridePublisher.isSelected();
        c.useAbsoluteTimestamp = chkAbsTs.isSelected();
        c.maxReaders           = (int) spMaxReaders.getValue();
        c.runOnInit            = tfRunOnInit.getText().trim();
        c.runOnInitRestart     = chkRunOnInitRestart.isSelected();
        c.runOnDemand          = tfRunOnDemand.getText().trim();
        c.runOnDemandRestart   = chkRunOnDemandRestart.isSelected();
        c.runOnReady           = tfRunOnReady.getText().trim();
        c.runOnReadyRestart    = chkRunOnReadyRestart.isSelected();
        c.runOnRead            = tfRunOnRead.getText().trim();
        c.runOnReadRestart     = chkRunOnReadRestart.isSelected();
        return c;
    }

    private void previewYaml() {
        String yaml = YamlPresetService.generate(buildConfig());
        JTextArea ta = new JTextArea(yaml);
        ta.setFont(Theme.FONT_MONO);
        ta.setEditable(false);
        ta.setBackground(Theme.TEXT);
        ta.setForeground(new Color(106, 153, 85));
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(760, 520));
        JOptionPane.showMessageDialog(this, sp,
            "Previsualizar YAML gerado", JOptionPane.PLAIN_MESSAGE);
    }

    private void applyYaml() {
        String yaml = YamlPresetService.generate(buildConfig());
        service.saveConfigContent(yaml);
        configPanel.setYaml(yaml);
        notifyApiPort();
        JOptionPane.showMessageDialog(this,
            "mediamtx.yml salvo com sucesso!\nVeja na aba Config YAML ou inicie o servidor.",
            "Aplicado com sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(Theme.FONT_MEDIUM);
        l.setForeground(Theme.TEXT);
        return l;
    }
    private JTextField tf(String val) {
        JTextField f = new JTextField(val);
        f.setFont(Theme.FONT_MEDIUM);
        return f;
    }
    private JCheckBox chk(String t) {
        JCheckBox cb = new JCheckBox(t);
        cb.setFont(Theme.FONT_MEDIUM);
        cb.setOpaque(false);
        return cb;
    }
    private JSpinner spinner(int val, int min, int max, int step) {
        return new JSpinner(new SpinnerNumberModel(val, min, max, step));
    }
    private JPanel subPanel(JPanel parent, String constraints) {
        JPanel p = new JPanel(new MigLayout("insets 4 0 0 0, gap 10 6",
                                            "[160px][grow][160px][grow]", ""));
        p.setOpaque(false);
        parent.add(p, constraints);
        return p;
    }
}
