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

        // ── Topo: titulo + badge de integracao ───────────────────────────
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel title = new JLabel("\uD83D\uDCCA  Dashboard");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        topRow.add(title, BorderLayout.WEST);

        JLabel badge = new JLabel("\uD83D\uDCF1  Integrado com camera2api-brSS");
        badge.setFont(Theme.FONT_BOLD);
        badge.setForeground(Theme.SUCCESS);
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.SUCCESS, 1, true),
            new EmptyBorder(4, 10, 4, 10)));
        badge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        badge.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                AppWindow.openBrowser(CAM2_URL);
            }
        });
        topRow.add(badge, BorderLayout.EAST);
        add(topRow, BorderLayout.NORTH);

        // ── Cards de protocolos ───────────────────────────────────────────
        JPanel cards = new JPanel(new GridLayout(2, 3, 12, 12));
        cards.setOpaque(false);
        cards.add(card("RTSP",   ":8554", "Streaming principal",  Theme.ACCENT,   "\uD83C\uDFA5"));
        cards.add(card("RTMP",   ":1935", "OBS Studio / Live",    Theme.ACCENT2,  "\uD83D\uDCE1"));
        cards.add(card("HLS",    ":8888", "Browser / Web Player", Theme.WARNING,  "\uD83C\uDF10"));
        cards.add(card("WebRTC", ":8889", "Tempo real no browser", Theme.SUCCESS,  "\u26A1"));
        cards.add(card("SRT",    ":8890", "Baixa lat\u00eancia",       Theme.PURPLE,   "\uD83D\uDD12"));
        cards.add(card("API",    ":9997", "REST Control",          Theme.TEXT_DIM, "\uD83D\uDD27"));
        add(cards, BorderLayout.CENTER);

        // ── Painel inferior: 2 colunas (setup + integracao) ───────────────
        JPanel bottom = new JPanel(new GridLayout(1, 2, 12, 0));
        bottom.setOpaque(false);
        bottom.add(buildSetupCard());
        bottom.add(buildIntegrationCard());
        add(bottom, BorderLayout.SOUTH);
    }

    // ── Card de configuracao inicial ──────────────────────────────────────
    private JPanel buildSetupCard() {
        String html =
            "<html><body style='font-family:sans-serif;font-size:12px;color:#e2e8f0'>" +
            "<b style='color:#38bdf8;font-size:13px'>\u2699 Configura\u00e7\u00e3o Inicial</b><br><br>" +

            "<b style='color:#fb923c'>1. Baixar o MediaMTX</b><br>" +
            "Acesse <a href='https://github.com/bluenviron/mediamtx/releases'>" +
            "github.com/bluenviron/mediamtx/releases</a><br>" +
            "Baixe o bin\u00e1rio para seu SO (<code>mediamtx.exe</code> no Windows).<br><br>" +

            "<b style='color:#fb923c'>2. Configurar o bin\u00e1rio</b><br>" +
            "Menu <b>Arquivo \u2192 Abrir bin\u00e1rio...</b><br>" +
            "Selecione o arquivo <code>mediamtx.exe</code> baixado.<br><br>" +

            "<b style='color:#fb923c'>3. Ajustar o YAML (opcional)</b><br>" +
            "Use a aba <b>\u2699 Config YAML</b> para personalizar portas,<br>" +
            "autentica\u00e7\u00e3o e grava\u00e7\u00e3o. Ou use a config padr\u00e3o.<br><br>" +

            "<b style='color:#fb923c'>4. Iniciar o servidor</b><br>" +
            "Clique em <b>\u25b6 Iniciar</b> e acompanhe o log abaixo." +
            "</body></html>";
        return infoCard(html, Theme.ACCENT);
    }

    // ── Card de integracao camera2api-brSS ────────────────────────────────
    private JPanel buildIntegrationCard() {
        String html =
            "<html><body style='font-family:sans-serif;font-size:12px;color:#e2e8f0'>" +
            "<b style='color:#34d399;font-size:13px'>\uD83D\uDCF1 Integra\u00e7\u00e3o com camera2api-brSS</b><br>" +
            "<a href='" + CAM2_URL + "' style='color:#38bdf8;font-size:11px'>github.com/luanscps/camera2api-brSS</a><br><br>" +

            "Transforme seu <b>Samsung Galaxy Note10+</b> (ou Android c/ m\u00faltiplas c\u00e2meras)<br>" +
            "em um servidor RTSP profissional e controle pelo MediaMTX GUI.<br><br>" +

            "<b style='color:#fb923c'>No Android (camera2api-brSS):</b><br>" +
            "\u2022 Instale o app e inicie o servidor RTSP<br>" +
            "\u2022 O app publica em: <code>rtsp://IP_CELULAR:8554/live</code><br>" +
            "\u2022 Painel web de controle: <code>http://IP_CELULAR:8080</code><br>" +
            "\u2022 Selecione entre Wide, UltraWide, Telephoto ou Frontal<br>" +
            "\u2022 Ajuste ISO, exposi\u00e7\u00e3o e foco manualmente<br><br>" +

            "<b style='color:#fb923c'>No MediaMTX GUI (este app):</b><br>" +
            "\u2022 Inicie o servidor e adicione um path no YAML:<br>" +
            "<code style='background:#0f172a;padding:1px 4px'>" +
            "paths:\u00a0\u00a0cam_celular:\u00a0\u00a0\u00a0\u00a0source:\u00a0rtsp://IP:8554/live</code><br>" +
            "\u2022 O stream fica dispon\u00edvel para <b>VLC, OBS, browser</b> via<br>" +
            "\u00a0\u00a0<code>rtsp://localhost:8554/cam_celular</code><br>" +
            "\u2022 Grave autom\u00e1tico na aba <b>Grava\u00e7\u00e3o</b><br>" +
            "\u2022 Distribua para m\u00faltiplos clientes simult\u00e2neos<br><br>" +

            "<b style='color:#fb923c'>Consumir o stream:</b><br>" +
            "\u2022 <b>VLC:</b> M\u00eddia \u2192 Abrir Fluxo \u2192 <code>rtsp://localhost:8554/live</code><br>" +
            "\u2022 <b>OBS:</b> Fonte \u2192 Media Source \u2192 desmarque Local File \u2192 cole URL<br>" +
            "\u2022 <b>Browser:</b> <code>http://localhost:8888/live</code> (HLS)<br>" +
            "\u2022 <b>WebRTC:</b> <code>http://localhost:8889/live</code>" +
            "</body></html>";
        return infoCard(html, Theme.SUCCESS);
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private JPanel infoCard(String html, Color borderColor) {
        JEditorPane pane = new JEditorPane("text/html", html);
        pane.setEditable(false);
        pane.setOpaque(false);
        pane.setBorder(new EmptyBorder(12, 14, 12, 14));
        pane.addHyperlinkListener(ev -> {
            if (ev.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED)
                AppWindow.openBrowser(ev.getURL().toString());
        });
        JScrollPane scroll = new JScrollPane(pane,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        scroll.getViewport().setBackground(Theme.BG_CARD);
        scroll.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        card.add(scroll);
        return card;
    }

    private JPanel card(String proto, String port, String sub, Color color, String icon) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Theme.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1, true),
            new EmptyBorder(14, 14, 14, 14)));

        JLabel ico  = new JLabel(icon + "  " + proto);
        ico.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        ico.setForeground(color);
        ico.setAlignmentX(CENTER_ALIGNMENT);

        JLabel portL = new JLabel(port);
        portL.setFont(Theme.FONT_MONO);
        portL.setForeground(Theme.TEXT_DIM);
        portL.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subL = new JLabel(sub);
        subL.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        subL.setForeground(Theme.TEXT_MUTED);
        subL.setAlignmentX(CENTER_ALIGNMENT);

        p.add(Box.createVerticalGlue());
        p.add(ico);
        p.add(Box.createVerticalStrut(4));
        p.add(portL);
        p.add(Box.createVerticalStrut(2));
        p.add(subL);
        p.add(Box.createVerticalGlue());
        return p;
    }
}
