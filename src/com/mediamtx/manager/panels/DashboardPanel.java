package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(MediaMTXService service) {
        setLayout(new MigLayout("insets 20, gap 14", "[grow]", "[][grow][grow]"));
        setBackground(Theme.BG);

        // ── Título ────────────────────────────────────────────────────────────
        JLabel title = new JLabel("  Dashboard");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        add(title, "growx, wrap");

        // ── Cards de protocolos — 3 colunas ────────────────────────────────
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

        // ── Card de configuração inicial (largura total) ────────────────────────
        add(buildSetupCard(), "growx, growy");
    }

    private JPanel buildSetupCard() {
        String html =
            "<html><body style='font-family:sans-serif;font-size:13px;margin:0;padding:0'>" +
            "<b style='color:#9f6bff;font-size:14px'>Configuração Inicial</b><br><br>" +
            "<b style='color:#ffc644'>Passo 1 &mdash; Baixar o MediaMTX</b><br>" +
            "Acesse <a href='https://github.com/bluenviron/mediamtx/releases'>" +
            "github.com/bluenviron/mediamtx/releases</a><br>" +
            "Baixe o binário para seu SO: " +
            "<code>mediamtx.exe</code> (Windows) ou " +
            "<code>mediamtx</code> (Linux/macOS).<br><br>" +
            "<b style='color:#ffc644'>Passo 2 &mdash; Configurar o binário</b><br>" +
            "Menu <b>Arquivo &rarr; Abrir binário...</b> e selecione o executável baixado.<br><br>" +
            "<b style='color:#ffc644'>Passo 3 &mdash; Ajustar o YAML (opcional)</b><br>" +
            "Use a aba <b>Config YAML</b> para personalizar portas, autenticação e gravação.<br><br>" +
            "<b style='color:#ffc644'>Passo 4 &mdash; Iniciar o servidor</b><br>" +
            "Clique em <b>Iniciar</b> no topo e acompanhe o log em tempo real.<br><br>" +
            "<b style='color:#ffc644'>Passo 5 &mdash; Monitorar streams</b><br>" +
            "Use a aba <b>Métricas</b> para ver canais ativos e quantidade de leitores em tempo real." +
            "</body></html>";
        return infoCard(html, Theme.ACCENT);
    }

    private JPanel infoCard(String html, Color borderColor) {
        javax.swing.text.html.HTMLEditorKit kit = new javax.swing.text.html.HTMLEditorKit();
        javax.swing.text.html.StyleSheet css = kit.getStyleSheet();
        css.addRule("body { background:#3c3f41; color:#bbbbbb; font-family:sans-serif; font-size:13px; }");
        css.addRule("a { color:#9f6bff; text-decoration:underline; }");
        css.addRule("code { background:#2b2b2b; color:#a9b7c6; font-size:12px; padding:1px 4px; }");
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
        p.setBackground(Theme.BG_CARD);
        p.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, color));

        JLabel tagL = new JLabel(tag);
        tagL.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        tagL.setForeground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 160));

        JLabel name = new JLabel(proto);
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        name.setForeground(color);

        JLabel portL = new JLabel(port);
        portL.setFont(Theme.FONT_MONO);
        portL.setForeground(Theme.TEXT_DIM);

        JLabel subL = new JLabel(sub);
        subL.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        subL.setForeground(Theme.TEXT_MUTED);

        p.add(tagL,  "wrap");
        p.add(name,  "wrap");
        p.add(portL, "wrap");
        p.add(subL,  "wrap");
        return p;
    }
}
