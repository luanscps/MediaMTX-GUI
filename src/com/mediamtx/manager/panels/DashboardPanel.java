package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private static final String CAM2_URL = "https://github.com/luanscps/camera2api-brSS/tree/v4-ui";

    public DashboardPanel(MediaMTXService service) {
        setLayout(new BorderLayout(14, 14));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ── Topo ─────────────────────────────────────────────────────────
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel title = new JLabel("  Dashboard");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        topRow.add(title, BorderLayout.WEST);

        JLabel badge = new JLabel("  Integrado com camera2api-brSS");
        badge.setFont(Theme.FONT_BOLD);
        badge.setForeground(Theme.SUCCESS);
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.SUCCESS, 1, true),
            new EmptyBorder(4, 10, 4, 10)));
        badge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        badge.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { AppWindow.openBrowser(CAM2_URL); }
        });
        topRow.add(badge, BorderLayout.EAST);
        add(topRow, BorderLayout.NORTH);

        // ── Cards de protocolos ───────────────────────────────────────────
        JPanel cards = new JPanel(new GridLayout(2, 3, 12, 12));
        cards.setOpaque(false);
        cards.add(card("RTSP",   ":8554", "Streaming principal",  new Color( 14,165,233), "[CAM]"));
        cards.add(card("RTMP",   ":1935", "OBS Studio / Live",    new Color( 59,130,246), "[LIVE]"));
        cards.add(card("HLS",    ":8888", "Browser / Web Player", new Color(217,119,  6), "[WEB]"));
        cards.add(card("WebRTC", ":8889", "Tempo real no browser",new Color( 22,163, 74), "[RTC]"));
        cards.add(card("SRT",    ":8890", "Baixa latencia",       new Color(124, 58,237), "[SRT]"));
        cards.add(card("API",    ":9997", "REST Control",         new Color( 71, 85,105), "[API]"));
        add(cards, BorderLayout.CENTER);

        // ── Painel inferior: 2 colunas ────────────────────────────────────
        JPanel bottom = new JPanel(new GridLayout(1, 2, 14, 0));
        bottom.setOpaque(false);
        bottom.add(buildSetupCard());
        bottom.add(buildIntegrationCard());
        add(bottom, BorderLayout.SOUTH);
    }

    // ── Card esquerdo: configuracao inicial ───────────────────────────────
    private JPanel buildSetupCard() {
        String html =
            "<html><body style='font-family:sans-serif;font-size:12px;color:#0f172a;margin:0;padding:0'>" +
            "<b style='color:#0ea5e9;font-size:13px'>Configuracao Inicial</b><br><br>" +

            "<b style='color:#ea580c'>Passo 1 &mdash; Baixar o MediaMTX</b><br>" +
            "Acesse <a href='https://github.com/bluenviron/mediamtx/releases' style='color:#0ea5e9'>" +
            "github.com/bluenviron/mediamtx/releases</a><br>" +
            "Baixe o binario para seu SO:&nbsp;" +
            "<code style='background:#f1f5f9;padding:1px 4px'>mediamtx.exe</code> (Windows)&nbsp;ou&nbsp;" +
            "<code style='background:#f1f5f9;padding:1px 4px'>mediamtx</code> (Linux/macOS).<br><br>" +

            "<b style='color:#ea580c'>Passo 2 &mdash; Configurar o binario</b><br>" +
            "Menu <b>Arquivo &rarr; Abrir binario...</b><br>" +
            "Selecione o executavel <code style='background:#f1f5f9;padding:1px 4px'>mediamtx.exe</code> baixado.<br><br>" +

            "<b style='color:#ea580c'>Passo 3 &mdash; Ajustar o YAML (opcional)</b><br>" +
            "Use a aba <b>Config YAML</b> para personalizar portas,<br>" +
            "autenticacao e gravacao, ou use a config padrao.<br><br>" +

            "<b style='color:#ea580c'>Passo 4 &mdash; Iniciar o servidor</b><br>" +
            "Clique em <b>Iniciar</b> no topo e acompanhe o log abaixo." +
            "</body></html>";
        return infoCard(html, new Color(14, 165, 233));
    }

    // ── Card direito: integracao camera2api-brSS ──────────────────────────
    private JPanel buildIntegrationCard() {
        String html =
            "<html><body style='font-family:sans-serif;font-size:12px;color:#0f172a;margin:0;padding:0'>" +
            "<b style='color:#16a34a;font-size:13px'>Integracao com camera2api-brSS</b><br>" +
            "<a href='" + CAM2_URL + "' style='color:#0ea5e9;font-size:11px'>github.com/luanscps/camera2api-brSS</a><br><br>" +

            "Transforme qualquer dispositivo <b>Android 7.0+ (API 24+)</b> com suporte a Camera2 API<br>" +
            "em servidor RTSP profissional integrado ao MediaMTX GUI.<br><br>" +

            "<b style='color:#ea580c'>No dispositivo Android:</b><br>" +
            "&bull; Instale o app <b>camera2api-brSS</b> e inicie o servidor<br>" +
            "&bull; Stream publicado em: <code style='background:#f1f5f9;padding:1px 3px'>rtsp://IP_DEVICE:8554/live</code><br>" +
            "&bull; Painel web de controle: <code style='background:#f1f5f9;padding:1px 3px'>http://IP_DEVICE:8080</code><br>" +
            "&bull; Selecione a camera (Wide, UltraWide, Telephoto, Frontal)<br>" +
            "&bull; Ajuste ISO, exposicao e foco manualmente<br><br>" +

            "<b style='color:#ea580c'>No MediaMTX GUI (este app):</b><br>" +
            "&bull; Adicione no YAML: <code style='background:#f1f5f9;padding:1px 3px'>source: rtsp://IP_DEVICE:8554/live</code><br>" +
            "&bull; O stream fica disponivel para multiplos clientes simultaneos<br><br>" +

            "<b style='color:#ea580c'>Consumir o stream:</b><br>" +
            "&bull; <b>VLC:</b> Midia &rarr; Fluxo &rarr; <code style='background:#f1f5f9;padding:1px 3px'>rtsp://localhost:8554/live</code><br>" +
            "&bull; <b>OBS:</b> Fonte &rarr; Media Source &rarr; desmarque Local File &rarr; cole a URL<br>" +
            "&bull; <b>Browser HLS:</b> <code style='background:#f1f5f9;padding:1px 3px'>http://localhost:8888/live</code><br>" +
            "&bull; <b>WebRTC:</b> <code style='background:#f1f5f9;padding:1px 3px'>http://localhost:8889/live</code>" +
            "</body></html>";
        return infoCard(html, new Color(22, 163, 74));
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private JPanel infoCard(String html, Color borderColor) {
        JEditorPane pane = new JEditorPane("text/html", html);
        pane.setEditable(false);
        pane.setOpaque(true);
        pane.setBackground(Theme.BG_CARD);
        pane.setBorder(new EmptyBorder(14, 16, 14, 16));
        pane.addHyperlinkListener(ev -> {
            if (ev.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED)
                AppWindow.openBrowser(ev.getURL().toString());
        });

        JScrollPane scroll = new JScrollPane(pane,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_CARD);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 2, true),
            new EmptyBorder(0, 0, 0, 0)));
        card.add(scroll);
        return card;
    }

    private JPanel card(String proto, String port, String sub, Color color, String tag) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Theme.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, color),
            new EmptyBorder(14, 14, 14, 14)));

        JLabel tagL = new JLabel(tag);
        tagL.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        tagL.setForeground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 160));
        tagL.setAlignmentX(CENTER_ALIGNMENT);

        JLabel name = new JLabel(proto);
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        name.setForeground(color);
        name.setAlignmentX(CENTER_ALIGNMENT);

        JLabel portL = new JLabel(port);
        portL.setFont(Theme.FONT_MONO);
        portL.setForeground(Theme.TEXT_DIM);
        portL.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subL = new JLabel(sub);
        subL.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        subL.setForeground(Theme.TEXT_MUTED);
        subL.setAlignmentX(CENTER_ALIGNMENT);

        p.add(Box.createVerticalGlue());
        p.add(tagL);
        p.add(Box.createVerticalStrut(2));
        p.add(name);
        p.add(Box.createVerticalStrut(4));
        p.add(portL);
        p.add(Box.createVerticalStrut(2));
        p.add(subL);
        p.add(Box.createVerticalGlue());
        return p;
    }
}
