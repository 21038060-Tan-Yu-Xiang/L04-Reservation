package sg.edu.rp.c346.id21038060.l04_reservation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    EditText etGroupSize;
    EditText etMobileNo;
    ToggleButton tbSmokingArea;
    DatePicker dp;
    TimePicker tp;
    Button btnConfirm;
    Button btnReset;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.editTextName);
        etGroupSize = findViewById(R.id.editTextGroupSize);
        etMobileNo = findViewById(R.id.editTextMobileNo);
        tbSmokingArea = findViewById(R.id.toggleButtonSmokingArea);
        dp = findViewById(R.id.datePicker);
        tp = findViewById(R.id.timePicker);
        btnConfirm = findViewById(R.id.buttonConfirm);
        btnReset = findViewById(R.id.buttonReset);

        Calendar c = Calendar.getInstance();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if DatePicker and TimePicker is valid on submission
                if (!((dp.getYear() >= c.get(Calendar.YEAR) && dp.getMonth() >= c.get(Calendar.MONTH) &&
                        dp.getDayOfMonth() > c.get(Calendar.DAY_OF_MONTH) &&
                        tp.getCurrentHour() >= 8 && tp.getCurrentHour() < 21) ||
                        (dp.getYear() == c.get(Calendar.YEAR) && dp.getMonth() == c.get(Calendar.MONTH) &&
                        dp.getDayOfMonth() == c.get(Calendar.DAY_OF_MONTH) &&
                        tp.getCurrentHour() >= c.get(Calendar.HOUR) &&
                        tp.getCurrentMinute() >= c.get(Calendar.MINUTE) &&
                        tp.getCurrentHour() >= 8 && tp.getCurrentHour() < 21))) {
                    //Invalid: DatePicker / TimePicker has invalid values
                    Toast.makeText(MainActivity.this, "Please note that the \nreservation Date and/or Time \nhas to be from now onwards and within 8am to 8:59pm", Toast.LENGTH_SHORT).show();
                }
                //Check if Required fields have values
                else if (!TextUtils.isEmpty(etName.getText().toString().trim()) &&
                        !TextUtils.isEmpty(etGroupSize.getText().toString().trim()) &&
                        !TextUtils.isEmpty(etMobileNo.getText().toString().trim()))
                {
                    //Valid: All validation completed
                    String output = "Thank you, "+etName.getText().toString()+"!"+
                            " We have received your reservation for "+etGroupSize.getText().toString()+
                            " using "+etMobileNo.getText().toString()+".\n"+
                            "\nYour reservation is on "+dp.getDayOfMonth()+"/"+dp.getMonth()+"/"+dp.getYear()+
                            " at "+tp.getCurrentHour()+":"+(tp.getCurrentMinute()<10?String.format("%02d", tp.getCurrentMinute()):tp.getCurrentMinute())+
                            " with seating preference in a "+tbSmokingArea.getText().toString()+".";
                    Toast.makeText(MainActivity.this, output, Toast.LENGTH_LONG).show();
                }
                else {
                    //Invalid: Required fields not filled up
                    Toast.makeText(MainActivity.this, "Required fields are missing", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setText("");
                etGroupSize.setText("");
                etMobileNo.setText("");
                tbSmokingArea.setChecked(false);

                dp.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                tp.setCurrentHour(8);
                tp.setCurrentMinute(0);
            }
        });

        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void timeRangeAlert() {
                Toast.makeText(MainActivity.this, "Please note that reservations\n are from 8am to 8:59pm", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                //Time Range
                if(hour < 8) {
                    timePicker.setCurrentHour(8);
                    timePicker.setCurrentMinute(0);
                    timeRangeAlert();
                }
                else if(hour >= 21) {
                    timePicker.setCurrentHour(20);
                    timePicker.setCurrentMinute(59);
                    timeRangeAlert();
                }
            }
        });

        dp.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                //Date Later Than Now
                if (year < c.get(Calendar.YEAR) || month < c.get(Calendar.MONTH) || day < c.get(Calendar.DAY_OF_MONTH)) {
                    dp.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    Toast.makeText(MainActivity.this, "Please note that the reservation date\n has to be from today onwards.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}