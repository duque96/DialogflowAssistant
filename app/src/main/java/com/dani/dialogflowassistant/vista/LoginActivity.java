package com.dani.dialogflowassistant.vista;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dani.dialogflowassistant.R;
import com.dani.dialogflowassistant.logica.async.AsyncLogin;
import com.dani.dialogflowassistant.vista.bottomsheet.DisableBottomSheet;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private DisableBottomSheet disableBottomSheet;
    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = this.getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);

        if (prefs.contains("userId")) {
            startMainActivity();
        }

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.lightTransparent));

        editTextUsername = findViewById(R.id.EditTextUsername);
        editTextPassword = findViewById(R.id.EditTextPassword);

        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && disableBottomSheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            disableBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                            changeBottomSheetBackground();
                        }
                    });
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && disableBottomSheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            disableBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                            changeBottomSheetBackground();
                        }
                    });
                }
            }
        });

        //BottomSheetBehaviour
        disableBottomSheet =
                (DisableBottomSheet) DisableBottomSheet.from(findViewById(R.id.bottom_sheet_login));
        disableBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_EXPANDED && !disableBottomSheet.isDisabled()) {
                    disableBottomSheet.setDisabled();
                    changeBottomSheetBackground();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        slideToTop(findViewById(R.id.coordinatorLayoutLogin));
    }

    private void changeBottomSheetBackground() {
        View v = findViewById(R.id.bottom_sheet_login);
        v.setBackground(null);
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    public void slideToTop(View view) {

        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY",
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                        getResources().getDisplayMetrics()));
        animation.setDuration(500);
        animation.start();
    }

    public void login(View view) {
        boolean usernameCheck = isEmpty(editTextUsername);
        boolean passwordCheck = isEmpty(editTextPassword);
        if (!usernameCheck && !passwordCheck) {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            new AsyncLogin(this).execute(username, password);
        }
    }

    private boolean isEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.setError("El campo está vacío");
            return true;
        } else {
            editText.setError(null);
            return false;
        }
    }

    public void saveUserId(StringBuilder sb) {
        SharedPreferences prefs = this.getSharedPreferences(
                getPackageName(), Context.MODE_PRIVATE);

        prefs.edit().putString("userId", sb.toString()).apply();
        startMainActivity();
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
