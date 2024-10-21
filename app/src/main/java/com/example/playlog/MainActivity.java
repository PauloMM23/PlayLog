package com.example.playlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GameDatabaseHelper dbHelper;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new GameDatabaseHelper(this);

        listView = findViewById(R.id.listView);
        gameList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameList);
        listView.setAdapter(adapter);

        // Carregar os jogos do banco de dados
        loadGames();

        // Botão para adicionar novos jogos
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddGameActivity.class);
            startActivity(intent);
        });

        // Clique simples para editar um jogo
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedGame = gameList.get(position);

            // Separar as informações do jogo
            String[] gameDetails = selectedGame.split(" - ");
            String title = gameDetails[0];
            String platform = gameDetails[1].substring(0, gameDetails[1].indexOf(" ("));
            String status = gameDetails[1].substring(gameDetails[1].indexOf("(") + 1, gameDetails[1].indexOf(")"));

            // Enviar os dados do jogo para a AddGameActivity
            Intent intent = new Intent(MainActivity.this, AddGameActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("platform", platform);
            intent.putExtra("status", status);
            startActivity(intent);
        });

        // Clique longo para deletar um jogo
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedGame = gameList.get(position);
                String title = selectedGame.split(" - ")[0];

                dbHelper.deleteGame(title);
                loadGames();  // Atualizar a lista após deletar
                return true;
            }
        });
    }

    private void loadGames() {
        Cursor cursor = dbHelper.getAllGames();
        gameList.clear();

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String platform = cursor.getString(cursor.getColumnIndex("platform"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                gameList.add(title + " - " + platform + " (" + status + ")");
            } while (cursor.moveToNext());
        }

        adapter.notifyDataSetChanged();
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGames();
    }
}

