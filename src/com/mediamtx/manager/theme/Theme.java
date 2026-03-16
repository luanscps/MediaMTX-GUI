package com.mediamtx.manager.theme;

import javax.swing.*;
import java.awt.*;

/**
 * Tema visual profissional - estilo monitoramento / camera / streaming.
 * Paleta: slate azulado medio com acentos ciano e verde vibrante.
 * Mais claro e legivel que o tema anterior.
 */
public class Theme {

    // ── Fundos (mais claros, menos pesados) ────────────────────────────────
    public static final Color BG         = new Color(15, 23, 42);   // slate-900
    public static final Color BG_CARD    = new Color(30, 41, 59);   // slate-800
    public static final Color BG_SIDEBAR = new Color(15, 23, 42);   // slate-900
    public static final Color BG_HEADER  = new Color(22, 33, 54);   // meio-termo
    public static final Color BG_INPUT   = new Color(30, 41, 59);   // slate-800

    // ── Acentos vibrantes ─────────────────────────────────────────────────
    public static final Color ACCENT     = new Color(56,  189, 248); // sky-400   (ciano claro)
    public static final Color ACCENT2    = new Color(99,  179, 237); // blue-300
    public static final Color SUCCESS    = new Color(52,  211, 153); // emerald-400
    public static final Color DANGER     = new Color(248,  113, 113); // red-400
    public static final Color WARNING    = new Color(251,  191,  36); // amber-400
    public static final Color PURPLE     = new Color(192,  132, 252); // violet-400
    public static final Color ORANGE     = new Color(251,  146,  60); // orange-400
    public static final Color TEAL       = new Color( 45,  212, 191); // teal-400

    // ── Texto (mais contrastado) ───────────────────────────────────────────
    public static final Color TEXT       = new Color(226, 232, 240); // slate-200
    public static final Color TEXT_DIM   = new Color(148, 163, 184); // slate-400
    public static final Color TEXT_MUTED = new Color( 71,  85, 105); // slate-600

    // ── Bordas ────────────────────────────────────────────────────────────
    public static final Color BORDER     = new Color( 51,  65,  85); // slate-700
    public static final Color BORDER_LIT = new Color( 56, 189, 248, 80); // accent c/ alpha

    // ── Fontes ────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE  = new Font(Font.SANS_SERIF, Font.BOLD,  18);
    public static final Font FONT_MEDIUM = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font FONT_MONO   = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    public static final Font FONT_BOLD   = new Font(Font.SANS_SERIF, Font.BOLD,  13);
    public static final Font FONT_LABEL  = new Font(Font.SANS_SERIF, Font.BOLD,  10);

    // ── Botoes factory ────────────────────────────────────────────────────
    public static JButton primaryButton(String text) {
        return styledButton(text, ACCENT, new Color(15, 23, 42));
    }
    public static JButton dangerButton(String text) {
        return styledButton(text, DANGER, Color.WHITE);
    }
    public static JButton successButton(String text) {
        return styledButton(text, SUCCESS, new Color(15, 23, 42));
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
