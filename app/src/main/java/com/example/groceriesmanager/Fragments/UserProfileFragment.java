package com.example.groceriesmanager.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.groceriesmanager.Activities.LoginActivity;
import com.example.groceriesmanager.Activities.MainActivity;
import com.example.groceriesmanager.R;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class UserProfileFragment extends Fragment {
    TextView tvProfileUsername;
    Button btnLogout;
    private static final String TAG = "UserProfileFragment";

    // required empty constructor
    public UserProfileFragment() {}

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_user_profile, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        tvProfileUsername = (TextView) view.findViewById(R.id.tvProfileUsername);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);

        tvProfileUsername.setText(ParseUser.getCurrentUser().getUsername());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Log.e(TAG, "Error signing out", e);
                            Toast.makeText(getContext(), "Error signing out", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.i(TAG, "Sign out successful");
                        goToLoginActivity();
                        Toast.makeText(getContext(), "Signed out", Toast.LENGTH_SHORT).show();
                    }
                   }
                );
            }
        });
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}
