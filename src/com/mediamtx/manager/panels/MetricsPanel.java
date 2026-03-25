package com.mediamtx.manager.panels;

import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MetricsPanel extends JPanel {

    private final MediaMTXService service;
    private Timer refreshTimer;
    private int apiPort = 9997;

    private JLabel lblTotalPaths;
    private JLabel lblReadyPaths;
    private JLabel lblTotalReaders;
    private JLabel lblApiStatus;

    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    private static final String[] COLS = {
        "Path", "Estado", "Leitores", "Tipo de Fonte", "Publisher Ativo"
    };

    public MetricsPanel(MediaMTXService service) {
        this.service = service;
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildTopBar(),  BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.BG);
        bar.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel title = new JLabel("\uD83D\uDCCA  Metricas em Tempo Real");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ACCENT);
        bar.add(title, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setBackground(Theme.BG);

        statusLabel = new JLabel("aguardando...");
        statusLabel.setFont(Theme.FONT_SMALL);
        statusLabel.setForeground(Theme.TEXT_DIM);
        right.add(statusLabel);

        JButton btnRefresh = makeBtn("Atualizar", Theme.TEXT_MUTED);
        btnRefresh.addActionListener(e -> refresh());
        right.add(btnRefresh);

        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildContent() {
        JPanel wrap = new JPanel(new BorderLayout(0, 16));
        wrap.setBackground(Theme.BG);

        // Cards de resumo
        JPanel cards = new JPanel(new MigLayout("insets 0, gap 16 0", "[grow][grow][grow][grow]", "[100px!]"));
        cards.setBackground(Theme.BG);

        lblApiStatus    = buildCard(cards, "API REST",      "verificando...", Theme.ACCENT);
        lblTotalPaths   = buildCard(cards, "Paths Totais",  "0",              Theme.PURPLE);
        lblReadyPaths   = buildCard(cards, "Paths Ativos",  "0",              Theme.SUCCESS);
        lblTotalReaders = buildCard(cards, "Leitores",      "0",              new Color(245, 158, 11));

        wrap.add(cards, BorderLayout.NORTH);

        // Tabela
        tableModel = new DefaultTableModel(COLS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(Theme.FONT_MEDIUM);
        table.setForeground(Theme.TEXT);
        table.setBackground(Theme.BG_CARD);
        table.setGridColor(Theme.BORDER);
        table.setRowHeight(30);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(Theme.FONT_BOLD);
        table.getTableHeader().setBackground(Theme.BG_HEADER);
        table.getTableHeader().setForeground(Theme.TEXT_DIM);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                String val = value == null ? "" : value.toString();
                if (col == 1) {
                    if (val.equalsIgnoreCase("ready")) {
                        setForeground(new Color(34, 197, 94));
                        setText("pronto");
                    } else {
                        setForeground(new Color(234, 179, 8));
                        setText("aguardando");
                    }
                } else if (col == 4) {
                    setForeground(val.equals("sim") ? new Color(34, 197, 94) : Theme.TEXT_DIM);
                    setText(val);
                } else {
                    setForeground(Theme.TEXT);
                    setText(val);
                }
                setBackground(isSelected ? new Color(14, 165, 233, 40)
                    : (row % 2 == 0 ? Theme.BG_CARD : Theme.BG));
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        });

        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setPreferredWidth(160);
        cm.getColumn(1).setPreferredWidth(100);
        cm.getColumn(2).setPreferredWidth(80);
        cm.getColumn(3).setPreferredWidth(180);
        cm.getColumn(4).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_MED));
        scroll.getViewport().setBackground(Theme.BG_CARD);

        JLabel tableTitle = new JLabel("  Detalhamento por Path");
        tableTitle.setFont(Theme.FONT_BOLD);
        tableTitle.setForeground(Theme.TEXT_DIM);
        tableTitle.setBorder(new EmptyBorder(8, 0, 6, 0));

        JPanel tableWrap = new JPanel(new BorderLayout());
        tableWrap.setBackground(Theme.BG);
        tableWrap.add(tableTitle, BorderLayout.NORTH);
        tableWrap.add(scroll, BorderLayout.CENTER);

        wrap.add(tableWrap, BorderLayout.CENTER);

        refreshTimer = new Timer(5000, e -> refresh());
        refreshTimer.start();
        refresh();

        return wrap;
    }

    private JLabel buildCard(JPanel parent, String title, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
            BorderFactory.createLineBorder(Theme.BORDER)));

        JLabel lblTitle = new JLabel("  " + title);
        lblTitle.setFont(Theme.FONT_SMALL);
        lblTitle.setForeground(Theme.TEXT_DIM);
        lblTitle.setBorder(new EmptyBorder(10, 4, 2, 0));

        JLabel lblValue = new JLabel("  " + value);
        lblValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        lblValue.setForeground(accent);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        parent.add(card, "grow");
        return lblValue;
    }

    private void refresh() {
        if (!service.isRunning()) {
            SwingUtilities.invokeLater(() -> {
                lblApiStatus.setText("  offline");
                lblApiStatus.setForeground(new Color(239, 68, 68));
                lblTotalPaths.setText("  -");
                lblReadyPaths.setText("  -");
                lblTotalReaders.setText("  -");
                tableModel.setRowCount(0);
                statusLabel.setText("servidor parado");
                statusLabel.setForeground(new Color(239, 68, 68));
            });
            return;
        }
        new Thread(() -> {
            try {
                String json = httpGet("http://localhost:" + apiPort + "/v3/paths/list");
                List<Object[]> rows = parsePaths(json);
                int total   = rows.size();
                long ready  = rows.stream().filter(r -> "ready".equals(r[1])).count();
                int readers = rows.stream().mapToInt(r -> (int) r[2]).sum();

                SwingUtilities.invokeLater(() -> {
                    lblApiStatus.setText("  online");
                    lblApiStatus.setForeground(new Color(34, 197, 94));
                    lblTotalPaths.setText("  " + total);
                    lblReadyPaths.setText("  " + ready);
                    lblTotalReaders.setText("  " + readers);
                    tableModel.setRowCount(0);
                    for (Object[] row : rows) tableModel.addRow(row);
                    statusLabel.setText("atualizado - " + total + " paths");
                    statusLabel.setForeground(new Color(34, 197, 94));
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    lblApiStatus.setText("  erro");
                    lblApiStatus.setForeground(new Color(234, 179, 8));
                    statusLabel.setText("API indisponivel (porta " + apiPort + ")");
                    statusLabel.setForeground(new Color(234, 179, 8));
                });
            }
        }, "metrics-refresh").start();
    }

    private String httpGet(String urlStr) throws Exception {
        URI uri = URI.create(urlStr);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(2000);
        conn.setReadTimeout(3000);
        try (InputStream is = conn.getInputStream();
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    private List<Object[]> parsePaths(String json) {
        List<Object[]> rows = new ArrayList<>();
        int itemsIdx = json.indexOf("\"items\"");
        if (itemsIdx < 0) return rows;
        int arrStart = json.indexOf('[', itemsIdx);
        int arrEnd   = json.lastIndexOf(']');
        if (arrStart < 0 || arrEnd < 0) return rows;
        String arr = json.substring(arrStart + 1, arrEnd);
        for (String obj : splitObjects(arr)) {
            String pathName = extractField(obj, "name");
            String state    = obj.contains("\"ready\":true") ? "ready" : "waiting";
            int    rCount   = countReaders(obj);
            String srcUrl   = extractSourceUrl(obj);
            String srcType  = detectType(obj, srcUrl);
            boolean hasPub  = obj.contains("\"publisher\":{") && !obj.contains("\"publisher\":null");
            rows.add(new Object[]{ pathName, state, rCount, srcType, hasPub ? "sim" : "nao" });
        }
        return rows;
    }

    private List<String> splitObjects(String arr) {
        List<String> list = new ArrayList<>();
        int depth = 0, start = -1;
        for (int i = 0; i < arr.length(); i++) {
            char ch = arr.charAt(i);
            if (ch == '{') { if (depth == 0) start = i; depth++; }
            else if (ch == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    list.add(arr.substring(start, i + 1));
                    start = -1;
                }
            }
        }
        return list;
    }

    private String extractField(String obj, String key) {
        String s = "\"" + key + "\":\"";
        int i = obj.indexOf(s);
        if (i < 0) return "";
        int st = i + s.length();
        int en = obj.indexOf('"', st);
        return en < 0 ? "" : obj.substring(st, en);
    }

    private String extractSourceUrl(String obj) {
        int i = obj.indexOf("\"source\":");
        if (i < 0) return "(publisher direto)";
        String after = obj.substring(i + 9).trim();
        if (after.startsWith("\"")) {
            int end = after.indexOf('"', 1);
            return end > 0 ? after.substring(1, end) : "(publisher direto)";
        }
        return "(publisher direto)";
    }

    private String detectType(String obj, String url) {
        String l = url.toLowerCase();
        if (l.startsWith("rtsp://"))  return "RTSP pull";
        if (l.startsWith("rtmp://"))  return "RTMP pull";
        if (l.startsWith("srt://"))   return "SRT pull";
        if (l.contains(".m3u8"))      return "HLS pull";
        if (l.startsWith("udp://"))   return "UDP/MPEG-TS";
        if (obj.contains("rtmpConn"))       return "RTMP push";
        if (obj.contains("rtspSession"))    return "RTSP push";
        if (obj.contains("srtConn"))        return "SRT push";
        if (obj.contains("webrtcSession"))  return "WebRTC push";
        return "-";
    }

    private int countReaders(String obj) {
        int idx = obj.indexOf("\"readers\"");
        if (idx < 0) return 0;
        int count = 0, pos = idx;
        int end = obj.indexOf(']', idx);
        while ((pos = obj.indexOf("\"type\"", pos + 1)) >= 0) {
            if (end > 0 && pos > end) break;
            count++;
        }
        return count;
    }

    public void setApiPort(int port) {
        this.apiPort = port;
    }

    public void stopRefresh() {
        if (refreshTimer != null) refreshTimer.stop();
    }

    private JButton makeBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(Theme.FONT_MEDIUM);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        return b;
    }
}
