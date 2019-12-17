package com.braincloud.testauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BraincloudClarified";

    private GoogleSignInClient googleSignInClient;
    private SignInButton googleSignInButton;
    private Button signOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleSignInButton = findViewById(R.id.sign_in_button);
        signOut=findViewById(R.id.sign_out);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.GAMES))
                .requestServerAuthCode("47930100991-ndh6mp76o991cqp6gfo87b93jspbrael.apps.googleusercontent.com")
                .requestIdToken("47930100991-ndh6mp76o991cqp6gfo87b93jspbrael.apps.googleusercontent.com")
                .requestId()
                .requestProfile()
                .requestEmail()
//                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
//                .requestScopes(GoogleSignInOptions.zav)
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*
          Sign-out is initiated by simply calling the googleSignInClient.signOut API. We add a
          listener which will be invoked once the sign out is the successful
           */
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loggedOut();
                    }
                });
            }
        });
    }

    private void loggedOut() {
        Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Not Logged In", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Not logged in");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//        if (!task.isSuccessful()) {
//            Log.w(TAG, "signInWithCredential:failure", task.getException());
//        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task2 = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task2.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
        }
    }

    private String playerId;
    private String authCode;

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onLoggedIn");
        authCode = googleSignInAccount.getServerAuthCode();
        String id = googleSignInAccount.getId();
        String idToken = googleSignInAccount.getIdToken();
        Log.d(TAG,"Auth code: " + authCode);

        PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
        playersClient.getCurrentPlayer()
                .addOnSuccessListener(new OnSuccessListener<Player>() {
                    @Override
                    public void onSuccess(Player player) {
                        playerId = player.getPlayerId();
                        Log.d(TAG, "playerId " + playerId);
                        bcLogin();
                    }
                });

        int i = 0;
    }

    private void bcLogin() {
        Toast.makeText(this, "brainCloud login", Toast.LENGTH_LONG).show();
    }
}
