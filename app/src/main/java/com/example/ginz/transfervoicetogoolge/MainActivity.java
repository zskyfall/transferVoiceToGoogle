package com.example.ginz.transfervoicetogoolge;

import android.content.Intent;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int SPEAK_REQUEST_CODE = 69;
    private TextToSpeech TTS;
    private Button btnSpeak;
    private TextView txtContent;
    private Spinner spinnerLanguage;
    private String languageCode = "vi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
    }

    private void Init() {
        btnSpeak = findViewById(R.id.button_speak);
        txtContent = findViewById(R.id.text_content);
        spinnerLanguage = findViewById(R.id.spinner_language);

        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    int language = TTS.setLanguage(new Locale("vi"));

                    if(language == TextToSpeech.LANG_MISSING_DATA
                            || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "Language doesn't supported!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {

                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Khong the khoi tao!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ArrayList<Language> listCountry = new ArrayList<>();
        listCountry.add(new Language(R.drawable.vi, "Vietnamese", "vi"));
        listCountry.add(new Language(R.drawable.en, "English", "en"));
        listCountry.add(new Language(R.drawable.ja, "Japanese", "ja"));
        listCountry.add(new Language(R.drawable.ko, "Korean", "ko"));
        listCountry.add(new Language(R.drawable.de, "Germany", "de"));
        listCountry.add(new Language(R.drawable.fr, "French", "fr"));
        listCountry.add(new Language(R.drawable.zhcn, "Chinese", "zh-CN"));
        listCountry.add(new Language(R.drawable.th, "Thai", "th"));
        listCountry.add(new Language(R.drawable.lo, "Laos", "lo"));
        listCountry.add(new Language(R.drawable.ar, "Arab", "ar"));
        listCountry.add(new Language(R.drawable.hi, "India", "hi"));
        listCountry.add(new Language(R.drawable.id, "Indonesia", "id"));
        listCountry.add(new Language(R.drawable.it, "Italia", "it"));
        listCountry.add(new Language(R.drawable.ru, "Russian", "ru"));
        listCountry.add(new Language(R.drawable.pt, "Portugal", "pt"));

        LanguageAdapter adapter = new LanguageAdapter(this, R.layout.row_country, listCountry);
        spinnerLanguage.setAdapter(adapter);

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                languageCode = listCountry.get(i).getCode();
                TTS.setLanguage(new Locale(languageCode));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechInput(SPEAK_REQUEST_CODE, languageCode);
            }
        });
    }

    public void getSpeechInput(int requestCode, String locale) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, requestCode);
        }
        else {
            Toast.makeText(this, "Language doesn't supported!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SPEAK_REQUEST_CODE:
                if(resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtContent.setText(result.get(0));
                    TTS.speak(result.get(0), TextToSpeech.QUEUE_FLUSH, null);
                }
                break;
        }
    }


}
