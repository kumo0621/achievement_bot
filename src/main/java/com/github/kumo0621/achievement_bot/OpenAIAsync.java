package com.github.kumo0621.ainana;

import org.bukkit.Bukkit;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class OpenAIAsync {
    static boolean run = false;

    public static CompletableFuture<String> openaiAsync(String text, String name, int set) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return openai(text, name, set);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    private static String ai(String text) throws Exception {
        String hostname = Ainana.config.getString("0");
        //String hostname = "localhost";// Pythonサーバーのホスト名
        int port = Ainana.config.getInt("1");
        ; // Pythonサーバーのポート番号

        Socket socket = new Socket(hostname, port);

        // データの送信
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);
        writer.println(text);

        // データの受信
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        byte[] response = reader.readLine().getBytes();
        String result = new String(response, java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("Pythonからの応答：" + result);
        // ソケットのクローズ
        input.close();
        output.close();
        socket.close();
        return result;
    }

    static String openai(String text, String name, int set) throws Exception {
        run = true;
        if (set == 0) {
            String result = ai(text);

            Bukkit.broadcastMessage("<NANA_0321> " + result);
            run = false;
            //Bukkit.broadcastMessage("<" + name + "> " + text);
            //Bukkit.broadcastMessage("<NANA_0321> " + spawn);
        } else if (set == 1) {
            String result = ai(text);

            Bukkit.broadcastMessage("<Nagatuki> " + result);
            run = false;
            //Bukkit.broadcastMessage("<" + name + "> " + text);
            //Bukkit.broadcastMessage("<NANA_0321> " + spawn);
        } else if (set == 2) {
            String result = ai(text);

            Bukkit.broadcastMessage("<kono_a> " + result);
            run = false;
            //Bukkit.broadcastMessage("<" + name + "> " + text);
            //Bukkit.broadcastMessage("<NANA_0321> " + spawn);
        }
        return null;
    }
}
