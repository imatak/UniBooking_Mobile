package unipu.hr.unibooking;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.service.autofill.FillCallback;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
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

public class RezervirajActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    CalendarView kalendar;
    Spinner termin;
    Spinner razlog;
    TextView terminTXT;
    TextView razlogTXT;
    TextView userEmailSpremi;
    EditText userText;
    EditText upisiRazlog;
    Button Spremi;
    Toolbar toolbar;
    ProgressBar progressBar;
    String curDate;
    String faks;
    TextView zatvoreno;
    Boolean vec_ima_rezervacija;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reff;
    Rezervacija rezervacija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezerviraj);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);




        kalendar = findViewById(R.id.kalendarRezervacijeR);
        termin = findViewById(R.id.terminSpinnerR);
        razlog = findViewById(R.id.razlogSpinnerR);
        terminTXT = findViewById(R.id.terminTxt);
        razlogTXT = findViewById(R.id.razlogTxtR);
        userEmailSpremi = findViewById(R.id.userEmailSpremiR);
        userText = findViewById(R.id.userTextR);
        progressBar = findViewById(R.id.progressBar_userR);
        Spremi = findViewById(R.id.btnSaveR);
        zatvoreno = findViewById(R.id.zatvorenoR);
        upisiRazlog = findViewById(R.id.etUpisiRazlogRezerviraj);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rezervacija = new Rezervacija();
        reff = FirebaseDatabase.getInstance().getReference().child("Rezervacije");


        // petlja za pretraživanje rezervacija za trenutno prijavljenog korisnika
        FirebaseDatabase.getInstance().getReference().child("Studenti")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Student a = snapshot.getValue(Student.class);

                            if (a.getEmail().equals(firebaseUser.getEmail())) {
                                String Fakultet = a.getFakultet();
                                faks = a.getFakultet();
                                rezervacija.setFakultet(Fakultet);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        List<String> categoriesTermin = new ArrayList<String>();

        Calendar cale = Calendar.getInstance();
        int danTjedan  = cale.get(Calendar.DAY_OF_WEEK);
        AzurirajTermine(danTjedan, categoriesTermin);


        Date now = Calendar.getInstance().getTime();

        LoopTable(now, categoriesTermin, faks);

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

        Calendar cal = Calendar.getInstance();
        int year  = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date  = cal.get(Calendar.DAY_OF_MONTH);
        curDate ="" + (date+1) + "." + (month+1) + "." + year + ".";
        kalendar.setDate(cal.getTimeInMillis()+24*60*60*1000, true, true);
        kalendar.setMinDate(cal.getTimeInMillis()+24*60*60*1000);
        kalendar.setMaxDate(cal.getTimeInMillis()+1209600*1000);
        Date date1= null;
        try {
            date1 = new SimpleDateFormat("dd.MM.yyyy").parse(curDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LoopTable(date1, categoriesTermin, faks);

        //kalendar listener za datum
        kalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String d = String.valueOf(dayOfMonth);
                String m = String.valueOf(month+1);
                String y = String.valueOf(year);
                curDate ="" + d + "." + m + "." + y + ".";
                //String date = "" + cl.get(Calendar.DAY_OF_MONTH) + "." + cl.get(Calendar.MONTH) + "." + cl.get(Calendar.YEAR);

                try {

                    Date date1=new SimpleDateFormat("dd.MM.yyyy").parse(curDate);
                    Calendar cale = Calendar.getInstance();
                    cale.setTime(date1);
                    int danTjedan2  = cale.get(Calendar.DAY_OF_WEEK);
                    AzurirajTermine(danTjedan2,categoriesTermin);
                    LoopTable(date1, categoriesTermin, faks);



                }
                catch(Exception e) {
                    //java.text.ParseException: Unparseable date: Geting error
                    System.out.println("Excep"+e);
                }


            }
        });


        Spremi.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                vec_ima_rezervacija = false;
                progressBar.setVisibility(View.VISIBLE);
                Spremi.setClickable(false);

                rezervacija.setDatum(curDate);
                rezervacija.setEmailUsera(userEmailSpremi.getText().toString().trim());
                rezervacija.setStatus("Na čekanju");
                rezervacija.setKomentar("");
                rezervacija.setUredio("");
                //rezervacija.setTimeStamp("BEeee");

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
                                    if (a.getEmailUsera().equals(firebaseUser.getEmail()) && a.getDatum().equals(curDate)) {
                                        vec_ima_rezervacija = true;
                                    }
                                }
                                if(vec_ima_rezervacija){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Spremi.setClickable(true);
                                    Toast.makeText(RezervirajActivity.this, "Dopuštena je samo jedna rezervacija dnevno!"
                                            , Toast.LENGTH_LONG).show();
                                } else if (rezervacija.getRazlog().equals("Upiši razlog:") && upisiRazlog.getText().toString().isEmpty()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Spremi.setClickable(true);
                                Toast.makeText(RezervirajActivity.this, "Morate upisati razlog!"
                                        , Toast.LENGTH_LONG).show();
                                } else {

                                    if(rezervacija.getRazlog().equals("Upiši razlog:")){
                                        rezervacija.setRazlog(upisiRazlog.getText().toString());
                                    }
                                    //rezervacija.setUserTekst(userText.getText().toString().trim());
                                    String key = reff.push().getKey();
                                    rezervacija.setID(key);
                                    reff.child(key).setValue(rezervacija);
                                    //reff.push().setValue(rezervacija);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Spremi.setClickable(true);
                                    Toast.makeText(RezervirajActivity.this, "Termin je uspješno rezerviran!"
                                            , Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RezervirajActivity.this, ProfileActivity.class));
                                }
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
                Intent intent = new Intent(RezervirajActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.mojerezervacije:
                intent = new Intent(RezervirajActivity.this, MojeRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.rezerviraj:
                intent = new Intent(RezervirajActivity.this, RezervirajActivity.class);
                startActivity(intent);
                break;
            case R.id.proslerezervacije:
                intent = new Intent(RezervirajActivity.this, ProsleRezervacijeActivity.class);
                startActivity(intent);
                break;
            case R.id.postavke:
                intent = new Intent(RezervirajActivity.this, PostavkeActivity.class);
                startActivity(intent);
                break;
            case R.id.odjava:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(RezervirajActivity.this, MainActivity.class);
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
            zatvoreno.setVisibility(View.GONE);
        } else {
            zatvoreno.setVisibility(View.VISIBLE);
            termin.setVisibility(View.INVISIBLE);
            razlog.setVisibility(View.INVISIBLE);
            Spremi.setVisibility(View.INVISIBLE);
        }
    }

    public void LoopTable(Date d, List<String> categoriesTermin, String faks){
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
                                if(baza.equals(d) && a.getFakultet().equals(faks)){
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


                        ArrayAdapter<String> dataAdapterTermin = new ArrayAdapter<String>(RezervirajActivity.this,android.R.layout.simple_spinner_item,symmetricDifference);
                        dataAdapterTermin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        termin.setAdapter(dataAdapterTermin);

                        /*
                        for (int i=0;i<categoriesTermin.size();i++){
                            System.out.println("categroje" +categoriesTermin.get(i));
                            for (int a=0;a<ListaPostojecihTermina.size();a++){
                                System.out.println("Postojeci" +ListaPostojecihTermina.get(a));
                                if (categoriesTermin.get(i)== ListaPostojecihTermina.get(a)){
                                    System.out.println("true");
                                    categoriesTermin.remove(i);
                                    dataAdapterTermin.notifyDataSetChanged();
                                }
                            }
                        }
                        */

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
}
