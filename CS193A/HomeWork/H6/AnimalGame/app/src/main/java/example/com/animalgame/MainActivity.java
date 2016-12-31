package example.com.animalgame;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private int parentID = 1, graphID, lastParentID;
    private String childType;
    private SQLiteDatabase database = MenuActivity.database;
    private boolean gameFinished = false;
    private String userAnimalName, userQuestion, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get text with ID equal to parentID from database
        getParentText(parentID);
    }

    private void getParentText(int Id) {
        // Get TextView
        TextView textView = (TextView) findViewById(R.id.text_box);

        // Start query and set textView text to 'text'
        String query = "SELECT * FROM nodes WHERE nodeID = \"" + Id + "\"";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        String text = cursor.getString(cursor.getColumnIndex("text"));
        cursor.close();
        Log.d("MainActivity", "Text: " + text);
        textView.setText(text);

    }

    private void doQuery(String type) {
        // Start query and set parentID to childID
        String query = "SELECT * FROM edges WHERE parentID = \"" + parentID + "\" AND type = \"" + type + "\""; // select row with parentID == 'parentID' and type == 'type' from database
        Cursor cursor = database.rawQuery(query, null);
        Log.d("MainActivity", "Cursor.moveToFirst(): " + cursor.moveToFirst());
        Log.d("MainActivity", "Query: " + query);

        if (cursor.moveToFirst()) {
            do {
                // Get child ID and child type from selected row
                int childID = cursor.getInt(cursor.getColumnIndex("childID"));
                childType = cursor.getString(cursor.getColumnIndex("type"));
                Log.d("MainActivity", "Child ID: " + childID);
                Log.d("MainActivity", "Parent ID: " + parentID);
                Log.d("MainActivity", "Child type: " + childType);

                // Set parentID to childID
                parentID = childID;
            } while (cursor.moveToNext());
            cursor.close();

        } else { // at last node
            Log.d("MainActivity", "Type: " + type);
            Log.d("MainActivity", "Parent ID: " + parentID);
            if (type == "yes") { // computer has won the game
                Toast.makeText(this, "I won!", Toast.LENGTH_SHORT).show();
                gameFinished = true;
                TextView textView = (TextView) findViewById(R.id.text_box);
                textView.setText("I WON!");
            }

            else if (type == "no") { // computer has lost the game
                Log.d("MainActivity", "HERE!");
                Toast.makeText(this, "I lost!", Toast.LENGTH_SHORT).show();
                gameFinished = true;
                TextView textView = (TextView) findViewById(R.id.text_box);
                textView.setText("I LOST!");

                // Get old parentID and graphID and set lastParentID to the last childID value
                query = "SELECT * FROM edges WHERE childID = \"" + parentID + "\" AND type = \"" + childType + "\""; // select row where childID == parentID (where parentID == last childID) and type == childType
                Log.d("MainActivity", "New query: " + query);
                cursor = database.rawQuery(query, null);
                cursor.moveToFirst();
                lastParentID = parentID; // set lastParentID to last childID value (parentID == childID (line 68))
                parentID = cursor.getInt(cursor.getColumnIndex("parentID")); // get old parentID
                graphID = cursor.getInt(cursor.getColumnIndex("graphID"));
                cursor.close();
                Log.d("MainActivity", "Old parent ID: " + parentID);
                Log.d("MainActivity", "Graph ID: " + graphID);
            }
        }
    }

    public java.sql.Timestamp getCurrentTimestamp() {
        // Create java calendar instance
        Calendar calendar = Calendar.getInstance();

        // Get Date from the calendar instance
        Date now = calendar.getTime();

        // Create currentTimestamp
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

        return currentTimestamp;

    }
    public void insertData(String animalName, String question, String type) {
        // Get current timestamp
        java.sql.Timestamp currentTimestamp = getCurrentTimestamp();

        // Nodes table

        // New question row
        ContentValues questionValues = new ContentValues();
        questionValues.put("type", "question");
        questionValues.put("text", question);
        questionValues.put("userID", MenuActivity.userID);
        questionValues.put("view_count", 0);
        questionValues.put("time_stamp", currentTimestamp.toString());

        long questionResult = database.insert("nodes", null, questionValues); // insert row (ContentValues) into database
        Log.d("Insert Data", "Question result: " + questionResult);

        // New answer row
        ContentValues answerValues = new ContentValues();
        answerValues.put("type", "answer");
        answerValues.put("text", animalName);
        answerValues.put("userID", MenuActivity.userID);
        answerValues.put("view_count", 0);
        answerValues.put("time_stamp", currentTimestamp.toString());
        long answerResult = database.insert("nodes", null, answerValues); // insert row (ContentValues) into database
        Log.d("Insert Data", "Answer result: " + answerResult);

        // Edges table
        String query = "REPLACE INTO edges VALUES (" + "\"" + graphID + "\"" + ", "  + "\"" + parentID + "\"" + ", " + "\"" + questionResult + "\"" + ", " + "\"" + childType + "\"" + ")" + ";"; // replace old row with 'PRIMARY KEY' == graphID with old parentID and new 'childID' == questionResult
        Log.d("Insert Data", "Query: " + query);
        database.execSQL(query);

        // New edge with the new question and answer
        ContentValues newEdgeValues = new ContentValues();
        newEdgeValues.put("parentID", questionResult);
        newEdgeValues.put("childID", answerResult);
        newEdgeValues.put("type", type); // type == 'questionType' based on typeDialog input 'yes'/'no'
        long newEdgeResult = database.insert("edges", null, newEdgeValues);

        // Modified edge with old answer (last childID) and new question
        ContentValues modifiedEdgeValues = new ContentValues();
        modifiedEdgeValues.put("parentID", questionResult);
        modifiedEdgeValues.put("childID", lastParentID); // last childID value
        if (type == "no") {
            modifiedEdgeValues.put("type", "yes");
        } else {
            modifiedEdgeValues.put("type", "no");
        }
        long modifiedEdgeResult = database.insert("edges", null, modifiedEdgeValues);

        if (newEdgeResult != -1 && modifiedEdgeResult != -1) {
            Log.d("Insert Data", "Inserting data was successful!");
        }
    }

    public void nameDialog() {
        // Create custom dialog instance
        final View customDialog = this.getLayoutInflater().inflate(R.layout.input_dialog, null);

        // Create animal name dialog builder and setView to 'customDialog'
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.animalName)
                .setView(customDialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get userInput from 'customDialog'
                        EditText editText = (EditText) customDialog.findViewById(R.id.input);
                        final String userInput = editText.getText().toString();
                        Log.d("MainActivity", "User Input: " + userInput);
                        dialogInterface.dismiss();

                        // Create question dialog builder
                        questionDialog(userInput);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();
    }
    public void questionDialog(final String userInput) {
        // Create new custom dialog instance
        final View customDialog = MainActivity.this.getLayoutInflater().inflate(R.layout.input_dialog, null);

        // Set editText hint value
        EditText editText = (EditText) customDialog.findViewById(R.id.input);
        editText.setHint("Enter a question");

        // Create question dialog builder and setView to 'customDialog'
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.animalQuestion, userInput))
                .setView(customDialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get userQuestion from 'customDialog'
                        EditText editText = (EditText) customDialog.findViewById(R.id.input);
                        String userQuestion = editText.getText().toString();
                        Log.d("MainActivity", "User question: " + userQuestion);
                        dialogInterface.dismiss();

                        // Create type dialog builder
                        typeDialog(userInput, userQuestion);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();

    }

    public void typeDialog(final String userInput, final String userQuestion) {
        // Create type dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.questionType, userInput))
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get questionType from dialog
                        String questionType = "no";
                        Log.d("MainActivity", "Question type: " + questionType);

                        // Insert data into database and dismiss dialog
                        insertData(userInput, userQuestion, questionType);
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Get questionType from dialog
                String questionType = "yes";
                Log.d("MainActivity", "Question type: " + questionType);

                // Insert data into database and dismiss dialog
                insertData(userInput, userQuestion, questionType);
                dialogInterface.dismiss();
            }
        }).create().show();

    }

    public void noButton(View view) {
        doQuery("no");
        if (!gameFinished) {
            getParentText(parentID);
        } else {
            nameDialog(); // call dialog when at last node (game lost), ask for user input
        }
    }

    public void yesButton(View view) {
        doQuery("yes");
        if(!gameFinished) {
            getParentText(parentID);
        } else { // go back to MenuActivity when at last node (game won)
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
    }
}
