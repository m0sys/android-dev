package example.com.madlibs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Scanner;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Welcome text
        TextView welcomeView = (TextView) findViewById(R.id.welcome_text);
        String welcomeText = readFile();
        welcomeView.setText(welcomeText);
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
        startActivity(intent);
    }
}
