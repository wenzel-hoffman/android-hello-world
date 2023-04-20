package com.example.dummy.helloworld;

import android.Manifest;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;


public class HelloActivity extends AppCompatActivity {

    private EditText testEditText = null;
    private TextView testTextView = null;
    private Button testButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_screen);

        this.testEditText = (EditText)findViewById(R.id.testEditText);
        this.testTextView = (TextView)findViewById(R.id.testTextView);
        this.testButton = (Button)findViewById(R.id.testButton);

        EditText editText = this.testEditText;
        TextView textView = this.testTextView;

        this.testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Text from the input:\n" + editText.getText());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("input", this.testEditText.getText().toString());
        savedInstanceState.putString("text", this.testTextView.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String input = savedInstanceState.getString("input");
        String text = savedInstanceState.getString("text");
        if (input != null) this.testEditText.setText(input);
        if (text != null) this.testTextView.setText(text);
    }

}
