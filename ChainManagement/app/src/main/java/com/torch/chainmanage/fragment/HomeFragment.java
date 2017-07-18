package com.torch.chainmanage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.torch.chainmanage.R;
import com.torch.chainmanage.activity.LoginActivity;


/**
 * 首页--默认展示
 */

public class HomeFragment extends BaseFragment {

    private View view;

    public static HomeFragment newInstance(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NAME, name);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView - " + mFragmentName);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView content = (TextView) view.findViewById(R.id.tv);
        content.setText(mFragmentName);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return view;
    }
}
