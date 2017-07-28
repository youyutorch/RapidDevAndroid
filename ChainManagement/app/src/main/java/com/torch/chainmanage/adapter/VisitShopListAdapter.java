package com.torch.chainmanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.torch.chainmanage.R;
import com.torch.chainmanage.model.ShopInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡店列表适配器
 */
public class VisitShopListAdapter extends RecyclerView.Adapter<VisitShopListAdapter.ShopViewHolder> {
    private Context mContext;
    private List<ShopInfo> list;

    public VisitShopListAdapter(Context mContxt, List<ShopInfo> list) {
        this.mContext = mContxt;
        this.list = list;
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.fragment_shop_item, null);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, final int position) {
        ShopInfo rd = list.get(position);
        holder.name.setText("店面名称：" + rd.getName());
        holder.where.setText("巡店地址：" + rd.getShoplocation());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "详情查看敬请期待", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView where, name;
        private RelativeLayout root;

        public ShopViewHolder(View itemView) {
            super(itemView);
            where = (TextView) itemView.findViewById(R.id.activity_visit_shop_item_where);
            name = (TextView) itemView.findViewById(R.id.activity_visit_shop_item_name);
            root = (RelativeLayout) itemView.findViewById(R.id.item_root_rl);
        }
    }

    public List<ShopInfo> getList() {
        return list;
    }

    public void setList(List<ShopInfo> list) {
        this.list = list;
        if (list == null) {
            list = new ArrayList<ShopInfo>();
        }
    }
}
