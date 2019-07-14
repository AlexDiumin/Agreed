package com.example.agreed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Random;

public class AuthorizationActivity extends Activity {

    /* Facebook */
    CallbackManager callbackManager; // менеджер обработки откликов входа facebook
    LoginButton facebookButton;

    /* Google */
    GoogleSignInClient mGoogleSignInClient;
    final int RC_SIGN_IN_GOOGLE = new Random().nextInt();
    final String TAG = "SIGN_IN_WITH_GOOGLE";
    final String OAUTH_CLIENT_ID = "704683976153-74oarg5o4j88a48enl5qts5gqo5h912b.apps.googleusercontent.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__autorization);

        /*
         * Facebook
         */
        callbackManager = CallbackManager.Factory.create(); // обработка откликов входа facebook

        facebookButton = findViewById(R.id.facebook_button);
        facebookButton.setPermissions("email");

        // Callback registration
        facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
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

        // проверка статуса входа (facebook)
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        /*
         * Google
         */
        // настройка входа в Google для запроса openid, email, profile (DEFAULT_SIGN_IN)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(OAUTH_CLIENT_ID) // ???
                .build();
        // создаем GoogleSignInClient с параметрами, указанными в gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        /* настройка кнопки входа с помощью Google */
        SignInButton googleButton = findViewById(R.id.google_button);
        // googleButton.setSize(SignInButton.SIZE_STANDARD); // установка размера
        googleButton.setOnClickListener(new View.OnClickListener() // установка слушателя для кнопки Google
        {
            @Override
            public void onClick(View view)
            {
                switch (view.getId())
                {
                    case R.id.google_button:
                        signInWithGoogle();
                        break;
                    // ...
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        /* Google */
        // проверка выполнял ли пользователь вход ранее
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data); /* Facebook */
        super.onActivityResult(requestCode, resultCode, data);

        /* Google */
        // Результат, полученный при запуске Intent из GoogleSignInClient.getSignInIntent (...)
        if (requestCode == RC_SIGN_IN_GOOGLE)
        {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /* Google */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account); // вход с помощью Google успешен
        }
        catch (ApiException e) // вход с помощью Google не успешен
        {
            // код состояния ApiException указывает подробную причину сбоя
            // обратитесь к ссылке на класс GoogleSignInStatusCodes для получения дополнительной информации
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    // выполняется при нажатии на вход с помощью Google
    public void signInWithGoogle()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    // обновление интерфейса при входе
    public void  updateUI(GoogleSignInAccount account)
    {
        if(account != null){
            // если вход с помощью Google неуспешен
        }
        else {
            // если вход с помощью Google успешен
        }
    }
}
