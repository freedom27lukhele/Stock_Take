package com.example.takestock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rejowan.cutetoast.CuteToast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    private Button RegisterBtn;
//    private Button loginNowBtn;

    //create object of DatabaseReference class to access firebases Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://stocktakeblu-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fullName = findViewById(R.id.fullname);
        final EditText email = findViewById(R.id.email);
        final EditText phoneNumber = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final EditText confirmPassword = findViewById(R.id.confirmPassword);

        RegisterBtn = findViewById(R.id.registerBtn);

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data from EditText
                final String fullnametxt = fullName.getText().toString();
                final String phonenumberTxt = phoneNumber.getText().toString();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String confirmpasswordTxt = confirmPassword.getText().toString();

                if (fullnametxt.isEmpty() || phonenumberTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty()) {
//                    Toast.makeText(Register.this, "please fill all fields ", Toast.LENGTH_SHORT).show();
                    CuteToast.ct(Register.this, "please fill all fields ", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();

                } else if (!passwordTxt.equals(confirmpasswordTxt)) {
//                    Toast.makeText(Register.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    CuteToast.ct(Register.this, "Passwords are not matching", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();

                } else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if phone is not registered before
                            if (snapshot.hasChild(phonenumberTxt)) {
//                                Toast.makeText(Register.this, "Phone is already registered", Toast.LENGTH_SHORT).show();
                                CuteToast.ct(Register.this, "Phone is already registered", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();
                            } else {
                                //sending data to firebase realtime database
                                //we are using phone number as unique identity of every user
                                //so all the other details of user comes under phone number
                                databaseReference.child("users").child(phonenumberTxt).child("fullname").setValue(fullnametxt);
                                databaseReference.child("users").child(phonenumberTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(phonenumberTxt).child("password").setValue(passwordTxt);

                                //show success message then finish activity
//                                Toast.makeText(Register.this, "User registered success", Toast.LENGTH_SHORT).show();
                                CuteToast.ct(Register.this, "User registered success", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();

                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });


    }


}