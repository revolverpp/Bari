package revolver.bari;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Locale;

import revolver.bari.adapters.Item;
import revolver.bari.adapters.NavigationMenuAdapter;
import revolver.bari.constants.Places;
import revolver.bari.fragments.CreditsFragment;
import revolver.bari.fragments.MapsFragment;
import revolver.bari.fragments.PlacesFragment;
import revolver.bari.fragments.WelcomeFragment;
import revolver.bari.utils.Assets;
import revolver.bari.utils.Metrics;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout layout;
    private NavigationView navi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // initialize stuff
        Assets.init(this);
        Metrics.init(this);
        Places.init(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new WelcomeFragment())
                .commit();

        layout = (DrawerLayout) findViewById(R.id.activity_main);
        layout.setStatusBarBackground(R.color.colorPrimaryDark);

        navi = (NavigationView) findViewById(R.id.navi);
        setupNavi();
    }

    private void setupNavi() {
        navi.removeAllViews();

        View v = View.inflate(this, R.layout.custom_navi, null);

        LinearLayout header = (LinearLayout) v.findViewById(R.id.custom_navi_header);
        ListView listView = (ListView) v.findViewById(R.id.custom_navi_list);
        final Item[] items = new Item[] {
                new Item(getString(R.string.menu_welcome), R.drawable.ic_home, false, true),
                new Item(getString(R.string.menu_places), R.drawable.ic_account_balance, false, true),
                new Item(getString(R.string.menu_map), R.drawable.ic_map, true, true),
                new Item(getString(R.string.language), R.drawable.ic_action_globe, false, false),
                new Item(getString(R.string.credits), R.drawable.ic_action_help, false, true)
        };

        header.setBackground(
                new BitmapDrawable(getResources(), BitmapFactory.decodeStream(
                        Assets.open("header.jpg")
                ))
        );

        final NavigationMenuAdapter adapter = new NavigationMenuAdapter(this, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items[position].checkable) {
                    adapter.setSelected(position);
                    adapter.notifyDataSetChanged();
                }
                MainActivity.this.onNavigationItemSelected(items[position].res);
            }
        });

        navi.addView(v);
    }

    // reading Configuration.locale is deprecated,
    // but Configuration.getLocales() requires api 24
    @SuppressWarnings("deprecation")
    public boolean onNavigationItemSelected(int iconRes) {
        switch (iconRes) {
            case R.drawable.ic_home:
                layout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.frame, new WelcomeFragment())
                        .commit();
                break;
            case R.drawable.ic_account_balance:
                layout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new PlacesFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            case R.drawable.ic_map:
                layout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new MapsFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            case R.drawable.ic_action_help:
                layout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new CreditsFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            case R.drawable.ic_action_globe:
                Locale currentLocale = getResources().getConfiguration().locale;
                if (currentLocale.getLanguage().equals(Locale.ENGLISH.getLanguage()))
                    changeLocale(Locale.ITALIAN, true);
                else if (currentLocale.getLanguage().equals(Locale.ITALY.getLanguage()))
                    changeLocale(Locale.ENGLISH, true);
                break;
            default:
                return false;
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    private void changeLocale(Locale locale, boolean reboot) {
        Locale.setDefault(locale);
        Configuration newDefault = new Configuration();
        newDefault.locale = locale;
        getApplicationContext().getResources().updateConfiguration(newDefault, null);

        if (reboot)
            startActivity(new Intent(this, MainActivity.class));
    }
}
