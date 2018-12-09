package edu.uoc.churtado.feelingloadapp.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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
import edu.uoc.churtado.feelingloadapp.models.Coach;

public class MainCoachActivity extends AppCompatActivity {

    private Coach coach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_coach);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Add Fragments to adapter one by one
        adapter.addFragment(new PlayersFragment(), "PLAYERS");
        adapter.addFragment(new TrainingsFragment(), "TRAININGS");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ListView listView = (ListView) findViewById(R.id.menu);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final String[] options = { "Logout" };

        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, options));
        // Set the list's click listener
        listView.setOnItemClickListener(new MainCoachActivity.DrawerItemClickListener());
    }

    @Override
    public void onBackPressed() {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            logout();
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent    );
    }
}
