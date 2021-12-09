package com.travel.journal.sign_in;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.travel.journal.room.UserDao;

/**
 * Local class for user data management.
 *
 * @author Alex_Smarandache
 */
 class LocalUser {
    /**
     * User name.
     */
    EditText name;

    /**
     * Email.
     */
    EditText email;

    /**
     * Password.
     */
    EditText password;

    /**
     * Verification password.
     */
    EditText passwordVerification;

    /**
     * Email layout.
     */
    TextInputLayout emailLayout;

    /**
     * Password layout.
     */
    TextInputLayout passwordLayout;

    /**
     * User password verification layout.
     */
    TextInputLayout passwordVerificationLayout;

    /**
     * The user dao for data access.
     */
    UserDao dao;
}