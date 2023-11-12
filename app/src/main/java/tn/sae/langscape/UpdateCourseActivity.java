package tn.sae.langscape;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateCourseActivity extends AppCompatActivity {
    EditText name_input, teacher_input, content_input, date_input;
    Button up_button;
    String id, name, teacher, content, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);

        name_input = findViewById(R.id.name_input2);
        teacher_input = findViewById(R.id.teacher_input2);
        content_input = findViewById(R.id.content_input2);
        date_input = findViewById(R.id.date_input2);
        up_button = findViewById(R.id.up_button);
        getAndSetIntentData();

        up_button.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateCourseActivity.this);
                                                name = name_input.getText().toString().trim();
                                                teacher = teacher_input.getText().toString().trim();
                                                content = content_input.getText().toString().trim();
                                                date = date_input.getText().toString().trim();
                                             myDB.updateData(id, name, teacher, content, date);

                                         }
                                     });
    }
    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("name") &&
                getIntent().hasExtra("teacher") &&
                getIntent().hasExtra("content") &&
                getIntent().hasExtra("date")){
            //Getting Data from Intent
             id = getIntent().getStringExtra("id");
             name = getIntent().getStringExtra("name");
             teacher = getIntent().getStringExtra("teacher");
             content = getIntent().getStringExtra("content");
             date = getIntent().getStringExtra("date");
            //Setting Intent Data
            name_input.setText(name);
            teacher_input.setText(teacher);
            content_input.setText(content);
            date_input.setText(date);
        }
    }
}