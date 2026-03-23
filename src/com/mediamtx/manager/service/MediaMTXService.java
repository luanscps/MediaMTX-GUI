package com.mediamtx.manager.service;

import com.mediamtx.manager.service.YamlPresetService;
import com.mediamtx.manager.service.YamlPresetService.Config;

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

    public String getBinaryPath() { return binaryPath; }
    public String getConfigPath()  { return configPath; }
    public void   setBinaryPath(String p) { this.binaryPath = p; }
    public void   setConfigPath(String p) { this.configPath = p; }
    public void   setLogConsumer(Consumer<String> c) { this.logConsumer = c; }
    private void  log(String msg) { if (logConsumer != null) logConsumer.accept(msg); }

    /**
     * YAML padrao completo e valido para o MediaMTX.
     * Gerado pelo YamlPresetService com config padrao.
     */
    public String defaultYaml() {
        return YamlPresetService.generate(new Config());
    }
}
