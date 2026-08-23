# 🛡️ Sentinel — AI Security Scanner

**🌐 Live website:** https://saurabh-gzp.github.io/Sentinel-AI-Security-Scanner/

An on-device, AI security app for Android. Scan any **app (APK / APKS / XAPK)** or **website link** and get an instant, definitive verdict in simple Hinglish — safe, or scam/malware/phishing.

---

## 📁 Repository structure

```
.
├── app/                        # All app versions (APK + source)
│   └── v1.0.0/
│       ├── Sentinel-v1.0.0.apk
│       └── Sentinel-app-source-v1.0.0.zip
├── web/                        # Website (GitHub Pages)
│   ├── index.html
│   ├── img/
│   └── favicon.svg
├── .github/workflows/          # CI: deploy website to Pages
├── README.md
└── LICENSE
```

- **APK:** `app/v1.0.0/Sentinel-v1.0.0.apk` — install on Android 7.0+ (API 24).
- **Source code:** `app/v1.0.0/Sentinel-app-source-v1.0.0.zip` — app source only (src/, res/, AndroidManifest.xml).

Each version gets its own subfolder under `app/`. Download, unzip, and build with your preferred Android toolchain.

---

## ✨ Highlights

- **App Scan** — decompiles the package (binary manifest decoder + a hand-written DEX bytecode disassembler), reads real code call-sites, checks for hidden data-theft / backdoors / malicious behaviour. Permission-presence alone is never flagged.
- **Web Scan** — renders the page in a real Chromium/WebView, follows the full redirect chain (catches `intent://` phishing), runs a deterministic rule engine (brand DB, scam templates, malware-host detection) + Gemini Vision.
- **Agentic** — the App scanner uses a Gemini **function-calling loop** (`list_package → analyze_apk → finish_report`).
- **Privacy-first** — on-device, temp files auto-deleted, **no API key bundled** (bring your own Gemini key).
- Polished UI, auto dark/light, abuse-language guard.

---

## ⚠️ Disclaimer

Educational / personal-use security assistant. Uses static analysis (does not execute scanned code). Use your own judgement; scan only apps/links you are authorised to analyse.

## 📄 License

MIT — see [LICENSE](LICENSE).
