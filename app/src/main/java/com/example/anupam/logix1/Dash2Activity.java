package com.example.anupam.logix1;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
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

public class Dash2Activity extends AppCompatActivity {

    CardView cexport,crates, cindex,cbuy,cout;
    TextView driversick,drivernotsick,driveronduty;
    DatabaseReference driverbase,combase;
    List<Commodity> comlist;
    ListView commoditylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash2);
        cexport =(CardView)findViewById(R.id.exportid);
        cout =(CardView)findViewById(R.id.signout);
        crates =(CardView)findViewById(R.id.ratesid);
        cindex =(CardView)findViewById(R.id.indexid);
        cbuy =(CardView)findViewById(R.id.buyid);
        driversick = (TextView)findViewById(R.id.driversick);
        drivernotsick = (TextView)findViewById(R.id.drivernotsick);
        driveronduty = (TextView)findViewById(R.id.driveronduty);
        comlist = new ArrayList<>();
        combase = FirebaseDatabase.getInstance().getReference("Commodities");
        commoditylist = (ListView)findViewById(R.id.transitview);


        driverbase = FirebaseDatabase.getInstance().getReference("Drivers");



        cexport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dash2Activity.this,AssetsDash.class);
                startActivity(intent);
            }
        });
        cout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Dash2Activity.this,MainLoginActivity.class);
                startActivity(intent1);
            }
        });
        crates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Dash2Activity.this,GsheetActivity.class);
                startActivity(intent2);
            }
        });
        cbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(Dash2Activity.this,BuyActivity.class);
                startActivity(intent3);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        combase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comlist.clear();

                for(DataSnapshot csnapshot: dataSnapshot.getChildren())
                {
                    Log.d("ITEM",csnapshot.toString());

                    /* */
                    Log.d("item is",csnapshot.getValue().toString());
                    Commodity c1 = csnapshot.getValue(Commodity.class);
                    Log.e("Header",c1.getHeaderid());
                    Log.e("SUB",c1.getSub());
                    comlist.add(c1);
                }

                ItemsAdapter adapter = new ItemsAdapter(Dash2Activity.this,comlist);
                commoditylist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        driverbase.addValueEventListener(new ValueEventListener() {
            int totalsick=0;
            int totalnotsick=0;
            int totalonduty=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot csnapshot: dataSnapshot.getChildren())
                {

                    /* */

                    for(DataSnapshot childsnap: csnapshot.getChildren())
                    {

                        /* */
                        Log.d("item is",childsnap.getValue().toString());
                        if(childsnap.getKey().toString().trim().equals("sick"))
                        {
                            if(childsnap.getValue().toString().trim().equals("true"))
                            {
                                totalsick++;
                            }
                            else
                            {
                                totalnotsick++;
                            }
                        }
                        if(childsnap.getKey().toString().trim().equals("onduty"))
                        {
                            if(childsnap.getValue().toString().trim().equals("true"))
                            {
                                totalonduty++;
                            }


                        }
                    }
                }
                driversick.setText("Sick : "+ totalsick );
                drivernotsick.setText("Not Sick :"+totalnotsick);
                driveronduty.setText("On Duty :"+totalonduty);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public class ItemsAdapter extends BaseAdapter {

        Context mContext;
        List<Commodity> linkedList;
        protected LayoutInflater vi;


        public ItemsAdapter(Context context, List<Commodity> linkedList) {
            this.mContext = context;
            this.linkedList = linkedList;
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

            final Dash2Activity.ItemsAdapter.ViewHolder holder;;
            if (convertView == null) {
                convertView = vi.inflate(R.layout.list_layout, parent, false);
                holder = new Dash2Activity.ItemsAdapter.ViewHolder();
                holder.iheader = (TextView) convertView.findViewById(R.id.tvheader);
                holder.isubs = (TextView) convertView.findViewById(R.id.tvsubs);

                convertView.setTag(holder);

            } else {
                holder = (Dash2Activity.ItemsAdapter.ViewHolder) convertView.getTag();
            }

            final Commodity placedItems = linkedList.get(position);


            holder.iheader.setText(placedItems.getHeaderid());
            holder.isubs.setText(placedItems.getSub());



            return convertView;
        }


        private class ViewHolder {
            TextView iheader;
            TextView isubs;

        }

    }

}



