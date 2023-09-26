package com.example.takestock;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.takestock.Stockfile.stockModal;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.rejowan.cutetoast.CuteToast;


import java.util.Calendar;
import java.util.Date;

public class AddStockActivity extends AppCompatActivity {

    // creating variables for our button, edit text,
    // firebase database, database reference, progress bar.
    private Button addCourseBtn;
    private TextInputEditText personNameEdt, deviceNameEdt, deviceSerialEdt, dateEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String stockId;

    String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        // initializing all our variables.
        addCourseBtn = findViewById(R.id.idBtnAddCourse);
        personNameEdt = findViewById(R.id.idEdtPersonName);
        deviceNameEdt = findViewById(R.id.idEdtDeviceName);
        deviceSerialEdt = findViewById(R.id.idEdtDeviceSerial);
        dateEdt = findViewById(R.id.idEdtDate);


        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://stocktakeblu-default-rtdb.firebaseio.com/");


        // adding click listener for our add course button.
//        dateEdt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // calender class's instance and get current date , month and year from calender
//                final Calendar c = Calendar.getInstance();
//                int mYear = c.get(Calendar.YEAR); // current year
//                int mMonth = c.get(Calendar.MONTH); // current month
//                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
//                // date picker dialog
//                datePickerDialog = new DatePickerDialog(AddStockActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                // set day of month , month and year value in the edit text
//                                dateEdt.setText(dayOfMonth + "/"
//                                        + (monthOfYear + 1) + "/" + year);
//
//
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//            }
//        });

        dateEdt.setText(currentDateTimeString);

        deviceSerialEdt.setOnClickListener(v -> {
            scanCode();
        });


        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                // getting data from our edit text.
                String personName = personNameEdt.getText().toString();
                String deviceName = deviceNameEdt.getText().toString();
                String deviceSerial = deviceSerialEdt.getText().toString();
                String date = dateEdt.getText().toString();
                stockId = deviceSerial;
                // on below line we are passing all data to our modal class.
                stockModal stock = new stockModal(stockId, personName, deviceName, deviceSerial, date);
                // on below line we are calling a add value event
                // to pass data to firebase database.
                databaseReference.child("Stock").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // on below line we are setting data in our firebase database.
                        // displaying a toast message.
//                        Toast.makeText(AddStockActivity.this, "Stock Added..", Toast.LENGTH_SHORT).show();
                        // starting a main activity.
                        personNameEdt.setText("");
                        deviceNameEdt.setText("");
                        deviceSerialEdt.setText("");

                        if (snapshot.hasChild(stockId)) {
                            CuteToast.ct(AddStockActivity.this, "Device already exist", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();
                        } else {
                            //sending data to firebase realtime database
                            //we are using phone number as unique identity of every user
                            //so all the other details of user comes under phone number
                            databaseReference.child("Stock").child(stockId).child("personsName").setValue(personName);
                            databaseReference.child("Stock").child(stockId).child("deviceName").setValue(deviceName);
                            databaseReference.child("Stock").child(stockId).child("deviceSerial").setValue(deviceSerial);
                            databaseReference.child("Stock").child(stockId).child("date").setValue(date);
                            CuteToast.ct(AddStockActivity.this, "Stock Added..", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();

                            loadingPB.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message on below line.
//                        Toast.makeText(AddStockActivity.this, "Fail to add Stock..", Toast.LENGTH_SHORT).show();
                        CuteToast.ct(AddStockActivity.this, "Fail to add Stock..", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();
                    }
                });
            }
        });

    }

    private void scanCode() {

        ScanOptions option = new ScanOptions();
        option.setPrompt("volume up tp flash on");
        option.setBeepEnabled(true);
        option.setOrientationLocked(true);
        option.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(option);

    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {

        if (result.getContents() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddStockActivity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }).show();

            deviceSerialEdt.setText(result.getContents());
        }
    });

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }


}
