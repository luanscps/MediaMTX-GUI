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
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_MED));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(241, 245, 249));
        header.setBorder(new EmptyBorder(4, 10, 4, 10));
        JLabel lbl = new JLabel("\uD83D\uDDC2  Log do Processo");
        lbl.setFont(Theme.FONT_BOLD);
        lbl.setForeground(Theme.TEXT_DIM);
        header.add(lbl, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setBackground(new Color(15, 23, 42));
        area.setForeground(new Color(134, 239, 172)); // verde claro
        area.setCaretColor(new Color(56, 189, 248));
        area.setBorder(new EmptyBorder(6, 10, 6, 10));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
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
