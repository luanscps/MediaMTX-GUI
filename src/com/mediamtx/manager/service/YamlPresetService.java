package com.mediamtx.manager.service;

/**
 * Gera o mediamtx.yml completo baseado na documentacao oficial.
 * Todos os parametros do YAML de referencia estao cobertos.
 */
public class YamlPresetService {

    // ── Enums ────────────────────────────────────────────────────────────

    public enum StreamQuality {
        //  label                              bitrate    preset       gop   height
        P360_BAIXA  ("Baixa      360p  512k",  "512k",   "ultrafast", "15", "360"),
        P480_SD     ("SD         480p  800k",  "800k",   "ultrafast", "20", "480"),
        P720_MEDIA  ("Media      720p  1.5M",  "1500k",  "ultrafast", "30", "720"),
        P720_ALTA   ("Media+     720p  2.5M",  "2500k",  "fast",      "30", "720"),
        P1080_ALTA  ("Alta      1080p  4M",    "4000k",  "fast",      "30", "1080"),
        P1080_60FPS ("Alta 60fps 1080p 6M",    "6000k",  "fast",      "60", "1080"),
        P1080_ULTRA ("Ultra     1080p  8M",    "8000k",  "medium",    "60", "1080"),
        P1440_QHD   ("QHD  2K   1440p  12M",  "12000k", "medium",    "60", "1440"),
        P2160_4K    ("4K        2160p  20M",   "20000k", "slow",      "60", "2160"),
        P2160_4K60  ("4K  60fps 2160p  40M",   "40000k", "slow",      "60", "2160"),
        P2160_4KULTRA("4K Ultra 2160p  80M",   "80000k", "veryslow",  "60", "2160");
        public final String label, bitrate, preset, gop, height;
        StreamQuality(String l, String b, String p, String g, String h) {
            label=l; bitrate=b; preset=p; gop=g; height=h;
        }
        @Override public String toString() { return label; }
    }

    public enum RecordMode {
        DESATIVADA("Desativada"),
        SEG_1S    ("Partes de 1 segundo"),
        SEG_30S   ("Segmentos de 30 segundos"),
        SEG_1MIN  ("Segmentos de 1 minuto"),
        SEG_5MIN  ("Segmentos de 5 minutos"),
        SEG_30MIN ("Segmentos de 30 minutos"),
        SEG_1H    ("Segmentos de 1 hora"),
        CONTINUA  ("Continua (1 segmento)");
        public final String label;
        RecordMode(String l) { this.label = l; }
        @Override public String toString() { return label; }
        public String toDuration() {
            switch(this){
                case SEG_1S:   return "1s";
                case SEG_30S:  return "30s";
                case SEG_1MIN: return "1m";
                case SEG_5MIN: return "5m";
                case SEG_30MIN:return "30m";
                case SEG_1H:   return "1h";
                default:       return "1h";
            }
        }
    }

    public enum RecordFormat {
        FMP4("fmp4 - Fragmented MP4 (recomendado)", "fmp4"),
        MPEGTS("mpegts - MPEG-TS", "mpegts");
        public final String label, value;
        RecordFormat(String l, String v) { label=l; value=v; }
        @Override public String toString() { return label; }
    }

    public enum AuthMode {
        NENHUMA("Sem autenticacao (aberto)"),
        SIMPLES("Usuario e senha (internal)"),
        HTTP   ("HTTP externo"),
        JWT    ("JWT Token (JWKS)");
        public final String label;
        AuthMode(String l) { this.label = l; }
        @Override public String toString() { return label; }
    }

    public enum LogLevel {
        ERROR("error"), WARN("warn"), INFO("info"), DEBUG("debug");
        public final String value;
        LogLevel(String v) { this.value = v; }
        @Override public String toString() { return value; }
    }

    public enum HlsVariant {
        LOW_LATENCY("lowLatency - Baixa Latencia (recomendado)", "lowLatency"),
        FMP4       ("fmp4 - Fragmented MP4",                     "fmp4"),
        MPEGTS     ("mpegts - Maximo de compatibilidade",        "mpegts");
        public final String label, value;
        HlsVariant(String l, String v) { label=l; value=v; }
        @Override public String toString() { return label; }
    }

    public enum RtspEncryption {
        NO      ("no - Sem criptografia",  "no"),
        STRICT  ("strict - Somente TLS",   "strict"),
        OPTIONAL("optional - TLS opcional","optional");
        public final String label, value;
        RtspEncryption(String l, String v) { label=l; value=v; }
        @Override public String toString() { return label; }
    }

    public enum RtmpEncryption {
        NO      ("no - Sem criptografia",  "no"),
        STRICT  ("strict - Somente RTMPS", "strict"),
        OPTIONAL("optional - RTMPS opcional","optional");
        public final String label, value;
        RtmpEncryption(String l, String v) { label=l; value=v; }
        @Override public String toString() { return label; }
    }

    public enum DeleteAfter {
        H1 ("1h"), H6("6h"), H12("12h"), D1("1d"),
        D2 ("2d"), D7("7d"), D30("30d"), NEVER("0s");
        public final String value;
        DeleteAfter(String v) { this.value = v; }
        @Override public String toString() { return value.equals("0s") ? "Nunca deletar" : value; }
    }

    // ── Modelo de configuracao (todos os campos do YAML oficial) ─────────

    public static class Config {
        // --- General ---
        public LogLevel logLevel          = LogLevel.INFO;
        public boolean  logToFile         = false;
        public String   logFile           = "mediamtx.log";
        public boolean  logStructured     = false;
        public String   readTimeout       = "10s";
        public String   writeTimeout      = "10s";
        public int      writeQueueSize    = 512;
        public int      udpMaxPayloadSize = 1452;
        public String   runOnConnect      = "";
        public boolean  runOnConnectRestart = false;

        // --- API ---
        public boolean enableApi          = true;
        public String  portApi            = "9997";
        public boolean apiEncryption      = false;
        public String  apiAllowOrigins    = "*";

        // --- Metrics ---
        public boolean enableMetrics      = false;
        public String  portMetrics        = "9998";

        // --- Playback ---
        public boolean enablePlayback     = false;
        public String  portPlayback       = "9996";

        // --- RTSP ---
        public boolean        enableRtsp      = true;
        public String         portRtsp        = "8554";
        public String         portRtsps       = "8322";
        public RtspEncryption rtspEncryption  = RtspEncryption.NO;
        public boolean        rtspUdp         = true;
        public boolean        rtspMulticast   = false;
        public String         portRtp         = "8000";
        public String         portRtcp        = "8001";

        // --- RTMP ---
        public boolean        enableRtmp      = true;
        public String         portRtmp        = "1935";
        public String         portRtmps       = "1936";
        public RtmpEncryption rtmpEncryption  = RtmpEncryption.NO;

        // --- HLS ---
        public boolean    enableHls           = true;
        public String     portHls             = "8888";
        public HlsVariant hlsVariant          = HlsVariant.LOW_LATENCY;
        public int        hlsSegmentCount     = 7;
        public String     hlsSegmentDuration  = "1s";
        public String     hlsPartDuration     = "200ms";
        public boolean    hlsAlwaysRemux      = false;
        public String     hlsDirectory        = "";

        // --- WebRTC ---
        public boolean enableWebrtc          = true;
        public String  portWebrtc            = "8889";
        public String  portWebrtcUDP         = "8189";
        public boolean webrtcIce             = false;
        public String  webrtcIceServer       = "stun:stun.l.google.com:19302";
        public boolean webrtcIPsFromInterfaces = true;

        // --- SRT ---
        public boolean enableSrt             = true;
        public String  portSrt               = "8890";

        // --- Auth ---
        public AuthMode authMode             = AuthMode.NENHUMA;
        public String   authUser             = "admin";
        public String   authPass             = "senha123";
        public String   authHTTPAddress      = "";
        public String   authJWTJWKS          = "";
        public String   authJWTClaimKey      = "mediamtx_permissions";

        // --- Record (pathDefaults) ---
        public RecordMode   recordMode        = RecordMode.DESATIVADA;
        public RecordFormat recordFormat      = RecordFormat.FMP4;
        public String       recordPath        = "./recordings/%path/%Y-%m-%d_%H-%M-%S-%f";
        public String       recordPartDuration= "1s";
        public DeleteAfter  recordDeleteAfter = DeleteAfter.D1;

        // --- Path defaults ---
        public boolean sourceOnDemand        = false;
        public int     maxReaders            = 0;
        public boolean overridePublisher     = true;
        public boolean useAbsoluteTimestamp  = false;
        public String  runOnReady            = "";
        public boolean runOnReadyRestart     = false;
        public String  runOnRead             = "";
        public boolean runOnReadRestart      = false;
        public String  runOnInit             = "";
        public boolean runOnInitRestart      = false;
        public String  runOnDemand           = "";
        public boolean runOnDemandRestart    = false;
        public String  runOnDemandCloseAfter = "10s";
    }

    // ── Gerador YAML completo ─────────────────────────────────────────────

    public static String generate(Config c) {
        StringBuilder s = new StringBuilder();
        s.append("# MediaMTX - Gerado pelo MediaMTX GUI\n");
        s.append("# Documentacao: https://github.com/bluenviron/mediamtx\n\n");

        // --- General ---
        s.append("###############################################\n");
        s.append("# Global settings\n\n");
        s.append("logLevel: ").append(c.logLevel.value).append("\n");
        if (c.logToFile) {
            s.append("logDestinations: [stdout, file]\n");
            s.append("logFile: ").append(c.logFile).append("\n");
        } else {
            s.append("logDestinations: [stdout]\n");
        }
        s.append("logStructured: ").append(c.logStructured).append("\n");
        s.append("readTimeout: ").append(c.readTimeout).append("\n");
        s.append("writeTimeout: ").append(c.writeTimeout).append("\n");
        s.append("writeQueueSize: ").append(c.writeQueueSize).append("\n");
        s.append("udpMaxPayloadSize: ").append(c.udpMaxPayloadSize).append("\n");
        if (!c.runOnConnect.isEmpty()) {
            s.append("runOnConnect: ").append(c.runOnConnect).append("\n");
            s.append("runOnConnectRestart: ").append(c.runOnConnectRestart).append("\n");
        }
        s.append("\n");

        // --- Auth ---
        s.append("###############################################\n");
        s.append("# Authentication\n\n");
        switch (c.authMode) {
            case NENHUMA:
                s.append("authMethod: internal\n");
                s.append("authInternalUsers:\n");
                s.append("- user: any\n  pass:\n  ips: []\n");
                s.append("  permissions:\n");
                s.append("  - action: publish\n    path:\n");
                s.append("  - action: read\n    path:\n");
                s.append("  - action: playback\n    path:\n");
                s.append("- user: any\n  pass:\n  ips: ['127.0.0.1', '::1']\n");
                s.append("  permissions:\n  - action: api\n  - action: metrics\n  - action: pprof\n");
                break;
            case SIMPLES:
                s.append("authMethod: internal\n");
                s.append("authInternalUsers:\n");
                s.append("- user: ").append(c.authUser).append("\n");
                s.append("  pass: ").append(c.authPass).append("\n");
                s.append("  ips: []\n");
                s.append("  permissions:\n");
                s.append("  - action: publish\n    path:\n");
                s.append("  - action: read\n    path:\n");
                s.append("  - action: playback\n    path:\n");
                s.append("  - action: api\n  - action: metrics\n  - action: pprof\n");
                s.append("- user: any\n  pass:\n  ips: ['127.0.0.1', '::1']\n");
                s.append("  permissions:\n  - action: api\n  - action: metrics\n  - action: pprof\n");
                break;
            case HTTP:
                s.append("authMethod: http\n");
                s.append("authHTTPAddress: ").append(c.authHTTPAddress).append("\n");
                s.append("authHTTPExclude:\n- action: api\n- action: metrics\n- action: pprof\n");
                break;
            case JWT:
                s.append("authMethod: jwt\n");
                s.append("authJWTJWKS: ").append(c.authJWTJWKS).append("\n");
                s.append("authJWTClaimKey: ").append(c.authJWTClaimKey).append("\n");
                s.append("authJWTInHTTPQuery: true\n");
                s.append("authJWTExclude: []\n");
                break;
        }
        s.append("\n");

        // --- API ---
        s.append("###############################################\n");
        s.append("# Control API\n\n");
        s.append("api: ").append(c.enableApi ? "true" : "false").append("\n");
        s.append("apiAddress: :").append(c.portApi).append("\n");
        s.append("apiEncryption: ").append(c.apiEncryption).append("\n");
        s.append("apiAllowOrigins: ['").append(c.apiAllowOrigins).append("']\n");
        s.append("apiTrustedProxies: []\n\n");

        // --- Metrics ---
        s.append("###############################################\n");
        s.append("# Metrics\n\n");
        s.append("metrics: ").append(c.enableMetrics ? "true" : "false").append("\n");
        s.append("metricsAddress: :").append(c.portMetrics).append("\n");
        s.append("metricsEncryption: false\n");
        s.append("metricsAllowOrigins: ['*']\n");
        s.append("metricsTrustedProxies: []\n\n");

        // --- Playback ---
        s.append("###############################################\n");
        s.append("# Playback server\n\n");
        s.append("playback: ").append(c.enablePlayback ? "true" : "false").append("\n");
        s.append("playbackAddress: :").append(c.portPlayback).append("\n");
        s.append("playbackEncryption: false\n");
        s.append("playbackAllowOrigins: ['*']\n");
        s.append("playbackTrustedProxies: []\n\n");

        // --- RTSP ---
        s.append("###############################################\n");
        s.append("# RTSP server\n\n");
        s.append("rtsp: ").append(c.enableRtsp ? "true" : "false").append("\n");
        if (c.enableRtsp) {
            StringBuilder transports = new StringBuilder("[tcp");
            if (c.rtspUdp)       transports.append(", udp");
            if (c.rtspMulticast) transports.append(", multicast");
            transports.append("]");
            s.append("rtspTransports: ").append(transports).append("\n");
            s.append("rtspEncryption: \"").append(c.rtspEncryption.value).append("\"\n");
            s.append("rtspAddress: :").append(c.portRtsp).append("\n");
            s.append("rtspsAddress: :").append(c.portRtsps).append("\n");
            if (c.rtspUdp) {
                s.append("rtpAddress: :").append(c.portRtp).append("\n");
                s.append("rtcpAddress: :").append(c.portRtcp).append("\n");
            }
            if (c.rtspMulticast) {
                s.append("multicastIPRange: 224.1.0.0/16\n");
                s.append("multicastRTPPort: 8002\n");
                s.append("multicastRTCPPort: 8003\n");
            }
            s.append("rtspAuthMethods: [basic]\n");
        }
        s.append("\n");

        // --- RTMP ---
        s.append("###############################################\n");
        s.append("# RTMP server\n\n");
        s.append("rtmp: ").append(c.enableRtmp ? "true" : "false").append("\n");
        if (c.enableRtmp) {
            s.append("rtmpEncryption: \"").append(c.rtmpEncryption.value).append("\"\n");
            s.append("rtmpAddress: :").append(c.portRtmp).append("\n");
            s.append("rtmpsAddress: :").append(c.portRtmps).append("\n");
        }
        s.append("\n");

        // --- HLS ---
        s.append("###############################################\n");
        s.append("# HLS server\n\n");
        s.append("hls: ").append(c.enableHls ? "true" : "false").append("\n");
        if (c.enableHls) {
            s.append("hlsAddress: :").append(c.portHls).append("\n");
            s.append("hlsEncryption: false\n");
            s.append("hlsAllowOrigins: ['*']\n");
            s.append("hlsTrustedProxies: []\n");
            s.append("hlsAlwaysRemux: ").append(c.hlsAlwaysRemux).append("\n");
            s.append("hlsVariant: ").append(c.hlsVariant.value).append("\n");
            s.append("hlsSegmentCount: ").append(c.hlsSegmentCount).append("\n");
            s.append("hlsSegmentDuration: ").append(c.hlsSegmentDuration).append("\n");
            s.append("hlsPartDuration: ").append(c.hlsPartDuration).append("\n");
            s.append("hlsSegmentMaxSize: 50M\n");
            if (!c.hlsDirectory.isEmpty())
                s.append("hlsDirectory: '").append(c.hlsDirectory).append("'\n");
            else
                s.append("hlsDirectory: ''\n");
            s.append("hlsMuxerCloseAfter: 60s\n");
        }
        s.append("\n");

        // --- WebRTC ---
        s.append("###############################################\n");
        s.append("# WebRTC server\n\n");
        s.append("webrtc: ").append(c.enableWebrtc ? "true" : "false").append("\n");
        if (c.enableWebrtc) {
            s.append("webrtcAddress: :").append(c.portWebrtc).append("\n");
            s.append("webrtcEncryption: false\n");
            s.append("webrtcAllowOrigins: ['*']\n");
            s.append("webrtcTrustedProxies: []\n");
            s.append("webrtcLocalUDPAddress: :").append(c.portWebrtcUDP).append("\n");
            s.append("webrtcLocalTCPAddress: ''\n");
            s.append("webrtcIPsFromInterfaces: ").append(c.webrtcIPsFromInterfaces).append("\n");
            s.append("webrtcIPsFromInterfacesList: []\n");
            s.append("webrtcAdditionalHosts: []\n");
            if (c.webrtcIce) {
                s.append("webrtcICEServers2:\n");
                s.append("  - url: ").append(c.webrtcIceServer).append("\n");
                s.append("    username: ''\n    password: ''\n    clientOnly: false\n");
            } else {
                s.append("webrtcICEServers2: []\n");
            }
            s.append("webrtcSTUNGatherTimeout: 5s\n");
            s.append("webrtcHandshakeTimeout: 10s\n");
            s.append("webrtcTrackGatherTimeout: 2s\n");
        }
        s.append("\n");

        // --- SRT ---
        s.append("###############################################\n");
        s.append("# SRT server\n\n");
        s.append("srt: ").append(c.enableSrt ? "true" : "false").append("\n");
        if (c.enableSrt)
            s.append("srtAddress: :").append(c.portSrt).append("\n");
        s.append("\n");

        // --- pathDefaults ---
        s.append("###############################################\n");
        s.append("# Default path settings\n\n");
        s.append("pathDefaults:\n");
        s.append("  source: publisher\n");
        s.append("  sourceOnDemand: ").append(c.sourceOnDemand).append("\n");
        s.append("  sourceOnDemandStartTimeout: 10s\n");
        s.append("  sourceOnDemandCloseAfter: 10s\n");
        s.append("  maxReaders: ").append(c.maxReaders).append("\n");
        s.append("  overridePublisher: ").append(c.overridePublisher).append("\n");
        s.append("  useAbsoluteTimestamp: ").append(c.useAbsoluteTimestamp).append("\n\n");

        // record
        s.append("  record: ").append(c.recordMode != RecordMode.DESATIVADA ? "true" : "false").append("\n");
        s.append("  recordPath: ").append(c.recordPath).append("\n");
        s.append("  recordFormat: ").append(c.recordFormat.value).append("\n");
        s.append("  recordPartDuration: ").append(c.recordPartDuration).append("\n");
        s.append("  recordMaxPartSize: 50M\n");
        if (c.recordMode != RecordMode.DESATIVADA)
            s.append("  recordSegmentDuration: ").append(c.recordMode.toDuration()).append("\n");
        else
            s.append("  recordSegmentDuration: 1h\n");
        s.append("  recordDeleteAfter: ").append(c.recordDeleteAfter.value).append("\n\n");

        // hooks
        if (!c.runOnInit.isEmpty()) {
            s.append("  runOnInit: ").append(c.runOnInit).append("\n");
            s.append("  runOnInitRestart: ").append(c.runOnInitRestart).append("\n");
        } else {
            s.append("  runOnInit:\n  runOnInitRestart: false\n");
        }
        if (!c.runOnDemand.isEmpty()) {
            s.append("  runOnDemand: ").append(c.runOnDemand).append("\n");
            s.append("  runOnDemandRestart: ").append(c.runOnDemandRestart).append("\n");
            s.append("  runOnDemandStartTimeout: 10s\n");
            s.append("  runOnDemandCloseAfter: ").append(c.runOnDemandCloseAfter).append("\n");
        } else {
            s.append("  runOnDemand:\n  runOnDemandRestart: false\n");
            s.append("  runOnDemandStartTimeout: 10s\n  runOnDemandCloseAfter: 10s\n");
        }
        if (!c.runOnReady.isEmpty()) {
            s.append("  runOnReady: ").append(c.runOnReady).append("\n");
            s.append("  runOnReadyRestart: ").append(c.runOnReadyRestart).append("\n");
        } else {
            s.append("  runOnReady:\n  runOnReadyRestart: false\n");
        }
        if (!c.runOnRead.isEmpty()) {
            s.append("  runOnRead: ").append(c.runOnRead).append("\n");
            s.append("  runOnReadRestart: ").append(c.runOnReadRestart).append("\n");
        } else {
            s.append("  runOnRead:\n  runOnReadRestart: false\n");
        }
        s.append("  runOnNotReady:\n  runOnUnread:\n  runOnUnDemand:\n");
        s.append("  runOnRecordSegmentCreate:\n  runOnRecordSegmentComplete:\n\n");

        // --- paths ---
        s.append("###############################################\n");
        s.append("# Path settings\n\n");
        s.append("paths:\n");
        s.append("  all_others:\n");
        s.append("  # Adicione paths especificos abaixo ou use a aba Sources\n");

        return s.toString();
    }
}
