package example.com.madlibs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ShowLib extends AppCompatActivity {
    private String userSelection;
    private boolean dialogAns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lib);

        // Default values
        dialogAns = false;

        // Extract 'completeText' from MainActivity
        Intent intent = getIntent();
        String completeStory = intent.getStringExtra("completeText");

        // Lib text
        TextView text = (TextView) findViewById(R.id.complete_text);
        text.setText(completeStory);
    }

    public void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose new story")
                .setItems(getResources().getStringArray(R.array.fileNames), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        userSelection = getResources().getStringArray(R.array.fileNames)[which];
                        dialogAns = true;
                    }
                }).create().show();
    }

    public void selectStoryButton(View view) {
        // Popup dialog
        dialog();
    }

    public void newStoryButton(View view) {
        if (dialogAns) { // set intent after user selection
            // Main Activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("UserSelection", userSelection);
            startActivity(intent);
        }
    }
}
