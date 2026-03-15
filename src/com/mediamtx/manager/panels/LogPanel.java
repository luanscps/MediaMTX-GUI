package com.mediamtx.manager.panels;

import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogPanel extends JPanel {

    private final JTextArea area;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LogPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_SIDEBAR);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));

        JLabel lbl = new JLabel("  Log do Processo");
        lbl.setFont(Theme.FONT_BOLD); lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setBorder(new EmptyBorder(4,4,4,4));
        add(lbl, BorderLayout.NORTH);

        area = new JTextArea();
        area.setEditable(false);
        area.setFont(Theme.FONT_MONO);
        area.setBackground(new Color(18, 18, 28));
        area.setForeground(new Color(0, 220, 100));
        area.setCaretColor(Theme.ACCENT);
        area.setBorder(new EmptyBorder(4, 8, 4, 8));
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    public void append(String msg) {
        String ts = LocalDateTime.now().format(FMT);
        area.append("[" + ts + "] " + msg + "\n");
        area.setCaretPosition(area.getDocument().getLength());
    }

    public void clear() { area.setText(""); }

    public void saveToFile(Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("mediamtx-log.txt"));
        if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            try {
                Files.writeString(fc.getSelectedFile().toPath(), area.getText());
                JOptionPane.showMessageDialog(parent, "Log salvo!", "OK", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
