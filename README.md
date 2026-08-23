# 🛡️ Sentinel — AI Security Scanner

**An on-device, agentic security app that decompiles Android packages (APK / APKS / XAPK) and live-renders websites, then lets a Gemini-powered analyst agent deliver a definitive verdict — in plain Hinglish.**

> Built without Android Studio, without Gradle, without an emulator — using a pure CLI toolchain (`aapt2 + javac + d8 + zipalign + apksigner`) on a 2 GB sandbox.

![banner](docs/hero.png)

---

## ✨ What it does

Two scanners, both powered by a **deterministic rule engine** *and* an **agentic Gemini AI**.

### 📱 App Scan
Pick any **`.apk`, `.apks` (App Bundle) or `.xapk` (APK + OBB)**. Sentinel:
- **Opens the container** itself — extracts the code-bearing base APK; OBB (large expansion data) is *sized but not binary-scanned*.
- **Decompiles it**: decodes binary `AndroidManifest.xml` to readable XML (apktool-style) **and** walks the actual Dalvik (DEX) bytecode to extract **real call-sites** — which sensitive APIs are called, from which method, with what data.
- Builds a **permission-vs-actual-use matrix**.
- An **agentic loop**: Gemini calls `list_package → analyze_apk → finish_report` and *decides itself* what to scan.

> A permission alone is **never** flagged. A messaging app using SMS = normal; a flashlight secretly sending SMS + IMEI to a server = malware.

### 🌐 Web Scan
Enter **any** URL (bare domains, paths, `mailto:`/`tel:`/`intent:` all accepted). Sentinel:
- **Renders the page in real Chromium/WebView** (JS executed), capturing every resource, SSL/cert state, the **full redirect chain** (including sneaky `intent://`/`package=` mobile-phishing redirects), console, and a **screenshot for Gemini Vision**.
- Runs a **deterministic engine**: 28+ brand-impersonation DB, free-reward/topup scam templates, malware-host detection, credential-form + SSL analysis.
- Gives a **definitive** verdict — never "ho sakta hai".

---

## 🤖 Why "agentic"?

The App scanner is a real **function-calling agent loop** (Gemini `tools` + `functionCall`/`functionResponse`):

| Tool | Purpose |
|---|---|
| `list_package` | Container structure (apk/apks/xapk, base, splits, OBB) |
| `analyze_apk` | Pull decompiled analysis for an inner APK |
| `finish_report` | Commit the final verdict |

The model **drives the investigation** and only concludes via `finish_report`. A deterministic single-shot scan is the automatic fallback — it never gets stuck.

---

## 🎯 Key features

- **3 package formats**: APK, APKS (bundle splits), XAPK (APK + OBB).
- **Real decompilation**: binary-XML manifest decoder + a hand-written **DEX bytecode disassembler** (call-sites + decompiled method bodies) — pure Java, no apktool/dex2jar bundled.
- **Real browser rendering** + **Gemini Vision** for the web.
- **Deterministic engine → exact verdicts** (no hedging).
- **Evidence-based**: permission-presence alone is not malice.
- **Claude-inspired UI** (warm cream + coral, serif headlines) with **auto dark/light**.
- **Practical advice** — even a "Safe" site warns about real usage risks (e.g. third-party Instagram tools can violate ToS / risk account ban).
- **Privacy-first**: temp files auto-deleted after every scan; **no API key bundled** — bring your own Gemini key.
- **Respectful by design**: a hard abusive-language filter + tone guardrails.

---

## 🔑 Setup

1. Get a free **Google Gemini API key**: https://aistudio.google.com/app/apikey
2. Install the app.
3. **Settings → paste your Gemini API key → Save** (verified once).
4. (Optional) **Load live models** to pick a model (defaults to `gemini-2.5-flash`).

Your key stays on your device and is verified **once**, not on every scan.

---

## 🔨 Build from source (CLI — no Android Studio)

Requires JDK 21 + Android SDK (`platform-tools`, `platforms;android-36`, `build-tools;36.0.0`).

```bash
aapt2 link -o linked.apk -I android.jar --manifest AndroidManifest.xml --java gen $(find res -type f)
javac --release 11 -cp android.jar -d classes $(find src gen -name '*.java')
d8 --min-api 24 --lib android.jar --output dex $(find classes -name '*.class')
zipalign -f -p 4 dex/... aligned.apk
apksigner sign --ks debug.keystore aligned.apk
```

---

## 📁 Project layout

```
AndroidManifest.xml
src/com/arena/sentinel/
  MainActivity.java        tabs, internet + API gates, orchestration
  ReportActivity.java      verdict report UI
  AppAgent.java            🤖 function-calling agent loop
  ApkAnalyzer.java         manifest + DEX analysis
  DexFlow.java             ✍️ DEX bytecode disassembler
  AxmlDecoder.java         ✍️ binary-XML manifest decoder
  PackageOpener.java       apk/apks/xapk container support
  WebProbe.java            redirect-chain probe
  WebEngine.java           deterministic web rule engine
  WebRender.java           Chromium/WebView renderer
  GeminiClient.java        generate / vision / function-calling
  ScanReport.java          report model + prompts
  Util.java                helpers + abuse-language guard
res/                       Claude-inspired UI
```

---

## 🧪 Validated

- **Web engine**: 21/21 diverse URLs classified correctly (legit tools like Upfluence → Safe; real URLhaus malware + free-fire scams → Malicious; official logins → Safe).
- **DEX disassembler**: tested on a real 12 MB third-party APK (9,793 classes, 43,811 invoked methods) without crashing.

---

## ⚠️ Disclaimer

Educational / research / personal-use security assistant. It uses **static analysis** (it does not execute scanned code). Heuristics and AI can be wrong — use judgement. Scan only apps you own or are authorised to analyse.

## 📄 License

MIT — see [LICENSE](LICENSE).

<sub>Built on Arena.ai Agent Mode • Powered by Google Gemini</sub>
