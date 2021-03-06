package com.example.a222latest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email;
    EditText password;
    private FirebaseUser user;

    /**
     * Sets the email and password.
     * @param savedInstanceState instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.mailAddress);
        password = findViewById(R.id.password);
    }

    /**
     * That method takes the mail and password information. If the that informations are valid, direct the user to the show post activity.
     * If the informations are invalid, send message.
     * @param view view.
     */
    public void sample(View view) {

        String mail = email.getText().toString();
        String pass = password.getText().toString();
        Context context = this;

        mAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(loginActivity.this, "You login Succesfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, ShowPostActivity.class);
                intent.putExtra("guest", false);
                startActivity(intent);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     *  That method direct the user to the reset activity to reset his/her password.
     * @param view view.
     */
    public void resetPassword(View view) {
        Intent intent = new Intent(this, resetActivity.class);
        startActivity(intent);
    }
}