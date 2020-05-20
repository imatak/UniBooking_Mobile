package unipu.hr.unibooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    EditText email_reg;
    EditText password_reg;
    EditText password_reg2;
    Button signup_reg;
    ProgressBar progressBar_reg;
    Toolbar toolbar_reg;
    Spinner fakultet;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        toolbar_reg = findViewById(R.id.toolbar_reg);
        email_reg = findViewById(R.id.etEmailRegistration);
        password_reg = findViewById(R.id.etPasswordRegistration);
        signup_reg = findViewById(R.id.btnSignUpRegistration);
        progressBar_reg = findViewById(R.id.progressBar_reg);
        password_reg2 = findViewById(R.id.etPasswordRegistration2);
        fakultet = findViewById(R.id.fakultetSpinnerR);

        DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference().child("Studenti");
        Student student = new Student();


        List<String> categoriesFakultet = new ArrayList<String>();

        categoriesFakultet.add("FET");
        categoriesFakultet.add("FOOZ");
        categoriesFakultet.add("FFPU");
        categoriesFakultet.add("FITIKS");
        categoriesFakultet.add("FIPU");
        categoriesFakultet.add("MAPU");
        categoriesFakultet.add("MFPU");
        categoriesFakultet.add("OPZS");
        categoriesFakultet.add("OTS");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesFakultet);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        fakultet.setAdapter(dataAdapter);

        fakultet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                // On selecting a spinner item
                student.setFakultet(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        signup_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_reg.getText().toString().isEmpty() || password_reg.getText().toString().isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Sva polja moraju biti popunjena!"
                            , Toast.LENGTH_LONG).show();
                } else{
                    if (password_reg.getText().toString().equals(password_reg2.getText().toString())) {
                        progressBar_reg.setVisibility(View.VISIBLE);
                        firebaseAuth.createUserWithEmailAndPassword(email_reg.getText().toString(), password_reg.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                        if(firebaseUser != null){
                                            String key = firebaseUser.getUid();
                                            student.setUserID(key);
                                            student.setEmail(email_reg.getText().toString());
                                            student.setLozinka(password_reg.getText().toString());
                                            reff.child(key).setValue(student);
                                            //reff.push().setValue(rezervacija);
                                        }

                                        progressBar_reg.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegistrationActivity.this, "Registracija uspješna!"
                                                    , Toast.LENGTH_LONG).show();

                                            String email = email_reg.getText().toString();
                                            email_reg.setText("");
                                            password_reg.setText("");

                                            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@unipu.hr");
                                            Matcher mat = pattern.matcher(email);

                                            if(mat.matches()){
                                                startActivity(new Intent(RegistrationActivity.this, RadnikDashboardActivity.class));
                                            }else{
                                                startActivity(new Intent(RegistrationActivity.this, ProfileActivity.class));
                                            }
                                        } else {
                                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage()
                                                    , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Lozinke ne odgovaraju! Pokušajte ponovno."
                                , Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
