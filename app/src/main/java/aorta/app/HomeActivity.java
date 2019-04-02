package aorta.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import aorta.barcode.BarcodeCaptureActivity;

public class HomeActivity extends AppCompatActivity {
    Button button_logout;
    Button button_scan_location;
    TextView mResultTextView;
    private int BARCODE_READER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mResultTextView = findViewById(R.id.result_view);

        button_logout = (Button) findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToMainActivity();
                } catch (Exception e) {

                }
            }
        });

        button_scan_location = (Button) findViewById(R.id.button_locatie);
        button_scan_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToScanActivity();
                } catch (Exception e) {

                }
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToScanActivity() {
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
                if (data != null) {
                    String barcode = "test";
                    mResultTextView.setText(barcode);
                } else
                    mResultTextView.setText("No barcode captured");

        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
