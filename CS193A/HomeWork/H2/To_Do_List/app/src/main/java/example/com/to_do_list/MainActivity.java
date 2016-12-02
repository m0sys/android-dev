package example.com.to_do_list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up data structures
        arrayList = new ArrayList<String>();

        showInputText();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList("arrayList", arrayList);
        toSave();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("arrayList")) {
            arrayList = savedInstanceState.getStringArrayList("arrayList");
            showInputText();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Scanner scan = new Scanner(openFileInput("saveFile.txt"));
            arrayList.clear();
            while (scan.hasNext()) {
                String line = scan.nextLine();
                arrayList.add(line);
            }
        } catch (Exception e) {
            //
        }
    }

    private void showInputText() {
        // Adapter
        ArrayAdapter<String> adapter = null;
        if (adapter == null) {
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayList);
        } else {
            adapter.notifyDataSetChanged();
        }

        ListView listView = (ListView) findViewById(R.id.to_do_list);
        listView.setAdapter(adapter);

        // Listen to long clicks on the ListView
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String taskClicked = arrayList.get(position);
                arrayList.remove(taskClicked);
                showInputText();
                return false;
            }
        });
    }

    public void addClick(View view) {
        EditText taskWordBox = (EditText) findViewById(R.id.new_task);
        String newTask = taskWordBox.getText().toString();
        taskWordBox.setText(null);
        arrayList.add(newTask);
        showInputText();
    }

    private void toSave() {
        // Write arrayList to file
        try {
            PrintStream output = new PrintStream(openFileOutput("saveFile.txt", MODE_PRIVATE));
            for (String e : arrayList) {
                output.println(e);
            }
            output.close();
        } catch (Exception e) {
            //
        }
    }
}
