package tn.sae.langscape;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TermDetail extends AppCompatActivity {
    // Declare the View object references
    TextView tvTermDetails, tvFullFormDetails;
    EditText etFullFormDetails;
    ImageButton ibEdit;
    // Declare a Term object reference to store the Term from Intent
    Term termSelected;
    // Declare an integer to store itemPosition from Intent
    int itemPosition;
    // Define a flag to store edit state.
    boolean editState = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_detail);

        // Get the information from Intent
        termSelected = (Term) getIntent().getSerializableExtra("termSelected");
        itemPosition = getIntent().getIntExtra("itemPosition", 1);

        // Get the handles for Views
        tvTermDetails = findViewById(R.id.tvTermDetails);
        tvFullFormDetails = findViewById(R.id.tvFullFormDetails);
        etFullFormDetails = findViewById(R.id.etFullFormDetails);
        ibEdit = findViewById(R.id.ibEdit);

        // Set the values of Views with values received from Intent
        tvTermDetails.setText(termSelected.getTerm());
        tvFullFormDetails.setText(termSelected.getFullForm());
        etFullFormDetails.setText(termSelected.getFullForm());
    }

    public void edit(View view) {
        if (editState == false) {
            // If edit ImageButton is clicked and editState is false
            // Change editState to true.
            editState = true;
            // Hide the TextView tvFullFormDetails
            tvFullFormDetails.setVisibility(View.GONE);
            // And show the EditText etFullFormDetails since we're going to edit
            etFullFormDetails.setVisibility(View.VISIBLE);
            // Change the ImageButton's background with a different image: save.
            ibEdit.setImageResource(R.drawable.save);
        } else {
            // If edit ImageButton is clicked and editState is true, meaning the user already has done editing,
            // change editState to false.
            editState = false;
            // Show the TextView for Full Form
            tvFullFormDetails.setVisibility(View.VISIBLE);
            // And hide the EditText for Full Form
            etFullFormDetails.setVisibility(View.GONE);
            // Change the ImageButton's background with the original image: edit.
            ibEdit.setImageResource(R.drawable.edit);
            // Get the text from EditText for Full Form
            String fullFormEdited = etFullFormDetails.getText().toString();

            // Update the DatabaseAdapter with the edited Full Form
            ShowTerm.databaseAdapter.updateTermFullForm(termSelected.getId(), fullFormEdited);

            // Set the TextView for Full Form with the String received from EditText for Full Form
            tvFullFormDetails.setText(fullFormEdited);

            // Create a new Term object with edited Full Form
            Term termItem = new Term(termSelected.getId(), termSelected.getTerm(), fullFormEdited);

            // Update the ArrayList of Terms with the newly created Term object in the same position
            ShowTerm.termsList.set(itemPosition, termItem);

            // Notify RecyclerView Adapter about the data change
            ShowTerm.rvTerms.getAdapter().notifyDataSetChanged();
        }
    }

    public void delete(View view) {
        // We'll use Android AlertDialog to ask the user about his/her choice to continue or discontinue the delete operation.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the title that appears in the dialog
        builder.setTitle("Delete Entry");

        // Set the message to be displayed in the alert dialog
        builder.setMessage("Are you sure you want to delete " + termSelected.getTerm() + " from Database?");

        // You can set the property that the dialog can be cancelled or not
        builder.setCancelable(true);

        // Set the positive (yes) or negative (no) buttons using the object of the AlertDialog.Builder object: builder.
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int Did) {
                        // Call a method in DatabaseAdapter and pass the Id for deletion
                        ShowTerm.databaseAdapter.deleteData(termSelected.getId());

                        // Remove the current Term object from ArrayList: termsList
                        ShowTerm.termsList.remove(itemPosition);

                        // Notify RecyclerView Adapter about the data change
                        ShowTerm.rvTerms.getAdapter().notifyDataSetChanged();

                        // Finish the current Activity
                        finish();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Simply close the dialog if No button is pressed by the user.
                        dialog.cancel();
                    }
                });

        // Create and show the alert dialog on the screen
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
