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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostavkeActivity extends AppCompatActivity {
    EditText email_reg;
    EditText password_reg;
    EditText password_reg2;
    Button signup_reg;
    Button update_password;
    ProgressBar progressBar_reg;
    Toolbar toolbar_reg;
    Spinner fakultet;
    String lozinka;
    String postaviSpinner;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavke);

        toolbar_reg = findViewById(R.id.toolbar_reg);
        email_reg = findViewById(R.id.etEmailRegistration);
        password_reg = findViewById(R.id.etPasswordRegistration);
        signup_reg = findViewById(R.id.btnSignUpRegistration);
        update_password = findViewById(R.id.btnSavePassword);
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

        FirebaseDatabase.getInstance().getReference().child("Studenti")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Student a = snapshot.getValue(Student.class);


                            if (a.getUserID().equals(firebaseUser.getUid())) {
                                postaviSpinner = a.getFakultet();
                                int spinnerPositionFakultet = dataAdapter.getPosition(postaviSpinner);
                                fakultet.setSelection(spinnerPositionFakultet);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

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


        signup_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // petlja za pretraživanje rezervacija za trenutno prijavljenog korisnika
                FirebaseDatabase.getInstance().getReference().child("Studenti")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Student a = snapshot.getValue(Student.class);


                                    if (a.getUserID().equals(firebaseUser.getUid())) {
                                        reff.child(a.getUserID()).child("fakultet").setValue(student.getFakultet());
                                    }
                                }

                                Intent intent = new Intent(PostavkeActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                Toast.makeText(PostavkeActivity.this, "Fakultet je uspješno ažuriran!"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }
        });

        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Studenti")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Student a = snapshot.getValue(Student.class);

                                    if (a.getUserID().equals(firebaseUser.getUid())) {
                                        lozinka = a.getLozinka();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                if(email_reg.getText().toString().isEmpty() || password_reg.getText().toString().isEmpty()){
                    Toast.makeText(PostavkeActivity.this, "Sva polja moraju biti popunjena!"
                            , Toast.LENGTH_LONG).show();
                } else{
                    if (password_reg.getText().toString().equals(password_reg2.getText().toString()) && email_reg.getText().toString().equals(lozinka) ) {
                        progressBar_reg.setVisibility(View.VISIBLE);
                        String newPassword = password_reg.getText().toString();
                        firebaseUser.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            FirebaseDatabase.getInstance().getReference().child("Studenti")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                Student a = snapshot.getValue(Student.class);


                                                                if (a.getUserID().equals(firebaseUser.getUid())) {
                                                                    reff.child(a.getUserID()).child("lozinka").setValue(newPassword);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });

                                            progressBar_reg.setVisibility(View.GONE);
                                            Toast.makeText(PostavkeActivity.this, "Lozinka je uspješno ažurirana!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(PostavkeActivity.this, ProfileActivity.class));
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(PostavkeActivity.this, "Lozinke ne odgovaraju! Pokušajte ponovno.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
