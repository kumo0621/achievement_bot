package com.github.kumo0621.achievement_bot;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Achievement_bot extends JavaPlugin implements org.bukkit.event.Listener {
    private DatabaseReference database;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
// Firebaseの初期化
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(getClass().getResourceAsStream("/your-firebase-service-account-key.json")))
                    .setDatabaseUrl("https://your-firebase-database-url.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
            database = FirebaseDatabase.getInstance().getReference();
        } catch (Exception e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // データの読み込み
        loadDataFromFirebase();
    }

    @Override
    public void onDisable() {
// Firebaseの後処理
        FirebaseApp.getInstance().delete();
    }


    private void loadDataFromFirebase() {
        // 例として"players"ノードのデータを読み込む
        DatabaseReference playersRef = database.child("players");
        playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // データを取得し、処理を行う
                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    String playerName = playerSnapshot.getKey();
                    Object playerData = playerSnapshot.getValue();
                    // ここでデータを処理する
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 読み込みエラーの場合の処理
                databaseError.toException().printStackTrace();
            }
        });
    }
    private void saveDataToFirebase(Player player, String data) {
        DatabaseReference playersRef = database.child("players");
        playersRef.child(player.getUniqueId().toString()).child("称号").setValueAsync(data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    // エラーが発生した場合の処理
                    databaseError.toException().printStackTrace();
                } else {
                    // 書き込みが成功した場合の処理
                    player.sendMessage("データをデータベースに保存しました！");
                }
            }
        });
    }

    @EventHandler
    public void Achievement(PlayerAdvancementDoneEvent e) {
        String player = e.getPlayer().getName();
        Player player1 = e.getPlayer();
        String advancementName = e.getAdvancement().getKey().getKey();
        String targetAdvancement = "door";
        if (advancementName.equals(targetAdvancement)) {
            Bukkit.broadcastMessage(ChatColor.GREEN + player + " が " + advancementName + " 実績を達成しました！");
            saveDataToFirebase(player1, "doorの称号を獲得しました！");
        }
    }

}