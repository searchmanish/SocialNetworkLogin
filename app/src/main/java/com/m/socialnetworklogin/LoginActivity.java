package com.m.socialnetworklogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private String TAG =" LoginActivity";
    LoginButton loginButton;
    CallbackManager callbackManager;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton=findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();

       // loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setReadPermissions("email","public_profile");

        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                String userId = loginResult.getAccessToken().getUserId();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                     displayUserInfo(object);
                    }
                });
                Bundle bundle =new Bundle();
                bundle.putString("fields","first_name,last_name,email,id");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }

    private void displayUserInfo(JSONObject object) {

        String first_name,last_name,email,id;
        first_name ="";
        last_name ="";
        email ="";
        id ="";
        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView tv_name,tv_email,tv_id;

        tv_name =findViewById(R.id.TV_name);
        tv_email = findViewById(R.id.TV_email);
        tv_id = findViewById(R.id.TV_id);
        tv_name.setText(first_name+" "+last_name);
        tv_email.setText("Email id =" +email);
        tv_id.setText("facebook id="+id);

        fbLogin( id, email, first_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void fbLogin(String user_id, String email, String username){



            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            //   Call<PlaceOrderActivity>  call = serviceWrapper.placceOrdercall("1234", SharePreferenceUtils.getInstance().getString(Constant.USER_DATA) )
            Call<Registration> call = serviceWrapper.fbRegister("1234",user_id, email, username );

            call.enqueue(new Callback<Registration>() {
                @Override
                public void onResponse(Call<Registration> call, Response<Registration> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {



                        }else {
                            //AppUtilits.displayMessage(PlaceOrderActivity.this, response.body().getMsg() );
                            Log.e(TAG, "  fail- get user address "+ response.body().getMsg());
                        }
                    }else {
                        //AppUtilits.displayMessage(PlaceOrderActivity.this, getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<Registration> call, Throwable t) {

                    Log.e(TAG, "  fail- get user address "+ t.toString());

                }
            });





    }
}
