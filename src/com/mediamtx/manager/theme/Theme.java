package com.mediamtx.manager.theme;

import javax.swing.*;
import java.awt.*;

public class Theme {

    // ── Backgrounds ───────────────────────────────────────────────────────
    public static final Color BG         = new Color( 43,  43,  43); // Darcula bg principal
    public static final Color BG_CARD    = new Color( 60,  63,  65); // Darcula card/panel
    public static final Color BG_HEADER  = new Color( 30,  30,  30); // header bem escuro
    public static final Color BG_INPUT   = new Color( 69,  73,  74); // input field
    public static final Color BG_HOVER   = new Color( 82,  82,  82); // hover state
    public static final Color BG_SELECT  = new Color( 75,  74, 110); // seleção purple

    // ── Texto ─────────────────────────────────────────────────────────────
    public static final Color TEXT       = new Color(187, 187, 187); // texto principal claro
    public static final Color TEXT_DIM   = new Color(160, 160, 160); // texto secundário
    public static final Color TEXT_MUTED = new Color(120, 120, 120); // texto apagado
    public static final Color TEXT_WHITE = new Color(214, 214, 214); // texto quase branco

    // ── Bordas ────────────────────────────────────────────────────────────
    public static final Color BORDER     = new Color( 81,  81,  81); // borda padrão
    public static final Color BORDER_MED = new Color(100, 100, 100); // borda média
    public static final Color BORDER_LIT = new Color(159, 107, 255,  80); // purple translúcido

    // ── Accent Purple (IntelliJ Darcula purple) ───────────────────────────
    public static final Color ACCENT     = new Color(159, 107, 255); // purple principal #9F6BFF
    public static final Color ACCENT2    = new Color(104,  87, 255); // indigo-purple   #6857FF
    public static final Color ACCENT_DIM = new Color( 98,  78, 178); // purple escuro hover

    // ── Status ────────────────────────────────────────────────────────────
    public static final Color SUCCESS    = new Color(106, 153,  85); // verde Darcula
    public static final Color DANGER     = new Color(255,  84,  84); // vermelho claro
    public static final Color WARNING    = new Color(255, 198,  68); // amarelo Darcula
    public static final Color PURPLE     = new Color(197, 134, 192); // lilás IntelliJ
    public static final Color ORANGE     = new Color(206, 116,  43); // laranja Darcula
    public static final Color TEAL       = new Color( 86, 156, 214); // azul IntelliJ

    // ── Sidebar ───────────────────────────────────────────────────────────
    public static final Color SIDEBAR_TEXT       = new Color(187, 187, 187);
    public static final Color SIDEBAR_TEXT_MUTED = new Color(120, 120, 120);

    // ── Fontes — aumentadas para melhor legibilidade ──────────────────────
    public static final Font FONT_TITLE  = new Font(Font.SANS_SERIF, Font.BOLD,  20); // era 18
    public static final Font FONT_BOLD   = new Font(Font.SANS_SERIF, Font.BOLD,  15); // era 13
    public static final Font FONT_MEDIUM = new Font(Font.SANS_SERIF, Font.PLAIN, 15); // era 13
    public static final Font FONT_SMALL  = new Font(Font.SANS_SERIF, Font.PLAIN, 13); // era 12
    public static final Font FONT_LABEL  = new Font(Font.SANS_SERIF, Font.BOLD,  12); // era 10
    public static final Font FONT_MONO   = new Font(Font.MONOSPACED, Font.PLAIN, 14); // era 12

    // ── Botões ────────────────────────────────────────────────────────────
    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_BOLD);
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        return b;
    }

    public static JButton successButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_BOLD);
        b.setBackground(SUCCESS);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        return b;
    }

    public static JButton warningButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_BOLD);
        b.setBackground(WARNING);
        b.setForeground(new Color(30, 30, 30));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        return b;
    }

    public static JButton dangerButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_BOLD);
        b.setBackground(DANGER);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        return b;
    }

    public static JButton secondaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_BOLD);
        b.setBackground(BG_CARD);
        b.setForeground(TEXT);
        b.setFocusPainted(false);
        b.setOpaque(true);
        return b;
    }
}
