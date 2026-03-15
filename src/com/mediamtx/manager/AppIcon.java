package com.mediamtx.manager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AppIcon {
    public static Image get() {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(30, 30, 46));
        g.fillRoundRect(0, 0, 64, 64, 16, 16);
        g.setColor(new Color(79, 195, 247));
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        g.drawString("M", 14, 44);
        g.dispose();
        return img;
    }
}
