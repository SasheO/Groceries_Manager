package com.example.groceriesmanager.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.Activities.LoginActivity;
import com.example.groceriesmanager.Activities.MainActivity;
import com.example.groceriesmanager.R;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class UserProfileFragment extends Fragment {
    TextView tvProfileUsername;
    Button btnLogout;
    RelativeLayout rlMyRecipes;
    RelativeLayout rlSavedRecipes;
    RelativeLayout rlSavedVideos;
    RecyclerView rvMyRecipes;
    RecyclerView rvSavedRecipes;
    RecyclerView rvSavedVideos;
    ImageButton ibExpandMyRecipes;
    ImageButton ibExpandSavedRecipes;
    ImageButton ibExpandSavedVideos;
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
        rlMyRecipes = (RelativeLayout) view.findViewById(R.id.rlMyRecipes);
        rlSavedRecipes = (RelativeLayout) view.findViewById(R.id.rlSavedRecipes);
        rlSavedVideos = (RelativeLayout) view.findViewById(R.id.rlSavedVideos);
        rvMyRecipes = (RecyclerView) view.findViewById(R.id.rvMyRecipes);
        rvSavedRecipes = (RecyclerView) view.findViewById(R.id.rvSavedRecipes);
        rvSavedVideos = (RecyclerView) view.findViewById(R.id.rvSavedVideos);
        ibExpandSavedVideos = (ImageButton) view.findViewById(R.id.ibExpandSavedVideos);
        ibExpandSavedRecipes = (ImageButton) view.findViewById(R.id.ibExpandSavedRecipes);
        ibExpandMyRecipes = (ImageButton) view.findViewById(R.id.ibExpandMyRecipes);

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

        rlMyRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri;
                if (rvMyRecipes.getVisibility() == View.GONE){
                    rvMyRecipes.setVisibility(View.VISIBLE);
                    uri = "@drawable/collapse_arrow";  // where myresource (without the extension) is the file
                }
                else{
                    rvMyRecipes.setVisibility(View.GONE);
                    uri = "@drawable/expand_arrow";  // where myresource (without the extension) is the file
                }
                // set image resource
                int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                ibExpandMyRecipes.setImageDrawable(res);
            }
        });
        rlSavedRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri;
                if (rvSavedRecipes.getVisibility() == View.GONE){
                    rvSavedRecipes.setVisibility(View.VISIBLE);
                    uri = "@drawable/collapse_arrow";  // where myresource (without the extension) is the file
                }
                else{
                    rvSavedRecipes.setVisibility(View.GONE);
                    uri = "@drawable/expand_arrow";  // where myresource (without the extension) is the file
                }
                // set image resource
                int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                ibExpandSavedRecipes.setImageDrawable(res);
            }
        });
        rlSavedVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri;
                if (rvSavedVideos.getVisibility() == View.GONE){
                    rvSavedVideos.setVisibility(View.VISIBLE);
                    uri = "@drawable/collapse_arrow";  // where myresource (without the extension) is the file
                }
                else {
                    rvSavedVideos.setVisibility(View.GONE);
                    uri = "@drawable/expand_arrow";  // where myresource (without the extension) is the file
                }
                // set image resource
                int imageResource = getResources().getIdentifier(uri, null, getContext().getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                ibExpandSavedVideos.setImageDrawable(res);
            }
        });

    }

    private void goToLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}
