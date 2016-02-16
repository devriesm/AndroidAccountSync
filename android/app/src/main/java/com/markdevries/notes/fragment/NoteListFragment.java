package com.markdevries.notes.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.markdevries.notes.NotesApplication;
import com.markdevries.notes.R;
import com.markdevries.notes.account.AccountManager;
import com.markdevries.notes.adapter.NoteListAdapter;
import com.markdevries.notes.loader.ModelLoader;
import com.markdevries.notes.models.Note;
import com.markdevries.notes.utils.MenuTintUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by mark on 2/15/16.
 */
public class NoteListFragment extends Fragment {

    private static final String TAG = NoteListFragment.class.getSimpleName();

    private ListView mListView;
    private NoteListAdapter mAdapter;

    @Inject
    AccountManager mAccountManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((NotesApplication) getActivity().getApplication()).getNotesComponent().inject(this);
        initAdapter();
        initLoader();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuTintUtils.tintAllIcons(menu, getResources().getColor(R.color.colorAccent));
    }

    private void initAdapter() {
        mAdapter = new NoteListAdapter();
        mListView.setAdapter(mAdapter);
    }


    public void initLoader() {
        if (mAccountManager.getCurrentUser() == null) {
            return;
        }

        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<List<Note>>() {
            @Override
            public Loader<List<Note>> onCreateLoader(int id, Bundle args) {
                From from = new Select().from(Note.class).where("User=?", mAccountManager.getCurrentUser().getId());
                return new ModelLoader<Note>(getActivity(), Note.class, from, true);
            }

            @Override
            public void onLoadFinished(Loader<List<Note>> loader, List<Note> notes) {
                mAdapter.setNotes(notes);
            }

            @Override
            public void onLoaderReset(Loader<List<Note>> loader) {
                mAdapter.setNotes(new ArrayList<Note>());
            }
        });
    }
}
