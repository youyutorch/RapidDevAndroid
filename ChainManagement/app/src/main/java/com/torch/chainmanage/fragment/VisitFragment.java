package com.torch.chainmanage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.torch.chainmanage.R;

/**
 * 拜访
 */
public class VisitFragment extends BaseFragment {
    private View mView;

    public static VisitFragment newInstance(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NAME, name);
        VisitFragment fragment = new VisitFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_visit, container, false);
        TextView content = (TextView) mView.findViewById(R.id.tv);
        content.setText(mFragmentName);
        return mView;
    }
}
