package com.mediamtx.manager.service;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.function.Consumer;

public class MediaMTXService {

    private Process process;
    private String  binaryPath = "./mediamtx";
    private String  configPath = "./mediamtx.yml";
    private Consumer<String> logConsumer;

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

    public void chooseBinary(Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Selecione o binario mediamtx");
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            binaryPath = fc.getSelectedFile().getAbsolutePath();
            log("[CONFIG] Binario: " + binaryPath);
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
        try { Files.writeString(Path.of(configPath), content); log("[CONFIG] YAML salvo."); }
        catch (IOException e) { log("[ERRO] Falha ao salvar YAML: " + e.getMessage()); }
    }

    public String getBinaryPath() { return binaryPath; }
    public String getConfigPath() { return configPath; }
    public void setLogConsumer(Consumer<String> c) { this.logConsumer = c; }
    private void log(String msg) { if (logConsumer != null) logConsumer.accept(msg); }

    public String defaultYaml() {
        return "# MediaMTX - Configuracao\n" +
               "# Docs: https://github.com/bluenviron/mediamtx\n\n" +
               "api: yes\napiAddress: :9997\n\n" +
               "rtsp: yes\nrtspAddress: :8554\n\n" +
               "rtmp: yes\nrtmpAddress: :1935\n\n" +
               "hls: yes\nhlsAddress: :8888\n\n" +
               "webrtc: yes\nwebrtcAddress: :8889\n\n" +
               "srt: yes\nsrtAddress: :8890\n\n" +
               "record: no\n" +
               "recordPath: ./recordings/%path/%Y-%m-%d_%H-%M-%S-%f\n" +
               "recordFormat: fmp4\n" +
               "recordSegmentDuration: 1h\n" +
               "recordDeleteAfter: 24h\n\n" +
               "paths:\n  all_others:\n";
    }
}
