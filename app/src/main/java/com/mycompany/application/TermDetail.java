package com.mycompany.application;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TermDetail extends AppCompatActivity {
    // Declare the View object references
    TextView tvTermDetails, tvFullFormDetails;
    EditText etFullFormDetails;
    ImageButton ibEdit;
    // Declare a Term object reference to store the Term from Intent
    Term wordSelected;
    // Declare an integer to store itemPosition from Intent
    int itemPosition;
    // Define a flag to store edit state.
    boolean editState = false;
    

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_detail);
        
        // Get the information from Intent
        wordSelected = (Term) getIntent().getSerializableExtra("wordSelected");
        itemPosition = getIntent().getIntExtra("itemPosition", 1);
        // Get the handles for Views
        tvTermDetails = findViewById(R.id.tvTermDetails);
        tvFullFormDetails = findViewById(R.id.tvFullFormDetails);
        etFullFormDetails = findViewById(R.id.etFullFormDetails);
        ibEdit = findViewById(R.id.ibEdit);
        // Set the values of Views with values received from Intent
        tvTermDetails.setText(wordSelected.getWords());
        tvFullFormDetails.setText(wordSelected.getMeaning());
        etFullFormDetails.setText(wordSelected.getMeaning());
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
            // If edit ImageButton is clicked and editState is true, meaning user already has done editing,
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
            // Next, go to ShowTerm and change the DatabaseAdapter object to static.
            // Call updateTermFullForm() method on databaseAdapter and pass Id and fullFormEdited as parameter.
            ShowTerm.databaseAdapter.updateTermFullForm(wordSelected.getId(), fullFormEdited);
            // Set the TextView for Full Form with the String received from EditText for Full Form
            tvFullFormDetails.setText(fullFormEdited);
            // Create a new Term object with edited Full Form
            Term termItem = new Term(wordSelected.getId(), wordSelected.getWords(), fullFormEdited);
            // Go to ShowTerm and make the RecyclerView and ArrayList static so that you can access them from here.
            // Next, update the ArrayList of Terms with the newly created Term object in same position
            ShowTerm.termsList.set(itemPosition, termItem);
            // Notify RecyclerView Adapter about the data change
            ShowTerm.rvTerms.getAdapter().notifyDataSetChanged();
        }
    }

    public void delete(View view) {
        // We'll use Android AlertDialog to ask the user about his/her choice to continue or discontinue the delete operation.
        // AlertDialog can be used to display the dialog message with OK-Cancel or Yes-No buttons.
        // In order to make an alert dialog, you need to make an object of AlertDialog.Builder which an inner class of AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the title that appears in the dialog
        builder.setTitle("Delete Entry");
        // Set the message to be displayed in the alert dialog
        builder.setMessage("Are you sure you want to delete " + wordSelected.getWords() + " from Database?");
        //  You can set the property that the dialog can be cancelled or not
        builder.setCancelable(true);
        // Set the positive (yes) or negative (no) buttons using the object of the AlertDialog.Builder object: builder.
        builder.setPositiveButton("Yes",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int Did) {
                    // Here call a method in DatabaseAdapter and pass the Id for deletion
                    ShowTerm.databaseAdapter.deleteData(wordSelected.getId());
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
        // After creating and setting the dialog builder, you need to create an alert dialog by calling the create() method
        // on the builder object.
        AlertDialog alertDialog = builder.create();
        // Show the alert dialog on the screen
        alertDialog.show();
    } 
}
