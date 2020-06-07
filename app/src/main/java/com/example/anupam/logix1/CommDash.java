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

public class CommDash extends AppCompatActivity {


    DatabaseReference combase;
    List<Commodity> comlist;
    ListView commoditylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_dash);
        comlist = new ArrayList<>();
        combase = FirebaseDatabase.getInstance().getReference("Commodities");
        commoditylist = (ListView)findViewById(R.id.comlistview);

        commoditylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String cid = ((TextView)view.findViewById(R.id.tvheader)).getText().toString();
                String sub = ((TextView)view.findViewById(R.id.tvsubs)).getText().toString();

                Intent intent = new Intent(CommDash.this,Commonitor.class);
                intent.putExtra("crateid",cid);
                intent.putExtra("contents",sub);
                startActivity(intent);
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

               ItemsAdapter adapter = new ItemsAdapter(CommDash.this,comlist);
                commoditylist.setAdapter(adapter);
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

            final CommDash.ItemsAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = vi.inflate(R.layout.list_layout, parent, false);
                holder = new CommDash.ItemsAdapter.ViewHolder();
                holder.iheader = (TextView) convertView.findViewById(R.id.tvheader);
                holder.isubs = (TextView) convertView.findViewById(R.id.tvsubs);

                convertView.setTag(holder);

            } else {
                holder = (CommDash.ItemsAdapter.ViewHolder) convertView.getTag();
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
