package edu.uoc.churtado.feelingloadapp.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.adapters.ViewPagerAdapter;
import edu.uoc.churtado.feelingloadapp.fragments.PlayersFragment;
import edu.uoc.churtado.feelingloadapp.fragments.TrainingsFragment;

public class MainCoachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_coach);

        //Initialize adapter to show tabs
        ViewPager viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Add Fragments to adapter one by one
        adapter.addFragment(new PlayersFragment(), "PLAYERS");
        adapter.addFragment(new TrainingsFragment(), "TRAININGS");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ListView listView = findViewById(R.id.menu);
        final String[] options = { "Logout" };

        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, options));
        // Set the list's click listener
        listView.setOnItemClickListener(new MainCoachActivity.DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //If menu item clicked, logout from Firebase
            logout();
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent    );
    }

    @Override
    public void onBackPressed() {

    }
}
