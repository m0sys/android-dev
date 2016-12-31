package example.com.madlibs;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static final int SPEECH_TO_TEXT_REQ_CODE = 1234;

    private String libText;
    private ArrayList<String> typeOfWords;
    private EditText inputText;
    private TextView wordCount;
    private TextView promptText;
    private int currentIndex = 0;
    private String fileName;
    private String[] fileNames;
    private String wordType;
    private TextToSpeech textToSpeech;
    private boolean ttsIsReady = false;
    private String userSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Extract "UserSelection" from Menu
        Intent intent = getIntent();
        String userSelection = intent.getStringExtra("UserSelection");
        Log.d("DebugMain", "User selection: " + userSelection);

        // Setup text to speech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsIsReady = true;
            }
        });


        // Generate random filename if userChoice = 'Random'
        fileNames = getResources().getStringArray(R.array.fileNames);
        if (userSelection.equals("Random")) {
            fileName = fileNames[new Random().nextInt(fileNames.length) + 1];
        } else {
            fileName = userSelection;
        }

        // Get text from file
        libText = readFile(fileName);
        typeOfWords = getWordTypes(libText);
        wordType = typeOfWords.get(currentIndex).toLowerCase(); // get word type
        inputText = (EditText) findViewById(R.id.input_text);
        inputText.setHint(wordType);

        wordCount = (TextView) findViewById(R.id.word_count);
        wordCount.setText(typeOfWords.size() + " word(s) left");
        Log.d("DebugMain", "User selection after: " + fileName);

        // Set text_prompt
        promptText = (TextView) findViewById(R.id.text_prompt);
        promptText.setText(getResources().getString(R.string.prompt_text, wordType));

        if (ttsIsReady) {
            String speech = getResources().getString(R.string.prompt_text, wordType);
            textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("libText", libText);
        outState.putStringArrayList("typeOfWords", typeOfWords);
        outState.putInt("currentIndex", currentIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("libText") &&
                savedInstanceState.containsKey("typeOfWords") &&
                savedInstanceState.containsKey("currentIndex")) {
            libText = savedInstanceState.getString("libText");
            typeOfWords = savedInstanceState.getStringArrayList("typeOfWords");
            currentIndex = savedInstanceState.getInt("currentIndex");
            int count = typeOfWords.size() - currentIndex;
            update(count);
        }
    }

    private String readFile (String fileName) {
        String text;
        Log.d("DebugMain", "File name in readFile: " + fileName);
        Log.d("DebugMain", String.valueOf(fileNames[0].equals(fileName)));
        Log.d("DebugMain", "File name at index 0: " + fileNames[0]);

        if (fileNames[1].equals(fileName)) {
            Log.d("DebugMain", "lib1");
            Scanner scan = new Scanner(getResources().openRawResource(R.raw.madlib1_tarzan));
            text = scan.nextLine();
            while (scan.hasNextLine()) {
                text += " " + scan.nextLine();
            }
        }

        else if (fileNames[2].equals(fileName)) {
            Log.d("DebugMain", "lib2");
            Scanner scan = new Scanner(getResources().openRawResource(R.raw.madlib2_university));
            text = scan.nextLine();
            while (scan.hasNextLine()) {
                text += " " + scan.nextLine();
            }
        }

        else if (fileNames[3].equals(fileName)) {
            Log.d("DebugMain", "lib3");
            Scanner scan = new Scanner(getResources().openRawResource(R.raw.madlib3_clothes));
            text = scan.nextLine();
            while (scan.hasNextLine()) {
                text += " " + scan.nextLine();
            }

        } else {
            Log.d("DebugMain", "lib4");
            Scanner scan = new Scanner(getResources().openRawResource(R.raw.madlibs4_dance));
            text = scan.nextLine();
            while (scan.hasNextLine()) {
                text += " " + scan.nextLine();
            }
        }
        return text;
    }

    private ArrayList<String> getWordTypes(String text) {
        ArrayList<String> arraySub = new ArrayList<>();
        int count = 0;
        while(text.contains("<") && text.contains(">")) {
            count ++;
            String subString = text.substring((text.indexOf("<") + 1), text.indexOf(">"));
            arraySub.add(subString);
            text = text.replaceFirst("<" + subString + ">", "");
            if (count > text.length()) {
                break;
            }
        }
        return arraySub;
    }

    public void addTextToStory() {
        String inputWord = inputText.getText().toString();
        Log.d("myDebug", "Before: " + libText);
        libText = libText.replaceFirst("<" + typeOfWords.get(currentIndex) + ">", inputWord.toString());
        Log.d("myDebug", "Input word: " + inputWord);
        Log.d("myDebug", "After: " + libText);

        if (currentIndex >= typeOfWords.size() - 1) {
            // ShowLib Activity
            Intent intent = new Intent(this, ShowLib.class);
            intent.putExtra("completeText", libText);
            intent.putExtra("inputWords", typeOfWords);
            startActivity(intent);
        } else {
            currentIndex += 1;
            Log.d("myDebug", "Current Index = " + currentIndex);
        }

        // Setting word count to text view
        int count = typeOfWords.size() - currentIndex;
        update(count);
    }
    public void OkButton(View view) {
        addTextToStory();
        // Text to speech
        textToSpeech.speak(getResources().getString(R.string.prompt_text, wordType),
                TextToSpeech.QUEUE_FLUSH, null);
    }

    private void update(int count) {
        // Get current word type
        wordType = typeOfWords.get(currentIndex).toLowerCase();

        // Update input_text
        inputText.setText("");
        inputText.setHint(wordType);
        wordCount.setText(count + " word(s) left");

        // Update text_prompt
        Log.d("DebugMain", "Prompt Text: " + getResources().getString(R.string.prompt_text, wordType));
        promptText.setText(getResources().getString(R.string.prompt_text, wordType));
    }

    public void SpeakButton(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Ask for speech
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.speech_text, wordType));
        startActivityForResult(intent, SPEECH_TO_TEXT_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_TO_TEXT_REQ_CODE) {
            if (data == null) {
                return;
            }

            // Get use speech
            ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            userSpeech = list.get(0);
            Log.d("DebugMain", "User speech: " + userSpeech);
            addSpeechToStory();
        }
    }

    public void addSpeechToStory() {
        Log.d("myDebug", "Before: " + libText);
        libText = libText.replaceFirst("<" + typeOfWords.get(currentIndex) + ">", userSpeech);
        Log.d("myDebug", "Input word: " + userSpeech);
        Log.d("myDebug", "After: " + libText);

        if (currentIndex >= typeOfWords.size() - 1) {
            // ShowLib Activity
            Intent intent = new Intent(this, ShowLib.class);
            intent.putExtra("completeText", libText);
            intent.putExtra("inputWords", typeOfWords);
            startActivity(intent);
        } else {
            currentIndex += 1;
            Log.d("myDebug", "Current Index = " + currentIndex);
        }

        // Setting word count to text view
        int count = typeOfWords.size() - currentIndex;
        update(count);
    }

}
