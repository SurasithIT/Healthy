package com.example.surasith.healthy.sleep;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.surasith.healthy.R;
import com.example.surasith.healthy.dbhelper.DBHelper;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SleepFormActivity extends AppCompatActivity {
    private static final String TAG = "Sleep Form";
    private DBHelper db = new DBHelper(this);
    private Calendar calendar;
    private TextView date;
    private TextView bedTime;
    private TextView wakingTime;
    private Button saveButton;
    private boolean haveExtra = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_form);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Sleep Form");
        }

        date = findViewById(R.id.sleep_form_date);
        bedTime = findViewById(R.id.sleep_form_start);
        wakingTime = findViewById(R.id.sleep_form_end);
        saveButton = findViewById(R.id.sleep_form_save);

        bedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(bedTime);
            }
        });
        wakingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(wakingTime);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveExtra) {
                    updateData();
                } else {
                    saveData();
                }
                finish();
            }
        });

        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
        date.setText(format.format(new Date()));

        getExtra();
    }

    private void setTime(final TextView textView) {
        // get current time
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // show time picker dialog
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d("Time Picker", "hour = " + hourOfDay + "|| minute = " + minute);
                String time = String.format("%02d:%02d", hourOfDay, minute);
                textView.setText(time);
            }
        };

        TimePickerDialog timePicker = new TimePickerDialog(this, listener, hour, minute, true);
        timePicker.show();
    }

    private String calculateTime() {
        /*
         * Reference url : https://www.mkyong.com/java/how-to-calculate-date-time-difference-in-java
         */
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String start = bedTime.getText().toString();
        String stop = wakingTime.getText().toString();
        Date date1;
        Date date2;
        String duration;
        String hDuration = "";
        String mDuration = "";

        try {
            date1 = format.parse(start);
            date2 = format.parse(stop);
            DateTime dateTime1 = new DateTime(date1);
            DateTime dateTime2 = new DateTime(date2);

            int hour = Hours.hoursBetween(dateTime1, dateTime2).getHours() % 24;
            int minute = Minutes.minutesBetween(dateTime1, dateTime2).getMinutes() % 60;
            Log.d("Sleep Form", dateTime1.toString() + " " + dateTime2.toString());

            if (hour < 0) {
                hDuration = String.format("%02d", 24 + hour);
            } else {
                hDuration = String.format("%02d", hour);
            }
            if (minute < 0) {
                mDuration = String.format("%02d", 60 + minute);
            } else {
                mDuration = String.format("%02d", minute);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        duration = String.format("%s:%s", hDuration, mDuration);
        Log.d(TAG, duration);
        return duration;
    }

    private void saveData() {
        SleepTimeItem item = new SleepTimeItem();
        item.setDateString(date.getText().toString());
        item.setStartTime(bedTime.getText().toString());
        item.setEndTime(wakingTime.getText().toString());
        item.setDuration(calculateTime());
        db.insertSleepTime(item);
        Log.d(TAG, "data saved");
    }

    private void updateData() {
        SleepTimeItem item = (SleepTimeItem) getIntent().getSerializableExtra("item");
        item.setDateString(date.getText().toString());
        item.setStartTime(bedTime.getText().toString());
        item.setEndTime(wakingTime.getText().toString());
        item.setDuration(calculateTime());
        db.update(item);
        Log.d(TAG, "item id" + item.getId());
    }

    private void getExtra() {
        SleepTimeItem item = (SleepTimeItem) getIntent().getSerializableExtra("item");
        if (item != null) {
            haveExtra = true;
            date.setText(item.getDateString());
            bedTime.setText(item.getStartTime());
            wakingTime.setText(item.getEndTime());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
