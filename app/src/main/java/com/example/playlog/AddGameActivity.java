package com.example.playlog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddGameActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextPlatform;
    private Spinner spinnerStatus;
    private GameDatabaseHelper dbHelper;
    private String oldTitle = null; // Usado para verificar se é edição

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        dbHelper = new GameDatabaseHelper(this);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextPlatform = findViewById(R.id.editTextPlatform);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        // Verificar se os dados foram passados para a edição
        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
            // Carregar os dados do jogo para edição
            oldTitle = intent.getStringExtra("title");
            String platform = intent.getStringExtra("platform");
            String status = intent.getStringExtra("status");

            // Preencher os campos de edição
            editTextTitle.setText(oldTitle);
            editTextPlatform.setText(platform);
            int statusIndex = getStatusIndex(status);
            spinnerStatus.setSelection(statusIndex);
        }

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(view -> {
            String newTitle = editTextTitle.getText().toString();
            String newPlatform = editTextPlatform.getText().toString();
            String newStatus = spinnerStatus.getSelectedItem().toString();

            if (!newTitle.isEmpty() && !newPlatform.isEmpty()) {
                if (oldTitle != null) {
                    // Atualizar jogo existente
                    dbHelper.updateGame(oldTitle, newPlatform, newStatus);
                } else {
                    // Adicionar um novo jogo
                    dbHelper.addGame(newTitle, newPlatform, newStatus);
                }
                finish(); // Voltar para a tela principal
            } else {
                Toast.makeText(AddGameActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para pegar o índice do status no Spinner
    private int getStatusIndex(String status) {
        switch (status) {
            case "Zerei":
                return 0;
            case "Jogando":
                return 1;
            case "Não joguei":
                return 2;
            case "Comprar":
                return 3;
            default:
                return 0;
        }
    }
}

