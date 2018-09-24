package com.powerranger.ginz.transfervoicetogoolge;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Environment;
import android.os.LocaleList;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnUtteranceCompletedListener {
    private static final int SPEAK_REQUEST_CODE = 69;
    private static final String UTTERANCE_REPEAT = "repeat";
    private TextToSpeech TTS;
    private Button btnSpeak;
    private Button btnRepeat;
    private TextView txtContent;
    private Spinner spinnerLanguage;
    private String languageCode = "vi";
    private Boolean isRepeating = false;
    private String speakSentence;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-2117205316433633~2991394307");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Init();
    }

    private void Init() {
        btnSpeak = findViewById(R.id.button_speak);
        btnRepeat = findViewById(R.id.btnRepeat);
        txtContent = findViewById(R.id.text_content);
        spinnerLanguage = findViewById(R.id.spinner_language);

        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    int language = TTS.setLanguage(new Locale("vi"));
                    onInitTTS();
                    if(language == TextToSpeech.LANG_MISSING_DATA
                            || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "Language doesn't supported!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {

                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Cannot Initiate!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ArrayList<Language> listCountry = new ArrayList<>();
        listCountry.add(new Language(R.drawable.vi, "Vietnamese", "vi"));
        listCountry.add(new Language(R.drawable.en, "English", "en"));
        listCountry.add(new Language(R.drawable.fr, "French", "fr"));
        listCountry.add(new Language(R.drawable.ja, "Japanese", "ja"));
        listCountry.add(new Language(R.drawable.de, "German", "de"));
        listCountry.add(new Language(R.drawable.zhcn, "Chinese", "zh-CN"));
        listCountry.add(new Language(R.drawable.ko, "Korean", "ko"));
        listCountry.add(new Language(R.drawable.ar, "Arabic", "ar"));
        listCountry.add(new Language(R.drawable.ru, "Russian", "ru"));
        listCountry.add(new Language(R.drawable.pt, "Portuguese ", "pt"));
        listCountry.add(new Language(R.drawable.th, "Thai", "th"));
        listCountry.add(new Language(R.drawable.lo, "Lao", "lo"));
        listCountry.add(new Language(R.drawable.hi, "India", "hi"));
        listCountry.add(new Language(R.drawable.id, "Indonesian", "id"));
        listCountry.add(new Language(R.drawable.it, "Italian", "it"));
        listCountry.add(new Language(R.drawable.km, "Khmer", "km"));
        listCountry.add(new Language(R.drawable.da, "Danish", "da"));
        listCountry.add(new Language(R.drawable.es, "Spanish (Spain)", "es"));
        listCountry.add(new Language(R.drawable.es, "Basque (Spain)", "eu"));
        listCountry.add(new Language(R.drawable.es, "Catalan (Spain)", "ca"));
        listCountry.add(new Language(R.drawable.ms, "Malay", "ms"));
        listCountry.add(new Language(R.drawable.el, "Greek", "el"));
        listCountry.add(new Language(R.drawable.tr, "Turkish", "tr"));
        listCountry.add(new Language(R.drawable.sv, "Swedish", "sv"));
        listCountry.add(new Language(R.drawable.pl, "Polish", "pl"));
        listCountry.add(new Language(R.drawable.nb, "Norwegian Bokmål ", "nb"));
        listCountry.add(new Language(R.drawable.nl, "Duth", "nl"));
        listCountry.add(new Language(R.drawable.ne, "Nepali", "ne"));
        listCountry.add(new Language(R.drawable.hu, "Hungarian ", "hu"));
        listCountry.add(new Language(R.drawable.hr, "Croatian", "hr"));
        listCountry.add(new Language(R.drawable.fi, "Finnish", "fi"));

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

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isRepeating) {
                    btnRepeat.setBackgroundResource(R.mipmap.ic_repeat);
                    isRepeating = false;
                    TTS.stop();
                }
                else {
                    btnRepeat.setBackgroundResource(R.mipmap.ic_stop);
                    isRepeating = true;
                    say(speakSentence, UTTERANCE_REPEAT);
                }
            }
        });


    }

    private void onInitTTS() {
            TTS.setOnUtteranceCompletedListener(this);
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

    private void say(String content) {
        if(content != null) {
            TTS.speak(content, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void say(String content, String utteranceId) {
        if(content != null) {
            HashMap<String, String> myHashRender = new HashMap<String, String>();
            myHashRender.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
            myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
            TTS.speak(content, TextToSpeech.QUEUE_FLUSH, myHashRender);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SPEAK_REQUEST_CODE:
                if(resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speakSentence = result.get(0);
                    txtContent.setText(speakSentence);
                    //TTS.speak(result.get(0), TextToSpeech.QUEUE_FLUSH, null);
                    say(speakSentence);
                }
                break;
        }
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        if(utteranceId.equals(UTTERANCE_REPEAT)) {
            if(isRepeating) {
                if(speakSentence != null) {
                    say(speakSentence, UTTERANCE_REPEAT);
                }
                else{
                    Toast.makeText(this, "Speak First", Toast.LENGTH_SHORT).show();
                }
            }
        }
        Toast.makeText(this, "da noi xong", Toast.LENGTH_SHORT).show();
        btnRepeat.setBackgroundResource(R.mipmap.ic_repeat);
    }
}
