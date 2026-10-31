package com.creatures.npclab;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONArray;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Searchable roster foundation; the playable 3D runtime is not integrated yet. */
public final class MainActivity extends Activity {
    private final List<String> roster = new ArrayList<>();
    private LinearLayout items;
    private TextView count;
    private final int bg = Color.rgb(17,17,27), panel = Color.rgb(29,29,45);
    private final int fg = Color.rgb(245,243,255), muted = Color.rgb(174,169,195);

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        readRoster();
        showScreen();
        render("");
    }

    private void readRoster() {
        try (InputStream in = getAssets().open("creature_catalog.json");
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[2048];
            int n;
            while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
            JSONArray array = new JSONArray(out.toString(StandardCharsets.UTF_8.name()));
            for (int i = 0; i < array.length(); i++) roster.add(array.getString(i));
        } catch (Exception e) {
            android.util.Log.e("NpcLab", "Catalog load error", e);
        }
    }

    private void showScreen() {
        ScrollView scroll = new ScrollView(this);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(20), dp(28), dp(20), dp(24));
        page.setBackgroundColor(bg);
        scroll.addView(page);
        setContentView(scroll);

        page.addView(label("SWORDIGO • PROJETO INDEPENDENTE", 11, 0xFFA78BFA, true));
        page.addView(label("NPC & INIMIGOS", 29, fg, true));
        page.addView(label("Base Android • catálogo de criaturas", 14, muted, false));

        TextView info = label("81 templates inventariados. Esta versão é somente a base do aplicativo: ainda não contém o runtime 3D nem personagens controláveis.", 13, fg, false);
        info.setPadding(dp(12), dp(12), dp(12), dp(12));
        info.setBackground(box(panel));
        page.addView(info);

        EditText search = new EditText(this);
        search.setSingleLine(true);
        search.setHint("Buscar NPC ou criatura");
        search.setTextColor(fg);
        search.setHintTextColor(muted);
        search.setTextSize(14);
        search.setBackground(box(panel));
        search.setPadding(dp(12), 0, dp(12), 0);
        page.addView(search, new LinearLayout.LayoutParams(-1, dp(50)));

        count = label("", 12, muted, false);
        page.addView(count);
        items = new LinearLayout(this);
        items.setOrientation(LinearLayout.VERTICAL);
        page.addView(items);

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int c, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int c) { render(s.toString()); }
            @Override public void afterTextChanged(Editable e) {}
        });
        page.addView(label("Pacote separado: com.creatures.npclab • v0.1.0", 10, muted, false));
    }

    private void render(String query) {
        items.removeAllViews();
        String q = query.trim().toLowerCase(Locale.ROOT);
        int visible = 0;
        for (String id : roster) {
            if (!id.toLowerCase(Locale.ROOT).contains(q)) continue;
            TextView row = label(id.replace('_', ' '), 15, fg, true);
            row.setPadding(dp(12), dp(14), dp(12), dp(14));
            row.setBackground(box(panel));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            params.bottomMargin = dp(7);
            row.setLayoutParams(params);
            row.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(id.replace('_', ' '))
                .setMessage("Template incluído no inventário. Ainda não convertido em personagem jogável nesta versão-base.")
                .setPositiveButton("Fechar", null).show());
            items.addView(row);
            visible++;
        }
        count.setText(visible + " de " + roster.size() + " templates");
    }

    private TextView label(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private GradientDrawable box(int color) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(12));
        return d;
    }

    private int dp(int n) {
        return Math.round(n * getResources().getDisplayMetrics().density);
    }
}