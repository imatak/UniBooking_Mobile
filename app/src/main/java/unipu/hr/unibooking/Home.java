package unipu.hr.unibooking;

import android.app.Application;
import android.content.Intent;
import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Home extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            //startActivity(new Intent(Home.this, ProfileActivity.class));
            String email = firebaseUser.getEmail();
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@unipu.hr");
            Matcher mat = pattern.matcher(email);

            if(mat.matches()){
                Intent intent = new Intent(Home.this, RadnikDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                Intent intent = new Intent(Home.this, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
