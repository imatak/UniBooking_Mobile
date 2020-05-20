package unipu.hr.unibooking;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MojeRezervacijeListAdapter extends ArrayAdapter<MojeRezervacijeStudent>{

    private static final String TAG = "MojeRezervacijeListAdapter";
    private Context mContext;
    int mResource;
    ArrayList<MojeRezervacijeStudent> podaci;

    /**
     * Default constructor for the MojeRezervacijeListAdapter
     * @param context
     * @param resource
     * @param objects
    */

    public MojeRezervacijeListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MojeRezervacijeStudent> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         String datum = getItem(position).getDatum();
         String vrijeme = getItem(position).getVrijeme();
         String status = getItem(position).getStatus();
         String razlog = getItem(position).getRazlog();
         String id = getItem(position).getID();
         String email = getItem(position).getEmail();
         String fakultet = getItem(position).getFakultet();
         String komentar = getItem(position).getKomentar();
         String uredio = getItem(position).getUredio();

         MojeRezervacijeStudent rezervacijeStud = new MojeRezervacijeStudent(datum,vrijeme,status,razlog,id,email,fakultet, komentar, uredio);

        LayoutInflater inflater= LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvDataum = (TextView) convertView.findViewById(R.id.textViewDatum);
        TextView tvVrijeme = (TextView) convertView.findViewById(R.id.textViewVrijeme);
        TextView tvRazlog = (TextView) convertView.findViewById(R.id.textViewRazlog);
        //TextView tvStatus = (TextView) convertView.findViewById(R.id.textViewStatus);
        ImageView tvStatus = (ImageView) convertView.findViewById(R.id.textViewStatus);

        tvDataum.setText(datum);
        tvVrijeme.setText(vrijeme);
        tvRazlog.setText(razlog);
        if(status.equals("Odobreno")){
            tvStatus.setImageResource(R.drawable.bigyes);
        } else if(status.equals("Otkazano")){
            tvStatus.setImageResource(R.drawable.bigno);
        } else if(status.equals("Na čekanju")){
            tvStatus.setImageResource(R.drawable.bigprogress);
        } else if(status.equals("Prošlo")){
            tvStatus.setImageResource(R.drawable.proslo2);
        }
        //tvStatus.setBackgroundResource(R.drawable.yes);

        return convertView;
    }
}
