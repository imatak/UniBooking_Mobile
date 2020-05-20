package unipu.hr.unibooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private DrawerLayout drawer;

    CalendarView kalendar;
    Spinner termin;
    Spinner razlog;
    TextView userEmailSpremi;
    EditText userText;
    Button Spremi;
    Toolbar toolbar;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reff;
    Rezervacija rezervacija;

    private Button buttonRezerviraj;
    private Button buttonMojeRezervacije;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonRezerviraj =findViewById(R.id.buttonRezerviraj);
        buttonRezerviraj.setOnClickListener(this);
        buttonMojeRezervacije =findViewById(R.id.buttonMojeRezervacije);
        buttonMojeRezervacije.setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rezervacija = new Rezervacija();
        reff = FirebaseDatabase.getInstance().getReference().child("Rezervacije");


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //za navbar otvaranje zatvaranje
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //za ispis liste
        ListView mListView = (ListView)findViewById(R.id.listViewDashboard);
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy");
        ArrayList<MojeRezervacijeStudent> ListaMojihRezervacija = new ArrayList<>();
        MojeRezervacijeListAdapter adapter =new MojeRezervacijeListAdapter(this, R.layout.adapterviewlayout, ListaMojihRezervacija);
        mListView.setAdapter(adapter);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, -1);
        c.set(Calendar.MINUTE, -1);
        c.set(Calendar.SECOND, -1);
        c.set(Calendar.MILLISECOND, -1);
        Date now = c.getTime();

        FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        c.set(Calendar.MONTH, 11);
                        c.set(Calendar.DATE, 05);
                        c.set(Calendar.YEAR, 2070);
                        Date d1 = c.getTime();
                        Date d2 = c.getTime();
                        Date d3 = c.getTime();

                        MojeRezervacijeStudent mrs1 = new MojeRezervacijeStudent("","","","", "", "", "", "", "");
                        MojeRezervacijeStudent mrs2 = new MojeRezervacijeStudent("","","","", "", "", "", "", "");
                        MojeRezervacijeStudent mrs3 = new MojeRezervacijeStudent("","","","", "", "", "", "", "");

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Rezervacija a = snapshot.getValue(Rezervacija.class);


                            if (a.getEmailUsera().equals(firebaseUser.getEmail())) {


                                String NDatum = a.getDatum();
                                String NRazlog = a.getRazlog();
                                String NTermin = a.getTermin();
                                String NStatus = a.getStatus();
                                String NID = a.getID();
                                String NEmail = a.getEmailUsera();
                                String NFakultet = a.getFakultet();
                                String NKomentar = a.getKomentar();
                                String NUredio = a.getUredio();

                                try {
                                    Date d=dateFormat.parse(NDatum);
                                    if (d.after(now)) {
                                        if (d.before(d1)) {
                                            d3=d2;
                                            d2=d1;
                                            d1=d;
                                            mrs3=mrs2;
                                            mrs2=mrs1;
                                            mrs1 = new MojeRezervacijeStudent(NDatum, NTermin, NStatus, NRazlog, NID, NEmail, NFakultet, NKomentar, NUredio);

                                        } else if (d.before(d2)) {
                                            d3 = d2;
                                            d2 = d;
                                            mrs3 = mrs2;
                                            mrs2 = new MojeRezervacijeStudent(NDatum, NTermin, NStatus, NRazlog, NID, NEmail, NFakultet, NKomentar, NUredio);

                                        } else if (d.before(d3)) {
                                            d3=d;
                                            mrs3 = new MojeRezervacijeStudent(NDatum, NTermin, NStatus, NRazlog, NID, NEmail, NFakultet, NKomentar, NUredio);

                                        }
                                    }

                                }
                                catch(Exception e) {
                                    //java.text.ParseException: Unparseable date: Geting error
                                    System.out.println("Excep"+e);
                                }

                            }

                            //User user = snapshot.getValue(User.class);
                            //System.out.println(user.email);
                        }

                        if (!mrs1.getDatum().equals("")){
                            ListaMojihRezervacija.add(mrs1);
                        }
                        if (!mrs2.getDatum().equals("")){
                            ListaMojihRezervacija.add(mrs2);
                        }
                        if (!mrs3.getDatum().equals("")){
                            ListaMojihRezervacija.add(mrs3);
                        }

                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MojeRezervacijeStudent value = (MojeRezervacijeStudent) adapter.getItem(i);
                //value.getDatum();
                Intent intent = new Intent(ProfileActivity.this, EditRezervacijaActivity.class);
                intent.putExtra("Uredi",value);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.buttonRezerviraj:
                Intent intent = new Intent(ProfileActivity.this, RezervirajActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonMojeRezervacije:
                intent = new Intent(ProfileActivity.this, MojeRezervacijeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.glavniizbornik:
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.mojerezervacije:
                intent = new Intent(ProfileActivity.this, MojeRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.rezerviraj:
                intent = new Intent(ProfileActivity.this, RezervirajActivity.class);
                startActivity(intent);
                break;
            case R.id.proslerezervacije:
                intent = new Intent(ProfileActivity.this, ProsleRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.postavke:
                intent = new Intent(ProfileActivity.this, PostavkeActivity.class);
                startActivity(intent);
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

}
