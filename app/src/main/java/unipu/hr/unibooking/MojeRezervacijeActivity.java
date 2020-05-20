package unipu.hr.unibooking;

import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MojeRezervacijeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reff;
    Rezervacija rezervacija;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moje_rezervacije_activity);
        ArrayList<MojeRezervacijeStudent> ListaMojihRezervacija = new ArrayList<>();



        ListView mListView = (ListView)findViewById(R.id.listViewStudent);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layoutMojeRezervacije);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reff = FirebaseDatabase.getInstance().getReference().child("Rezervacije");
        /*
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                nesto.setText(name);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         */



        MojeRezervacijeListAdapter adapter =new MojeRezervacijeListAdapter(this, R.layout.adapterviewlayout, ListaMojihRezervacija);
        mListView.setAdapter(adapter);
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy");
        /*
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date now = c.getTime();
        */
        Date now = Calendar.getInstance().getTime();

        // petlja za pretraživanje rezervacija za trenutno prijavljenog korisnika
        FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
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
                                        ListaMojihRezervacija.add(new MojeRezervacijeStudent(NDatum, NTermin, NStatus, NRazlog, NID, NEmail, NFakultet, NKomentar, NUredio));
                                    }else if(d.equals(now)){
                                        ListaMojihRezervacija.add(new MojeRezervacijeStudent(NDatum, NTermin, NStatus, NRazlog, NID, NEmail, NFakultet, NKomentar, NUredio));
                                    }

                                }
                                catch(Exception e) {
                                    //java.text.ParseException: Unparseable date: Geting error
                                    System.out.println("Excep"+e);
                                }

                                adapter.notifyDataSetChanged();
                            }

                            //User user = snapshot.getValue(User.class);
                            //System.out.println(user.email);
                        }

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
                Intent intent = new Intent(MojeRezervacijeActivity.this, EditRezervacijaActivity.class);
                intent.putExtra("Uredi",value);
                startActivity(intent);
            }
        });


    }


    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.glavniizbornik:
                Intent intent = new Intent(MojeRezervacijeActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.mojerezervacije:
                intent = new Intent(MojeRezervacijeActivity.this, MojeRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.rezerviraj:
                intent = new Intent(MojeRezervacijeActivity.this, RezervirajActivity.class);
                startActivity(intent);
                break;
            case R.id.proslerezervacije:
                intent = new Intent(MojeRezervacijeActivity.this, ProsleRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.postavke:
                intent = new Intent(MojeRezervacijeActivity.this, PostavkeActivity.class);
                startActivity(intent);
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(MojeRezervacijeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

        }
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
