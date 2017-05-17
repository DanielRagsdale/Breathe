package io.studiodan.breathe.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.compat.BuildConfig;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

import io.studiodan.breathe.ActivityAddTask;
import io.studiodan.breathe.R;
import io.studiodan.breathe.models.AdapterToDo;
import io.studiodan.breathe.models.ToDoList;
import io.studiodan.breathe.util.UtilFile;

/**
 * Fragment representing ToDoList pane
 */
public class FragmentLifeList extends Fragment
{
    public static ToDoList topList = new ToDoList("all");

    private RecyclerView mRecyclerView;
    private AdapterToDo mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton mFABAdd;

    private String mFilePath;

    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mFilePath = getContext().getFilesDir() + "/tdl_data.json";

        //Load the serialized to do lists
        try
        {

//            Log.v("Breathe", "********** File Read ***********" + mFilePath);

            String data = UtilFile.getStringFromFile(mFilePath);
            JSONObject jsonData = new JSONObject(data);

            topList = new Gson().fromJson(data, ToDoList.class);
        }
        catch(Exception e)
        {
            //Log.d("Breathe", e.toString() + "   while loading To-Do lists");
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_life_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_to_do_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mFABAdd = (FloatingActionButton) rootView.findViewById(R.id.fab_add_todo);
        mFABAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Transition to add screen
                Intent intent = new Intent(getActivity(), ActivityAddTask.class);

                intent.putExtra(getString(R.string.single_ToDo_id), mAdapter.getSingleSelected());
                getActivity().startActivity(intent);
            }
        });


        if(!BuildConfig.BUILD_TYPE.equals("ad_free"))
        {
            mAdView = (AdView) rootView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     *  Set adapter for mRecyclerView
     */
    @Override
    public void onResume()
    {
        super.onResume();

        if(mRecyclerView != null)
        {
            // specify an adapter (see also next example)
            mAdapter = new AdapterToDo(topList, this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * Write the current state of the ToDoList to disk
     */
    @Override
    public void onPause()
    {
        super.onPause();

        String jsonOut = new Gson().toJson(topList);
        FileOutputStream outputStream;

        try
        {
            outputStream = new FileOutputStream (new File(mFilePath), false);
            outputStream.write(jsonOut.getBytes());
            outputStream.close();

        }
        catch (Exception e)
        {
            //Log.d("Breathe", e.toString() + "      while writing tdl");
            e.printStackTrace();
        }
    }
}
