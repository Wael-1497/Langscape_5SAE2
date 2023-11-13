package tn.sae.langscape;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDatabaseHelper myDB;
    ArrayList<String> course_id, course_name, course_teacher, course_content, course_date;
    CustomAdapter customAdapter;
    ImageView empty_imageview;
    TextView no_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);
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
        customAdapter = new CustomAdapter(CourseActivity.this,this, course_id, course_name, course_teacher, course_content, course_date);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CourseActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            recreate();
        }
    }

    void displayData(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else {
            while (cursor.moveToNext()){
                course_id.add(cursor.getString(0));
                course_name.add(cursor.getString(1));
                course_teacher.add(cursor.getString(2));
                course_content.add(cursor.getString(3));
                course_date.add(cursor.getString(4));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.delete_all){
            confirmDialogDelete();
        }
        return super.onOptionsItemSelected(item);
    }
    void confirmDialogDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All ?");
        builder.setMessage("Are you sure you want to delete all data ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(CourseActivity.this);
                myDB.deleteAllData();
                Intent intent = new Intent(CourseActivity.this, CourseActivity.class);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }


}
