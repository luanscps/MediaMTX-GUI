package com.mediamtx.manager.theme;

import javax.swing.*;
import java.awt.*;

/**
 * Tema CLARO profissional - estilo painel de monitoramento / streaming.
 * Fundo branco/cinza claro, acentos azul/verde vibrantes, texto escuro.
 */
public class Theme {

    // ── Fundos claros ────────────────────────────────────────────────────
    public static final Color BG          = new Color(245, 247, 250); // cinza quase branco
    public static final Color BG_CARD     = new Color(255, 255, 255); // branco puro
    public static final Color BG_SIDEBAR  = new Color(30,  41,  59);  // sidebar escura (contraste)
    public static final Color BG_HEADER   = new Color(30,  41,  59);  // header escuro
    public static final Color BG_INPUT    = new Color(248, 250, 252); // input levemente cinza

    // ── Acentos ──────────────────────────────────────────────────────────
    public static final Color ACCENT      = new Color( 14, 165, 233); // sky-500
    public static final Color ACCENT2     = new Color( 59, 130, 246); // blue-500
    public static final Color SUCCESS     = new Color( 22, 163,  74); // green-600
    public static final Color DANGER      = new Color(220,  38,  38); // red-600
    public static final Color WARNING     = new Color(217, 119,   6); // amber-600
    public static final Color PURPLE      = new Color(124,  58, 237); // violet-600
    public static final Color ORANGE      = new Color(234,  88,  12); // orange-600
    public static final Color TEAL        = new Color( 13, 148, 136); // teal-600

    // ── Texto escuro (legivel sobre fundo claro) ─────────────────────────
    public static final Color TEXT        = new Color( 15,  23,  42); // slate-900
    public static final Color TEXT_DIM    = new Color( 71,  85, 105); // slate-600
    public static final Color TEXT_MUTED  = new Color(148, 163, 184); // slate-400

    // ── Bordas ───────────────────────────────────────────────────────────
    public static final Color BORDER      = new Color(226, 232, 240); // slate-200
    public static final Color BORDER_MED  = new Color(203, 213, 225); // slate-300
    public static final Color BORDER_LIT  = new Color( 14, 165, 233, 90);

    // ── Sidebar (texto claro sobre fundo escuro) ─────────────────────────
    public static final Color SIDEBAR_TEXT       = new Color(226, 232, 240);
    public static final Color SIDEBAR_TEXT_MUTED = new Color(100, 116, 139);

    // ── Fontes ───────────────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font(Font.SANS_SERIF, Font.BOLD,  18);
    public static final Font FONT_MEDIUM  = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font FONT_MONO    = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    public static final Font FONT_BOLD    = new Font(Font.SANS_SERIF, Font.BOLD,  13);
    public static final Font FONT_LABEL   = new Font(Font.SANS_SERIF, Font.BOLD,  10);

    // ── Botoes factory ───────────────────────────────────────────────────
    public static JButton primaryButton(String text) {
        return styledButton(text, ACCENT, Color.WHITE);
    }
    public static JButton dangerButton(String text) {
        return styledButton(text, DANGER, Color.WHITE);
    }
    public static JButton successButton(String text) {
        return styledButton(text, SUCCESS, Color.WHITE);
    }
    public static JButton warningButton(String text) {
        return styledButton(text, WARNING, Color.WHITE);
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
