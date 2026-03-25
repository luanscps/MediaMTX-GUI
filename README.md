# MediaMTX GUI

<p align="center">
  <b>Interface gráfica desktop em Java para gerenciar o servidor de mídia <a href="https://github.com/bluenviron/mediamtx">MediaMTX</a></b><br/>
  Controle completo do servidor, streams, gravação, autenticação e muito mais — sem tocar na linha de comando.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-orange?style=flat-square"/>
  <img src="https://img.shields.io/badge/MediaMTX-compatible-blue?style=flat-square"/>
  <img src="https://img.shields.io/badge/Platform-Linux%20%7C%20Windows%20%7C%20macOS-lightgrey?style=flat-square"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=flat-square"/>
  <img src="https://img.shields.io/badge/UI-Java%20Swing%20%2B%20MigLayout-blueviolet?style=flat-square"/>
</p>

---

## O que é o MediaMTX GUI?

O **MediaMTX GUI** é uma interface gráfica desktop desenvolvida em **Java (Swing + MigLayout)** para gerenciar o servidor de mídia [MediaMTX](https://github.com/bluenviron/mediamtx) de forma visual e produtiva. O app encapsula o processo do servidor, oferecendo controle total por abas especializadas: dashboard, fontes de streams, **assistente de configuração visual**, métricas em tempo real, editor YAML direto e console de log.

O MediaMTX suporta **RTSP, RTMP, HLS, WebRTC, SRT** e muito mais. Este GUI expõe todas essas configurações de forma clara e estruturada, eliminando a necessidade de editar o `mediamtx.yml` manualmente para operações do dia a dia.

---

## Pré-requisitos

- **Java 17 ou superior** — [Baixar JDK (Adoptium)](https://adoptium.net/)
- **Binário do MediaMTX** — [Baixar releases](https://github.com/bluenviron/mediamtx/releases)

---

## Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/luanscps/MediaMTX-GUI.git
cd MediaMTX-GUI
```

### 2. Compile

**Linux / macOS:**
```bash
chmod +x build.sh
./build.sh
```

**Windows:**
```bat
build.bat
```

### 3. Execute

```bash
java -jar MediaMTX-GUI.jar
```

### 4. Configure o binário do MediaMTX

No menu: **Arquivo → Abrir binário...** → selecione o executável `mediamtx` baixado.

Opcionalmente: **Arquivo → Abrir config YAML...** → aponte para um `mediamtx.yml` existente.

---

## Interface — Abas principais

O app possui **6 abas** na janela principal:

| Aba | Ícone | Descrição |
| :-- | :-- | :-- |
| **Dashboard** | 🏠 | Visão geral, status do servidor, portas e instruções rápidas |
| **Sources** | 📹 | Fontes de retransmissão ativas via API REST, com refresh automático |
| **Assistente** | ⚙ | Configurador visual completo do `mediamtx.yml` — sem editar YAML manualmente |
| **Métricas** | 📊 | Streams ativos, leitores e estatísticas em tempo real |
| **Config YAML** | 📄 | Editor bruto do `mediamtx.yml` com possibilidade de edição direta |
| **Log** | 🗂 | Console com saída do processo MediaMTX em tempo real |

---

## Aba Sources — Fontes de Retransmissão

A aba **Sources** exibe todos os paths ativos no servidor via **API REST** (`/v3/paths/list`), com atualização automática a cada **5 segundos**. A tabela mostra:

| Coluna | Descrição |
| :-- | :-- |
| **Path / Nome** | Nome do path configurado |
| **Tipo de Fonte** | Detectado automaticamente (RTSP pull, RTMP push, SRT, HLS, WebRTC, UDP, etc.) |
| **URL / Source** | Endereço da fonte ou `(publisher direto)` |
| **Estado** | `● ready` (verde), `◌ waiting` (amarelo) ou erro (vermelho) |
| **Leitores** | Número de leitores conectados no momento |

### Adicionar Fonte — Dialog completo

Clique em **➕ Adicionar Fonte** para abrir o assistente de inserção. Ele gera automaticamente o bloco YAML correto para o tipo escolhido e insere direto no editor.

**Tipos de fonte suportados:**

| # | Tipo | Descrição |
| :-- | :-- | :-- |
| 1 | **RTMP push** | Câmera ou OBS enviando para o servidor |
| 2 | **RTSP pull** | Servidor puxa de câmera IP |
| 3 | **SRT push** | Encoder enviando SRT para o servidor |
| 4 | **SRT pull** | Servidor puxa stream SRT externo |
| 5 | **FFmpeg runOnDemand** | Webcam local ou arquivo via FFmpeg |
| 6 | **Câmera Android** | Integração com CAMSTREAMER-BR (abre WebControl) |
| 7 | **HLS pull** | Servidor puxa stream HLS (`.m3u8`) |
| 8 | **UDP/MPEG-TS** | Encoder FFmpeg enviando UDP |
| 9 | **RTMP pull** | Servidor puxa de RTMP externo |
| 10 | **RTSP push passivo** | Câmera envia diretamente, sem source configurado |
| 11 | **WebRTC WHIP** | Browser ou OBS via protocolo WHIP |
| 12 | **Reolink / Dahua NVR** | RTSP com autenticação, canal e subtipo configuráveis |

Cada tipo exibe campos dinâmicos relevantes (URL, IP, porta, usuário, senha) e um **preview do YAML** em tempo real. O botão **✔ Inserir no YAML** injeta o bloco diretamente no editor e navega para a aba **Config YAML**.

---

## Aba Assistente — Configurador Visual

O **Assistente de Configuração** é o painel mais poderoso do app. Ele gera o `mediamtx.yml` completo visualmente, sem precisar conhecer a sintaxe YAML. Organizado em **cards temáticos** com scroll, cada seção corresponde a uma área do arquivo de configuração oficial.

---

### 🔧 Configurações Gerais

| Campo | Padrão | Descrição |
| :-- | :-- | :-- |
| Nível de log | `info` | `error`, `warn`, `info`, `debug` |
| Read timeout | `10s` | Timeout de leitura de conexões |
| Write timeout | `10s` | Timeout de escrita de conexões |
| Salvar log em arquivo | `false` | Redireciona log para arquivo além do stdout |
| Arquivo de log | `mediamtx.log` | Caminho do arquivo de log |
| Write queue size | `512` | Tamanho da fila de escrita (64–65536) |
| UDP max payload | `1452` | Tamanho máximo do payload UDP |

---

### 📡 Protocolos — Habilitar / Porta

Cada protocolo pode ser habilitado/desabilitado individualmente, com porta configurável:

| Protocolo | Padrão Habilitado | Porta Padrão |
| :-- | :-- | :-- |
| **RTSP** | ✅ Sim | `8554` |
| **RTMP** | ✅ Sim | `1935` |
| **HLS** | ✅ Sim | `8888` |
| **WebRTC** | ✅ Sim | `8889` |
| **SRT** | ✅ Sim | `8890` |
| **API REST** | ✅ Sim | `9997` |

---

### 🔒 RTSP — Configurações Avançadas

| Campo | Padrão | Descrição |
| :-- | :-- | :-- |
| Criptografia | `no` | `no`, `strict` (somente TLS), `optional` |
| Porta RTSPS (TLS) | `8322` | Porta para RTSP sobre TLS |
| Habilitar UDP (RTP/RTCP) | ✅ | Transportes UDP além do TCP |
| Porta RTP | `8000` | Porta RTP UDP |
| Porta RTCP | `8001` | Porta RTCP UDP |
| Habilitar Multicast | ❌ | Transmissão multicast (`224.1.0.0/16`) |

---

### 🔴 RTMP — Configurações Avançadas

| Campo | Padrão | Descrição |
| :-- | :-- | :-- |
| Criptografia RTMP | `no` | `no`, `strict` (somente RTMPS), `optional` |
| Porta RTMPS | `1936` | Porta para RTMP sobre TLS |

---

### 🟡 HLS — Configurações Avançadas

| Campo | Padrão | Descrição |
| :-- | :-- | :-- |
| Variante HLS | `lowLatency` | `lowLatency` (recomendado), `fmp4`, `mpegts` |
| Segmentos em cache | `7` | Número de segmentos mantidos em memória |
| Duração do segmento | `1s` | Duração de cada segmento HLS |
| Duração da part (LL-HLS) | `200ms` | Para Low-Latency HLS |
| Gerar HLS sempre | ❌ | `alwaysRemux` — gera HLS mesmo sem leitores |
| Diretório HLS | *(vazio = RAM)* | Pasta para armazenar segmentos em disco |

---

### 🟢 WebRTC — Configurações Avançadas

| Campo | Padrão | Descrição |
| :-- | :-- | :-- |
| Porta UDP local | `8189` | Porta UDP para WebRTC |
| Detectar IPs por interfaces | ✅ | Descobre IPs automaticamente pelas interfaces de rede |
| ICE Server externo | ❌ | Habilita STUN/TURN externo |
| ICE Server URL | `stun:stun.l.google.com:19302` | URL do servidor ICE (ativado condicionalmente) |

---

### 🔵 Serviços Extras (Metrics / Playback)

| Campo | Padrão | Porta | Descrição |
| :-- | :-- | :-- | :-- |
| **Metrics (Prometheus)** | ❌ | `9998` | Exporta métricas no formato Prometheus |
| **Playback server** | ❌ | `9996` | Servidor de reprodução de gravações |

---

### 🔑 Autenticação

O método é selecionado em um ComboBox e os campos mudam dinamicamente:

**Sem autenticação (padrão):**
> Qualquer usuário pode publicar, ler e acessar playback. API/Métricas liberadas apenas para `127.0.0.1`.

**Usuário e senha (internal):**

| Campo | Padrão |
| :-- | :-- |
| Usuário | `admin` |
| Senha | `senha123` |

**HTTP externo:**

| Campo | Exemplo |
| :-- | :-- |
| URL de autenticação | `http://localhost:9000/auth` |

**JWT Token (JWKS):**

| Campo | Exemplo |
| :-- | :-- |
| JWKS URL | `http://localhost:9000/.well-known/jwks.json` |
| Claim key | `mediamtx_permissions` |

---

### 🟠 Gravação Local (Record)

| Campo | Padrão | Opções |
| :-- | :-- | :-- |
| Modo de gravação | `Desativada` | Partes de 1s, 30s, 1min, 5min, 30min, 1h, Contínua |
| Formato | `fmp4` (recomendado) | `fmp4`, `mpegts` |
| Deletar após | `1d` | 1h, 6h, 12h, 1d, 2d, 7d, 30d, Nunca |
| Pasta de gravação | `./recordings/%path/%Y-%m-%d_%H-%M-%S-%f` | Suporta variáveis de path e data |
| Duração da parte (RPO) | `1s` | Granularidade mínima de segmento |

> O painel de opções aparece automaticamente quando o modo de gravação é ativado.

---

### 🎨 Qualidade do Stream

Preset de qualidade para uso como referência ao configurar paths FFmpeg:

| Preset | Resolução | Bitrate | FFmpeg preset | GOP |
| :-- | :-- | :-- | :-- | :-- |
| Baixa | 360p | 512k | ultrafast | 15 |
| SD | 480p | 800k | ultrafast | 20 |
| **Média (padrão)** | **720p** | **1.5M** | **ultrafast** | **30** |
| Média+ | 720p | 2.5M | fast | 30 |
| Alta | 1080p | 4M | fast | 30 |
| Alta 60fps | 1080p | 6M | fast | 60 |
| Ultra | 1080p | 8M | medium | 60 |
| QHD 2K | 1440p | 12M | medium | 60 |
| 4K | 2160p | 20M | slow | 60 |
| 4K 60fps | 2160p | 40M | slow | 60 |
| 4K Ultra | 2160p | 80M | veryslow | 60 |

---

### 🟣 Path Defaults — Comportamento Padrão dos Paths

Controla o comportamento padrão de **todos os paths** configurados no servidor:

| Campo | Padrão | Descrição |
| :-- | :-- | :-- |
| `sourceOnDemand` | ❌ | Puxa a source somente quando há pelo menos 1 leitor |
| `overridePublisher` | ✅ | Permite substituir um publisher ativo por outro |
| `useAbsoluteTimestamp` | ❌ | Usa timestamp absoluto nos pacotes RTP |
| `maxReaders` | `0` (ilimitado) | Número máximo de leitores simultâneos por path |

#### Hooks — Comandos por Evento

Cada hook aceita um comando de shell e um checkbox **Restart** (reinicia o comando automaticamente se encerrar):

| Hook | Evento |
| :-- | :-- |
| `runOnInit` | Executado ao iniciar o servidor (sempre) |
| `runOnDemand` | Executado quando o primeiro leitor chega |
| `runOnReady` | Executado quando a source fica disponível |
| `runOnRead` | Executado quando um leitor conecta |

**Exemplo `runOnDemand` com FFmpeg:**
```bash
ffmpeg -i /dev/video0 -c:v libx264 -preset ultrafast -b:v 1500k -g 30 \
       -c:a aac -b:a 128k -f rtsp rtsp://localhost:8554/$MTX_PATH
```

**Exemplo `runOnInit` UDP/MPEG-TS:**
```bash
ffmpeg -i udp://0.0.0.0:8890 -c copy -f rtsp rtsp://localhost:8554/meu_path
```

---

### Botões do Assistente

| Botão | Ação |
| :-- | :-- |
| **Pré-visualizar YAML** | Abre um dialog com o YAML gerado antes de salvar |
| **Gerar + Aplicar** | Salva o `mediamtx.yml` e sincroniza com a aba Config YAML |

---

## Configurações de Entrada e Saída

### Entradas (Publisher → Servidor)

Streams enviados **para** o servidor MediaMTX:

| Protocolo | Endereço padrão | Exemplo |
| :-- | :-- | :-- |
| **RTMP push** | `rtmp://HOST:1935/PATH` | OBS → `rtmp://localhost:1935/live` |
| **RTSP push** | `rtsp://HOST:8554/PATH` | Câmera → `rtsp://localhost:8554/cam1` |
| **SRT push** | `srt://HOST:8890?streamid=publish:PATH&pkt_size=1316` | Encoder SRT |
| **WebRTC WHIP** | `http://HOST:8889/PATH/whip` | OBS / Browser WHIP endpoint |
| **UDP/MPEG-TS** | `udp://0.0.0.0:8890` | `ffmpeg -i arquivo.mp4 -f mpegts udp://HOST:8890` |

### Saídas (Servidor → Player / Leitor)

Streams disponíveis **para leitura** a partir do servidor:

| Protocolo | Endereço padrão | Uso típico |
| :-- | :-- | :-- |
| **RTSP** | `rtsp://HOST:8554/PATH` | VLC, câmeras, ffplay, ffmpeg |
| **RTMP** | `rtmp://HOST:1935/PATH` | OBS (entrada), players RTMP |
| **HLS** | `http://HOST:8888/PATH` | Browser, VLC, players HLS |
| **WebRTC** | `http://HOST:8889/PATH` | Browser nativo (sem plugin) |
| **SRT pull** | `srt://HOST:8890?streamid=request:PATH` | Encoders e players SRT |

### Exemplos práticos

**Câmera IP RTSP → redistribuir em HLS e WebRTC:**
```yaml
paths:
  cam1:
    source: rtsp://192.168.1.100:554/stream
    sourceOnDemand: false
```
Acesse no browser: `http://localhost:8888/cam1` (HLS) ou `http://localhost:8889/cam1` (WebRTC)

**Câmera Android (CAMSTREAMER-BR):**
```yaml
paths:
  android:
    source: rtsp://192.168.1.X:8554/live
    # WebControl: http://192.168.1.X:8080
```

**OBS → RTMP → redistribuir:**
```
OBS → rtmp://localhost:1935/live
Viewers → rtsp://localhost:8554/live
          http://localhost:8888/live  (HLS)
          http://localhost:8889/live  (WebRTC)
```

**Webcam local via FFmpeg on-demand:**
```yaml
paths:
  webcam:
    runOnDemand: >-
      ffmpeg -i /dev/video0
      -c:v libx264 -preset ultrafast -b:v 1500k -g 30
      -c:a aac -b:a 128k
      -f rtsp rtsp://localhost:8554/$MTX_PATH
    runOnDemandRestart: yes
```

**NVR Reolink / Dahua:**
```yaml
paths:
  nvr_ch1:
    source: rtsp://admin:senha@192.168.1.200:554/cam/realmonitor?channel=1&subtype=0
    sourceOnDemand: false
    # Ajuste channel= e subtype= conforme modelo
```

---

## Métricas (Prometheus)

Quando habilitado no Assistente, o endpoint de métricas fica disponível em:
```
http://localhost:9998/metrics
```
A aba **Métricas** do app consome a API REST e exibe streams ativos e leitores em tempo real diretamente na interface.

---

## Menu completo

| Menu | Opções |
| :-- | :-- |
| **Arquivo** | Abrir binário, Abrir config YAML, Sair |
| **Servidor** | Iniciar, Parar, Reiniciar |
| **Ajuda** | Documentação MediaMTX, Repositório GitHub, Sobre |

---

## Estrutura do Projeto

```
MediaMTX-GUI/
├── src/
│   └── com/mediamtx/manager/
│       ├── Main.java                       ← Ponto de entrada
│       ├── AppWindow.java                  ← Janela principal, abas e menus
│       ├── AppIcon.java                    ← Ícone gerado programaticamente
│       ├── theme/
│       │   └── Theme.java                  ← Paleta de cores, fontes e botões
│       ├── service/
│       │   ├── MediaMTXService.java        ← Gerenciamento do processo
│       │   └── YamlPresetService.java      ← Gerador YAML + todos os enums
│       └── panels/
│           ├── HeaderPanel.java            ← Barra superior com controles e status
│           ├── SidebarPanel.java           ← Sidebar de protocolos
│           ├── DashboardPanel.java         ← Visão geral e instruções
│           ├── SourcesPanel.java           ← Tabela de fontes via API REST
│           ├── AddSourceDialog.java        ← Dialog de inserção de fonte (12 tipos)
│           ├── ConfigWizardPanel.java      ← Assistente de configuração visual
│           ├── MetricsPanel.java           ← Métricas em tempo real
│           ├── ConfigPanel.java            ← Editor YAML bruto
│           ├── RecordPanel.java            ← Config de gravação
│           ├── PathsPanel.java             ← Tabela de paths
│           └── LogPanel.java              ← Console em tempo real
├── MANIFEST.MF
├── build.sh
├── build.bat
├── .gitignore
└── README.md
```

---

## Licença

MIT License — veja [LICENSE](LICENSE)

---

## Créditos

Desenvolvido por **Luan Silva**

- GitHub: [github.com/luanscps](https://github.com/luanscps)
- Repositório: [github.com/luanscps/MediaMTX-GUI](https://github.com/luanscps/MediaMTX-GUI)

Baseado no servidor open-source [MediaMTX](https://github.com/bluenviron/mediamtx) por [bluenviron](https://github.com/bluenviron).

---

<p align="center">Feito com ❤️ por <a href="https://github.com/luanscps"><b>Luan Silva</b></a></p>
