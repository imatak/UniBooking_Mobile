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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RadnikPretrazivanjeActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {


    Spinner razlogpretrazivanja;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reff;
    Rezervacija rezervacija;
    Toolbar toolbar;
    private DrawerLayout drawer;
    String RazlogStr;
    EditText EnterTextPretrazovanjeRadnik;


    private Button ButtonTrazi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radnik_pretrazivanje);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layoutRadnikPretrazivanje);
        NavigationView navigationView =findViewById(R.id.nav_viewRadnikPretrazivanje);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,  R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        RazlogStr = "Email";


        ButtonTrazi =findViewById(R.id.ButtonRadnikPretrazivanje);
        ButtonTrazi.setOnClickListener(this);
        EnterTextPretrazovanjeRadnik = findViewById(R.id.EnterTextPretrazovanjeRadnik);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rezervacija = new Rezervacija();
        reff = FirebaseDatabase.getInstance().getReference().child("Rezervacije");

        List<String> categoriesPretrazivanje = new ArrayList<String>();

        razlogpretrazivanja = findViewById(R.id.spinnerPretrazivanje);

        categoriesPretrazivanje.add("Email");
        categoriesPretrazivanje.add("Razlog");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesPretrazivanje);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        razlogpretrazivanja.setAdapter(dataAdapter);

        razlogpretrazivanja.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                // On selecting a spinner item
                RazlogStr = (parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.glavniizbornikRadnik:
                Intent intent = new Intent(RadnikPretrazivanjeActivity.this, RadnikDashboardActivity.class);
                startActivity(intent);
                break;
            case R.id.Pregledrezervacija:
                intent = new Intent(RadnikPretrazivanjeActivity.this,RadnikPregledRezervacijaActivity.class);
                startActivity(intent);
                break;
            case R.id.Pretrazivanje:
                intent = new Intent(RadnikPretrazivanjeActivity.this, RadnikPretrazivanjeActivity.class);
                startActivity(intent);
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(RadnikPretrazivanjeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void FillTable(String Raz){

        ArrayList<MojeRezervacijeStudent> ListaMojihRezervacija = new ArrayList<>();
        ListView mListView = (ListView)findViewById(R.id.listViewRadnikPregled1);
        MojeRezervacijeListAdapter adapter =new MojeRezervacijeListAdapter(this, R.layout.adapterviewlayout, ListaMojihRezervacija);
        mListView.setAdapter(adapter);
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MojeRezervacijeStudent value = (MojeRezervacijeStudent) adapter.getItem(i);
                //value.getDatum();
                Intent intent = new Intent(RadnikPretrazivanjeActivity.this, RadnikEditRezervacijaActivity.class);
                intent.putExtra("Uredi",value);
                startActivity(intent);
            }
        });

            FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Rezervacija a = snapshot.getValue(Rezervacija.class);

                                String NDatum = a.getDatum();
                                String NTermin = a.getTermin();
                                String NEmail = a.getEmailUsera();
                                String NRazlog = a.getRazlog();
                                String NStatus = a.getStatus();
                                String NID = a.getID();
                                String NFakultet = a.getFakultet();
                                String NKomentar = a.getKomentar();
                                String NUredio = a.getUredio();

                                if (Raz == "Email"){
                                        if ((EnterTextPretrazovanjeRadnik.getText().toString()).equals(NEmail)){
                                            ListaMojihRezervacija.add(new MojeRezervacijeStudent(NTermin, NDatum, NStatus, NRazlog,  NID , NEmail, NFakultet, NKomentar, NUredio));
                                            adapter.notifyDataSetChanged();
                                        }
                                }else{
                                        if (EnterTextPretrazovanjeRadnik.getText().toString().equals(NRazlog)){
                                            ListaMojihRezervacija.add(new MojeRezervacijeStudent(NTermin, NDatum, NStatus, NRazlog,  NID , NEmail, NFakultet, NKomentar, NUredio));
                                            adapter.notifyDataSetChanged();
                                        }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
    }

    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.ButtonRadnikPretrazivanje:
                FillTable(RazlogStr);
                break;
        }
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
