package jaber.hygieneapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jaber on 11/03/2016.
 */
public class Fragment_list extends Fragment {

    public Fragment_list() {
        //empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout with this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

}