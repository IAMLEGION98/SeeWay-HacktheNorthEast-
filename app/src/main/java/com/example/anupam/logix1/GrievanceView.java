package com.example.anupam.logix1;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GrievanceView extends AppCompatActivity {


    DatabaseReference controlbase;
    ListView problems;
    List<Grievance> glist;
    List gsno;
    CardView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_view);
        glist = new ArrayList<>();
        gsno = new ArrayList();
        backbtn = (CardView)findViewById(R.id.backfromtc);
        problems = (ListView)findViewById(R.id.formsview);
        controlbase = FirebaseDatabase.getInstance().getReference("Greivance");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GrievanceView.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        controlbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                glist.clear();
                gsno.clear();
                for (DataSnapshot gsnapshot : dataSnapshot.getChildren()) {
                    String keyval = gsnapshot.getKey().toString().trim();
                    Grievance grievance = gsnapshot.getValue(Grievance.class);
                    glist.add(grievance);
                    gsno.add(keyval);
                }
                ToppingAdapter adapter = new ToppingAdapter(GrievanceView.this,glist,gsno);
                problems.setAdapter(adapter);

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class ToppingAdapter extends BaseAdapter {

        Context mContext;
        List<Grievance> linkedList;
        protected LayoutInflater vi;
        List serialno;



        public ToppingAdapter(Context context, List<Grievance> linkedList, List sno) {
            this.mContext = context;
            this.linkedList = linkedList;
            serialno = sno;
            this.vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return linkedList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ToppingAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = vi.inflate(R.layout.list_layoutview, parent, false);
                holder = new ToppingAdapter.ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.tvid);
                holder.cause = (TextView) convertView.findViewById(R.id.tvcause);
                convertView.setTag(holder);

            } else {
                holder = (ToppingAdapter.ViewHolder) convertView.getTag();
            }

            final Grievance grievance = linkedList.get(position);
            String val = (String) serialno.get(position);


            holder.cause.setText(grievance.getRating());
            holder.title.setText(grievance.getTruckid());



            /**Set tag to all checkBox**/
            holder.cause.setTag(grievance);

            return convertView;
        }


        private class ViewHolder {
            TextView title;
            TextView cause;
        }





    }

}

