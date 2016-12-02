package example.com.madlibs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ShowLib extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lib);

        // Extract 'completeText' from MainActivity
        Intent intent = getIntent();
        String completeStory = intent.getStringExtra("completeText");

        // Lib text
        TextView text = (TextView) findViewById(R.id.complete_text);
        text.setText(completeStory);

    }

    public void newStoryButton(View view) {
        // Main Activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
