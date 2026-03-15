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
        cards.add(card("RTSP",   ":8554", "Streaming",   Theme.ACCENT));
        cards.add(card("RTMP",   ":1935", "OBS / Live",  Theme.ACCENT2));
        cards.add(card("HLS",    ":8888", "Browser",     Theme.WARNING));
        cards.add(card("WebRTC", ":8889", "Tempo real",  Theme.SUCCESS));
        cards.add(card("SRT",    ":8890", "Baixa latencia", new Color(206,147,216)));
        cards.add(card("API",    ":9997", "REST Control", Theme.TEXT_MUTED));
        add(cards, BorderLayout.CENTER);

        JLabel tip = new JLabel("<html><b>Como usar:</b> Inicie o servidor, publique um stream via RTSP/RTMP e consuma em qualquer player (VLC, OBS, browser). Configure paths e gravacao nas abas acima.</html>");
        tip.setFont(Theme.FONT_SMALL);
        tip.setForeground(Theme.TEXT_MUTED);
        tip.setBorder(new EmptyBorder(12, 12, 12, 12));
        JPanel tipCard = new JPanel(new BorderLayout());
        tipCard.setBackground(Theme.BG_CARD);
        tipCard.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));
        tipCard.add(tip);
        add(tipCard, BorderLayout.SOUTH);
    }

    private JPanel card(String proto, String port, String sub, Color color) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Theme.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1, true),
            new EmptyBorder(16, 16, 16, 16)));
        JLabel name = new JLabel(proto); name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16)); name.setForeground(color); name.setAlignmentX(CENTER_ALIGNMENT);
        JLabel portL = new JLabel(port); portL.setFont(Theme.FONT_MONO); portL.setForeground(Theme.TEXT_MUTED); portL.setAlignmentX(CENTER_ALIGNMENT);
        JLabel subL  = new JLabel(sub);  subL.setFont(Theme.FONT_SMALL); subL.setForeground(Theme.TEXT_MUTED);  subL.setAlignmentX(CENTER_ALIGNMENT);
        p.add(Box.createVerticalGlue()); p.add(name); p.add(Box.createVerticalStrut(4)); p.add(portL); p.add(subL); p.add(Box.createVerticalGlue());
        return p;
    }
}
