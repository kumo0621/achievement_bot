package com.github.kumo0621.achievement_bot;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class OpenAIAsync {
    static String hostname = "localhost";  // Pythonスクリプトが動作するホストのIPアドレスまたはホスト名
    static int port = 38244;

    public static CompletableFuture<Void> sendTextAsync(String text) {
        return CompletableFuture.runAsync(() -> {
            try {
                sendText(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void sendText(String text) throws Exception {
        Socket socket = new Socket(hostname, port);

        // データの送信
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);
        writer.println(text);

        // ソケットをクローズ
        socket.close();
    }
}
