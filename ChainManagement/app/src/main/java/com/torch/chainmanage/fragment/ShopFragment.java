package com.torch.chainmanage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.torch.chainmanage.R;

/**
 * 巡店
 */

public class ShopFragment extends BaseFragment {
    private View view;

    public static ShopFragment newInstance(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NAME, name);
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        TextView content = (TextView) view.findViewById(R.id.tv);
        content.setText(mFragmentName);
        return view;
    }
}
