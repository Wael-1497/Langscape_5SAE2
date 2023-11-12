package tn.sae.langscape;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddCourseActivity extends AppCompatActivity {
    EditText name_input, teacher_input, content_input, date_input;
    Button add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        name_input = findViewById(R.id.name_input);
        teacher_input = findViewById(R.id.teacher_input);
        content_input = findViewById(R.id.content_input);
        date_input = findViewById(R.id.date_input);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddCourseActivity.this);
                myDB.addCourse(name_input.getText().toString().trim(),
                        teacher_input.getText().toString().trim(),
                        content_input.getText().toString().trim(),
                        date_input.getText().toString().trim());
            }
        });
    }
}