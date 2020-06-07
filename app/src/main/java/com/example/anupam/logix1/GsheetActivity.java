package com.example.anupam.logix1;

import android.app.ProgressDialog;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GsheetActivity extends AppCompatActivity implements AsyncResponse {


    TextView tvrate;
    TextView bttv;
    String usrate;
    Button btget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsheet);
        tvrate = (TextView)findViewById(R.id.ratev);

        bttv =(TextView)findViewById(R.id.tvdo);
        bttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object info[] = new Object[1];
                String link = "https://script.googleusercontent.com/macros/echo?user_content_key=qoCywEaqNaNCgFk9diEbavu4eF_qc7BaSdfDumT6VjiDX8LeFK2tc1gd-3LpQ1DFSpqNfVHnUt_3MgELhM5SKnO0LKcNS-oqOJmA1Yb3SEsKFZqtv3DaNYcMrmhZHmUMWojr9NvTBuBLhyHCd5hHa1GhPSVukpSQTydEwAEXFXgt_wltjJcH3XHUaaPC1fv5o9XyvOto09QuWI89K6KjOu0SP2F-BdwU9cPgR8aptwtAleBmcA47MFD5yc0bzouSCplxt9BpQfTm275QsP3hJESuhdcRGtDn5y7FLqOV0Tk27B8Rh4QJTQ&lib=MnrE7b2I2PjfH799VodkCPiQjIVyBAxva";
                info[0]= link;
                CurrencyRoutine currencyRoutine = new CurrencyRoutine(GsheetActivity.this);
                currencyRoutine.execute(info);
                final ProgressDialog progressDialog = new ProgressDialog(GsheetActivity.this);
                progressDialog.setMessage("Finding Current Exhange rate....");
                //progressDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setrates();//progressDialog.dismiss();
                    }
                },5000);
            }
        });

    }




    public void setrates()
    {
        tvrate.setText(usrate);
    }

    @Override
    public void processFinish(String output) {
        usrate =output;

    }
}

