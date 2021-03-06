package com.example.a222latest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class selectMembershipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_membership);
    }

    /**
     * That method provide the sign as student.
     * @param view view
     */
    public void signStudent(View view) {
        Intent intent = new Intent(this,signActivity.class);
        intent.putExtra("membership", "student");
        startActivity(intent);
    }

    /**
     * That method provide the sign as teacher.
     * @param view view
     */
    public void signTeacher(View view) {
        Intent intent = new Intent(this,signActivity.class);
        intent.putExtra("membership", "teacher");
        startActivity(intent);
    }
}
