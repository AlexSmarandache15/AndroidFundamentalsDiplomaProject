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

import com.travel.journal.ApplicationController;
import com.travel.journal.HomeActivity;
import com.travel.journal.R;
import com.travel.journal.room.User;
import com.travel.journal.room.UserDataBase;

/**
 * The log in activity.
 *
 * @author Alex_Smarandache
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Used to store the user information.
     */
    private final LocalUser user = new LocalUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        final UserDataBase userDataBase = Room.databaseBuilder(this,
                UserDataBase.class, ApplicationController.USERS_DB_NAME).allowMainThreadQueries().build();

        user.dao            = userDataBase.getUserDao();
        user.email          = findViewById(R.id.text_field_email_value);
        user.password       = findViewById(R.id.text_field_password_value);
        user.emailLayout    = findViewById(R.id.text_field_email);
        user.passwordLayout = findViewById(R.id.text_field_password);

        user.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.emailLayout.setError(!user.email.getText().toString().isEmpty() ?
                        null : getString(R.string.error_required));

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
                user.passwordLayout.setError(!user.password.getText().toString().isEmpty() ?
                        null : getString(R.string.error_required));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });
    }

    /**
     * Open the register activity.
     *
     * @param view The view.
     */
    public void openRegisterActivity(View view) {
        startActivity(new Intent(view.getContext(), RegisterActivity.class));
    }

    /**
     * Start the login.
     *
     * @param view The view.
     */
    public void startLogin(View view) {
        final String email = user.email.getText().toString().trim();
        final String password = user.password.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            final User concreteUser = user.dao.getUser(email, password);

            if (concreteUser != null) {
                ApplicationController.setLoggedInUser(concreteUser, view.getContext());
                startActivity(new Intent(view.getContext(), HomeActivity.class));
            } else {
                user.emailLayout.setError(getString(R.string.error_wrong_credentials));
            }

        } else {
            user.emailLayout.setError(email.isEmpty() ? getString(R.string.error_required) : null);
            user.passwordLayout.setError(password.isEmpty() ? getString(R.string.error_required) : null);
        }
    }
}