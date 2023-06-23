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

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://stocktakeblu-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
//                    Toast.makeText(Login.this, "phone number or password are empty ", Toast.LENGTH_SHORT).show();
                    CuteToast.ct(Login.this, "phone number or password are empty ", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();

                } else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //check if mobile phone number exists in firebase
                            if (snapshot.hasChild(phoneTxt)) {

                                //if mobile exists in firebase database
                                //now get the password of the user from firebase and match it with what the user entered
                                final String getPassword = snapshot.child(phoneTxt).child("password").getValue(String.class);

                                if (getPassword.equals(passwordTxt)) {
//                                    Toast.makeText(Login.this, "Successful Logged in", Toast.LENGTH_SHORT).show();
                                    CuteToast.ct(Login.this, "Successful Logged in", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                                    //open mainActivity class
                                    startActivity(new Intent(Login.this, AddStockActivity.class));
                                    finish();
                                } else {
//                                    Toast.makeText(Login.this, "Incorrect Password please try again", Toast.LENGTH_SHORT).show();
                                    CuteToast.ct(Login.this, "Incorrect Password please try again", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();

                                }
                            } else {
//                                Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                CuteToast.ct(Login.this, "Wrong password", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        //Open Register activity
        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
}