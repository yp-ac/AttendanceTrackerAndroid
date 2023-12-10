package com.example.microproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;

// Glide for action bar
// https://stackoverflow.com/questions/48515066/set-icon-on-actionbar-using-glide#:~:text=First%2C%20we%20set%20getSupportActionBar(),setIcon(bitmap)%20or%20actionBar.

public class HomePage extends AppCompatActivity {
    private SearchBar searchBar;
    private SearchView searchView;
    MenuItem profile;
    RecyclerView recyclerView;
    ArrayList<SectionData> recyclerSectionData;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.sectionRV);
        recyclerSectionData = new ArrayList<>();

        recyclerSectionData.add(new SectionData("N3", "ODD2023"));
        recyclerSectionData.add(new SectionData("H3", "ODD2023"));
        recyclerSectionData.add(new SectionData("G3", "ODD2023"));
        recyclerSectionData.add(new SectionData("N2", "EVEN2023"));
        recyclerSectionData.add(new SectionData("H2", "ODD2023"));
        recyclerSectionData.add(new SectionData("G2", "EVEN2023"));
        recyclerSectionData.add(new SectionData("H1", "ODD2023"));

        searchBar = findViewById(R.id.search_bar);
        searchView = findViewById(R.id.search_view);

        Menu menu = searchBar.getMenu();
        profile = menu.findItem(R.id.profile);

        // Connect SearchView with SearchBar
        searchView.setupWithSearchBar(searchBar);

        Glide.with(this)
            .load(sharedPreferences.getString("PROFILE_URL", ""))
            .placeholder(R.drawable.profile_loading)
            .error(R.drawable.blue)
            .fallback(R.drawable.blue)
            .centerCrop()
            .circleCrop()
            .into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    profile.setIcon(resource);
                    Log.i("ICON", "done set icon");
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });

        searchView.getEditText().setOnEditorActionListener(
            (v, actionId, event) -> {
                searchBar.setText(searchView.getText());
                searchView.hide();
                return false;
            });

        profile.setOnMenuItemClickListener(menuItem -> {
            showModal();
            return false;
        });

        SectionRecyclerAdapter adapter = new SectionRecyclerAdapter(recyclerSectionData,this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    void showModal() {
        ProfileBottomSheetFragment profileBottomSheetFragment = new ProfileBottomSheetFragment();
        profileBottomSheetFragment.show(getSupportFragmentManager(), profileBottomSheetFragment.getTag());
    }
}