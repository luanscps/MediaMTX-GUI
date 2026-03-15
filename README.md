# MediaMTX GUI

<p align="center">
  <b>Interface gráfica profissional em Java para gerenciar o servidor de mídia <a href="https://github.com/bluenviron/mediamtx">MediaMTX</a></b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-orange?style=flat-square"/>
  <img src="https://img.shields.io/badge/MediaMTX-compatible-blue?style=flat-square"/>
  <img src="https://img.shields.io/badge/Platform-Linux%20%7C%20Windows%20%7C%20macOS-lightgrey?style=flat-square"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=flat-square"/>
</p>

---

## O que é o MediaMTX GUI?

O **MediaMTX GUI** é uma interface gráfica desktop desenvolvida em **Java (Swing)** que permite gerenciar o servidor de mídia [MediaMTX](https://github.com/bluenviron/mediamtx) de forma visual, sem precisar usar a linha de comando para operações do dia a dia.

O MediaMTX suporta **RTSP, RTMP, HLS, WebRTC, SRT** e muito mais. Este GUI encapsula o processo do servidor, oferecendo controle completo via interface amigável.

---

## Funcionalidades

| Funcionalidade | Descrição |
|---|---|
| **Iniciar / Parar / Reiniciar** | Controle do processo MediaMTX com um clique |
| **Dashboard** | Visão geral dos protocolos e portas ativos |
| **Editor de Config YAML** | Edite o `mediamtx.yml` diretamente na interface |
| **Gravação** | Configure gravação automática: diretório, formato, duração |
| **Gerenciamento de Paths** | Visualização dos streams configurados |
| **Log em tempo real** | Console com timestamp e opção de salvar em arquivo |
| **API REST** | Atalho direto para a API do MediaMTX no browser |
| **Tema escuro** | UI moderna com tema dark profissional |

---

## Pré-requisitos

- **Java 17 ou superior** — [Baixar JDK](https://adoptium.net/)
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

### 4. Configure o binário

No menu: **Arquivo → Abrir binário...** → selecione o executável `mediamtx` baixado.

---

## Estrutura do Projeto

```
MediaMTX-GUI/
├── src/
│   └── com/mediamtx/manager/
│       ├── Main.java                  ← Ponto de entrada
│       ├── AppWindow.java             ← Janela principal + menus
│       ├── AppIcon.java               ← Ícone gerado programaticamente
│       ├── theme/
│       │   └── Theme.java             ← Paleta de cores e fontes
│       ├── service/
│       │   └── MediaMTXService.java   ← Gerenciamento do processo
│       └── panels/
│           ├── HeaderPanel.java       ← Barra superior
│           ├── SidebarPanel.java      ← Sidebar com protocolos
│           ├── DashboardPanel.java    ← Visão geral
│           ├── ConfigPanel.java       ← Editor de YAML
│           ├── RecordPanel.java       ← Config de gravação
│           ├── PathsPanel.java        ← Tabela de streams
│           └── LogPanel.java          ← Console em tempo real
├── MANIFEST.MF
├── build.sh
├── build.bat
├── .gitignore
└── README.md
```

---

## Casos de uso

### Câmera Android / dispositivo como câmera
```bash
ffmpeg -i /dev/video0 -f rtsp rtsp://localhost:8554/cam1
```

### Live streaming com OBS
```
rtmp://localhost:1935/live
```

### Visualizar no browser
```
http://localhost:8888/live     # HLS
http://localhost:8889/live     # WebRTC
```

---

## Menu completo

| Menu | Opções |
|---|---|
| **Arquivo** | Abrir binário, Abrir config YML, Sair |
| **Servidor** | Iniciar, Parar, Reiniciar, Abrir API no browser |
| **Visualizar** | Limpar log, Salvar log em arquivo |
| **Ajuda** | Documentação MediaMTX, Repositório, Sobre |

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
