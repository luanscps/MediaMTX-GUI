package com.mediamtx.manager.service;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.function.Consumer;

public class MediaMTXService {

    private Process  process;
    private String   binaryPath = "./mediamtx";
    private String   configPath = "./mediamtx.yml";
    private Consumer<String> logConsumer;
    private Consumer<String> versionConsumer;

    public void start() {
        if (isRunning()) { log("[AVISO] Servidor ja esta em execucao."); return; }
        try {
            ProcessBuilder pb = new ProcessBuilder(binaryPath, configPath);
            pb.redirectErrorStream(true);
            process = pb.start();
            log("[INFO] MediaMTX iniciado | PID: " + process.pid());
            new Thread(this::pipeOutput, "mtx-log").start();
        } catch (IOException e) {
            log("[ERRO] Falha ao iniciar: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "Nao foi possivel iniciar o MediaMTX.\n\n" + e.getMessage() +
                "\n\nVerifique o caminho do binario em Arquivo > Abrir binario...",
                "Erro ao iniciar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void stop() {
        if (!isRunning()) { log("[AVISO] Nenhum processo ativo."); return; }
        process.destroy();
        log("[INFO] Servidor parado.");
    }

    public void restart() {
        stop();
        try { Thread.sleep(800); } catch (Exception ignored) {}
        start();
    }

    public boolean isRunning() { return process != null && process.isAlive(); }

    private void pipeOutput() {
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) log(line);
        } catch (IOException ignored) {}
        log("[INFO] Processo encerrado.");
    }

    /**
     * Detecta a versao do binario executando: mediamtx --version
     * Resultado e enviado de forma assincrona ao versionConsumer (ex: HeaderPanel).
     */
    public void detectVersion() {
        if (binaryPath == null || binaryPath.isEmpty()) return;
        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder(binaryPath, "--version");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(p.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String trimmed = line.trim();
                        if (!trimmed.isEmpty()) {
                            final String version = parseVersion(trimmed);
                            if (versionConsumer != null) {
                                SwingUtilities.invokeLater(() -> versionConsumer.accept(version));
                            }
                            break;
                        }
                    }
                }
                p.waitFor();
            } catch (Exception e) {
                log("[AVISO] Nao foi possivel detectar versao: " + e.getMessage());
            }
        }, "mtx-version").start();
    }

    /** Extrai "v1.9.1" de strings como "mediamtx v1.9.1" ou simplesmente "1.9.1" */
    private String parseVersion(String raw) {
        java.util.regex.Matcher m =
            java.util.regex.Pattern.compile("v?(\\d+\\.\\d+[\\.\\d]*)").matcher(raw);
        return m.find() ? "v" + m.group(1) : raw;
    }

    public void chooseBinary(Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Selecione o binario mediamtx");
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            binaryPath = fc.getSelectedFile().getAbsolutePath();
            log("[CONFIG] Binario: " + binaryPath);
            detectVersion();
        }
    }

    public void chooseConfig(Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Selecione mediamtx.yml");
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            configPath = fc.getSelectedFile().getAbsolutePath();
            log("[CONFIG] Config: " + configPath);
        }
    }

    public String loadConfigContent() {
        try { return Files.readString(Path.of(configPath)); }
        catch (IOException e) { return defaultYaml(); }
    }

    public void saveConfigContent(String content) {
        try {
            Files.writeString(Path.of(configPath), content);
            log("[CONFIG] mediamtx.yml salvo em: " + configPath);
        } catch (IOException e) {
            log("[ERRO] Falha ao salvar YAML: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "Nao foi possivel salvar o arquivo:\n" + configPath +
                "\n\n" + e.getMessage(),
                "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getBinaryPath()  { return binaryPath; }
    public String getConfigPath()  { return configPath; }
    public void   setBinaryPath(String p) { this.binaryPath = p; }
    public void   setConfigPath(String p) { this.configPath = p; }
    public void   setLogConsumer(Consumer<String> c) { this.logConsumer = c; }
    public void   setVersionConsumer(Consumer<String> c) { this.versionConsumer = c; }
    private void  log(String msg) { if (logConsumer != null) logConsumer.accept(msg); }

    public String defaultYaml() {
        return YamlPresetService.generate(new YamlPresetService.Config());
    }
}
