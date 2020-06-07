package com.example.anupam.logix1;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class FleetDash extends AppCompatActivity {


    ListView fleetlistview;
    DatabaseReference assetbase;
    List<Assets> assetlist;
    TextView grievance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_dash);
        assetbase = FirebaseDatabase.getInstance().getReference("Assets");
        fleetlistview = (ListView)findViewById(R.id.assetslistview);
        grievance = (TextView)findViewById(R.id.tvgrievance);
        assetlist = new ArrayList<>();

        fleetlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tid = ((TextView)view.findViewById(R.id.tvheader)).getText().toString();


                Intent intent = new Intent(FleetDash.this,FleetMonitor.class);
                intent.putExtra("truckid",tid);
                startActivity(intent);
            }
        });

        grievance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FleetDash.this,GrievanceView.class);
                startActivity(intent);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        assetbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assetlist.clear();
                for(DataSnapshot csnapshot: dataSnapshot.getChildren())
                {
                    Log.d("ITEM",csnapshot.toString());

                    /* */
                    //Log.d("item is",csnapshot.getValue().toString());
                    Assets a1 = csnapshot.getValue(Assets.class);
                    Log.e("Header",a1.getHeaderid());
                    Log.e("SUB",a1.getType());
                    assetlist.add(a1);
                }

               ItemsAdapter adapter = new ItemsAdapter(FleetDash.this,assetlist);
                fleetlistview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class ItemsAdapter extends BaseAdapter {

        Context mContext;
        List<Assets> linkedList;
        protected LayoutInflater vi;


        public ItemsAdapter(Context context, List<Assets> linkedList) {
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

            final FleetDash.ItemsAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = vi.inflate(R.layout.list_layout, parent, false);
                holder = new FleetDash.ItemsAdapter.ViewHolder();
                holder.iheader = (TextView) convertView.findViewById(R.id.tvheader);
                holder.isubs = (TextView) convertView.findViewById(R.id.tvsubs);

                convertView.setTag(holder);

            } else {
                holder = (FleetDash.ItemsAdapter.ViewHolder) convertView.getTag();
            }

            final Assets placedItems = linkedList.get(position);


            holder.iheader.setText(placedItems.getHeaderid());
            holder.isubs.setText(placedItems.getType());



            return convertView;
        }


        private class ViewHolder {
            TextView iheader;
            TextView isubs;


        }

    }
}
