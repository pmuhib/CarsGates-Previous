package com.carsgates.cr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carsgates.cr.R;

/**
 * Created by sony on 01-05-2017.
 */

public class QuotedDetailsActivity extends AppBaseActivity {
    TextView refnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes_details);
        refnumber = (TextView) findViewById(R.id.qdetail_refnumber);
        Intent it = getIntent();
        String ref = it.getStringExtra("ref");
        refnumber.setText(ref);
        Toast.makeText(getApplicationContext(),""+ref,Toast.LENGTH_LONG).show();
        actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        setuptoolbar();
    }

    private void setuptoolbar() {
        Toolbar toolbar= (Toolbar) findViewById(R.id.bottomToolBar);
        TextView txt= (TextView) toolbar.findViewById(R.id.txt_bot);
        ImageView imageView= (ImageView) toolbar.findViewById(R.id.img_bot);
        imageView.setImageResource(R.drawable.ic_add);
        txt.setText("Book this Car");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(QuotedDetailsActivity.this,DriverSelectionActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle("Quote");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


