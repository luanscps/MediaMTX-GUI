package com.mediamtx.manager.panels;

import com.mediamtx.manager.AppWindow;
import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SourcesPanel extends JPanel {

    private static final int DEFAULT_API_PORT = 9997;

    private final MediaMTXService service;
    private final AppWindow appWindow;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private Timer autoRefreshTimer;
    private int apiPort = DEFAULT_API_PORT;

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

    /** Chamado pelo AppWindow/Wizard quando a porta da API muda */
    public void setApiPort(int port) {
        this.apiPort = port;
    }

    private String apiUrl(String path) {
        return "http://localhost:" + apiPort + path;
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

        JButton btnKick = makeButton("🗑 Remover Path", new Color(220, 38, 38));
        btnKick.addActionListener(e -> kickSelectedPath());
        btns.add(btnKick);

        JButton btnCopyUrl = makeButton("📋 Copiar URL", new Color(124, 58, 237));
        btnCopyUrl.addActionListener(e -> copyUrlDialog());
        btns.add(btnCopyUrl);

        JButton btnRefresh = makeButton("↺ Atualizar", Theme.TEXT_MUTED);
        btnRefresh.addActionListener(e -> refreshPaths());
        btns.add(btnRefresh);

        top.add(btns, BorderLayout.EAST);
        return top;
    }

    // ── Tabela ───────────────────────────────────────────────────────────────
    private JTable table;

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(COLS, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(Theme.FONT_MEDIUM);
        table.setForeground(Theme.TEXT);
        table.setBackground(Theme.BG_CARD);
        table.setGridColor(Theme.BORDER);
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(14, 165, 233, 40));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(Theme.FONT_BOLD);
        table.getTableHeader().setBackground(Theme.BG_HEADER);
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
                    : (r % 2 == 0 ? Theme.BG_CARD : Theme.BG));
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
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(true);
        footer.setBackground(new Color(50, 48, 65));
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
            new EmptyBorder(1, 12, 7, 12)));

        JLabel info = new JLabel(
            "<html>" +
            "<span style='color:#9f6bff'><b>Atualizacao automatica a cada 5s</b></span>" +
            "<span style='color:#bbbbbb'> via API REST &nbsp;&middot;&nbsp; </span>" +
            "<span style='color:#bbbbbb'>Use </span>" +
            "<span style='color:#ffc644'><b>+ Adicionar Fonte</b></span>" +
            "<span style='color:#bbbbbb'> para inserir path no mediamtx.yml &nbsp;&middot;&nbsp; </span>" +
            "<span style='color:#90ee90'><b>Remover Path</b></span>" +
            "<span style='color:#bbbbbb'> faz kick via API REST sem parar o servidor</span>" +
            "</html>");
        info.setFont(Theme.FONT_SMALL);
        footer.add(info, BorderLayout.CENTER);
        footer.setMinimumSize(new Dimension(0, 70));
        footer.setPreferredSize(new Dimension(0, 70));
        return footer;
    }

    // ── Kick / Remover path ──────────────────────────────────────────────────
    private void kickSelectedPath() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Selecione um path na tabela antes de remover.",
                "Nenhum path selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String pathName = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Remover (kick) o path \"" + pathName + "\" do servidor?\n" +
            "Isso desconecta publishers e leitores ativos desse path.",
            "Confirmar remoção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        new Thread(() -> {
            try {
                String url = apiUrl("/v3/paths/kick/" + URLEncoder.encode(pathName, "UTF-8"));
                httpDelete(url);
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("● path removido: " + pathName);
                    statusLabel.setForeground(new Color(34, 197, 94));
                    refreshPaths();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(this,
                        "Erro ao remover path via API:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE));
            }
        }, "sources-kick").start();
    }

    // ── Copiar URL ───────────────────────────────────────────────────────────
    private void copyUrlDialog() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Selecione um path na tabela primeiro.",
                "Nenhum path selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String pathName = (String) tableModel.getValueAt(row, 0);
        String host = "localhost";

        String[] options = {
            "RTSP   → rtsp://" + host + ":8554/" + pathName,
            "RTMP   → rtmp://" + host + ":1935/" + pathName,
            "HLS    → http://" + host + ":8888/" + pathName,
            "WebRTC → http://" + host + ":8889/" + pathName,
            "SRT    → srt://"  + host + ":8890?streamid=request:" + pathName,
        };

        String choice = (String) JOptionPane.showInputDialog(this,
            "Escolha o protocolo para copiar a URL do path \"" + pathName + "\":",
            "Copiar URL de acesso", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice != null) {
            String url = choice.substring(choice.indexOf("→ ") + 2).trim();
            Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(url), null);
            statusLabel.setText("● URL copiada: " + url);
            statusLabel.setForeground(new Color(34, 197, 94));
        }
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
                String json = httpGet(apiUrl("/v3/paths/list"));
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

    // ── HTTP GET ─────────────────────────────────────────────────────────────
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

    // ── HTTP DELETE ──────────────────────────────────────────────────────────
    private void httpDelete(String urlStr) throws Exception {
        URI uri = URI.create(urlStr);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("DELETE");
        conn.setConnectTimeout(2000);
        conn.setReadTimeout(3000);
        int code = conn.getResponseCode();
        if (code >= 400) throw new IOException("HTTP " + code);
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
