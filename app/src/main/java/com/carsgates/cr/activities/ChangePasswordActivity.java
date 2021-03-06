package com.carsgates.cr.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.carsgates.cr.R;
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

public class ChangePasswordActivity extends AppBaseActivity {
    TextInputEditText tiEt_currentpass,tiEt_newpass,tiEt_confirmpass;
    TextInputLayout layoutCurrentpass,layoutNewpass,layoutConfirmpass;
    private TinyDB sherprf;
    private String token;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        sherprf = new TinyDB(getApplicationContext());
        token= sherprf.getString("access_token") ;
        userId = sherprf.getString("userid");
        //Handling Layout EditText
        layoutCurrentpass= (TextInputLayout) findViewById(R.id.lay_current_pass);
        layoutNewpass= (TextInputLayout) findViewById(R.id.lay_new_pass);
        layoutConfirmpass=(TextInputLayout) findViewById(R.id.lay_confirm_pass);

        //Handing Edit Text
        tiEt_currentpass= (TextInputEditText) findViewById(R.id.tietxt_current_pass);
        tiEt_newpass=(TextInputEditText) findViewById(R.id.tietxt_new_pass);
        tiEt_confirmpass=(TextInputEditText) findViewById(R.id.tietxt_confirmnew_pass);


        actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);

        }
        setuptoolbar();
    }
    private void setuptoolbar() {
        final Toolbar toolbar= (Toolbar) findViewById(R.id.bottomToolBar);
        TextView textView= (TextView) toolbar.findViewById(R.id.txt_bot);
        textView.setText("Save Changes");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPass = tiEt_currentpass.getText().toString().trim() ;
                String newPass =  tiEt_newpass.getText().toString().trim() ;
                String newConfirmPass = tiEt_confirmpass.getText().toString().trim() ;
                if(!currentPass.isEmpty() && !newPass.isEmpty() && !newConfirmPass.isEmpty() && newPass.equals(newConfirmPass))
                {
                    changePassword(currentPass,newPass);
                    Snackbar.make(v, "Saved Successfully", Snackbar.LENGTH_LONG).setAction("Finish",
                            new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }
                else {
//layoutCurrentpass.setError("Please Check");
                    tiEt_currentpass.setError("Check your Current password ");
                    tiEt_confirmpass.setError("Check your Confirm password ");
                    tiEt_newpass.setError("Check your New password ");
                    //tiEt_currentpass.setBackgroundColor(getResources().getColor(R.color.white));

                }
            }
        });
    }

    private void changePassword(String currentPass, String newPass) {
        RetroFitApis retroFitApis = RetrofitApiBuilder.getRetrofitGlobal() ;

        Call<ApiResponse> responseCall = retroFitApis.change_pass(token,currentPass,newPass,userId) ;
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Toast.makeText(ChangePasswordActivity.this, response.body().msg, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle("Change your password");
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
