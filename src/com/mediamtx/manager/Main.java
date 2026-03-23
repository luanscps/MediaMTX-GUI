package com.mediamtx.manager;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc",                    999);
            UIManager.put("Component.arc",                 8);
            UIManager.put("TextComponent.arc",             6);
            UIManager.put("ScrollBar.thumbArc",            999);
            UIManager.put("ScrollBar.width",               10);
            UIManager.put("TabbedPane.tabHeight",          38);
            UIManager.put("Component.focusWidth",          1);
            UIManager.put("Table.rowHeight",               32);
            UIManager.put("Panel.background",              new Color(245, 247, 250));
            UIManager.put("TabbedPane.background",         new Color(245, 247, 250));
            UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
            UIManager.put("MenuBar.background",            new Color(30, 41, 59));
            UIManager.put("MenuBar.foreground",            new Color(226, 232, 240));
            UIManager.put("Menu.foreground",               new Color(226, 232, 240));
            UIManager.put("Menu.background",               new Color(30, 41, 59));
            UIManager.put("MenuItem.background",           Color.WHITE);
            UIManager.put("MenuItem.foreground",           new Color(15, 23, 42));
            UIManager.put("Separator.foreground",          new Color(51, 65, 85));
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new AppWindow().setVisible(true));
    }
}
