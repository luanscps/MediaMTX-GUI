package com.mediamtx.manager;

import com.mediamtx.manager.panels.*;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class AppWindow extends JFrame {

    private final MediaMTXService service = new MediaMTXService();
    private LogPanel         logPanel;
    private ConfigPanel      configPanel;
    private SourcesPanel     sourcesPanel;
    private HeaderPanel      headerPanel;
    private MetricsPanel     metricsPanel;
    private RecordingsPanel  recordingsPanel;
    private JTabbedPane      tabs;

    public AppWindow() {
        super("MediaMTX GUI v1.2.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(860, 560));
        setLocationRelativeTo(null);
        try { setIconImage(AppIcon.get()); } catch (Exception ignored) {}

        service.setLogConsumer(msg -> SwingUtilities.invokeLater(() -> {
            if (logPanel    != null) logPanel.append(msg);
            if (headerPanel != null) headerPanel.updateStatus(service.isRunning());
        }));

        buildUI();
        setJMenuBar(buildMenuBar());
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        headerPanel     = new HeaderPanel(service, this);
        add(headerPanel, BorderLayout.NORTH);

        tabs = new JTabbedPane();

        DashboardPanel   dashboard  = new DashboardPanel(service);
        logPanel         = new LogPanel();
        configPanel      = new ConfigPanel(service);
        sourcesPanel     = new SourcesPanel(service, this);
        metricsPanel     = new MetricsPanel(service);
        recordingsPanel  = new RecordingsPanel(service);

        ConfigWizardPanel wizardPanel = new ConfigWizardPanel(service, configPanel);

        wizardPanel.setApiPortListener(port -> {
            sourcesPanel.setApiPort(port);
            metricsPanel.setApiPort(port);
            recordingsPanel.setApiPort(port);
        });

        tabs.addTab("🏠  Dashboard",    null, dashboard,       "Visao geral e instrucoes");
        tabs.addTab("📹  Sources",       null, sourcesPanel,    "Fontes de retransmissao ativas");
        tabs.addTab("⚙  Assistente",          null, wizardPanel,     "Configurar sem editar YAML manualmente");
        tabs.addTab("📊  Metricas",      null, metricsPanel,    "Streams ativos e leitores em tempo real");
        tabs.addTab("📀  Gravacoes",     null, recordingsPanel, "Gravacoes locais via API REST");
        tabs.addTab("📄  Config YAML",   null, configPanel,     "Editar o mediamtx.yml diretamente");
        tabs.addTab("🗎  Log",           null, logPanel,        "Saida do processo MediaMTX");

        tabs.setForegroundAt(2, Theme.SUCCESS);
        tabs.setForegroundAt(3, Theme.ACCENT);
        tabs.setForegroundAt(4, Theme.ORANGE);

        add(tabs, BorderLayout.CENTER);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();

        JMenu mFile = new JMenu("Arquivo");
        JMenuItem miOpenBin = new JMenuItem("Abrir binario...");
        JMenuItem miOpenCfg = new JMenuItem("Abrir config YAML...");
        JMenuItem miExit    = new JMenuItem("Sair");
        miOpenBin.addActionListener(e -> service.chooseBinary(this));
        miOpenCfg.addActionListener(e -> service.chooseConfig(this));
        miExit.addActionListener(e -> { service.stop(); System.exit(0); });
        mFile.add(miOpenBin); mFile.add(miOpenCfg);
        mFile.addSeparator(); mFile.add(miExit);

        JMenu mServer = new JMenu("Servidor");
        JMenuItem miStart   = new JMenuItem("Iniciar");
        JMenuItem miStop    = new JMenuItem("Parar");
        JMenuItem miRestart = new JMenuItem("Reiniciar");
        miStart.addActionListener(e -> service.start());
        miStop.addActionListener(e -> service.stop());
        miRestart.addActionListener(e -> service.restart());
        mServer.add(miStart); mServer.add(miStop); mServer.add(miRestart);

        JMenu mHelp = new JMenu("Ajuda");
        JMenuItem miDocs   = new JMenuItem("Documentacao MediaMTX");
        JMenuItem miGithub = new JMenuItem("GitHub do projeto");
        JMenuItem miAbout  = new JMenuItem("Sobre");
        miDocs.addActionListener(e -> openBrowser("https://github.com/bluenviron/mediamtx"));
        miGithub.addActionListener(e -> openBrowser("https://github.com/luanscps/MediaMTX-GUI"));
        miAbout.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "MediaMTX GUI v1.2.0\nDesenvolvido por Luan\nhttps://luanscps.github.io/",
                "Sobre", JOptionPane.INFORMATION_MESSAGE));
        mHelp.add(miDocs); mHelp.add(miGithub);
        mHelp.addSeparator(); mHelp.add(miAbout);

        mb.add(mFile); mb.add(mServer); mb.add(mHelp);
        return mb;
    }

    public void insertYamlAndNavigate(String block) {
        configPanel.appendYaml(block);
        tabs.setSelectedIndex(5);
    }

    public static void openBrowser(String url) {
        try { Desktop.getDesktop().browse(new URI(url)); }
        catch (Exception ignored) {}
    }

    @Override
    public void dispose() {
        if (sourcesPanel    != null) sourcesPanel.stopRefresh();
        if (metricsPanel    != null) metricsPanel.stopRefresh();
        if (recordingsPanel != null) recordingsPanel.stopRefresh();
        service.stop();
        super.dispose();
    }
}
