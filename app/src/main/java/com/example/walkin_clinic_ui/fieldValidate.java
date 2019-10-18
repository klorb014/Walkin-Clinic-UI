package com.example.walkin_clinic_ui;

import java.util.regex.Pattern;

/**
 * This class is used by both the MainActivity and the CreateAccountActivity to validate fields.
 */
public class fieldValidate {

    /* The password constraints */
    private static int numPasswordCharacters = 10;
    private static boolean requirePasswordLowerCase = true;
    private static boolean requirePasswordUpperCase = true;
    private static boolean requirePasswordNumbers = true;
    private static boolean requirePasswordSymbols = false;

    /* Username must be between 1 and 16 characters, and contain only letters, numbers, dashes, and
     * underscores */
    private static Pattern usernameRegex = Pattern.compile("^[a-zA-Z0-9_-]{1,16}$");

    //Uses OWASP Validation Regex Repository.
    private static Pattern emailRegex = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    /**
     * This method reduces communication with the server by ensuring the entered username meets the
     * program's username constraints.
     *
     * @param input The username to be tested.
     * @return true if valid, false if not.
     */
    public static boolean usernameValidate(String input)
    {
        if(input == null)
            return false;

        return usernameRegex.matcher(input).matches();
    }

    /**
     * This method reduces communication with the server by validating part of the user's password
     * client side. It checks to see if the password meets the set password constraints, which is
     * necessary for any valid password.
     *
     * Note: Constraints are currently hardcoded into this class. I see no downside to this.
     *
     * @param input The password string to be tested.
     * @return true if valid password, false if invalid.
     */
    public static boolean passwordValidate(String input)
    {
        if(input == null)
            return false;

        int inputLength = input.length();

        if(inputLength < numPasswordCharacters)
            return false;

        boolean alphaLower = false;
        boolean alphaHigher = false;
        boolean number = false;
        boolean symbol = false;

        for(int i = 0; i < inputLength; i++)
        {
            char x = input.charAt(i);

            if(Character.isLetter(x))
            {
                if(Character.isUpperCase(x))
                    alphaHigher = true;

                else
                    alphaLower = true;

                continue;
            }

            else if(Character.isDigit(x))
            {
                number = true;
                continue;
            }

            else // Assume everything else is a symbol.
            {
                symbol = true;
            }
        }


        /* Determines if password conditions are not met. Kind of hacky, feel free to implement something better. */
        if(requirePasswordLowerCase & alphaLower ^ requirePasswordLowerCase | requirePasswordUpperCase & alphaHigher ^ requirePasswordUpperCase |
                requirePasswordNumbers & number ^ requirePasswordNumbers | requirePasswordSymbols & symbol ^ requirePasswordSymbols)
            return false;

        return true;
    }

    public static boolean emailValidate(String input)
    {
        if (input == null)
            return false;

        boolean validEmail = emailRegex.matcher(input).matches();

        if (!validEmail)
            return false;

        //Do some stuff with the server.
        return true;
    }
}
