package com.mediamtx.manager.theme;

import javax.swing.*;
import java.awt.*;

public class Theme {
    public static final Color BG         = new Color(30, 30, 46);
    public static final Color BG_CARD    = new Color(40, 40, 58);
    public static final Color BG_SIDEBAR = new Color(24, 24, 36);
    public static final Color ACCENT     = new Color(79, 195, 247);
    public static final Color ACCENT2    = new Color(130, 177, 255);
    public static final Color SUCCESS    = new Color(102, 187, 106);
    public static final Color DANGER     = new Color(239, 83, 80);
    public static final Color WARNING    = new Color(255, 183, 77);
    public static final Color TEXT       = new Color(224, 224, 224);
    public static final Color TEXT_MUTED = new Color(140, 140, 160);
    public static final Color BORDER     = new Color(60, 60, 80);

    public static final Font FONT_TITLE  = new Font(Font.SANS_SERIF, Font.BOLD, 18);
    public static final Font FONT_MEDIUM = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font FONT_MONO   = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    public static final Font FONT_BOLD   = new Font(Font.SANS_SERIF, Font.BOLD, 13);

    public static JButton primaryButton(String text) { return styledButton(text, ACCENT, new Color(20,20,32)); }
    public static JButton dangerButton(String text)  { return styledButton(text, DANGER, Color.WHITE); }

    private static JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }
}
