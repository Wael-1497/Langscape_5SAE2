package tn.sae.langscape;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

   private Context context;
   Activity activity;
   private ArrayList course_id, course_name, course_teacher, course_content, course_date;
Animation translate_anim;

    CustomAdapter(Activity activity, Context context,
                  ArrayList course_id,
                  ArrayList course_name,
                  ArrayList course_teacher,
                  ArrayList course_content,
                  ArrayList course_date){
        this.activity = activity;
        this.context = context;
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_teacher = course_teacher;
        this.course_content = course_content;
        this.course_date = course_date;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.course_id_txt.setText(String.valueOf(course_id.get(position)));
        holder.course_name_txt.setText(String.valueOf(course_name.get(position)));
        holder.course_teacher_txt.setText(String.valueOf(course_teacher.get(position)));
        holder.course_content_txt.setText(String.valueOf(course_content.get(position)));
        holder.course_date_txt.setText(String.valueOf(course_date.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateCourseActivity.class);
                intent.putExtra("id", String.valueOf(course_id.get(position)));
                intent.putExtra("name", String.valueOf(course_name.get(position)));
                intent.putExtra("teacher", String.valueOf(course_teacher.get(position)));
                intent.putExtra("content", String.valueOf(course_content.get(position)));
                intent.putExtra("date", String.valueOf(course_date.get(position)));
                activity.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return course_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView course_id_txt, course_name_txt, course_teacher_txt, course_content_txt, course_date_txt;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course_id_txt = itemView.findViewById(R.id.course_id_txt);
            course_name_txt = itemView.findViewById(R.id.course_name_txt);
            course_teacher_txt = itemView.findViewById(R.id.course_teacher_txt);
            course_content_txt = itemView.findViewById(R.id.course_content_txt);
            course_date_txt = itemView.findViewById(R.id.course_date_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
