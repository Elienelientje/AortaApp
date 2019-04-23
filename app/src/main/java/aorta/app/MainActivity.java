package aorta.app;

import android.net.Credentials;
import android.net.wifi.hotspot2.pps.Credential;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button_login;
    private EditText editText_username;
    private EditText editText_password;
    private String username;
    private String password;
    private String baseUrl;
    private String token = null;
    private String tokenSchema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Replace this with your own IP address or URL.
        baseUrl = "http://10.110.162.52:8080/Project1819-G5REST/";

        editText_username = (EditText) findViewById(R.id.text_login);
        editText_password = (EditText) findViewById(R.id.text_password);

        button_login = (Button) findViewById(R.id.button_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    username = editText_username.getText().toString();
                    password = editText_password.getText().toString();

                    RESTCall loginCall =
                            new RESTCall(
                                    baseUrl
                                    , username
                                    , password
                            );
                    loginCall.setParameter("login", username);
                    loginCall.setParameter("password", password);

                    AsyncTask<Void, Void, String> execute = new ExecuteNetworkOperation(loginCall);
                    execute.execute();
                } catch (Exception ex) {

                }
            }
        });
    }

    /**
     * This subclass handles the network operations in a new thread.
     * It starts the progress bar, makes the API call, and ends the progress bar.
     */
    public class ExecuteNetworkOperation extends AsyncTask<Void, Void, String> {
        private RESTCall restCall;

        /**
         * Overload the constructor to pass objects to this class.
         */
        public ExecuteNetworkOperation(RESTCall restCall) {
            this.restCall = restCall;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Display the progress bar.
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                restCall.execute();
                String[] strList = restCall.getHeaderFields().get("Authorization").get(0).split(" ");
                token = strList[1];
                tokenSchema = strList[0];

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Hide the progress bar.
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            // Login Success
            if (token != null) {
                goToHomeActivity();
            }
            // Login Failure
            else {
                // Temporary message dat je niet inglogd bent.
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Open a new activity.
     */
    private void goToHomeActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("password", password);
        bundle.putString("token", token);
        bundle.putString("baseUrl", baseUrl);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}