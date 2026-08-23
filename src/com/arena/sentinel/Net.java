package com.arena.sentinel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class Net {

    static String get(String urlStr) throws Exception {
        HttpURLConnection c = open(urlStr);
        try {
            c.setRequestMethod("GET");
            c.setConnectTimeout(25000);
            c.setReadTimeout(60000);
            return read(c);
        } finally {
            c.disconnect();
        }
    }

    static String post(String urlStr, String body) throws Exception {
        Exception last = null;
        for (int attempt = 0; attempt < 2; attempt++) {
            HttpURLConnection c = open(urlStr);
            try {
                c.setRequestMethod("POST");
                c.setConnectTimeout(25000);
                c.setReadTimeout(120000);
                c.setDoOutput(true);
                c.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                OutputStream os = c.getOutputStream();
                os.write(body.getBytes("UTF-8"));
                os.close();
                try {
                    return read(c);
                } catch (IOException422 e) {
                    throw e; // hard API error (bad key, bad model, policy)
                } catch (Exception e) {
                    last = e; // transient (503, reset, timeout) -> retry once
                }
            } finally {
                c.disconnect();
            }
        }
        throw last == null ? new Exception("Request failed") : last;
    }

    private static HttpURLConnection open(String urlStr) throws Exception {
        URL u = new URL(urlStr);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.setRequestProperty("User-Agent", "Sentinel/1.0");
        return c;
    }

    private static String read(HttpURLConnection c) throws Exception {
        InputStream in;
        int code = c.getResponseCode();
        if (code >= 200 && code < 300) in = c.getInputStream();
        else in = c.getErrorStream();
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line;
        while ((line = r.readLine()) != null) sb.append(line).append('\n');
        r.close();
        String body = sb.toString();
        if (code >= 400) {
            throw new IOException422(parseApiError(body, code));
        }
        return body;
    }

    static class IOException422 extends Exception {
        IOException422(String m) { super(m); }
    }

    private static String parseApiError(String body, int code) {
        String msg = body;
        // crude extraction of "message" from Gemini error json
        int i = body.indexOf("\"message\"");
        if (i >= 0) {
            int c1 = body.indexOf('"', i + 9);
            int c2 = body.indexOf('"', c1 + 1);
            if (c1 > 0 && c2 > c1) msg = body.substring(c1 + 1, c2);
        }
        return "API " + code + ": " + (msg.length() > 300 ? msg.substring(0, 300) : msg);
    }
}
