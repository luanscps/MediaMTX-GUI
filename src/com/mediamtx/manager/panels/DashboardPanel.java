package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(MediaMTXService service) {
        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Dashboard");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 3, 12, 12));
        cards.setOpaque(false);
        cards.add(card("RTSP",   ":8554", "Streaming",      Theme.ACCENT));
        cards.add(card("RTMP",   ":1935", "OBS / Live",     Theme.ACCENT2));
        cards.add(card("HLS",    ":8888", "Browser",        Theme.WARNING));
        cards.add(card("WebRTC", ":8889", "Tempo real",     Theme.SUCCESS));
        cards.add(card("SRT",    ":8890", "Baixa latencia", new Color(206, 147, 216)));
        cards.add(card("API",    ":9997", "REST Control",   Theme.TEXT_MUTED));
        add(cards, BorderLayout.CENTER);

        // ── Painel de instrucoes expandido ──
        JPanel tipCard = new JPanel(new BorderLayout());
        tipCard.setBackground(Theme.BG_CARD);
        tipCard.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));

        String html =
            "<html><body style='font-family:sans-serif; padding:4px'>" +

            "<b style='color:#4fc3f7'>&#128295; Configuracao inicial (obrigatorio):</b><br>" +
            "<ol style='margin:4px 0 8px 18px'>" +
            "<li>Baixe o bin\u00e1rio do MediaMTX em: " +
            "<a href='https://github.com/bluenviron/mediamtx/releases'>" +
            "github.com/bluenviron/mediamtx/releases</a></li>" +
            "<li>No menu <b>Arquivo \u2192 Abrir bin\u00e1rio...</b>, selecione o arquivo baixado " +
            "(<code>mediamtx.exe</code> no Windows ou <code>mediamtx</code> no Linux/macOS).</li>" +
            "<li>Opcionalmente configure o <b>mediamtx.yml</b> na aba <b>Config YAML</b> " +
            "ou use a configuracao padrao gerada automaticamente.</li>" +
            "</ol>" +

            "<b style='color:#4fc3f7'>&#9654; Como usar:</b><br>" +
            "<ol style='margin:4px 0 8px 18px'>" +
            "<li>Clique em <b>Iniciar</b> para subir o servidor MediaMTX.</li>" +
            "<li>Publique um stream a partir de qualquer fonte:" +
            "<ul style='margin:2px 0 2px 16px'>" +
            "<li>OBS Studio \u2192 <code>rtmp://localhost:1935/live</code></li>" +
            "<li>FFmpeg \u2192 <code>ffmpeg -i input -f rtsp rtsp://localhost:8554/cam1</code></li>" +
            "<li>C\u00e2mera Android via scrcpy/ADB \u2192 redirecione para RTSP local</li>" +
            "</ul></li>" +
            "<li>Consuma o stream em qualquer player (VLC, browser, outro dispositivo):" +
            "<ul style='margin:2px 0 2px 16px'>" +
            "<li>RTSP: <code>rtsp://IP:8554/cam1</code></li>" +
            "<li>HLS (browser): <code>http://IP:8888/cam1</code></li>" +
            "<li>WebRTC (browser): <code>http://IP:8889/cam1</code></li>" +
            "</ul></li>" +
            "<li>Configure grava\u00e7\u00e3o autom\u00e1tica na aba <b>Grava\u00e7\u00e3o</b> e paths na aba <b>Paths</b>.</li>" +
            "</ol>" +
            "</body></html>";

        JEditorPane pane = new JEditorPane("text/html", html);
        pane.setEditable(false);
        pane.setOpaque(false);
        pane.setBackground(Theme.BG_CARD);
        pane.setBorder(new EmptyBorder(10, 14, 10, 14));
        pane.addHyperlinkListener(ev -> {
            if (ev.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED)
                AppWindow.openBrowser(ev.getURL().toString());
        });

        tipCard.add(pane);
        add(tipCard, BorderLayout.SOUTH);
    }

    private JPanel card(String proto, String port, String sub, Color color) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Theme.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1, true),
            new EmptyBorder(16, 16, 16, 16)));
        JLabel name  = new JLabel(proto); name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16)); name.setForeground(color);       name.setAlignmentX(CENTER_ALIGNMENT);
        JLabel portL = new JLabel(port);  portL.setFont(Theme.FONT_MONO);                         portL.setForeground(Theme.TEXT_MUTED); portL.setAlignmentX(CENTER_ALIGNMENT);
        JLabel subL  = new JLabel(sub);   subL.setFont(Theme.FONT_SMALL);                          subL.setForeground(Theme.TEXT_MUTED);  subL.setAlignmentX(CENTER_ALIGNMENT);
        p.add(Box.createVerticalGlue());
        p.add(name);
        p.add(Box.createVerticalStrut(4));
        p.add(portL);
        p.add(subL);
        p.add(Box.createVerticalGlue());
        return p;
    }
}
