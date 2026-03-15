package com.mediamtx.manager;

import com.mediamtx.manager.panels.*;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class AppWindow extends JFrame {

    public static final String VERSION    = "1.0.0";
    public static final String REPO_URL   = "https://github.com/luanscps/MediaMTX-GUI";
    public static final String MEDIAMTX_URL = "https://github.com/bluenviron/mediamtx";
    public static final String AUTHOR     = "Luan Silva";
    public static final String AUTHOR_URL = "https://github.com/luanscps";

    private final MediaMTXService service = new MediaMTXService();
    private HeaderPanel  headerPanel;
    private LogPanel     logPanel;
    private SidebarPanel sidebarPanel;
    private JTabbedPane  tabbedPane;

    public AppWindow() {
        setTitle("MediaMTX GUI \u2014 v" + VERSION);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setIconImage(AppIcon.get());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (service.isRunning()) {
                    int opt = JOptionPane.showConfirmDialog(AppWindow.this,
                        "O servidor MediaMTX esta rodando.\nDeseja parar e sair?",
                        "Confirmacao", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (opt != JOptionPane.YES_OPTION) return;
                    service.stop();
                }
                dispose(); System.exit(0);
            }
        });

        buildMenuBar();
        buildUI();
        service.setLogConsumer(msg -> SwingUtilities.invokeLater(() -> {
            logPanel.append(msg);
            headerPanel.updateStatus(service.isRunning());
            sidebarPanel.updateStatus(service.isRunning());
        }));
    }

    private void buildMenuBar() {
        JMenuBar bar = new JMenuBar();
        bar.setBorder(new EmptyBorder(2, 4, 2, 4));

        JMenu mArquivo = menu("Arquivo");
        mArquivo.add(item("Abrir binario...",    e -> service.chooseBinary(this)));
        mArquivo.add(item("Abrir config YML...", e -> service.chooseConfig(this)));
        mArquivo.addSeparator();
        mArquivo.add(item("Sair", e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))));

        JMenu mServidor = menu("Servidor");
        mServidor.add(item("\u25b6  Iniciar",   e -> service.start()));
        mServidor.add(item("\u25a0  Parar",     e -> service.stop()));
        mServidor.add(item("\u21ba  Reiniciar", e -> service.restart()));
        mServidor.addSeparator();
        mServidor.add(item("Abrir API no browser", e -> openBrowser("http://localhost:9997")));

        JMenu mView = menu("Visualizar");
        mView.add(item("Limpar log",    e -> logPanel.clear()));
        mView.add(item("Salvar log...", e -> logPanel.saveToFile(this)));

        JMenu mAjuda = menu("Ajuda");
        mAjuda.add(item("Documentacao MediaMTX",    e -> openBrowser(MEDIAMTX_URL)));
        mAjuda.add(item("Repositorio MediaMTX-GUI", e -> openBrowser(REPO_URL)));
        mAjuda.addSeparator();
        mAjuda.add(item("Sobre", e -> showAbout()));

        bar.add(mArquivo); bar.add(mServidor); bar.add(mView); bar.add(mAjuda);
        setJMenuBar(bar);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);

        headerPanel  = new HeaderPanel(service, this);
        root.add(headerPanel, BorderLayout.NORTH);

        sidebarPanel = new SidebarPanel(service, this);
        root.add(sidebarPanel, BorderLayout.WEST);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(Theme.FONT_MEDIUM);
        tabbedPane.addTab("  \ud83d\udcca Dashboard  ",   new DashboardPanel(service));
        tabbedPane.addTab("  \u2699 Config YAML  ",      new ConfigPanel(service));
        tabbedPane.addTab("  \ud83c\udfa4 Gravacao  ",    new RecordPanel(service));
        tabbedPane.addTab("  \ud83d\udd17 Paths  ",       new PathsPanel(service));
        root.add(tabbedPane, BorderLayout.CENTER);

        logPanel = new LogPanel();
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, root, logPanel);
        split.setResizeWeight(0.72);
        split.setDividerSize(5);
        split.setBorder(null);
        add(split);
    }

    private void showAbout() {
        String html = "<html><body style='font-family:sans-serif;padding:8px;width:340px'>" +
            "<h2 style='color:#4fc3f7'>MediaMTX GUI</h2>" +
            "<p>Versao: <b>" + VERSION + "</b></p>" +
            "<p>Interface grafica profissional para gerenciar o servidor <b>MediaMTX</b>.</p>" +
            "<hr>" +
            "<p>&#9998; Desenvolvido por: <b>" + AUTHOR + "</b><br>" +
            "<a href='" + AUTHOR_URL + "'>" + AUTHOR_URL + "</a></p>" +
            "<p>&#128230; Repositorio:<br><a href='" + REPO_URL + "'>" + REPO_URL + "</a></p>" +
            "<p>&#128295; MediaMTX:<br><a href='" + MEDIAMTX_URL + "'>" + MEDIAMTX_URL + "</a></p>" +
            "<hr><small>Licenca MIT &#8226; " + AUTHOR + " &#169; 2026</small>" +
            "</body></html>";
        JEditorPane pane = new JEditorPane("text/html", html);
        pane.setEditable(false);
        pane.setOpaque(false);
        pane.addHyperlinkListener(ev -> {
            if (ev.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED)
                openBrowser(ev.getURL().toString());
        });
        JOptionPane.showMessageDialog(this, pane, "Sobre o MediaMTX GUI", JOptionPane.PLAIN_MESSAGE);
    }

    private JMenu menu(String name) { JMenu m = new JMenu(name); m.setFont(Theme.FONT_MEDIUM); return m; }
    private JMenuItem item(String name, ActionListener al) {
        JMenuItem i = new JMenuItem(name); i.setFont(Theme.FONT_SMALL); i.addActionListener(al); return i;
    }
    public static void openBrowser(String url) {
        try { Desktop.getDesktop().browse(new java.net.URI(url)); } catch (Exception ignored) {}
    }
    public LogPanel getLogPanel() { return logPanel; }
}
