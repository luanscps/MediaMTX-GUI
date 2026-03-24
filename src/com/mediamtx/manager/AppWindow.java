package com.mediamtx.manager;

import com.mediamtx.manager.panels.*;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class AppWindow extends JFrame {

    private final MediaMTXService service = new MediaMTXService();
    private LogPanel       logPanel;
    private ConfigPanel    configPanel;
    private SourcesPanel   sourcesPanel;
    private HeaderPanel    headerPanel;
    private JTabbedPane    tabs;
    private com.mediamtx.manager.panels.MetricsPanel metricsPanel;

    public AppWindow() {
        super("MediaMTX GUI v1.0.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(860, 560));
        setLocationRelativeTo(null);
        try { setIconImage(AppIcon.get()); } catch (Exception ignored) {}

        service.setLogConsumer(msg -> SwingUtilities.invokeLater(() -> {
            if (logPanel != null) logPanel.append(msg);
            if (headerPanel != null) headerPanel.updateStatus(service.isRunning());
        }));

        buildUI();
        setJMenuBar(buildMenuBar());
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────────
        headerPanel = new HeaderPanel(service, this);
        add(headerPanel, BorderLayout.NORTH);

        // ── Tabs principais ───────────────────────────────────────────────
        tabs = new JTabbedPane();

        DashboardPanel dashboard = new DashboardPanel(service);
        logPanel     = new LogPanel();
        configPanel  = new ConfigPanel(service);
        sourcesPanel = new SourcesPanel(service, this);

        // ── NOVA ABA: Assistente de Configuracao ──────────────────────────
        ConfigWizardPanel wizardPanel = new ConfigWizardPanel(service, configPanel);
        metricsPanel = new com.mediamtx.manager.panels.MetricsPanel(service);

        tabs.addTab("\uD83C\uDFE0  Dashboard",    null, dashboard,    "Visao geral e instrucoes");
        tabs.addTab("\uD83D\uDCF9  Sources",       null, sourcesPanel, "Fontes de retransmissao ativas");
        tabs.addTab("\u2699  Assistente",          null, wizardPanel,  "Configurar sem editar YAML manualmente");
        tabs.addTab("\uD83D\uDCCA  M\u00e9tricas",  null, metricsPanel, "Streams ativos e leitores em tempo real");
        tabs.addTab("\uD83D\uDCC4  Config YAML",   null, configPanel,  "Editar o mediamtx.yml diretamente");
        tabs.addTab("\uD83D\uDDCE  Log",           null, logPanel,     "Saida do processo MediaMTX");

        // Destaques nas abas
        tabs.setForegroundAt(2, Theme.SUCCESS);
        tabs.setForegroundAt(3, Theme.ACCENT);

        add(tabs, BorderLayout.CENTER);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();

        // ── Menu Arquivo ──────────────────────────────────────────────────
        JMenu mFile = new JMenu("Arquivo");
        JMenuItem miOpenBin = new JMenuItem("Abrir binario...");
        JMenuItem miOpenCfg = new JMenuItem("Abrir config YAML...");
        JMenuItem miExit    = new JMenuItem("Sair");
        miOpenBin.addActionListener(e -> service.chooseBinary(this));
        miOpenCfg.addActionListener(e -> service.chooseConfig(this));
        miExit.addActionListener(e -> {
            service.stop();
            System.exit(0);
        });
        mFile.add(miOpenBin); mFile.add(miOpenCfg);
        mFile.addSeparator(); mFile.add(miExit);

        // ── Menu Servidor ─────────────────────────────────────────────────
        JMenu mServer = new JMenu("Servidor");
        JMenuItem miStart   = new JMenuItem("Iniciar");
        JMenuItem miStop    = new JMenuItem("Parar");
        JMenuItem miRestart = new JMenuItem("Reiniciar");
        miStart.addActionListener(e -> service.start());
        miStop.addActionListener(e -> service.stop());
        miRestart.addActionListener(e -> service.restart());
        mServer.add(miStart); mServer.add(miStop); mServer.add(miRestart);

        // ── Menu Ajuda ────────────────────────────────────────────────────
        JMenu mHelp = new JMenu("Ajuda");
        JMenuItem miDocs    = new JMenuItem("Documentacao MediaMTX");
        JMenuItem miGithub  = new JMenuItem("GitHub do projeto");
        JMenuItem miAbout   = new JMenuItem("Sobre");
        miDocs.addActionListener(e ->
            openBrowser("https://github.com/bluenviron/mediamtx"));
        miGithub.addActionListener(e ->
            openBrowser("https://github.com/luanscps/MediaMTX-GUI"));
        miAbout.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "MediaMTX GUI v1.0.0\nDesenvolvido por Luan\n" +
                "https://luanscps.github.io/\n\n",
                "Sobre", JOptionPane.INFORMATION_MESSAGE));
        mHelp.add(miDocs); mHelp.add(miGithub);
        mHelp.addSeparator(); mHelp.add(miAbout);

        mb.add(mFile); mb.add(mServer); mb.add(mHelp);
        return mb;
    }

    /** Insere bloco YAML no editor e navega para a aba Config YAML */
    public void insertYamlAndNavigate(String block) {
        configPanel.appendYaml(block);
        tabs.setSelectedIndex(4); // aba Config YAML
    }

    public static void openBrowser(String url) {
        try { Desktop.getDesktop().browse(new URI(url)); }
        catch (Exception ignored) {}
    }

    @Override
    public void dispose() {
        if (sourcesPanel != null) sourcesPanel.stopRefresh();
        if (metricsPanel != null) metricsPanel.stopRefresh();
        service.stop();
        super.dispose();
    }
}
