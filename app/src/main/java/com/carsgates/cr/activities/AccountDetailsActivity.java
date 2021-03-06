package com.carsgates.cr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.carsgates.cr.R;
import com.carsgates.cr.Utils.Utility;
import com.carsgates.cr.fragments.AccountFragment;
import com.carsgates.cr.fragments.MyAccountsFragment;
import com.carsgates.cr.webservices.ApiResponse;
import com.carsgates.cr.webservices.RetroFitApis;
import com.carsgates.cr.webservices.RetrofitApiBuilder;
import com.mukesh.tinydb.TinyDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sony on 05-05-2017.
 */

public class AccountDetailsActivity extends AppBaseActivity {
    private TinyDB sharedpref;
    String userId,token,title,fname,lname,phone,ages,Rtitle;
    Spinner spTitle;
    int age;
    EditText etUserFirstName,etUserLastName, etUserEmail,etUserPhoneNo,etUserAge;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        sharedpref = new TinyDB(getApplicationContext());
        userId=sharedpref.getString("userid");
        token=sharedpref.getString("access_token");
        Rtitle= sharedpref.getString("usertitle");
        actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        setUptoolbar();

        spTitle= (Spinner) findViewById(R.id.sp_title);
        if(!Rtitle.isEmpty() || !Rtitle.equals(null))
        {
            updatespinner();
        }
        else
        {
            setupspinner();
        }

        etUserFirstName =  (EditText) findViewById(R.id.etUserFirstName);
        etUserLastName =  (EditText) findViewById(R.id.etUserLastName);
        etUserEmail =  (EditText) findViewById(R.id.etUserEmail);
        etUserPhoneNo =  (EditText) findViewById(R.id.etUserPhoneNo);
        etUserPhoneNo.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etUserAge =  (EditText) findViewById(R.id.etUserAge);
        etUserFirstName.setText(sharedpref.getString("user_name"));
        etUserLastName.setText(sharedpref.getString("user_lname"));
        if(!userId.isEmpty() || !userId.equals(null))
        {
            if(!sharedpref.getString("user_email").equals(null) || !sharedpref.getString("user_email").isEmpty()) {
                etUserEmail.setEnabled(false);
                etUserEmail.setText(sharedpref.getString("user_email"));
            }
        }
        etUserPhoneNo.setText(sharedpref.getString("user_phone"));
        etUserAge.setText(Integer.toString(sharedpref.getInt("userage")));
    }

    private void updatespinner() {
        ArrayAdapter<CharSequence> charSequenceArrayAdapter=ArrayAdapter.createFromResource(this,R.array.Titles,android.R.layout.simple_spinner_item);
        spTitle.setAdapter(charSequenceArrayAdapter);
        if(!Rtitle.equals(null))
        {
            int i=charSequenceArrayAdapter.getPosition(Rtitle);
            spTitle.setSelection(i);
        }
    }

    private void setupspinner() {
        spTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                title= (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Toolbar toolbar ;
    private void setUptoolbar() {

        toolbar= (Toolbar) findViewById(R.id.bottomToolBar);
        TextView textView= (TextView) toolbar.findViewById(R.id.txt_bot);
        textView.setText("Save & Exit");

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title= (String) spTitle.getSelectedItem();
                fname = etUserFirstName.getText().toString().trim();
                lname = etUserLastName.getText().toString().trim();
                phone = etUserPhoneNo.getText().toString().trim();
                ages = etUserAge.getText().toString().trim();
                age = (!ages.equals("") || !ages.isEmpty()) ? Integer.parseInt(ages) : 0;
                RetroFitApis fitApis=RetrofitApiBuilder.getRetrofitGlobal();
                if(age>10 && age<=99)
                {
                    if(isValidMobile(phone))
                    {
                        Call<ApiResponse> call = fitApis.update_profile(token, title, fname, lname, phone, age, userId);
                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().status == true) {
                                        Utility.message(AccountDetailsActivity.this, response.body().msg);
                                        sharedpref.putString("usertitle",title);
                                        sharedpref.putString("user_name", fname);
                                        sharedpref.putString("user_lname", lname);
                                        sharedpref.putString("user_phone", phone);
                                        sharedpref.putInt("userage", age);
                                        finish();
                                    } else {
                                        Utility.message(AccountDetailsActivity.this, response.body().msg);
                                    }
                                } else {
                                    Utility.message(AccountDetailsActivity.this, "Getting Some Error");
                                }
                            }
                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Utility.message(AccountDetailsActivity.this, "Connection Error");
                            }
                        });
                    }
                    else
                    {
                        Utility.message(AccountDetailsActivity.this, "Enter valid Phone Number");
                    }
                }
                else
                {
                    Utility.message(AccountDetailsActivity.this, "Enter Valid Age");
                }
            }
        });

    }
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle("Account Details");

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
