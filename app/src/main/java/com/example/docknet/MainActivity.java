package com.example.docknet;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docknet.data.SystemRepository;
import com.example.docknet.network.ServerStatusManager;
import com.example.docknet.ui.AnimationHelper;
import com.example.docknet.ui.SystemInfoController;
import com.example.docknet.ui.ServerStatusController;
import com.example.docknet.ui.StarsController;
import com.example.docknet.ui.ShipsController;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * Prostsza wersja MainActivity — ta sama funkcjonalność, czytelniejszy kod.
 */
public class MainActivity extends AppCompatActivity {

    // Zależności potrzebne w cyklu życia
    private SystemRepository systemRepository;
    private ServerStatusManager serverStatusManager;

    // Prosty stos nawigacji (top = pierwszy element)
    private enum Screen { MAIN, SYSTEM_INFO, STARS, SHIPS }
    private final Deque<Screen> navStack = new ArrayDeque<>();

    // Dodaj ekran na stos tylko jeśli jest inny niż aktualny
    private void pushScreen(Screen s) {
        if (navStack.isEmpty() || navStack.peek() != s) {
            navStack.push(s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicjalizacja "serwisów" używanych w aplikacji
        com.example.docknet.network.NetworkClient networkClient = new com.example.docknet.network.RetrofitNetworkClient();
        systemRepository = new SystemRepository(networkClient);
        serverStatusManager = new ServerStatusManager(networkClient);

        // Zarejestruj callback dla przycisku "wstecz" (nowe API)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                hideKeyboard();
                if (navStack.size() > 1) {
                    navStack.pop();
                    Screen prev = navStack.peek();
                    if (prev == Screen.MAIN) setupMainView();
                    else if (prev == Screen.SYSTEM_INFO) setupSystemInfo();
                    else if (prev == Screen.STARS) setupStarsList();
                    else if (prev == Screen.SHIPS) setupShipsList();
                    else finish();
                } else {
                    finish();
                }
            }
        });

        // Przywróć nawigację po rotacji, jeśli była zapisana
        if (savedInstanceState != null) {
            ArrayList<String> saved = savedInstanceState.getStringArrayList("navStack");
            if (saved != null && !saved.isEmpty()) {
                navStack.clear();
                // saved[0] = top, więc wczytujemy od końca
                for (int i = saved.size() - 1; i >= 0; i--) {
                    try { navStack.push(Screen.valueOf(saved.get(i))); } catch (Exception ignored) {}
                }
                // Pokaż aktualny (top) ekran
                Screen top = navStack.peek();
                if (top == Screen.SYSTEM_INFO) setupSystemInfo();
                else if (top == Screen.STARS) setupStarsList();
                else if (top == Screen.SHIPS) setupShipsList();
                else setupMainView();
                return;
            }
        }

        // Domyślny ekran
        setupMainView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Zamykamy zasoby
        if (systemRepository != null) systemRepository.shutdown();
        if (serverStatusManager != null) serverStatusManager.shutdown();
        stopAnimations();
    }

    private void stopAnimations() {
        // star_image may exist in current layout — try to stop animation if present
        try {
            android.view.View v = findViewById(R.id.star_image);
            if (v instanceof android.widget.ImageView) {
                com.example.docknet.ui.AnimationHelper.stopImageAnimation((android.widget.ImageView) v);
            }
        } catch (Exception ignored) {}
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!navStack.isEmpty()) {
            ArrayList<String> list = new ArrayList<>();
            for (Screen s : navStack) list.add(s.name());
            outState.putStringArrayList("navStack", list);
        }
    }

    // ----------------- Setup widoków -----------------

    private void setupMainView() {
        stopAnimations();
        setContentView(R.layout.activity_main);

        // użyj lokalnego kontrolera — nie potrzebujemy pola
        ServerStatusController serverStatusController = new ServerStatusController(this, serverStatusManager);
        serverStatusController.setup();

        Button changeToSystemInfo = findViewById(R.id.change_to_system_info);
        changeToSystemInfo.setOnClickListener(v -> setupSystemInfo());

        Button changeToStarsList = findViewById(R.id.change_to_stars_list);
        changeToStarsList.setOnClickListener(v -> setupStarsList());

        Button changeToShipsList = findViewById(R.id.change_to_ships_list);
        changeToShipsList.setOnClickListener(v -> setupShipsList());

        pushScreen(Screen.MAIN);
    }

    private void setupSystemInfo() {
        stopAnimations();
        setContentView(R.layout.system_info);
        AnimationHelper.setupImageAnimation(findViewById(R.id.star_image));

        SystemInfoController systemInfoController = new SystemInfoController(this, systemRepository);
        systemInfoController.setup();

        setupReturn();
        pushScreen(Screen.SYSTEM_INFO);
    }

    private void setupStarsList() {
        stopAnimations();
        setContentView(R.layout.stars_list);
        StarsController starsController = new StarsController(this);
        starsController.setup();

        setupReturn();
        pushScreen(Screen.STARS);
    }

    private void setupShipsList() {
        stopAnimations();
        setContentView(R.layout.ships_list);
        ShipsController controller = new ShipsController(this);
        controller.setup();

        setupReturn();
        pushScreen(Screen.SHIPS);
    }

    // Kliknięcie tytułu powoduje powrót (prosty mechanizm)
    private void setupReturn() {
        TextView title = findViewById(R.id.title_text);
        if (title != null) title.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    // Schowaj klawiaturę
    private void hideKeyboard() {
        View v = getCurrentFocus();
        if (v == null) v = findViewById(android.R.id.content);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && v != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            v.clearFocus();
        }
    }
}
