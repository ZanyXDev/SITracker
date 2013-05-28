package com.andrada.sitracker.phoneactivities;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.andrada.sitracker.R;
import com.andrada.sitracker.fragment.PublicationsFragment;

import static com.andrada.sitracker.fragment.AuthorsFragment.OnAuthorSelectedListener;

/**
 * Created by ggodonoga on 27/05/13.
 */
public class PublicationsActivity extends SherlockFragmentActivity implements AuthorsFragment.OnAuthorSelectedListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        setContentView(R.layout.activity_main);

        if (extras != null) {
            // Take the info from the intent and deliver it to the fragment so it can update
            long authorId = extras.getLong(PublicationsFragment.ARG_ID);
            PublicationsFragment frag = (PublicationsFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_publications);
            frag.updatePublicationsView(authorId, this);
        }
    }

}
