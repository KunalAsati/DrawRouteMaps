package com.ahmadrosid.drawroutemaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onDone(View view) {

        Intent i = new Intent(this, MapsActivity.class);

        EditText mainTextInput = (EditText) findViewById(R.id.editText3);
        String srcmsg = mainTextInput.getText().toString();
        i.putExtra("src", srcmsg);
        startActivity(i);
    }

    public void onMy(View view) {

        Intent i = new Intent(this, MapsActivity.class);
        String srcmsg = "MyLocation";
        i.putExtra("src", srcmsg);
        startActivity(i);
    }
}