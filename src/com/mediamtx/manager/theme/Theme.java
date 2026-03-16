package com.mediamtx.manager.theme;

import javax.swing.*;
import java.awt.*;

/**
 * Tema visual profissional - estilo monitoramento / camera de seguranca.
 * Paleta: azul marinho escuro + ciano tecnologico.
 */
public class Theme {

    // Fundos
    public static final Color BG         = new Color(10, 18, 32);
    public static final Color BG_CARD    = new Color(16, 28, 50);
    public static final Color BG_SIDEBAR = new Color(8, 14, 26);
    public static final Color BG_HEADER  = new Color(12, 22, 40);

    // Acentos
    public static final Color ACCENT     = new Color(0, 212, 255);
    public static final Color ACCENT2    = new Color(56, 189, 248);
    public static final Color SUCCESS    = new Color(34, 211, 144);
    public static final Color DANGER     = new Color(248, 81, 73);
    public static final Color WARNING    = new Color(251, 191, 36);
    public static final Color PURPLE     = new Color(167, 139, 250);
    public static final Color ORANGE     = new Color(251, 146, 60);

    // Texto
    public static final Color TEXT       = new Color(226, 232, 240);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);
    public static final Color TEXT_DIM   = new Color(148, 163, 184);

    // Bordas
    public static final Color BORDER     = new Color(30, 52, 80);
    public static final Color BORDER_LIT = new Color(0, 212, 255, 60);

    // Fontes
    public static final Font FONT_TITLE  = new Font(Font.SANS_SERIF, Font.BOLD, 18);
    public static final Font FONT_MEDIUM = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font FONT_MONO   = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    public static final Font FONT_BOLD   = new Font(Font.SANS_SERIF, Font.BOLD, 13);

    public static JButton primaryButton(String text) {
        return styledButton(text, ACCENT, new Color(8, 14, 26));
    }
    public static JButton dangerButton(String text) {
        return styledButton(text, DANGER, Color.WHITE);
    }

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
