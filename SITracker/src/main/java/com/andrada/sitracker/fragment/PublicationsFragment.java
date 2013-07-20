package com.andrada.sitracker.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.andrada.sitracker.R;
import com.andrada.sitracker.contracts.AuthorMarkedAsReadListener;
import com.andrada.sitracker.fragment.adapters.PublicationsAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_publications)
public class PublicationsFragment extends SherlockFragment implements AuthorMarkedAsReadListener, ExpandableListView.OnChildClickListener {

    @Bean
    PublicationsAdapter adapter;

    @ViewById(R.id.publication_list)
    ExpandableListView mListView;

    @InstanceState
    long mCurrentId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @AfterViews
    void bindAdapter() {
        mListView.setAdapter(adapter);
        mListView.setOnChildClickListener(this);
        mListView.setOnItemLongClickListener(adapter);
        updatePublicationsView(mCurrentId);
    }

    public void updatePublicationsView(long id) {
        mCurrentId = id;
        adapter.reloadPublicationsForAuthorId(id);
    }

    @Override
    public void onAuthorMarkedAsRead(long authorId) {
        if (mCurrentId == authorId) {
            //That means that we are viewing the current author
            //Just do a reload.
            updatePublicationsView(authorId);
        }
    }


    @Override
    public boolean onChildClick(ExpandableListView expandableListView,
                                View view, int groupPosition, int childPosition, long l) {
        //TODO redirect to other publication details fragment
        return false;
    }
}
