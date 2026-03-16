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
            public void mouseClicked(java.awt.event.MouseEvent e) { AppWindow.openBrowser(CAM2_URL); }
        });
        topRow.add(badge, BorderLayout.EAST);
        add(topRow, BorderLayout.NORTH);

        // ── Cards de protocolos ───────────────────────────────────────────
        JPanel cards = new JPanel(new GridLayout(2, 3, 12, 12));
        cards.setOpaque(false);
        cards.add(card("RTSP",   ":8554", "Streaming principal",   new Color( 14,165,233), "\uD83C\uDFA5"));
        cards.add(card("RTMP",   ":1935", "OBS Studio / Live",      new Color( 59,130,246), "\uD83D\uDCE1"));
        cards.add(card("HLS",    ":8888", "Browser / Web Player",   new Color(217,119,  6), "\uD83C\uDF10"));
        cards.add(card("WebRTC", ":8889", "Tempo real no browser",  new Color( 22,163, 74), "\u26A1"));
        cards.add(card("SRT",    ":8890", "Baixa lat\u00eancia",         new Color(124, 58,237), "\uD83D\uDD12"));
        cards.add(card("API",    ":9997", "REST Control",            new Color( 71, 85,105), "\uD83D\uDD27"));
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
            "<b style='color:#0ea5e9;font-size:13px'>&#9881; Configura&#231;&#227;o Inicial</b><br><br>" +

            "<b style='color:#ea580c'>1&#65039;&#8419; Baixar o MediaMTX</b><br>" +
            "Acesse <a href='https://github.com/bluenviron/mediamtx/releases' style='color:#0ea5e9'>" +
            "github.com/bluenviron/mediamtx/releases</a><br>" +
            "Baixe o bin&#225;rio para seu SO <code style='background:#f1f5f9;padding:1px 4px'>(mediamtx.exe</code> Windows&nbsp;/&nbsp;<code style='background:#f1f5f9;padding:1px 4px'>mediamtx</code> Linux).<br><br>" +

            "<b style='color:#ea580c'>2&#65039;&#8419; Configurar o bin&#225;rio</b><br>" +
            "Menu <b>Arquivo &#8594; Abrir bin&#225;rio...</b><br>" +
            "Selecione o execut&#225;vel <code style='background:#f1f5f9;padding:1px 4px'>mediamtx.exe</code> baixado.<br><br>" +

            "<b style='color:#ea580c'>3&#65039;&#8419; Ajustar o YAML (opcional)</b><br>" +
            "Use a aba <b>&#9881; Config YAML</b> para personalizar portas,<br>" +
            "autentica&#231;&#227;o e grava&#231;&#227;o, ou use a config padr&#227;o.<br><br>" +

            "<b style='color:#ea580c'>4&#65039;&#8419; Iniciar o servidor</b><br>" +
            "Clique em <b>&#9654; Iniciar</b> e acompanhe o log abaixo." +
            "</body></html>";
        return infoCard(html, new Color(14, 165, 233));
    }

    // ── Card direito: integracao camera2api-brSS ──────────────────────────
    private JPanel buildIntegrationCard() {
        String html =
            "<html><body style='font-family:sans-serif;font-size:12px;color:#0f172a;margin:0;padding:0'>" +
            "<b style='color:#16a34a;font-size:13px'>&#128241; Integra&#231;&#227;o com camera2api-brSS</b><br>" +
            "<a href='" + CAM2_URL + "' style='color:#0ea5e9;font-size:11px'>github.com/luanscps/camera2api-brSS</a><br><br>" +

            "Transforme qualquer dispositivo <b>Android 7.0+ (API 24+)</b><br>" +
            "em servidor RTSP profissional controlado pelo MediaMTX GUI.<br><br>" +

            "<b style='color:#ea580c'>No dispositivo Android:</b><br>" +
            "&#8226; Instale o app <b>camera2api-brSS</b> e inicie o servidor<br>" +
            "&#8226; Stream publicado em: <code style='background:#f1f5f9;padding:1px 3px'>rtsp://IP_DEVICE:8554/live</code><br>" +
            "&#8226; Painel web de controle: <code style='background:#f1f5f9;padding:1px 3px'>http://IP_DEVICE:8080</code><br>" +
            "&#8226; Selecione a c&#226;mera (Wide, UltraWide, Telephoto, Frontal)<br>" +
            "&#8226; Ajuste ISO, exposi&#231;&#227;o e foco manualmente<br>" +
            "&#8226; Requer Android 7.0+ com suporte a Camera2 API<br><br>" +

            "<b style='color:#ea580c'>No MediaMTX GUI (este app):</b><br>" +
            "&#8226; Adicione no YAML: <code style='background:#f1f5f9;padding:1px 3px'>source: rtsp://IP:8554/live</code><br>" +
            "&#8226; O stream fica dispon&#237;vel para m&#250;ltiplos clientes<br><br>" +

            "<b style='color:#ea580c'>Consumir o stream:</b><br>" +
            "&#8226; <b>VLC:</b> M&#237;dia &#8594; Fluxo &#8594; <code style='background:#f1f5f9;padding:1px 3px'>rtsp://localhost:8554/live</code><br>" +
            "&#8226; <b>OBS:</b> Fonte &#8594; Media Source &#8594; desmarque Local File<br>" +
            "&#8226; <b>Browser HLS:</b> <code style='background:#f1f5f9;padding:1px 3px'>http://localhost:8888/live</code><br>" +
            "&#8226; <b>WebRTC:</b> <code style='background:#f1f5f9;padding:1px 3px'>http://localhost:8889/live</code>" +
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

    private JPanel card(String proto, String port, String sub, Color color, String icon) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Theme.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, color),
            new EmptyBorder(14, 14, 14, 14)));

        JLabel ico   = new JLabel(icon + "  " + proto);
        ico.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        ico.setForeground(color);
        ico.setAlignmentX(CENTER_ALIGNMENT);

        JLabel portL = new JLabel(port);
        portL.setFont(Theme.FONT_MONO);
        portL.setForeground(Theme.TEXT_DIM);
        portL.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subL  = new JLabel(sub);
        subL.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        subL.setForeground(Theme.TEXT_MUTED);
        subL.setAlignmentX(CENTER_ALIGNMENT);

        p.add(Box.createVerticalGlue());
        p.add(ico);
        p.add(Box.createVerticalStrut(5));
        p.add(portL);
        p.add(Box.createVerticalStrut(2));
        p.add(subL);
        p.add(Box.createVerticalGlue());
        return p;
    }
}
