package com.example.caloriecounter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caloriecounter.R;
import com.example.caloriecounter.calorieActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BmiActivity extends AppCompatActivity implements View.OnClickListener {
    EditText yas, kg, cm;
    TextView sonuc;
    ImageView male, female;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_bmi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_bmi:
                        return true;
                    case R.id.bottom_home:
                        startActivity(new Intent(getApplicationContext(), calorieActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return true;
                }
                return false;
            }
        });

        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        sonuc = findViewById(R.id.sonuc);
        yas = findViewById(R.id.yas);
        kg = findViewById(R.id.kg);
        cm = findViewById(R.id.cm);

        female.setOnClickListener(this);
        male.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == female) {
            calculateBMI("female");
        } else if (v == male) {
            calculateBMI("male");
        }
    }

    private void calculateBMI(String gender) {
        // Giriş verilerini al
        String ageStr = yas.getText().toString().trim();
        String weightStr = kg.getText().toString().trim();
        String heightStr = cm.getText().toString().trim();

        // Giriş verilerinin doğruluğunu kontrol et
        if (TextUtils.isEmpty(ageStr)) {
            yas.setError("Yaş girilmesi gerekiyor");
            yas.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(weightStr)) {
            kg.setError("Kilo girilmesi gerekiyor");
            kg.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(heightStr)) {
            cm.setError("Boy girilmesi gerekiyor");
            cm.requestFocus();
            return;
        }

        // BMI hesapla
        int age = Integer.parseInt(ageStr);
        double weight = Double.parseDouble(weightStr);
        double height = Double.parseDouble(heightStr) / 100; // CM'yi M'ye dönüştür

        double bmi = weight / (height * height);

        // BMI sonucunu formatla ve ekrana yazdır
        String status = getBmiStatus(gender, bmi);
        String result = String.format("Yaş: %d\nBMI: %.2f\nDurum: %s", age, bmi, status);
        sonuc.setText(result);
    }

    private String getBmiStatus(String gender, double bmi) {
        if (gender.equals("female")) {
            if (bmi < 18.5) {
                return "Zayıf";
            } else if (bmi < 24.9) {
                return "Normal";
            } else if (bmi < 29.9) {
                return "Fazla Kilolu";
            } else {
                return "Obez";
            }
        } else if (gender.equals("male")) {
            if (bmi < 20.7) {
                return "Zayıf";
            } else if (bmi < 26.4) {
                return "Normal";
            } else if (bmi < 31.1) {
                return "Fazla Kilolu";
            } else {
                return "Obez";
            }
        }

        return "";
    }
}
