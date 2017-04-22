package com.vishant.DentalJobVideo.fragment.employer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vishant.DentalJobVideo.R;
import com.vishant.DentalJobVideo.adapter.employer.MessagesRVAdapter;
import com.vishant.DentalJobVideo.model.recycleview.MessagesRVModel;
import com.vishant.DentalJobVideo.ui.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment {

    TextView tvHeaderApply;
    ImageView tvHeaderEdit;
    TextView tvHeaderTitle;
    TextView tvHeaderPostJob;
    Toolbar toolbar;
    RecyclerView tvJobRecyclerView;
    MessagesRVAdapter adapter;
    List<MessagesRVModel> messagesList;

    public final static String MESSAGES_FRAGMENT = "MessagesFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tvHeaderApply = (TextView) toolbar.findViewById(R.id.nav_actionbar_apply);
        tvHeaderApply.setVisibility(View.GONE);
        tvHeaderEdit = (ImageView) toolbar.findViewById(R.id.nav_actionbar_edit);
        tvHeaderEdit.setVisibility(View.GONE);
        tvHeaderPostJob = (TextView) toolbar.findViewById(R.id.nav_actionbar_postjob);
        tvHeaderPostJob.setVisibility(View.GONE);
        View view = inflater.inflate(R.layout.fragment_employer_messages, container, false);
        tvHeaderTitle = (TextView) view.findViewById(R.id.fragment_actionbar_text);
        tvHeaderTitle.setText(getResources().getString(R.string.messages_fragment));
        tvJobRecyclerView = (RecyclerView) view.findViewById(R.id.messages_employeer_recyclerview);
        messagesList = new ArrayList<MessagesRVModel>();

        initializeRecyclerView();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initializeRecyclerView(){
        MessagesRVModel jobSeeker1 = new MessagesRVModel(true, "Joe Smith", getString(R.string.hidden_resume_item_desc_dummytext) );
        MessagesRVModel jobSeeker2 = new MessagesRVModel(true, "Gary Jones",  getString(R.string.hidden_resume_item_desc_dummytext) );
        MessagesRVModel jobSeeker3 = new MessagesRVModel(false, "Jane Hunter",  getString(R.string.hidden_resume_item_desc_dummytext) );
        MessagesRVModel jobSeeker4 = new MessagesRVModel(false, "Paul Wright",  getString(R.string.hidden_resume_item_desc_dummytext) );
        MessagesRVModel jobSeeker5 = new MessagesRVModel(true, "Sue Anderson",  getString(R.string.hidden_resume_item_desc_dummytext) );
        MessagesRVModel jobSeeker6 = new MessagesRVModel(false, "Micheal John",  getString(R.string.hidden_resume_item_desc_dummytext) );
        messagesList.add(jobSeeker1);
        messagesList.add(jobSeeker2);
        messagesList.add(jobSeeker3);
        messagesList.add(jobSeeker4);
        messagesList.add(jobSeeker5);
        messagesList.add(jobSeeker6);
        adapter = new MessagesRVAdapter(messagesList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        tvJobRecyclerView.setLayoutManager(mLayoutManager);
        tvJobRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tvJobRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        tvJobRecyclerView.setAdapter(adapter);
    }
}
