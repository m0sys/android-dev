package example.com.animalgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class MenuActivity extends AppCompatActivity {
    public static SQLiteDatabase database;
    public static int userID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Create database from .sql file
        try {
            database = importSQLHelper(R.raw.animal_game, "animal_game");
        } catch (SQLiteException e) {
            // pass
        }

        if (database == null) { // check if database is null
            database = openOrCreateDatabase("animal_game", MODE_PRIVATE, null);
        }

        // Generate new userID if userID == -1
        if (userID == -1) {
            userID = generateRandomUserID(100, 0);
        }

        Log.d("MenuActivity", "User ID = " + userID);

        // Debug query
        // String debugQuery = "SELECT * FROM edges;";
        // Cursor cursor = database.rawQuery(debugQuery, null);
        // if (cursor.moveToFirst()) {
        //     do {
        //         int parentID = cursor.getInt(cursor.getColumnIndex("parentID"));
        //         int childID = cursor.getInt(cursor.getColumnIndex("childID"));
        //         String type = cursor.getString(cursor.getColumnIndex("type"));
        //         Log.d("EdgesDebugQuery", "Parent ID: " + parentID);
        //         Log.d("EdgesDebugQuery", "Child ID: " + childID);
        //         Log.d("EdgesDebugQuery", "Type: " + type);
        //         Log.d("EdgesDebugQuery", "\n");

        //     } while (cursor.moveToNext());
        //     cursor.close();
        // }

        // debugQuery = "SELECT * FROM nodes;";
        // cursor = database.rawQuery(debugQuery, null);
        // if(cursor.moveToFirst()) {
        //     do {
        //         String type = cursor.getString(cursor.getColumnIndex("type"));
        //         String text = cursor.getString(cursor.getColumnIndex("text"));
        //         Log.d("NodesDebugQuery", "Type: " + type);
        //         Log.d("NodesDebugQuery", "Text: " + text);

        //     } while (cursor.moveToNext());
        //     cursor.close();
        // }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("userID", userID);
        toSave();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("userID")) {
            userID = savedInstanceState.getInt("userID");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get userID from saveFile if available
        try {
            Scanner scan = new Scanner(openFileInput("saveFile.txt"));
            while (scan.hasNext()) {
                String line = scan.nextLine();
                userID = Integer.parseInt(line);
            }

        } catch (Exception e) {
            // pass
        }
        Log.d("MenuActivity", "Recovered userID: " + userID);
    }

    private int generateRandomUserID(int max, int min) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private SQLiteDatabase importSQLHelper (int fileID, String databaseName) {
        // Import database from .sql file
        SQLiteDatabase database = getApplicationContext().openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
        Scanner scan = new Scanner(getResources().openRawResource(fileID));

        String query = "";
        while (scan.hasNext()) { // build and execute queries
            query += scan.nextLine() + "\n";
            if (query.trim().endsWith(";")) {
                database.execSQL(query);
                query = "";
            }
        }
        return database;
    }

    public void dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instructions");
        builder.setMessage(R.string.instructions);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public void instructionsButton(View view) {
        // Start dialog
        dialog();
    }

    public void playButton(View view) {
        // Start MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void toSave() {
        // Write userID to file
        try {
            PrintStream output = new PrintStream(openFileOutput("saveFile.txt", MODE_PRIVATE));
            output.println(userID);
        } catch (Exception e) {
            // pass
        }
    }
}
