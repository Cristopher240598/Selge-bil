package com.example.selge_bil.buyerOptions.userManual;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.example.selge_bil.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserManualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserManualFragment extends Fragment
{

    VideoView vVVideo;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserManualFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserManualFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserManualFragment newInstance(String param1, String param2)
    {
        UserManualFragment fragment = new UserManualFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_manual, container, false);
        vVVideo = root.findViewById(R.id.vV_video_manual);
        vVVideo.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.manual_comprador));
        MediaController mediaController = new MediaController(root.getContext());
        vVVideo.setMediaController(mediaController);
        vVVideo.start();
        return root;
    }
}