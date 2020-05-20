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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditRezervacijaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    CalendarView kalendar;
    Spinner termin;
    Spinner razlog;
    TextView userEmailSpremi;
    EditText userText;
    Button Spremi;
    Toolbar toolbar;
    ProgressBar progressBar;
    String curDate;
    Button izbrisi;
    TextView zatvoreno;
    Boolean vec_ima_rezervacija;
    EditText upisiRazlog;
    TextView uredioTxt;
    TextView komentarTxt;

    TextView Teksttermin;
    TextView TekstRazlog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reff;
    Rezervacija rezervacija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rezervacija);

        Intent i = getIntent();
        MojeRezervacijeStudent value = (MojeRezervacijeStudent) i.getSerializableExtra("Uredi");
        String ID_rezervacije = value.getID();
        String vrijeme = value.getVrijeme();
        String faks = value.getFakultet();

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        kalendar = findViewById(R.id.kalendarRezervacijeR);
        termin = findViewById(R.id.terminSpinnerR);
        razlog = findViewById(R.id.razlogSpinnerR);
        userEmailSpremi = findViewById(R.id.userEmailSpremiR);
        userText = findViewById(R.id.userTextR);
        progressBar = findViewById(R.id.progressBar_userR);
        Spremi = findViewById(R.id.btnSaveR);
        izbrisi = findViewById(R.id.btnDeleteR);
        zatvoreno = findViewById(R.id.zatvorenoEditR);
        upisiRazlog = findViewById(R.id.etUpisiRazlog);
        uredioTxt = findViewById(R.id.uredioTxtR);
        komentarTxt = findViewById(R.id.komentarTxtR);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rezervacija = new Rezervacija();
        reff = FirebaseDatabase.getInstance().getReference().child("Rezervacije");

        SimpleDateFormat formatter1=new SimpleDateFormat("dd.MM.yyyy.");
        Date date1 = new Date();
        try {
            date1=formatter1.parse(value.getDatum());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        kalendar.setDate(date1.getTime());
        rezervacija.setDatum(value.getDatum());

        Calendar cal = Calendar.getInstance();
        kalendar.setMinDate(cal.getTimeInMillis()+24*60*60*1000);
        kalendar.setMaxDate(cal.getTimeInMillis()+1209600*1000);
        cal.setTimeInMillis(kalendar.getDate());
        int year  = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date  = cal.get(Calendar.DAY_OF_MONTH);
        Integer danTjedan = cal.get(Calendar.DAY_OF_WEEK);
        curDate ="" + date + "." + (month+1) + "." + year + ".";

        Calendar calend = Calendar.getInstance();
        calend.getTimeInMillis();

        List<String> categoriesTermin = new ArrayList<String>();

        //Date date1=new SimpleDateFormat("dd.MM.yyyy").parse(curDate);
        //Calendar cale = Calendar.getInstance();
        AzurirajTermine(danTjedan,categoriesTermin);
        uredioTxt.setText(value.getUredio() + ":");
        komentarTxt.setText(value.getKomentar());

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterTermin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesTermin);

        // Drop down layout style - list view with radio button
        dataAdapterTermin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        termin.setAdapter(dataAdapterTermin);

        int spinnerPositionTermin = dataAdapterTermin.getPosition(value.getVrijeme());
        termin.setSelection(spinnerPositionTermin);

        Date now = Calendar.getInstance().getTime();

        LoopTable(now, categoriesTermin, ID_rezervacije, vrijeme, faks);

        List<String> categories = new ArrayList<String>();
        categories.add("Ispis kolegija");
        categories.add("Upis na studij");
        categories.add("Ispis potvrde");
        categories.add("Prebacivanje studija");
        categories.add("Upiši razlog:");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        razlog.setAdapter(dataAdapter);

        int spinnerPositionRazlog = dataAdapter.getPosition(value.getRazlog());
        razlog.setSelection(spinnerPositionRazlog);
        if(value.getRazlog().equals("Ispis kolegija") ||
                value.getRazlog().equals("Upis na studij") ||
                value.getRazlog().equals("Ispis potvrde") ||
                value.getRazlog().equals("Prebacivanje studija")){
            upisiRazlog.setVisibility(View.INVISIBLE);
            razlog.setSelection(spinnerPositionRazlog);
        } else {
            upisiRazlog.setVisibility(View.VISIBLE);
            int spinnerPositionRazlog2 = dataAdapter.getPosition("Upiši razlog:");
            razlog.setSelection(spinnerPositionRazlog2);
            upisiRazlog.setText(value.getRazlog());
        }
        //rezervacija.setRazlog(value.getRazlog());
        //rezervacija.setTermin(value.getVrijeme());

        //ovdje je uzeto na početak formatter1, a date2 je novo
        Date date2= null;
        try {
            date2 = new SimpleDateFormat("dd.MM.yyyy").parse(curDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LoopTable(date2, categoriesTermin, ID_rezervacije, vrijeme, faks);


        razlog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                // On selecting a spinner item
                rezervacija.setRazlog(parentView.getItemAtPosition(position).toString());
                if(rezervacija.getRazlog().equals("Upiši razlog:")){
                    upisiRazlog.setVisibility(View.VISIBLE);
                } else {
                    upisiRazlog.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

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

        //kalendar listener za datum
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

                try {

                    Date date1=new SimpleDateFormat("dd.MM.yyyy").parse(curDate);
                    Calendar cale = Calendar.getInstance();
                    cale.setTime(date1);
                    int danTjedan2  = cale.get(Calendar.DAY_OF_WEEK);
                    AzurirajTermine(danTjedan2,categoriesTermin);

                    LoopTable(date1, categoriesTermin, ID_rezervacije, vrijeme, faks);



                }
                catch(Exception e) {
                    //java.text.ParseException: Unparseable date: Geting error
                    System.out.println("Excep"+e);
                }

            }
        });


        Spremi.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                // petlja za pretraživanje rezervacija za trenutno prijavljenog korisnika
                vec_ima_rezervacija = false;

                DateFormat df = new SimpleDateFormat("dd.MM.yyyy. hh:mm");
                try {
                    Date dt = df.parse(rezervacija.getDatum() + " " + rezervacija.getTermin());
                    Calendar ca = Calendar.getInstance();
                    ca.setTime(dt);
                    Long TimeStampZaSpremiti = ca.getTimeInMillis();
                    String spremiTimeStamp = TimeStampZaSpremiti.toString();
                    String spremiTimeStampSkraceno = spremiTimeStamp.substring(0, spremiTimeStamp.length() - 5);
                    rezervacija.setTimeStamp(spremiTimeStampSkraceno);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Rezervacija a = snapshot.getValue(Rezervacija.class);
                                    if (a.getEmailUsera().equals(firebaseUser.getEmail()) && a.getDatum().equals(rezervacija.getDatum()) && !(a.getID().equals(ID_rezervacije))) {
                                        vec_ima_rezervacija = true;
                                    }
                                }
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Rezervacija a = snapshot.getValue(Rezervacija.class);

                                    if (a.getID().equals(ID_rezervacije)) {
                                        if(vec_ima_rezervacija){
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Spremi.setClickable(true);
                                            Toast.makeText(EditRezervacijaActivity.this, "Dopuštena je samo jedna rezervacija dnevno!"
                                                    , Toast.LENGTH_LONG).show();
                                        } else if (rezervacija.getRazlog().equals("Upiši razlog:") && upisiRazlog.getText().toString().isEmpty()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Spremi.setClickable(true);
                                            Toast.makeText(EditRezervacijaActivity.this, "Morate upisati razlog!"
                                                    , Toast.LENGTH_LONG).show();
                                        } else {
                                            //String key = snapshot.getKey();
                                            //rezervacija.setDatum(curDate);
                                            rezervacija.setEmailUsera(userEmailSpremi.getText().toString().trim());
                                            String x = rezervacija.getDatum();
                                            if(x != null && !x.isEmpty()){
                                                reff.child(a.getID()).child("datum").setValue(rezervacija.getDatum());
                                            }
                                            if(rezervacija.getRazlog().equals("Upiši razlog:")){
                                                reff.child(a.getID()).child("razlog").setValue(upisiRazlog.getText().toString());
                                            } else {
                                                reff.child(a.getID()).child("razlog").setValue(rezervacija.getRazlog());
                                            }

                                            reff.child(a.getID()).child("termin").setValue(rezervacija.getTermin());
                                            reff.child(a.getID()).child("timeStamp").setValue(rezervacija.getTimeStamp());
                                            reff.child(a.getID()).child("status").setValue("Na čekanju");
                                            reff.child(a.getID()).child("uredio").setValue("");
                                            reff.child(a.getID()).child("komentar").setValue("");

                                            Intent intent = new Intent(EditRezervacijaActivity.this, ProfileActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(EditRezervacijaActivity.this, "Termin je uspješno ažuriran! Status je na čekanju radi izmjene."
                                                    , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }

        });

        izbrisi.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                // petlja za pretraživanje rezervacija za trenutno prijavljenog korisnika
                FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Rezervacija a = snapshot.getValue(Rezervacija.class);


                                    if (a.getID().equals(value.getID())) {

                                        //String key = snapshot.getKey();
                                        reff.child(a.getID()).removeValue();
                                    }
                                }

                                Intent intent = new Intent(EditRezervacijaActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                Toast.makeText(EditRezervacijaActivity.this, "Rezervacija je izbrisana!"
                                        , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }

        });


        userEmailSpremi.setText(firebaseUser.getEmail());


        drawer = findViewById(R.id.drawer_layoutRezerviraj);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.glavniizbornik:
                Intent intent = new Intent(EditRezervacijaActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.mojerezervacije:
                intent = new Intent(EditRezervacijaActivity.this, MojeRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.rezerviraj:
                intent = new Intent(EditRezervacijaActivity.this, RezervirajActivity.class);
                startActivity(intent);
                break;
            case R.id.proslerezervacije:
                intent = new Intent(EditRezervacijaActivity.this, ProsleRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(EditRezervacijaActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

        }
        return true;
    }

    public void AzurirajTermine(Integer danTjedan, List<String> categoriesTermin){
        categoriesTermin.clear();
        if (danTjedan.equals(2) || danTjedan.equals(3) || danTjedan.equals(5)) {
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
            razlog.setVisibility(View.VISIBLE);
            termin.setVisibility(View.VISIBLE);
            Spremi.setVisibility(View.VISIBLE);
            izbrisi.setVisibility(View.VISIBLE);
            zatvoreno.setVisibility(View.GONE);
        } else if (danTjedan.equals(4)) {
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
            categoriesTermin.add("15:00");
            categoriesTermin.add("15:10");
            categoriesTermin.add("15:20");
            categoriesTermin.add("15:30");
            categoriesTermin.add("15:40");
            categoriesTermin.add("15:50");
            categoriesTermin.add("16:00");
            categoriesTermin.add("16:10");
            categoriesTermin.add("16:20");
            razlog.setVisibility(View.VISIBLE);
            termin.setVisibility(View.VISIBLE);
            Spremi.setVisibility(View.VISIBLE);
            izbrisi.setVisibility(View.VISIBLE);
            zatvoreno.setVisibility(View.GONE);
        } else if (danTjedan.equals(6)) {
            categoriesTermin.add("11:00");
            categoriesTermin.add("11:10");
            categoriesTermin.add("11:20");
            categoriesTermin.add("11:30");
            categoriesTermin.add("11:40");
            categoriesTermin.add("11:50");
            categoriesTermin.add("12:00");
            categoriesTermin.add("12:10");
            categoriesTermin.add("12:20");
            razlog.setVisibility(View.VISIBLE);
            termin.setVisibility(View.VISIBLE);
            Spremi.setVisibility(View.VISIBLE);
            izbrisi.setVisibility(View.VISIBLE);
            zatvoreno.setVisibility(View.GONE);
        } else {
            zatvoreno.setVisibility(View.VISIBLE);
            termin.setVisibility(View.INVISIBLE);
            razlog.setVisibility(View.INVISIBLE);
            Spremi.setVisibility(View.INVISIBLE);
            izbrisi.setVisibility(View.INVISIBLE);
        }
    }

    public void LoopTable(Date d, List<String> categoriesTermin, String ID_rezervacije, String vrijeme, String faks){
        List<String> ListaPostojecihTermina = new ArrayList<String>();

        FirebaseDatabase.getInstance().getReference().child("Rezervacije")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Rezervacija a = snapshot.getValue(Rezervacija.class);
                            String NDatum = a.getDatum();
                            String NTermin = a.getTermin();
                            try {
                                SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy");
                                Date baza=dateFormat.parse(NDatum);
                                if(baza.equals(d)  && !(a.getID().equals(ID_rezervacije)) && a.getFakultet().equals(faks)){
                                    ListaPostojecihTermina.add(NTermin);
                                }

                            }
                            catch(Exception e) {
                                //java.text.ParseException: Unparseable date: Geting error
                                System.out.println("Excep"+e);
                            }

                        }
                        List<String> union = new ArrayList(categoriesTermin);
                        union.addAll(ListaPostojecihTermina);
                        List<String> intersection = new ArrayList(categoriesTermin);
                        intersection.retainAll(ListaPostojecihTermina);
                        List<String> symmetricDifference = new ArrayList(union);
                        symmetricDifference.removeAll(intersection);


                        ArrayAdapter<String> dataAdapterTermin = new ArrayAdapter<String>(EditRezervacijaActivity.this,android.R.layout.simple_spinner_item,symmetricDifference);
                        dataAdapterTermin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        termin.setAdapter(dataAdapterTermin);

                        int spinnerPositionTermin = dataAdapterTermin.getPosition(vrijeme);
                        termin.setSelection(spinnerPositionTermin);

                        dataAdapterTermin.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
