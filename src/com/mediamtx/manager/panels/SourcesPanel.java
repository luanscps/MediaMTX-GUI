package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SourcesPanel extends JPanel {

    private static final String API_PATHS = "http://localhost:9997/v3/paths/list";

    private final MediaMTXService service;
    private final AppWindow appWindow;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private Timer autoRefreshTimer;

    private static final String[] COLS = {
        "Path / Nome", "Tipo de Fonte", "URL / Source", "Estado", "Leitores"
    };

    public SourcesPanel(MediaMTXService service, AppWindow appWindow) {
        this.service   = service;
        this.appWindow = appWindow;
        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(),  BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
        autoRefreshTimer = new Timer(5000, e -> refreshPaths());
        autoRefreshTimer.start();
        refreshPaths();
    }

    // ── Header ───────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setBackground(Theme.BG);
        top.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel title = new JLabel("\uD83D\uDCF9  Fontes de Retransmissão (Sources)");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        top.add(title, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(Theme.BG);

        statusLabel = new JLabel("●  aguardando...");
        statusLabel.setFont(Theme.FONT_SMALL);
        statusLabel.setForeground(Theme.TEXT_DIM);
        btns.add(statusLabel);

        JButton btnAdd = makeButton("➕ Adicionar Fonte", new Color(14, 165, 233));
        btnAdd.addActionListener(e -> openAddSourceDialog());
        btns.add(btnAdd);

        JButton btnRefresh = makeButton("↺ Atualizar", new Color(100, 116, 139));
        btnRefresh.addActionListener(e -> refreshPaths());
        btns.add(btnRefresh);

        top.add(btns, BorderLayout.EAST);
        return top;
    }

    // ── Tabela ───────────────────────────────────────────────────────────────
    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(COLS, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(Theme.FONT_MEDIUM);
        table.setForeground(Theme.TEXT);
        table.setBackground(Theme.BG_CARD);
        table.setGridColor(Theme.BORDER);
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(14, 165, 233, 40));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(Theme.FONT_BOLD);
        table.getTableHeader().setBackground(new Color(30, 41, 59));
        table.getTableHeader().setForeground(Theme.TEXT_DIM);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                String val = v == null ? "" : v.toString();
                if (c == 3) {
                    if (val.equalsIgnoreCase("ready")) {
                        setForeground(new Color(34, 197, 94));
                        setText("● ready");
                    } else if (val.equalsIgnoreCase("waiting")) {
                        setForeground(new Color(234, 179, 8));
                        setText("◌ waiting");
                    } else {
                        setForeground(new Color(239, 68, 68));
                        setText("✕ " + val);
                    }
                } else {
                    setForeground(Theme.TEXT);
                    setText(val);
                }
                setBackground(sel ? new Color(14, 165, 233, 40)
                    : (r % 2 == 0 ? Theme.BG_CARD : new Color(22, 30, 46)));
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setPreferredWidth(140);
        cm.getColumn(1).setPreferredWidth(160);
        cm.getColumn(2).setPreferredWidth(320);
        cm.getColumn(3).setPreferredWidth(100);
        cm.getColumn(4).setPreferredWidth(80);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_MED));
        scroll.getViewport().setBackground(Theme.BG_CARD);
        return scroll;
    }

    // ── Footer ───────────────────────────────────────────────────────────────
    private JLabel buildFooter() {
        JLabel info = new JLabel(
            "<html>Atualização automática a cada <b>5s</b> via API REST " +
            "(<code>GET /v3/paths/list</code>). " +
            "Use <b>➕ Adicionar Fonte</b> para inserir um novo path no <b>mediamtx.yml</b>. " +
            "Suporte a <b>RTMP</b>, <b>RTSP</b>, <b>SRT</b>, <b>HLS</b>, <b>WebRTC WHIP</b>, " +
            "<b>UDP/MPEG-TS</b>, <b>NVR Reolink/Dahua</b> e <b>Câmera Android</b>.</html>");
        info.setFont(Theme.FONT_SMALL);
        info.setForeground(Theme.TEXT_DIM);
        info.setBorder(new EmptyBorder(10, 0, 0, 0));
        return info;
    }

    // ── Refresh via API REST ─────────────────────────────────────────────────
    private void refreshPaths() {
        if (!service.isRunning()) {
            SwingUtilities.invokeLater(() -> {
                tableModel.setRowCount(0);
                statusLabel.setText("●  servidor parado");
                statusLabel.setForeground(new Color(239, 68, 68));
            });
            return;
        }
        new Thread(() -> {
            try {
                String json = httpGet(API_PATHS);
                List<Object[]> rows = parsePaths(json);
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    for (Object[] row : rows) tableModel.addRow(row);
                    statusLabel.setText("●  " + rows.size() + " path(s) ativos");
                    statusLabel.setForeground(new Color(34, 197, 94));
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("●  API indisponível");
                    statusLabel.setForeground(new Color(234, 179, 8));
                });
            }
        }, "sources-refresh").start();
    }

    // ── HTTP GET — sem new URL(String) deprecated ────────────────────────────
    private String httpGet(String urlStr) throws Exception {
        URI uri = URI.create(urlStr);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(2000);
        conn.setReadTimeout(3000);
        conn.setRequestProperty("Accept", "application/json");
        try (InputStream is = conn.getInputStream();
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    // ── Parser JSON manual ───────────────────────────────────────────────────
    private List<Object[]> parsePaths(String json) {
        List<Object[]> rows = new ArrayList<>();
        int itemsIdx = json.indexOf("\"items\"");
        if (itemsIdx < 0) return rows;
        int arrStart = json.indexOf('[', itemsIdx);
        int arrEnd   = json.lastIndexOf(']');
        if (arrStart < 0 || arrEnd < 0) return rows;
        String arr = json.substring(arrStart + 1, arrEnd);
        for (String obj : splitTopLevelObjects(arr)) {
            String name       = extractString(obj, "name");
            String state      = obj.contains("\"ready\":true") ? "ready" : "waiting";
            String sourceUrl  = extractSourceUrl(obj);
            String sourceType = detectSourceType(obj, sourceUrl);
            int    readers    = countReaders(obj);
            rows.add(new Object[]{ name, sourceType, sourceUrl, state, readers });
        }
        return rows;
    }

    private List<String> splitTopLevelObjects(String arr) {
        List<String> list = new ArrayList<>();
        int depth = 0, start = -1;
        for (int i = 0; i < arr.length(); i++) {
            char ch = arr.charAt(i);
            if (ch == '{') { if (depth == 0) start = i; depth++; }
            else if (ch == '}') {
                depth--;
                if (depth == 0 && start >= 0) { list.add(arr.substring(start, i + 1)); start = -1; }
            }
        }
        return list;
    }

    private String extractString(String obj, String key) {
        String search = "\"" + key + "\":\"";
        int idx = obj.indexOf(search);
        if (idx < 0) return "";
        int start = idx + search.length();
        int end = obj.indexOf('"', start);
        return end < 0 ? "" : obj.substring(start, end);
    }

    private String extractSourceUrl(String obj) {
        int srcIdx = obj.indexOf("\"source\":");
        if (srcIdx < 0) return "(publisher direto)";
        String after = obj.substring(srcIdx + 9).trim();
        if (after.startsWith("\"")) {
            int end = after.indexOf('"', 1);
            return end > 0 ? after.substring(1, end) : "(publisher direto)";
        }
        if (after.startsWith("{")) {
            String type = extractString(after, "type");
            String id   = extractString(after, "id");
            if (!id.isEmpty()) return type + " / " + id;
            return type.isEmpty() ? "(publisher direto)" : type;
        }
        return "(publisher direto)";
    }

    private String detectSourceType(String obj, String sourceUrl) {
        String lower = sourceUrl.toLowerCase();
        if (lower.startsWith("rtsp://"))    return "RTSP pull";
        if (lower.startsWith("rtmp://"))    return "RTMP pull";
        if (lower.startsWith("srt://"))     return "SRT pull";
        if (lower.startsWith("http://")  && lower.contains(".m3u8")) return "HLS pull";
        if (lower.startsWith("https://") && lower.contains(".m3u8")) return "HLS pull";
        if (lower.startsWith("whip://"))    return "WebRTC WHIP";
        if (lower.startsWith("udp://"))     return "UDP/MPEG-TS";
        if (obj.contains("rtmpConn"))       return "RTMP push";
        if (obj.contains("rtspSession"))    return "RTSP push";
        if (obj.contains("srtConn"))        return "SRT push";
        if (obj.contains("webrtcSession"))  return "WebRTC push";
        if (obj.contains("rpiCamera"))      return "RPi Camera";
        return "publisher externo";
    }

    private int countReaders(String obj) {
        int idx = obj.indexOf("\"readers\"");
        if (idx < 0) return 0;
        int count = 0, pos = idx;
        int bracketClose = obj.indexOf(']', idx);
        while ((pos = obj.indexOf("\"type\"", pos + 1)) >= 0) {
            if (bracketClose > 0 && pos > bracketClose) break;
            count++;
        }
        return count;
    }

    private void openAddSourceDialog() {
        AddSourceDialog dialog = new AddSourceDialog(
                SwingUtilities.getWindowAncestor(this),
                (pathName, yamlBlock) -> appWindow.insertYamlAndNavigate(yamlBlock)
        );
        dialog.setVisible(true);
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_MEDIUM);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        return btn;
    }

    public void stopRefresh() {
        if (autoRefreshTimer != null) autoRefreshTimer.stop();
    }
}
