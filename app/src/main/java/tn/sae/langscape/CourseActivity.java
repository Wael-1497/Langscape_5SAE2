package tn.sae.langscape;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDatabaseHelper myDB;
    ArrayList<String> course_id, course_name, course_teacher, course_content, course_date;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_course);


        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });
        myDB = new MyDatabaseHelper(CourseActivity.this);
        course_id = new ArrayList<>();
        course_name = new ArrayList<>();
        course_teacher = new ArrayList<>();
        course_content = new ArrayList<>();
        course_date = new ArrayList<>();
        displayData();
        customAdapter = new CustomAdapter(CourseActivity.this, course_id, course_name, course_teacher, course_content, course_date);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CourseActivity.this));

    }
    void displayData(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                course_id.add(cursor.getString(0));
                course_name.add(cursor.getString(1));
                course_teacher.add(cursor.getString(2));
                course_content.add(cursor.getString(3));
                course_date.add(cursor.getString(4));
            }
        }
    }
}
