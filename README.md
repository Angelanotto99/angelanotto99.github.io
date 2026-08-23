# 🛡️ Sentinel — AI Security Scanner

**🌐 Live website:** https://saurabh-gzp.github.io/Sentinel-AI-Security-Scanner/

An on-device, AI security app for Android. Scan any **app (APK / APKS / XAPK)** or **website link** and get an instant, definitive verdict in simple Hinglish — safe, or scam/malware/phishing.

---

## 🔗 Links

- **Website:** https://saurabh-gzp.github.io/Sentinel-AI-Security-Scanner/
- **Download APK (v1.0.0):** https://github.com/Saurabh-gzp/Sentinel-AI-Security-Scanner/releases/download/v1.0.0/Sentinel-v1.0.0.apk
- **Source code:** [`Sentinel-source.zip`](Sentinel-source.zip) (in this repo)

---

## ✨ Highlights

- **App Scan** — decompiles the package (binary manifest decoder + a hand-written DEX bytecode disassembler), reads real code call-sites, and checks for hidden data-theft, backdoors, and malicious behaviour. Permission-presence alone is never flagged.
- **Web Scan** — renders the page in a real Chromium/WebView, follows the full redirect chain (catches `intent://` phishing), runs a deterministic rule engine (brand DB, scam templates, malware-host detection) + Gemini Vision.
- **Agentic** — the App scanner uses a Gemini **function-calling loop** (`list_package → analyze_apk → finish_report`).
- **Privacy-first** — on-device, temp files auto-deleted, **no API key bundled** (bring your own Gemini key).
- Polished UI, auto dark/light, abuse-language guard.

---

## 🔧 Source code

The full Android source is bundled as **[`Sentinel-source.zip`](Sentinel-source.zip)** (Java + resources + `AndroidManifest.xml`). Download and unzip it, then build with your preferred Android toolchain. Target Android 7.0+ (API 24).

## 🚀 Website

This repository also hosts the product website in the **`web/`** folder via GitHub Pages.

---

## ⚠️ Disclaimer

Educational / personal-use security assistant. Uses static analysis (does not execute scanned code). Use your own judgement; scan only apps/links you are authorised to analyse.

## 📄 License

MIT — see [LICENSE](LICENSE).
