package example.com.madlibs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private String libText;
    private ArrayList<String> typeOfWords;
    private EditText inputText;
    private TextView wordCount;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Generate random filename
        String[] fileNames = new String[] {"madlibs1", "madlibs2", "madlibs3", "madlibs4"};
        String fileName = fileNames[new Random().nextInt(fileNames.length)];

        // Get text from file
        libText = readFile(fileName);
        typeOfWords = getWordTypes(libText);
        inputText = (EditText) findViewById(R.id.input_text);
        inputText.setHint(typeOfWords.get(currentIndex));

        wordCount = (TextView) findViewById(R.id.word_count);
        wordCount.setText(typeOfWords.size() + " word(s) left");
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
        if (fileName == "madlibs1") {
            Scanner scan = new Scanner(getResources().openRawResource(R.raw.madlib1_tarzan));
            text = scan.nextLine();
            while (scan.hasNextLine()) {
                text += " " + scan.nextLine();
            }
        }

        else if (fileName == "madlibs2") {
            Scanner scan = new Scanner(getResources().openRawResource(R.raw.madlib2_university));
            text = scan.nextLine();
            while (scan.hasNextLine()) {
                text += " " + scan.nextLine();
            }
        }

        else if (fileName == "madlibs3") {
            Scanner scan = new Scanner(getResources().openRawResource(R.raw.madlib3_clothes));
            text = scan.nextLine();
            while (scan.hasNextLine()) {
                text += " " + scan.nextLine();
            }

        } else {
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

    public void OkButton(View view) {
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

        // Set word count to text view
        int count = typeOfWords.size() - currentIndex;
        update(count);
    }

    private void update(int count) {
        inputText.setText("");
        inputText.setHint(typeOfWords.get(currentIndex).toLowerCase());
        wordCount.setText(count + " word(s) left");
    }
}
