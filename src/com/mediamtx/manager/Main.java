package com.mediamtx.manager;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.mediamtx.manager.theme.Theme;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            FlatDarculaLaf.setup();

            // ── Geometria dos componentes ──────────────────────────────────
            UIManager.put("Button.arc",                    999);
            UIManager.put("Component.arc",                 8);
            UIManager.put("TextComponent.arc",             6);
            UIManager.put("ScrollBar.thumbArc",            999);
            UIManager.put("ScrollBar.width",               10);
            UIManager.put("TabbedPane.tabHeight",          42);
            UIManager.put("Component.focusWidth",          1);
            UIManager.put("Table.rowHeight",               36);

            // ── Backgrounds dark ──────────────────────────────────────────
            UIManager.put("Panel.background",              new Color( 43,  43,  43));
            UIManager.put("TabbedPane.background",         new Color( 43,  43,  43));
            UIManager.put("TabbedPane.selectedBackground", new Color( 60,  63,  65));

            // ── Menu bar escuro com texto claro ───────────────────────────
            UIManager.put("MenuBar.background",            new Color( 30,  30,  30));
            UIManager.put("MenuBar.foreground",            new Color(187, 187, 187));
            UIManager.put("Menu.background",               new Color( 30,  30,  30));
            UIManager.put("Menu.foreground",               new Color(187, 187, 187));
            UIManager.put("MenuItem.background",           new Color( 60,  63,  65));
            UIManager.put("MenuItem.foreground",           new Color(187, 187, 187));
            UIManager.put("Separator.foreground",          new Color( 81,  81,  81));

            // ── Accent purple ─────────────────────────────────────────────
            UIManager.put("Component.focusColor",          new Color(159, 107, 255));
            UIManager.put("Component.linkColor",           new Color(159, 107, 255));
            UIManager.put("TabbedPane.underlineColor",     new Color(159, 107, 255));
            UIManager.put("Button.default.background",     new Color(159, 107, 255));

            // ── Fontes globais aumentadas ─────────────────────────────────
            UIManager.put("defaultFont",                   Theme.FONT_MEDIUM);
            UIManager.put("Table.font",                    Theme.FONT_MEDIUM);
            UIManager.put("TableHeader.font",              Theme.FONT_BOLD);
            UIManager.put("MenuItem.font",                 Theme.FONT_MEDIUM);
            UIManager.put("Menu.font",                     Theme.FONT_MEDIUM);
            UIManager.put("ToolTip.font",                  Theme.FONT_SMALL);

        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new AppWindow().setVisible(true));
    }
}
