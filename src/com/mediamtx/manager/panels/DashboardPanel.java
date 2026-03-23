package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private static final String CAM2_URL = "https://github.com/luanscps/camera2api-brSS/tree/v4-ui";

    public DashboardPanel(MediaMTXService service) {
        setLayout(new MigLayout("insets 20, gap 14", "[grow]", "[][grow][grow]"));
        setBackground(Theme.BG);

        // ── Topo ──────────────────────────────────────────────────────────
        JPanel topRow = new JPanel(new MigLayout("insets 0, gap 8", "[grow][]", "[]"));
        topRow.setOpaque(false);

        JLabel title = new JLabel("  Dashboard");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        topRow.add(title, "growx");

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
        topRow.add(badge);
        add(topRow, "growx, wrap");

        // ── Cards de protocolos — 3 colunas ──────────────────────────────
        JPanel cards = new JPanel(new MigLayout(
            "insets 0, gap 12",
            "[grow][grow][grow]",
            "[grow][grow]"
        ));
        cards.setOpaque(false);
        cards.add(card("RTSP",   ":8554", "Streaming principal",   new Color( 14,165,233), "[CAM]"),  "grow");
        cards.add(card("RTMP",   ":1935", "OBS Studio / Live",     new Color( 59,130,246), "[LIVE]"), "grow");
        cards.add(card("HLS",    ":8888", "Browser / Web Player",  new Color(217,119,  6), "[WEB]"),  "grow, wrap");
        cards.add(card("WebRTC", ":8889", "Tempo real no browser", new Color( 22,163, 74), "[RTC]"),  "grow");
        cards.add(card("SRT",    ":8890", "Baixa latência",        new Color(124, 58,237), "[SRT]"),  "grow");
        cards.add(card("API",    ":9997", "REST Control",          new Color( 71, 85,105), "[API]"),  "grow, wrap");
        add(cards, "growx, growy, wrap");

        // ── Cards informativos — 2 colunas ────────────────────────────────
        JPanel bottom = new JPanel(new MigLayout("insets 0, gap 14", "[grow][grow]", "[grow]"));
        bottom.setOpaque(false);
        bottom.add(buildSetupCard(),       "grow");
        bottom.add(buildIntegrationCard(), "grow");
        add(bottom, "growx, growy");
    }

    private JPanel buildSetupCard() {
        String html =
            "<html><body style='font-family:sans-serif;font-size:12px;margin:0;padding:0'>" +
            "<b style='color:#0ea5e9;font-size:13px'>Configuração Inicial</b><br><br>" +
            "<b style='color:#f97316'>Passo 1 &mdash; Baixar o MediaMTX</b><br>" +
            "Acesse <a href='https://github.com/bluenviron/mediamtx/releases' style='color:#0ea5e9'>" +
            "github.com/bluenviron/mediamtx/releases</a><br>" +
            "Baixe o binário para seu SO: " +
            "<code style='background:#1e293b;padding:1px 4px'>mediamtx.exe</code> (Windows) ou " +
            "<code style='background:#1e293b;padding:1px 4px'>mediamtx</code> (Linux/macOS).<br><br>" +
            "<b style='color:#f97316'>Passo 2 &mdash; Configurar o binário</b><br>" +
            "Menu <b>Arquivo &rarr; Abrir binário...</b><br>" +
            "Selecione o executável <code style='background:#1e293b;padding:1px 4px'>mediamtx.exe</code> baixado.<br><br>" +
            "<b style='color:#f97316'>Passo 3 &mdash; Ajustar o YAML (opcional)</b><br>" +
            "Use a aba <b>Config YAML</b> para personalizar portas, autenticação e gravação.<br><br>" +
            "<b style='color:#f97316'>Passo 4 &mdash; Iniciar o servidor</b><br>" +
            "Clique em <b>Iniciar</b> no topo e acompanhe o log abaixo." +
            "</body></html>";
        return infoCard(html, Theme.ACCENT);
    }

    private JPanel buildIntegrationCard() {
        String html =
            "<html><body style='font-family:sans-serif;font-size:12px;margin:0;padding:0'>" +
            "<b style='color:#22c55e;font-size:13px'>Integração com camera2api-brSS</b><br>" +
            "<a href='" + CAM2_URL + "' style='color:#0ea5e9;font-size:11px'>github.com/luanscps/camera2api-brSS</a><br><br>" +
            "Transforme qualquer <b>Android 7.0+</b> em servidor RTSP integrado ao MediaMTX GUI.<br><br>" +
            "<b style='color:#f97316'>No dispositivo Android:</b><br>" +
            "&bull; Instale o app e inicie o servidor<br>" +
            "&bull; Stream: <code style='background:#1e293b;padding:1px 3px'>rtsp://IP:8554/live</code><br>" +
            "&bull; WebControl: <code style='background:#1e293b;padding:1px 3px'>http://IP:8080</code><br><br>" +
            "<b style='color:#f97316'>No MediaMTX GUI:</b><br>" +
            "&bull; <b>Sources</b> &rarr; Adicionar &rarr; <b>Câmera Android</b><br><br>" +
            "<b style='color:#f97316'>Consumir o stream:</b><br>" +
            "&bull; <b>VLC:</b> <code style='background:#1e293b;padding:1px 3px'>rtsp://localhost:8554/live</code><br>" +
            "&bull; <b>HLS:</b> <code style='background:#1e293b;padding:1px 3px'>http://localhost:8888/live</code><br>" +
            "&bull; <b>WebRTC:</b> <code style='background:#1e293b;padding:1px 3px'>http://localhost:8889/live</code>" +
            "</body></html>";
        return infoCard(html, Theme.SUCCESS);
    }

    private JPanel infoCard(String html, Color borderColor) {
        javax.swing.text.html.HTMLEditorKit kit = new javax.swing.text.html.HTMLEditorKit();
        javax.swing.text.html.StyleSheet css = kit.getStyleSheet();
        css.addRule("body { background:#3c3f41; color:#bbbbbb; font-family:sans-serif; font-size:14px; }");
        css.addRule("a { color:#9f6bff; text-decoration:underline; }");
        css.addRule("code { background:#2b2b2b; color:#a9b7c6; font-size:13px; padding:1px 4px; }");
        css.addRule("b { color:#e0e0e0; }");

        JEditorPane pane = new JEditorPane();
        pane.setEditorKit(kit);
        pane.setText(html);
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
        JPanel p = new JPanel(new MigLayout("insets 14, gap 2, align center center", "[center]", "[][][][]"));
        p.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, color));

        JLabel tagL = new JLabel(tag);
        tagL.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        tagL.setForeground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 160));

        JLabel name = new JLabel(proto);
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        name.setForeground(color);

        JLabel portL = new JLabel(port);
        portL.setFont(Theme.FONT_MONO);
        portL.setForeground(Theme.TEXT_DIM);

        JLabel subL = new JLabel(sub);
        subL.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        subL.setForeground(Theme.TEXT_MUTED);

        p.add(tagL,  "wrap");
        p.add(name,  "wrap");
        p.add(portL, "wrap");
        p.add(subL,  "wrap");
        return p;
    }
}
