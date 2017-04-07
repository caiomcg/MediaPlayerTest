package com.caiomcg.testplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.editText);
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Invalid path", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    startActivity(new Intent(getApplicationContext(), PlayerActivity.class).putExtra("url", editText.getText().toString()));
                }
            }
        });
    }
}
