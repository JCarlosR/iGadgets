package com.programacionymas.igadgets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSeeOffers = (Button) findViewById(R.id.btnSeeOffers);
        TextView tvSkipOffers = (TextView) findViewById(R.id.tvSkipOffers);

        btnSeeOffers.setOnClickListener(this);
        tvSkipOffers.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSeeOffers:
                Toast.makeText(this, "Esta opción se encuentra aún en desarrollo", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tvSkipOffers:
                Intent i = new Intent(this, MenuActivity.class);
                startActivity(i);
                break;
        }
    }

}
