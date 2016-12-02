package example.com.berkeleyadmissionquizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import static example.com.berkeleyadmissionquizapp.R.id.points_view;
import static example.com.berkeleyadmissionquizapp.R.id.wrong_view;
// import static example.com.berkeleyadmissionquizapp.R.id.wrong_view;

public class MainActivity extends AppCompatActivity {
    private int num1;
    private int num2;
    private int points;
    private int wrongPoints;
    private TextView pointsView;
    private TextView wrongView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        points = 0;
        wrongPoints = 0;
        pickNumbers();
    }

    private void pickNumbers() {
        pointsView = (TextView) findViewById(points_view);
        pointsView.setText("Points: " + points);

        wrongView = (TextView) findViewById(wrong_view);
        wrongView.setText("Incorrect: " + wrongPoints);

        // Initialization code
        Random randy = new Random();
        num1 = randy.nextInt(10);
        num2 = randy.nextInt(10);
        Button left = (Button) findViewById(R.id.left_button);
        Button right = (Button) findViewById(R.id.right_button);

        // Set text to random numbers
        left.setText(String.valueOf(num1));
        right.setText(String.valueOf(num2));
    }


    // Button click functions
    public void leftButtonClick(View view) {
        if (num1 > num2) {
            // Correct
            pointsView.setTextColor(getResources().getColor(R.color.correct));
            points ++;
        } else {
            // Incorrect
            pointsView.setTextColor(getResources().getColor(R.color.inCorrect));
            points --;
            wrongPoints ++;
        }

        pickNumbers();
    }

    public void rightButtonClick(View view) {
        if (num2 > num1) {
            // Correct
            pointsView.setTextColor(getResources().getColor(R.color.correct));
            points ++;
        } else {
            // Incorrect
            pointsView.setTextColor(getResources().getColor(R.color.inCorrect));
            points --;
            wrongPoints ++;
        }
        pickNumbers();
    }

    public void equalButtonClick(View view) {
        if (num1 == num2) {
            // Correct
            pointsView.setTextColor(getResources().getColor(R.color.correct));
            points ++;
        } else {
            // Incorrect
            pointsView.setTextColor(getResources().getColor(R.color.inCorrect));
            points --;
            wrongPoints ++;
        }
        pickNumbers();
    }
}
