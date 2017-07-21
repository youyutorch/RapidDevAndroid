package com.torch.chainmanage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.torch.chainmanage.R;
import com.torch.chainmanage.util.ImageLoader;
import com.torch.chainmanage.view.SetItemView;

/**
 * 个人中心-主界面
 */
public class MeFragment extends BaseFragment {
    private View mView;
    private SetItemView mMeItem;
    private SetItemView mAboutItem;

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
        initView();
        return mView;
    }

    /**
     * 初始化控件信息
     */
    private void initView() {
        mMeItem = (SetItemView) mView.findViewById(R.id.rl_me);
        mAboutItem = (SetItemView) mView.findViewById(R.id.rl_about);

        mMeItem.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Toast.makeText(mActivity, "点击了个人资料", Toast.LENGTH_SHORT).show();
            }
        });
        mAboutItem.setmOnSetItemClick(new SetItemView.OnSetItemClick() {
            @Override
            public void click() {
                Toast.makeText(mActivity, "点击了关于", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
