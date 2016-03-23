package com.example.ivansv.fittest.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ivansv.fittest.R;
import com.example.ivansv.fittest.RecyclerViewAdapter;
import com.example.ivansv.fittest.model.Datum;

public class ListFragment extends Fragment {
    private OnListFragmentInteractionListener interactionListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerViewContacts = (RecyclerView) rootView.findViewById(R.id.list);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.datums, interactionListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewContacts.setAdapter(adapter);
        recyclerViewContacts.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            interactionListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Datum datum);
    }
}
