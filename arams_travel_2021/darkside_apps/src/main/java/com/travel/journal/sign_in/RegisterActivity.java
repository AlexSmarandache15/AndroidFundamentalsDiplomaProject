package com.travel.journal.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.travel.journal.ApplicationController;
import com.travel.journal.HomeActivity;
import com.travel.journal.R;
import com.travel.journal.room.User;
import com.travel.journal.room.UserDataBase;

/**
 * Activity for user registration.
 *
 * @author  Alex_Smarandache
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     * The current local user.
     */
    private final LocalUser user = new LocalUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        final UserDataBase userDataBase = Room.databaseBuilder(this,
                UserDataBase.class, ApplicationController.USERS_DB_NAME).allowMainThreadQueries().build();

        user.dao                  = userDataBase.getUserDao();
        user.name                 = findViewById(R.id.text_field_name_value);
        user.email                = findViewById(R.id.text_field_email_value);
        user.password             = findViewById(R.id.text_field_password_value);
        user.passwordVerification = findViewById(R.id.text_field_password_verification_value);

        user.emailLayout                = findViewById(R.id.text_field_email);
        user.passwordLayout             = findViewById(R.id.text_field_password);
        user.passwordVerificationLayout = findViewById(R.id.text_field_password_verification);

        user.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!user.email.getText().toString().isEmpty()) user.emailLayout.setError(null);
                else user.emailLayout.setError(getString(R.string.error_required));

                if (!TextUtils.isEmpty(s) && !Patterns.EMAIL_ADDRESS.matcher(s).matches())
                    user.emailLayout.setError(getString(R.string.error_email_pattern));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });

        user.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!user.password.getText().toString().isEmpty()) {
                    boolean isValid = !user.passwordVerification.getText().toString().isEmpty()
                            && !user.passwordVerification.getText()
                            .toString().equals(user.password.getText().toString());
                    user.passwordVerificationLayout.setError(isValid ?
                            getString(R.string.error_password_verification) : null);

                    user.passwordLayout.setError(null);
                } else {
                    user.passwordLayout.setError(getString(R.string.error_required));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });

        user.passwordVerification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!user.passwordVerification.getText().toString().isEmpty()) {
                    user.passwordVerificationLayout.setError(null);
                }

                if (!user.passwordVerification.getText().toString().equals(user.password.getText().toString())) {
                    user.passwordVerificationLayout.setError(getString(R.string.error_password_verification));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });

    }

    /**
     * Used to apen the login activity.
     *
     * @param view The view.
     */
    public void openLoginActivity(View view) {
        startActivity(new Intent(view.getContext(), LoginActivity.class));
    }

    /**
     * Used to create a new account
     *
     * @param view The view.
     */
    public void createAccount(View view) {
        final String name = user.name.getText().toString().trim();
        final String email = user.email.getText().toString().trim();
        final String password = user.password.getText().toString().trim();
        final String passwordVerification = user.passwordVerification.toString().trim();

        if (!name.isEmpty()
                && !email.isEmpty()
                && !password.isEmpty()
                && !passwordVerification.isEmpty()) {
            final User existingUser = user.dao.getUserByEmail(email);

            if (existingUser != null) {
                Snackbar.make(view, getString(R.string.email_already_exist), BaseTransientBottomBar.LENGTH_SHORT).show();
            } else {
                if (!user.passwordVerification.getText().toString().equals(user.password.getText().toString())) {
                    Snackbar.make(view, R.string.error_form_generic, BaseTransientBottomBar.LENGTH_SHORT).show();
                } else {
                    final User newUser = new User(name, email, password);
                    final long newUserId = user.dao.insert(newUser);
                    newUser.setId(newUserId);

                    ApplicationController.setLoggedInUser(newUser, view.getContext());

                    Intent intent = new Intent(view.getContext(), HomeActivity.class);
                    startActivity(intent);
                }
            }
        } else {
            Snackbar.make(view, R.string.error_unfilled, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }
}