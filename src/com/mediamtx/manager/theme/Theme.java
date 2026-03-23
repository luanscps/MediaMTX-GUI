package com.mediamtx.manager.theme;

import javax.swing.*;
import java.awt.*;

/**
 * Modern Light — inspirado em Linear / Vercel / Raycast
 * Paleta com contraste elevado: texto mais escuro, bordas mais visíveis.
 */
public class Theme {

    // ── Fundos ────────────────────────────────────────────────────────────
    public static final Color BG         = new Color(248, 250, 252); // slate-50   #F8FAFC
    public static final Color BG_CARD    = new Color(255, 255, 255); // branco puro #FFFFFF
    public static final Color BG_HEADER  = new Color( 15,  23,  42); // slate-900  #0F172A
    public static final Color BG_INPUT   = new Color(241, 245, 249); // slate-100  #F1F5F9
    public static final Color BG_HOVER   = new Color(226, 232, 240); // slate-200  #E2E8F0

    // ── Texto — mais escuro para melhor leitura ───────────────────────────
    public static final Color TEXT       = new Color( 15,  23,  42); // slate-900  #0F172A
    public static final Color TEXT_DIM   = new Color( 51,  65,  85); // slate-700  #334155
    public static final Color TEXT_MUTED = new Color(100, 116, 139); // slate-500  #64748B

    // ── Bordas — mais visíveis que antes ─────────────────────────────────
    public static final Color BORDER     = new Color(203, 213, 225); // slate-300  #CBD5E1
    public static final Color BORDER_MED = new Color(148, 163, 184); // slate-400  #94A3B8
    public static final Color BORDER_LIT = new Color( 14, 165, 233, 100);

    // ── Acentos vibrantes ─────────────────────────────────────────────────
    public static final Color ACCENT     = new Color( 14, 165, 233); // sky-500    #0EA5E9
    public static final Color ACCENT2    = new Color( 99, 102, 241); // indigo-500 #6366F1
    public static final Color SUCCESS    = new Color( 22, 163,  74); // green-600  #16A34A
    public static final Color DANGER     = new Color(220,  38,  38); // red-600    #DC2626
    public static final Color WARNING    = new Color(217, 119,   6); // amber-600  #D97706
    public static final Color PURPLE     = new Color(124,  58, 237); // violet-600 #7C3AED
    public static final Color ORANGE     = new Color(234,  88,  12); // orange-600 #EA580C
    public static final Color TEAL       = new Color( 13, 148, 136); // teal-600   #0D9488

    // ── Sidebar (fundo escuro, texto claro) ───────────────────────────────
    public static final Color SIDEBAR_TEXT       = new Color(226, 232, 240); // slate-200
    public static final Color SIDEBAR_TEXT_MUTED = new Color(100, 116, 139); // slate-500

    // ── Tipografia ────────────────────────────────────────────────────────
    public static final Font FONT_TITLE  = new Font(Font.SANS_SERIF, Font.BOLD,  18);
    public static final Font FONT_BOLD   = new Font(Font.SANS_SERIF, Font.BOLD,  13);
    public static final Font FONT_MEDIUM = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font FONT_LABEL  = new Font(Font.SANS_SERIF, Font.BOLD,  10);
    public static final Font FONT_MONO   = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    // ── Botões factory ────────────────────────────────────────────────────
    public static JButton primaryButton(String text) {
        return styledButton(text, ACCENT, Color.WHITE);
    }
    public static JButton successButton(String text) {
        return styledButton(text, SUCCESS, Color.WHITE);
    }
    public static JButton dangerButton(String text) {
        return styledButton(text, DANGER, Color.WHITE);
    }
    public static JButton warningButton(String text) {
        return styledButton(text, WARNING, Color.WHITE);
    }
    public static JButton secondaryButton(String text) {
        return styledButton(text, BG_HOVER, TEXT);
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
