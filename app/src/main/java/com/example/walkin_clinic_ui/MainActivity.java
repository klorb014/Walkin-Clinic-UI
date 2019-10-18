package com.example.walkin_clinic_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.accounts,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void OnCreateAccountButton(View view){
        Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
        startActivityForResult (intent,0);
    }


    /**
     * This method gets called when the login button gets clicked. Does all possible client side
     * validation, then sends the information to the server for validation.
     */
    public void onClickLogin(View view)
    {
        EditText userText = (EditText)findViewById(R.id.fieldUsernameLogin);
        EditText passwordText = (EditText)findViewById(R.id.fieldPasswordLogin);

        String username = userText.getText().toString();
        String password = passwordText.getText().toString();

        /* Resets the hints after we try new input. Some redundancy, feel free to make a better solution. */
        userText.setHint(getResources().getString(R.string.loginUsernameHint));
        userText.setHintTextColor(getResources().getColor(R.color.hintNeutral));
        passwordText.setHint(getResources().getString(R.string.loginPasswordHint));
        passwordText.setHintTextColor(getResources().getColor(R.color.hintNeutral));

        /* Checks for blank input in either username or password fields, and returns appropriate errors. */
        if(username.length() == 0 || password.length() == 0)
        {
            if(username.length() == 0)
            {
                userText.setHint("Username cannot be blank.");
                userText.setHintTextColor(getResources().getColor(R.color.hintFail));
                fieldClear(passwordText);
            }

            if(password.length() == 0)
            {
                passwordText.setHint("Password cannot be blank.");
                passwordText.setHintTextColor(getResources().getColor(R.color.hintFail));
            }

            return;
        }

        /* Calls the client-side user validation method. */
        boolean validUser = fieldValidate.usernameValidate(username);

        /* No sense validating the password if the username fails */
        if(!validUser)
        {
            fieldClear(userText);
            fieldClear(passwordText);
            userText.setHint("Invalid username.");
            userText.setHintTextColor(getResources().getColor(R.color.hintFail));
            return;
        }

        /* Calls the client-side password validation method. */
        boolean validPassword = fieldValidate.passwordValidate(password);

        /* Assume the username is valid if it met our client-side check. The user will find out it
         * isn't once he types in his password correctly. */
        if(!validPassword)
        {
            fieldClear(passwordText);
            passwordText.setHint("Incorrect Password.");
            passwordText.setHintTextColor(getResources().getColor(R.color.hintFail));
            return;
        }

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
         * Send user string to server for validation
         *
         * Receive variable byte salt from server and prepend it to hash before sending it out.
         *
         * Send salted hash to server.
         *
         * Better error handling for hashing issues.
         */


        /**
         * IF THE LOGIN CREDENTIALS ARE VALID, LAUNCH THE WELCOME SCREEN
         */
        //Intent intent = new Intent(getApplicationContext(), WelcomeScreenActivity.class);
        //startActivityForResult (intent,0);



    }

    /**
     * Clears the field of the provided EditText.
     */
    public void fieldClear(EditText e)
    {
        e.setText("");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
