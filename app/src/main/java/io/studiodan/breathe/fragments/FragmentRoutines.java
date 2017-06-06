package io.studiodan.breathe.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.studiodan.breathe.ActivityAddRoutine;
import io.studiodan.breathe.BuildConfig;
import io.studiodan.breathe.R;
import io.studiodan.breathe.models.routines.AdapterTimeline;
import io.studiodan.breathe.models.routines.ITimer;
import io.studiodan.breathe.models.routines.ITimerJsonAdapter;
import io.studiodan.breathe.models.routines.TimerMonthlyDate;
import io.studiodan.breathe.models.routines.RoutineElement;
import io.studiodan.breathe.models.routines.RoutineInstance;
import io.studiodan.breathe.util.UtilFile;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class FragmentRoutines extends Fragment
{
    public static List<RoutineElement> mRoutines = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private AdapterTimeline mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton mFABAdd;

    private String mFilePath;

    private AdView mAdView;


    Gson gsonObj;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mFilePath = getContext().getFilesDir() + "/routine_data.json";

        {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ITimer.class, new ITimerJsonAdapter());
            gsonObj = builder.create();
        }

        //Load the serialized routines
        try
        {
            String data = UtilFile.getStringFromFile(mFilePath);

            Type collectionType = new TypeToken<ArrayList<RoutineElement>>(){}.getType();

            mRoutines = gsonObj.fromJson(data, collectionType);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        //TODO Create actual system for creating routines
        //mRoutines.add(new RoutineElement("Test item 0", new TimerMonthlyDate(3*60)));
        //mRoutines.add(new RoutineElement("Test item 1", new TimerMonthlyDate(33), new TimerMonthlyDate(10*60 + 45)));
        //mRoutines.add(new RoutineElement("Test item 2", new TimerMonthlyDate(17*60 + 5)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_routines, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_routines);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFABAdd = (FloatingActionButton) rootView.findViewById(R.id.fab_add_todo);
        mFABAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Transition to add screen
                Intent intent = new Intent(getActivity(), ActivityAddRoutine.class);
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
            List<RoutineInstance> tempL = new ArrayList<RoutineInstance>();

            for(RoutineElement re : mRoutines)
            {
                tempL.addAll(re.getInstancesForDay(Calendar.getInstance()));
            }

            mAdapter = new AdapterTimeline(tempL, getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * Write the current state of the routines to disk
     */
    @Override
    public void onPause()
    {
        super.onPause();

        saveData();
    }

    private void saveData()
    {
        String jsonOut = gsonObj.toJson(mRoutines);
        FileOutputStream outputStream;

        Log.d("Breathe", "FilePath:    " + mFilePath);

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




