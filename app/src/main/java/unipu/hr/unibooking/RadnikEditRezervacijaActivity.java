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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RadnikEditRezervacijaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    CalendarView kalendar;
    TextView termin;
    TextView razlog;
    TextView datum;
    TextView datumTXT;
    TextView userEmailSpremi;
    EditText userText;
    Button odobri;
    Toolbar toolbar;
    ProgressBar progressBar;
    String curDate;
    Button otkazi;
    TextView terminTXT;
    TextView razlogTXT;

    TextView Teksttermin;
    TextView TekstRazlog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reff;
    Rezervacija rezervacija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radnik_edit_rezervacija);
        Toolbar toolbar = findViewById(R.id.toolbarRadnikEdit);

        setSupportActionBar(toolbar);

        Intent i = getIntent();
        MojeRezervacijeStudent value = (MojeRezervacijeStudent) i.getSerializableExtra("Uredi");
        String ID_rezervacije = value.getID();

        //kalendar = findViewById(R.id.kalendarRezervacijeR);
        termin = findViewById(R.id.terminSpinnerR);
        razlog = findViewById(R.id.razlogSpinnerR);
        userEmailSpremi = findViewById(R.id.userEmailSpremiR);
        userText = findViewById(R.id.userTextR);
        progressBar = findViewById(R.id.progressBar_userR);
        odobri = findViewById(R.id.btnSaveR);
        otkazi = findViewById(R.id.btnDeleteR);
        terminTXT = findViewById(R.id.terminTxtR);
        razlogTXT = findViewById(R.id.razlogTxtR);
        datumTXT = findViewById(R.id.datumSpinnerR);
        //datum = findViewById(R.id.datumTxtR);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rezervacija = new Rezervacija();
        reff = FirebaseDatabase.getInstance().getReference().child("Rezervacije");


        //nav bar
        drawer = findViewById(R.id.drawer_layoutRadnikEditRezervacija);
        NavigationView navigationView =findViewById(R.id.nav_viewRadnikEdit);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*
        List<String> categoriesTermin = new ArrayList<String>();
        categoriesTermin.add("12:00");
        categoriesTermin.add("12:10");
        categoriesTermin.add("12:20");
        categoriesTermin.add("12:30");
        categoriesTermin.add("12:40");
        categoriesTermin.add("12:50");
        categoriesTermin.add("13:00");
        categoriesTermin.add("13:10");
        categoriesTermin.add("13:20");
        categoriesTermin.add("13:30");
        categoriesTermin.add("13:40");
        categoriesTermin.add("13:50");

        List<String> categories = new ArrayList<String>();
        categories.add("Ispis kolegija");
        categories.add("Upis na studij");
        categories.add("Ispis potvrde");
        categories.add("Prebacivanje studija");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapterTermin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesTermin);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterTermin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        //razlog.setAdapter(dataAdapter);
        //termin.setAdapter(dataAdapterTermin); */
        /*
        int spinnerPositionRazlog = dataAdapter.getPosition(value.getRazlog());
        //razlog.setSelection(spinnerPositionRazlog);
        //rezervacija.setRazlog(value.getRazlog());
        int spinnerPositionTermin = dataAdapterTermin.getPosition(value.getDatum());
        //termin.setSelection(spinnerPositionTermin);
        termin.setText(value.getVrijeme());
        razlog.setText(value.getRazlog());
        //razlogTXT.setText(value.getRazlog());
        //terminTXT.setText(value.getVrijeme());
*/         /*
        SimpleDateFormat formatter1=new SimpleDateFormat("dd.MM.yyyy.");
        //rezervacija.setTermin(value.getVrijeme());
        Date date1 = new Date();
        try {
            date1=formatter1.parse(value.getDatum());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */
        /*
        //kalendar.setDate(date1.getTime());
        rezervacija.setDatum(value.getDatum());

        Calendar cal = Calendar.getInstance();
        kalendar.setMinDate(cal.getTimeInMillis()+24*60*60*1000);
        kalendar.setMaxDate(cal.getTimeInMillis()+1209600*1000);
        cal.setTimeInMillis(kalendar.getDate());
        int year  = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date  = cal.get(Calendar.DAY_OF_MONTH);
        curDate ="" + date + "." + (month+1) + "." + year + ".";
        */
        /*
        razlog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                // On selecting a spinner item
                rezervacija.setRazlog(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        */
        /*
        termin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                // On selecting a spinner item
                rezervacija.setTermin(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        */
        //kalendar listener za datum
        /*
        kalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String d = String.valueOf(dayOfMonth);
                String m = String.valueOf(month+1);
                String y = String.valueOf(year);
                curDate ="" + d + "." + m + "." + y + ".";
                rezervacija.setDatum(curDate);
                //String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "." + cl.get(Calendar.MONTH) + "." + cl.get(Calendar.YEAR);
            }
        });
        */

        termin.setText(value.getDatum());
        razlog.setText(value.getRazlog());
        datumTXT.setText(value.getVrijeme());

        odobri.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                // petlja za pretraživanje rezervacija za trenutno prijavljenog korisnika
                FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Rezervacija a = snapshot.getValue(Rezervacija.class);


                                    if (a.getID().equals(value.getID())) {
                                        reff.child(a.getID()).child("status").setValue("Odobreno");
                                    }
                                }

                                Intent intent = new Intent(RadnikEditRezervacijaActivity.this, RadnikDashboardActivity.class);
                                startActivity(intent);
                                Toast.makeText(RadnikEditRezervacijaActivity.this, "Rezervacija je odobrena!"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }

        });

        otkazi.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                // petlja za pretraživanje rezervacija za trenutno prijavljenog korisnika
                FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Rezervacija a = snapshot.getValue(Rezervacija.class);


                                    if (a.getID().equals(value.getID())) {
                                        reff.child(a.getID()).child("status").setValue("Otkazano");
                                    }
                                }

                                Intent intent = new Intent(RadnikEditRezervacijaActivity.this, RadnikDashboardActivity.class);
                                startActivity(intent);
                                Toast.makeText(RadnikEditRezervacijaActivity.this, "Rezervacija je otkazana!"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }

        });


        userEmailSpremi.setText(firebaseUser.getEmail());

        /*
        drawer = findViewById(R.id.drawer_layoutRezerviraj);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         */
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.glavniizbornikRadnik:
                Intent intent = new Intent(RadnikEditRezervacijaActivity.this, RadnikDashboardActivity.class);
                startActivity(intent);
                break;
            case R.id.Pregledrezervacija:
                intent = new Intent(RadnikEditRezervacijaActivity.this,RadnikPregledRezervacijaActivity.class);
                startActivity(intent);
                break;
            case R.id.Pretrazivanje:
                intent = new Intent(RadnikEditRezervacijaActivity.this, RadnikPretrazivanjeActivity.class);
                startActivity(intent);
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(RadnikEditRezervacijaActivity.this, MainActivity.class);
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

