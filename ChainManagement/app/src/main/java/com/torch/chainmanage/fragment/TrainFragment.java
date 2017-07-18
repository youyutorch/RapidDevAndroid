package com.torch.chainmanage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.torch.chainmanage.R;

/**
 * 培训
 */
public class TrainFragment extends BaseFragment{

	private View view;

	public static TrainFragment newInstance(String name) {
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_NAME, name);
		TrainFragment fragment = new TrainFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_train, container, false);
		TextView content = (TextView) view.findViewById(R.id.tv);
		content.setText(mFragmentName);
		return view;
	}
}
