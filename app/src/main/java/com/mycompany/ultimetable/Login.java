package com.mycompany.ultimetable;

//import android.support.v4.view.ViewPager;
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
 * Created by Loaner on 22/02/2015.
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
                            Toast.makeText(Login.this,"Must enter a valid student ID",Toast.LENGTH_LONG).show();
                            //Add further validation for student ID
                        }
                        else {
//                            Intent secondIntent = new Intent(Login.this, MainActivity.class);
//                            startActivity(secondIntent);
//                            //Login.this.finish();
                            Intent intent = new Intent(Login.this, DownloadData.class);
                            intent.putExtra("STUDENT_ID", "13029096");
                            startService(intent);

                        }
                    }
                }
        );
    }

}
