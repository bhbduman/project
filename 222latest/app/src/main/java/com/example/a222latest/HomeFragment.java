package com.example.a222latest;

import android.app.SearchManager;
//import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;


/**
 * A simple {@link Fragment} subclass.
 * home fragment to view all posts
 */
public class HomeFragment extends Fragment {
    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    PriorityQueue<ModelPost> postList;
    List<ModelPost> queueToList = new LinkedList<ModelPost>();
    List<ModelPost> searchList = new LinkedList<ModelPost>();
    AdapterPost adapterPost;
    ModelPost postTemp;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    LinearLayoutManager layoutManager;

    /**
     * no parameter constructor
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     *To view fragment
     * @param inflater to get layout of home fragment
     * @param container of the datas
     * @param savedInstanceState used for passing data between various Android activities
     * @return returns the view of fragment
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //logIn();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //init
        firebaseAuth = FirebaseAuth.getInstance();
        //init post list
        postList = new PriorityQueue<ModelPost>();
        //recycler view and its properties
        recyclerView = view.findViewById(R.id.postRecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //set adapter to recycler
        //show newest post first, for this from load last
        layoutManager.setReverseLayout(true);

        //setlayout to recycler


        loadPosts();
        layoutManager.setReverseLayout(true);
        return view;
    }

    /**
     * loads the post and from database
     * first collect the into priority queue according to time then send it to adapter with in linkedlist
     */
    private void loadPosts() {
        adapterPost = new AdapterPost(getActivity(), queueToList);
        recyclerView.setAdapter(adapterPost);
        //path of all posts
        layoutManager.setReverseLayout(true);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                    //adapter
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                }
                Iterator<ModelPost> iter = postList.iterator();
                while (iter.hasNext()) {
                    postTemp = iter.next();
                    queueToList.add(postTemp);
                }
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                System.out.println(postList + "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                adapterPost.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //if there is an error
                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * search posts witin linked list which is loaded from load post
     */
    public void searchPost(String searchQuery) {
        //path of all posts
        searchList.clear();
        adapterPost = new AdapterPost(getActivity(), searchList);
        recyclerView.setAdapter(adapterPost);
        layoutManager.setReverseLayout(false);
        //path of all posts

        for (ModelPost modelPost : queueToList) {

            if (modelPost.getpTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
                searchList.add(modelPost);
            }
//                    recyclerView.setAdapter(adapterPost);
            adapterPost.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
        }


    }

    //Check user status

    /**
     * menu to creating search bar
     * @param menu menu layout
     * @param inflater to get menu layout
     */
    //     @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_post, menu);
        MenuItem item = menu.findItem(R.id.post_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query.trim())) {
                    searchPost(query);
                } else {
                    loadPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText.trim())) {
                    searchPost(newText);
                } else {
                    loadPosts();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * initial state sets the menu options true
     * @param savedInstanceState to passing  data another activity or class
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    /**
     * calls super method of menu seleted
     * @param item the item in menu
     * @return the super method
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // int id = item.getItemId();
        //if (id == R.id.post_search) {
        // Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
        //  return true;
        //}
        // searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }


}
