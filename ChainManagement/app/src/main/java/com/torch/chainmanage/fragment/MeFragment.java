package com.torch.chainmanage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.torch.chainmanage.R;

/**
 * 个人中心-主界面
 */
public class MeFragment extends BaseFragment {
    private View mView;

    public static MeFragment newInstance(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NAME, name);
        MeFragment fragment = new MeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载布局
        mView = inflater.inflate(R.layout.fragment_me, container, false);
        TextView content = (TextView) mView.findViewById(R.id.tv);
        content.setText(mFragmentName);
        return mView;
    }
}
