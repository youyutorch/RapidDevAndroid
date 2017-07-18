package com.torch.chainmanage.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.torch.chainmanage.R;

/**
 * Fragment基类
 */

public class BaseFragment extends Fragment {
    public static final String BUNDLE_NAME = "fragment_name";
    protected Activity mActivity;
    protected String mFragmentName;
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach - " + mFragmentName);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mFragmentName = bundle.getString(BUNDLE_NAME);
        }
        Log.d(TAG, "[onCreate] - " + mFragmentName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView - " + mFragmentName);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated - " + mFragmentName);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView - " + mFragmentName);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy - " + mFragmentName);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach - " + mFragmentName);
        super.onDetach();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_in, 0);
    }
    
}
