package com.example.speedometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements LocationListener {
    private static  final int REQ_CODE = 777;
    private static float speedLimit= 1;
    LocationManager locationManager;
    private FirebaseAuth mAuth;
    String userID;
    TextView textView,editTextTextEmail,editTextTextPassword,textView5speed,textView6location;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        editTextTextEmail = findViewById(R.id.editTextTextEmail);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        textView5speed = findViewById(R.id.textView5speed);
        textView6location = findViewById(R.id.textView6location);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null)
            textView.setText("No user yet...");


    }


    //Register User To Firebase
        public void signupuser(View view){
        mAuth.createUserWithEmailAndPassword(editTextTextEmail.getText().toString(),
                editTextTextPassword.getText().toString()).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUserDetails("Kostas",user);
                            textView.setText("User Created!Please Login Now");
                        } else {
                            textView.setText(task.getException().getMessage());
                        }
                    }
                });
    }

    private void addUserDetails(String displayName, FirebaseUser user){
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();
        user.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_LONG).show();
                    }
                });
    }

    //Login user to FireBase
    public void signinuser(View view){
        mAuth.signInWithEmailAndPassword(editTextTextEmail.getText().toString(),
                editTextTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        textView.setText("You have Loging to Speedometer APP!");
                        textView5speed.setVisibility(TextView.VISIBLE);
                        textView6location.setVisibility(TextView.VISIBLE);
                        Button resetButton=(Button)findViewById(R.id.startAppButn);
                        resetButton.setVisibility(View.VISIBLE); //To set the Button  visible
                        Button resetButton1=(Button)findViewById(R.id.button4);
                        resetButton1.setVisibility(View.VISIBLE); //To set the Button  visible
                        Button resetButton2=(Button)findViewById(R.id.button5);
                        resetButton2.setVisibility(View.VISIBLE); //To set the Button  visible
                        //textView.setText(user.getUid());



                        //mAuth.signOut();
                    }
                });
    }


    public void runpermission(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQ_CODE);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this );
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        textView6location.setText(String.valueOf(location.getLatitude())+ "," +String.valueOf(location.getLongitude()));
        textView5speed.setText(String.valueOf(3.6*location.getSpeed())+ " Km/h ");

        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        float speed = location.getSpeed();
        String time = String.valueOf(location.getTime());
        userID = mAuth.getUid().toString();

        UserLoginAttributes userAttributes = new UserLoginAttributes(userID,latitude,longitude,speed,time);
        if (speed > speedLimit) {
            myRef = database.getReference("Users");
            myRef.child(userID).push().setValue(userAttributes);
            Toast.makeText(this, "WARNING  SPEEDING OFFENSE", Toast.LENGTH_SHORT).show();
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        }else {
            Toast.makeText(this, "YOUR SPEED IS OK", Toast.LENGTH_SHORT).show();
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public void userStatFromFirebase (View view){
        Intent myIntent = new Intent(this, ListActivity.class);
        myIntent.putExtra("key", userID); //Optional parameters
        this.startActivity(myIntent);
    }
}