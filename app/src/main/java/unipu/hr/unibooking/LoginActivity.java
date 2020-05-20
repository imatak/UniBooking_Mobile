package unipu.hr.unibooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    EditText userEmail;
    EditText userPass;
    Button userLogin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbar_user);
        progressBar = findViewById(R.id.progressBar_user);
        userEmail = findViewById(R.id.etUserEmail);
        userPass = findViewById(R.id.etUserPassword);
        userLogin = findViewById(R.id.btnUserLogin);

        firebaseAuth = firebaseAuth.getInstance();

        userLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view) {
                if (userEmail.getText().toString().isEmpty() || userPass.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Sva polja moraju biti popunjena!"
                            , Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        String email = userEmail.getText().toString();
                                        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@unipu.hr");
                                        Matcher mat = pattern.matcher(email);

                                        if(mat.matches()){
                                            startActivity(new Intent(LoginActivity.this, RadnikDashboardActivity.class));
                                        }else{
                                            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage()
                                                , Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
