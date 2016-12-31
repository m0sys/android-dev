package example.com.madlibs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Scanner;

public class Menu extends AppCompatActivity {
    private Spinner spinner;
    private String userSelection;
    private String[] fileNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Default values
        userSelection = "Random";

        // Welcome text
        TextView welcomeView = (TextView) findViewById(R.id.welcome_text);
        String welcomeText = readFile();
        welcomeView.setText(welcomeText);

        // Set adapter and listener for lib_spinner
        fileNames = getResources().getStringArray(R.array.fileNames);
        loadSpinner(fileNames);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                userSelection = fileNames[position];
                Log.d("DebugMenu", "User selection: " + userSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void loadSpinner(String[] fileNames) {
        // Add filenames to lib_spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                fileNames);
        // ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
        //         R.array.fileNames,
        //         android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.lib_spinner);
        spinner.setPromptId(R.string.spinner_prompt);
        spinner.setAdapter(adapter);
    }

    private String readFile () {
        String text;
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.welcome_text));
        text = scan.nextLine();
        while (scan.hasNextLine()) {
            text += " " + scan.nextLine();
        }
        return text;
    }

    public void playButton(View view) {
        // Main Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("UserSelection", userSelection);
        startActivity(intent);
    }
}
