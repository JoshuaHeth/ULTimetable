package com.mycompany.ultimetable;

//import android.support.v4.view.ViewPager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
//import android.widget.TextView;
import android.widget.EditText;
//import android.content.Intent;
//import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
//import android.view.ViewGroup;
//import android.view.Menu;
//import android.view.MenuItem;


/**
 * Created by Josh on 22/02/2015.
 */
public class Login extends ActionBarActivity {

    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        input = (EditText)findViewById(R.id.idInput);
        loginBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        final String userID = input.getText().toString();

                        if(userID.length() == 0 || userID.length() != 8){
                            //using R.string.thestringname references it from the strings.xml so no hard-coding text
                            Toast.makeText(Login.this, R.string.login_error,Toast.LENGTH_LONG).show();
                            //Add further validation for student ID
                            /*First 2 digits = Year of application
                              Next 5 digits = Sequential number
                              Last digit = A MOD 11 check digit*/
                        }
                        else {

                            //Stores the Student ID (userID) into shared preferences
                            SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("studentID", userID);
                            editor.apply();

                            Intent intent = new Intent(Login.this, DownloadData.class);
                            //No need to pass student ID to the intent as it's stored in shared preferences(see line 42 in DownloadData to know how to access)
                            //intent.putExtra("STUDENT_ID", "13029096");
                            startService(intent);

                            //Need a check/splash screen to know when service is complete before loading MainActivity for the first time

                            Intent secondIntent = new Intent(Login.this, MainActivity.class);
                            startActivity(secondIntent);

                            /*Intent intent = new Intent(Login.this, DownloadData.class);
                            //No need to pass student ID to the intent as it's stored in shared preferences
                            intent.putExtra("STUDENT_ID", "13029096");
                            startService(intent);*/

                        }
                    }
                }
        );
    }
}
