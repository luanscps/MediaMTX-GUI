package com.mediamtx.manager.panels;

import com.mediamtx.manager.service.MediaMTXService;
import com.mediamtx.manager.theme.Theme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RecordingsPanel extends JPanel {

    private final MediaMTXService service;
    private int apiPort = 9997;

    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel lblTotal;
    private JLabel lblSize;
    private Timer refreshTimer;

    private static final String[] COLS = {
        "Path", "Arquivo", "Inicio", "Duracao", "Tamanho"
    };

    public RecordingsPanel(MediaMTXService service) {
        this.service = service;
        setLayout(new BorderLayout(0, 12));
        setBackground(Theme.BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildTable(),   BorderLayout.CENTER);
        add(buildFooter(),  BorderLayout.SOUTH);

        refreshTimer = new Timer(10000, e -> refresh());
        refreshTimer.start();
        refresh();
    }

    // ── Header ───────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.BG);
        bar.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel title = new JLabel("\uD83D\uDCC0  Gravacoes Locais");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.ORANGE);
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

        JButton btnOpenFolder = makeBtn("Abrir Pasta", Theme.ORANGE);
        btnOpenFolder.addActionListener(e -> openSelectedFolder());
        right.add(btnOpenFolder);

        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ── Cards resumo ─────────────────────────────────────────────────────────
    private JPanel buildSummaryCards() {
        JPanel cards = new JPanel(new GridLayout(1, 2, 12, 0));
        cards.setBackground(Theme.BG);
        cards.setBorder(new EmptyBorder(0, 0, 12, 0));

        lblTotal = buildCard(cards, "Total de Gravacoes", "0", Theme.ORANGE);
        lblSize  = buildCard(cards, "Tamanho Total",      "0 MB", new Color(124, 58, 237));

        return cards;
    }

    private JLabel buildCard(JPanel parent, String title, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
            BorderFactory.createLineBorder(Theme.BORDER)));

        JLabel t = new JLabel("  " + title);
        t.setFont(Theme.FONT_SMALL);
        t.setForeground(Theme.TEXT_DIM);
        t.setBorder(new EmptyBorder(8, 4, 2, 0));

        JLabel v = new JLabel("  " + value);
        v.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        v.setForeground(accent);
        v.setBorder(new EmptyBorder(0, 0, 8, 0));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        parent.add(card);
        return v;
    }

    // ── Tabela ───────────────────────────────────────────────────────────────
    private JPanel buildTable() {
        JPanel wrap = new JPanel(new BorderLayout(0, 8));
        wrap.setBackground(Theme.BG);
        wrap.add(buildSummaryCards(), BorderLayout.NORTH);

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
                    JTable t, Object value, boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, foc, row, col);
                String val = value == null ? "" : value.toString();
                if (col == 0) setForeground(Theme.ORANGE);
                else if (col == 4) setForeground(new Color(124, 58, 237));
                else setForeground(Theme.TEXT);
                setText(val);
                setBackground(sel ? new Color(245, 158, 11, 40)
                    : (row % 2 == 0 ? Theme.BG_CARD : Theme.BG));
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        });

        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setPreferredWidth(120);
        cm.getColumn(1).setPreferredWidth(280);
        cm.getColumn(2).setPreferredWidth(160);
        cm.getColumn(3).setPreferredWidth(90);
        cm.getColumn(4).setPreferredWidth(90);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_MED));
        scroll.getViewport().setBackground(Theme.BG_CARD);

        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }

    // ── Footer ───────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel f = new JPanel(new BorderLayout());
        f.setBackground(new Color(50, 48, 65));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
            new EmptyBorder(8, 12, 8, 12)));

        JLabel info = new JLabel(
            "<html><span style='color:#bbbbbb'>Atualizacao automatica a cada 10s via API REST &nbsp;&middot;&nbsp; " +
            "Selecione uma gravacao e clique em </span>" +
            "<span style='color:#f97316'><b>Abrir Pasta</b></span>" +
            "<span style='color:#bbbbbb'> para abrir no explorador de arquivos</span></html>");
        info.setFont(Theme.FONT_SMALL);
        f.add(info, BorderLayout.CENTER);
        f.setPreferredSize(new Dimension(0, 50));
        return f;
    }

    // ── Refresh ──────────────────────────────────────────────────────────────
    private void refresh() {
        if (!service.isRunning()) {
            SwingUtilities.invokeLater(() -> {
                tableModel.setRowCount(0);
                if (lblTotal != null) lblTotal.setText("  -");
                if (lblSize  != null) lblSize.setText("  -");
                statusLabel.setText("servidor parado");
                statusLabel.setForeground(new Color(239, 68, 68));
            });
            return;
        }
        new Thread(() -> {
            try {
                String json = httpGet("http://localhost:" + apiPort + "/v3/recordings/list");
                List<Object[]> rows = parseRecordings(json);
                long totalBytes = rows.stream()
                    .mapToLong(r -> (long) r[5])
                    .sum();

                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    for (Object[] row : rows) {
                        tableModel.addRow(new Object[]{ row[0], row[1], row[2], row[3], row[4] });
                    }
                    if (lblTotal != null) lblTotal.setText("  " + rows.size());
                    if (lblSize  != null) lblSize.setText("  " + formatSize(totalBytes));
                    statusLabel.setText("atualizado - " + rows.size() + " gravacao(oes)");
                    statusLabel.setForeground(new Color(34, 197, 94));
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("API indisponivel (porta " + apiPort + ")");
                    statusLabel.setForeground(new Color(234, 179, 8));
                });
            }
        }, "recordings-refresh").start();
    }

    // ── Abrir pasta da gravacao selecionada ──────────────────────────────────
    private JTable getTable() {
        // acessa a tabela pelo componente da scroll pane
        for (Component c : getComponents()) {
            if (c instanceof JPanel) {
                for (Component inner : ((JPanel) c).getComponents()) {
                    if (inner instanceof JScrollPane) {
                        return (JTable) ((JScrollPane) inner).getViewport().getView();
                    }
                }
            }
        }
        return null;
    }

    private void openSelectedFolder() {
        // tenta encontrar a tabela e abrir a pasta do arquivo selecionado
        Component center = ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (!(center instanceof JPanel)) return;
        JScrollPane sp = null;
        for (Component c : ((JPanel) center).getComponents()) {
            if (c instanceof JScrollPane) { sp = (JScrollPane) c; break; }
        }
        if (sp == null) return;
        JTable table = (JTable) sp.getViewport().getView();
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Selecione uma gravacao na tabela primeiro.",
                "Nenhuma gravacao selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String filePath = (String) tableModel.getValueAt(row, 1);
        try {
            File folder = Paths.get(filePath).toAbsolutePath().getParent().toFile();
            if (folder != null && folder.exists()) {
                Desktop.getDesktop().open(folder);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Pasta nao encontrada:\n" + filePath,
                    "Pasta nao encontrada", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Nao foi possivel abrir a pasta:\n" + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── HTTP GET ─────────────────────────────────────────────────────────────
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

    // ── Parser JSON manual ───────────────────────────────────────────────────
    private List<Object[]> parseRecordings(String json) {
        List<Object[]> rows = new ArrayList<>();
        int itemsIdx = json.indexOf("\"items\"");
        if (itemsIdx < 0) return rows;
        int arrStart = json.indexOf('[', itemsIdx);
        int arrEnd   = json.lastIndexOf(']');
        if (arrStart < 0 || arrEnd < 0) return rows;
        String arr = json.substring(arrStart + 1, arrEnd);

        for (String obj : splitObjects(arr)) {
            String pathName = extractField(obj, "name");
            // segments dentro de cada recording
            int segIdx = obj.indexOf("\"segments\"");
            if (segIdx < 0) continue;
            int segArrStart = obj.indexOf('[', segIdx);
            int segArrEnd   = obj.indexOf(']', segArrStart);
            if (segArrStart < 0 || segArrEnd < 0) continue;
            String segArr = obj.substring(segArrStart + 1, segArrEnd);
            for (String seg : splitObjects(segArr)) {
                String filePath = extractField(seg, "name");
                String start    = extractField(seg, "start");
                String dur      = extractField(seg, "duration");
                long   size     = extractLong(seg, "size");
                // formata
                start = start.length() > 19 ? start.substring(0, 19).replace("T", " ") : start;
                dur   = dur.isEmpty() ? "-" : dur;
                rows.add(new Object[]{ pathName, filePath, start, dur, formatSize(size), size });
            }
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

    private long extractLong(String obj, String key) {
        String s = "\"" + key + "\":";
        int i = obj.indexOf(s);
        if (i < 0) return 0;
        int st = i + s.length();
        StringBuilder sb = new StringBuilder();
        for (int j = st; j < obj.length(); j++) {
            char ch = obj.charAt(j);
            if (Character.isDigit(ch)) sb.append(ch);
            else if (sb.length() > 0) break;
        }
        try { return Long.parseLong(sb.toString()); }
        catch (NumberFormatException e) { return 0; }
    }

    private String formatSize(long bytes) {
        if (bytes <= 0)          return "0 B";
        if (bytes < 1024)        return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024L * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    public void setApiPort(int port) { this.apiPort = port; }

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
