package com.example.walkin_clinic_ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.security.MessageDigest;

public class CreateAccountActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.accounts,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void onCreateAccount(View view){

        /* Pulls all needed EditTexts. */
        EditText userText = (EditText)findViewById(R.id.fieldUsernameCreate);
        EditText passwordText = (EditText)findViewById(R.id.fieldPasswordCreate);
        EditText passwordText2 = (EditText)findViewById(R.id.fieldPasswordCreate2);
        EditText emailText = (EditText)findViewById(R.id.fieldEmailCreate);

        /* Retrieved strings used for validation. */
        String username = userText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordConfirm = passwordText2.getText().toString();
        String email = emailText.getText().toString();

        /* Resets the hints after we try new input. Some redundancy, feel free to make a better solution. */
        userText.setHint(getResources().getString(R.string.createUsernameHint));
        userText.setHintTextColor(getResources().getColor(R.color.hintNeutral));
        passwordText.setHint(getResources().getString(R.string.createPasswordHint));
        passwordText.setHintTextColor(getResources().getColor(R.color.hintNeutral));
        passwordText2.setHint(getResources().getString(R.string.createPassword2Hint));
        passwordText2.setHintTextColor(getResources().getColor(R.color.hintNeutral));
        emailText.setHint(getResources().getString(R.string.createEmailHint));
        emailText.setHintTextColor(getResources().getColor(R.color.hintNeutral));

        /* Checks for blank input in either username or password fields, and returns appropriate errors. */
        if(username.length() == 0 || password.length() == 0 || passwordConfirm.length() == 0 || email.length() == 0)
        {
            fieldClear(passwordText);
            fieldClear(passwordText2);

            if(username.length() == 0)
            {
                userText.setHint("Username cannot be blank.");
                userText.setHintTextColor(getResources().getColor(R.color.hintFail));
            }

            if(password.length() == 0 && passwordConfirm.length() == 0)
            {
                passwordText.setHint("Password cannot be blank.");
                passwordText.setHintTextColor(getResources().getColor(R.color.hintFail));
            }

            else if(password.equals(passwordConfirm) == false)
            {
                passwordText.setHint("Passwords do not match.");
                passwordText.setHintTextColor(getResources().getColor(R.color.hintFail));
            }

            if(email.length() == 0)
            {
                emailText.setHint("Email address cannot be blank.");
                emailText.setHintTextColor(getResources().getColor(R.color.hintFail));
            }

            return;
        }

        /* Needed because we want to keep validating client-side even if some fields fail, but not
         * continue with the server validation. */
        boolean validateFlag = true;

        /* Calls the client-side user validation method. */
        boolean validUser = fieldValidate.usernameValidate(username);

        /* No sense validating the password if the username fails */
        if(!validUser)
        {
            fieldClear(userText);
            fieldClear(passwordText);
            userText.setHint("Invalid username.");
            userText.setHintTextColor(getResources().getColor(R.color.hintFail));
            validateFlag = false;
        }

        else
        {
            /* Calls the client-side password validation method. */
            boolean validPassword = fieldValidate.passwordValidate(password);

            /* Assume the username is valid if it met our client-side check. The user will find out it
             * isn't once he types in his password correctly. */
            if(!validPassword)
            {
                fieldClear(passwordText);
                fieldClear(passwordText2);
                passwordText.setHint("Incorrect Password.");
                passwordText.setHintTextColor(getResources().getColor(R.color.hintFail));
                validateFlag = false;
            }
        }

        boolean validEmail = fieldValidate.emailValidate(email);

        if(!validEmail)
        {
            fieldClear(emailText);
            emailText.setHint("Invalid Email Address.");
            emailText.setHintTextColor(getResources().getColor(R.color.hintFail));
            validateFlag = false;
        }

        if(!validateFlag)
            return;

        // If we've gotten this far, we can run server side validation.

        /* This section hashes the password and converts its output to a string. */
        try {

            MessageDigest chomper = MessageDigest.getInstance("SHA-256");
            chomper.update(password.getBytes());

            byte[] passwordBytes = chomper.digest(); // Digest outputs hash as byte array.

            /* Convert bytes to a string so we can prepend the variable byte salt to it. */
            StringBuilder byteConvert = new StringBuilder();
            for (byte b : passwordBytes) {
                byteConvert.append(String.format("%02x", b));
            }

            /* Variables containing unhashed password/intermediaries are nulled so garbage collection
            will remove all instances of the password other than the hash string. */
            password = byteConvert.toString();
            passwordBytes = null;
            chomper = null;
            byteConvert = null;


        } catch(Exception e){ // I'm not sure how we should handle hashing problems.

            fieldClear(passwordText);
            passwordText.setHint("Error Processing Password. Please Contact Technical Support.");
            passwordText.setHintTextColor(getResources().getColor(R.color.hintFail));
        }

        /**
         * TODO
         *
         * Send user account to server to check if it's already in use
         *
         * Send email to server to check if it's already in use.
         *
         * Send salted hash to server.
         *
         * Better error handling for hashing issues.
         */


        /*
         * IF ALL FIELDS ARE VALID, CREATE THE USER AND RETURN TO LOGIN SCREEN
         */

        //Intent returnIntent = new Intent();

        //setResult(RESULT_OK, returnIntent);

        //finish();
    }

    /**
     * Clears the field of the provided EditText.
     */
    public void fieldClear(EditText e)
    {
        e.setText("");
    }

}
