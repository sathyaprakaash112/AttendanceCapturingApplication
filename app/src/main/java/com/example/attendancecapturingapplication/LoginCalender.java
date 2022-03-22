package com.example.attendancecapturingapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class LoginCalender extends AppCompatActivity {

    CalendarView calendarView;
    TextView selectedDate;
    Button nextbtn;

    FirebaseFirestore firestore;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_calender);

        nextbtn = findViewById(R.id.calenderNxtbtn);
        firestore = FirebaseFirestore.getInstance();
        calendarView = findViewById(R.id.calender);
        selectedDate = findViewById(R.id.selectedDate);
        nextbtn = findViewById(R.id.calenderNxtbtn);
        date = "";

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = i2 + "." + i1 + "." + i;
                selectedDate.setText("Selected Date is : " + date);
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),PEDSelectGameActivity.class));
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                //sending to the list
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("date1", date); //InputString: from the EditText
                editor.commit();
            }
        });



    }

}