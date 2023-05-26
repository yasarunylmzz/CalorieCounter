package com.example.caloriecounter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class calorieActivity extends AppCompatActivity {

    public int totalFoodValue = 0;

    Button button,button2,button3;
    TextView textView,textView3;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calori);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        updateProgressIndicator();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        return true;
                    case R.id.bottom_bmi:
                        startActivity(new Intent(getApplicationContext(), BmiActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        return true;
                }
                return false;
            }
        });

        textView = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView);

        button2 = findViewById(R.id.breakfast);
        button3 = findViewById(R.id.lunch);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showPopup2();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup3();
            }
        });

        button = findViewById(R.id.dinner);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
    }
    private void showPopup() {
        // PopupWindow oluştur
        PopupWindow popupWindow = new PopupWindow(this);

        // Popup içeriğini inflate et
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);

        // Popup özelliklerini ayarla
        popupWindow.setContentView(popupView);
        popupWindow.setWidth(500); // Kare boyutunu belirleyin
        popupWindow.setHeight(300);
        popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        // Arka planı saydam gri olarak ayarla
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#80000000")); // Griye yakın saydam bir renk
        popupWindow.setBackgroundDrawable(colorDrawable);

        // Popup penceresini ortala
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        EditText editTextFoodName = popupView.findViewById(R.id.EditText1);
        EditText editTextFoodValue = popupView.findViewById(R.id.EditText2);
        Button buttonAdd = popupView.findViewById(R.id.saveButton);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = editTextFoodName.getText().toString();
                int foodValue = Integer.parseInt(editTextFoodValue.getText().toString());

                // Kahvaltı için Firestore'da yeni bir belge oluşturun ve besin detaylarını kaydedin
                Map<String, Object> dinner = new HashMap<>();
                dinner.put("besinAdi", foodName);
                dinner.put("KaloriDegeri", foodValue);

                String currentUser = mAuth.getCurrentUser().getUid();
                db.collection("kullanicilar").document(currentUser)
                        .collection("Aksamyemegi").add(dinner)
                        .addOnSuccessListener(documentReference -> {
                            // Başarılı, gerekirse bir şey yapabilirsiniz
                            Toast.makeText(calorieActivity.this,"ekleme başarılı",Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Hata işleme, gerekirse bir şey yapabilirsiniz
                            Toast.makeText(calorieActivity.this,"ekleme hatalı" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        });
                // Besin adı ve değerini al



                // Besin değerini toplam değere ekle
                totalFoodValue += foodValue;
                textView3.setText(String.valueOf(totalFoodValue));

                // İlerleme çubuğunu güncelle
                CircularProgressIndicator progressBar = findViewById(R.id.circularProgressIndicator);
                int maxValue = 100; // Maksimum değeri burada belirleyin
                int progress = (int) (totalFoodValue / maxValue);
                progressBar.setProgress(progress);
                textView.setText(progress + "%");


                // Popup penceresini kapat
                popupWindow.dismiss();
            }
        });
    }

    private void showPopup2() {
        // PopupWindow oluştur
        PopupWindow popupWindow = new PopupWindow(this);

        // Popup içeriğini inflate et
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);

        // Popup özelliklerini ayarla
        popupWindow.setContentView(popupView);
        popupWindow.setWidth(500); // Kare boyutunu belirleyin
        popupWindow.setHeight(300);
        popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        // Arka planı saydam gri olarak ayarla
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#80000000")); // Griye yakın saydam bir renk
        popupWindow.setBackgroundDrawable(colorDrawable);

        // Popup penceresini ortala
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        EditText editTextFoodName = popupView.findViewById(R.id.EditText1);
        EditText editTextFoodValue = popupView.findViewById(R.id.EditText2);
        Button buttonAdd = popupView.findViewById(R.id.saveButton);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodName = editTextFoodName.getText().toString();
                int foodValue = Integer.parseInt(editTextFoodValue.getText().toString());

                // Kahvaltı için Firestore'da yeni bir belge oluşturun ve besin detaylarını kaydedin
                Map<String, Object> breakfast = new HashMap<>();
                breakfast.put("besinAdi", foodName);
                breakfast.put("KaloriDegeri", foodValue);

                String currentUser = mAuth.getCurrentUser().getUid();
                db.collection("kullanicilar").document(currentUser)
                        .collection("kahvalti").add(breakfast)
                        .addOnSuccessListener(documentReference -> {
                            // Başarılı, gerekirse bir şey yapabilirsiniz
                            Toast.makeText(calorieActivity.this,"ekleme başarılı",Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Hata işleme, gerekirse bir şey yapabilirsiniz
                            Toast.makeText(calorieActivity.this,"ekleme hatalı" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        });
                // Besin adı ve değerini al


                // Besin değerini toplam değere ekle
                totalFoodValue += foodValue;
                textView3.setText(String.valueOf(totalFoodValue));

                // İlerleme çubuğunu güncelle
                CircularProgressIndicator progressBar = findViewById(R.id.circularProgressIndicator);
                int maxValue = 100; // Maksimum değeri burada belirleyin
                int progress = (int) (totalFoodValue / maxValue);
                progressBar.setProgress(progress);
                textView.setText(progress + "%");


                // Popup penceresini kapat
                popupWindow.dismiss();
            }
        });

    }
    private void showPopup3() {
        // PopupWindow oluştur
        PopupWindow popupWindow = new PopupWindow(this);

        // Popup içeriğini inflate et
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);

        // Popup özelliklerini ayarla
        popupWindow.setContentView(popupView);
        popupWindow.setWidth(500); // Kare boyutunu belirleyin
        popupWindow.setHeight(300);
        popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        // Arka planı saydam gri olarak ayarla
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#80000000")); // Griye yakın saydam bir renk
        popupWindow.setBackgroundDrawable(colorDrawable);

        // Popup penceresini ortala
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        EditText editTextFoodName = popupView.findViewById(R.id.EditText1);
        EditText editTextFoodValue = popupView.findViewById(R.id.EditText2);
        Button buttonAdd = popupView.findViewById(R.id.saveButton);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodName = editTextFoodName.getText().toString();
                int foodValue = Integer.parseInt(editTextFoodValue.getText().toString());

                // Kahvaltı için Firestore'da yeni bir belge oluşturun ve besin detaylarını kaydedin
                Map<String, Object> lunch = new HashMap<>();
                lunch.put("besinAdi", foodName);
                lunch.put("KaloriDegeri", foodValue);

                String currentUser = mAuth.getCurrentUser().getUid();
                db.collection("kullanicilar").document(currentUser)
                        .collection("Ogle Yemegi").add(lunch)
                        .addOnSuccessListener(documentReference -> {
                            // Başarılı, gerekirse bir şey yapabilirsiniz
                            Toast.makeText(calorieActivity.this,"ekleme başarılı",Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Hata işleme, gerekirse bir şey yapabilirsiniz
                            Toast.makeText(calorieActivity.this,"ekleme hatalı" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        });
                // Besin adı ve değerini al



                // Besin değerini toplam değere ekle
                totalFoodValue += foodValue;
                textView3.setText(String.valueOf(totalFoodValue));

                // İlerleme çubuğunu güncelle
                CircularProgressIndicator progressBar = findViewById(R.id.circularProgressIndicator);
                int maxValue = 100; // Maksimum değeri burada belirleyin
                int progress = (int) (totalFoodValue / maxValue);
                progressBar.setProgress(progress);
                textView.setText(progress + "%");


                // Popup penceresini kapat
                popupWindow.dismiss();
            }
        });
    }
    private void updateProgressIndicator() {
        String currentUser = mAuth.getCurrentUser().getUid();

        db.collection("kullanicilar").document(currentUser)
                .collection("kahvalti")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    AtomicInteger totalFoodValue = new AtomicInteger();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        int foodValue = documentSnapshot.getLong("KaloriDegeri").intValue();
                        totalFoodValue.addAndGet(foodValue);
                    }

                    db.collection("kullanicilar").document(currentUser)
                            .collection("Ogle Yemegi")
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots1) {
                                    int foodValue = documentSnapshot.getLong("KaloriDegeri").intValue();
                                    totalFoodValue.addAndGet(foodValue);
                                }

                                db.collection("kullanicilar").document(currentUser)
                                        .collection("Aksamyemegi")
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots2 -> {
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots2) {
                                                int foodValue = documentSnapshot.getLong("KaloriDegeri").intValue();
                                                totalFoodValue.addAndGet(foodValue);
                                            }
                                            textView3.setText(String.valueOf(totalFoodValue.get()) + " kcal");
                                            CircularProgressIndicator progressBar = findViewById(R.id.circularProgressIndicator);
                                            int maxValue = 100; // Maksimum değeri burada belirleyin

                                            int progress = (int) ((totalFoodValue.get()) / maxValue);
                                            progressBar.setProgress(progress);
                                            textView.setText(progress + "%");

                                            // Kalori değerini textView3'e yazdır

                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(calorieActivity.this,"HATA AKSAM"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(calorieActivity.this,"HATA OGLE"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(calorieActivity.this,"HATA SABAH"+e.getMessage(),Toast.LENGTH_SHORT).show();
                });
    }







}
