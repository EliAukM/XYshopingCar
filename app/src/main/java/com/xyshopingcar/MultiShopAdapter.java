package com.xyshopingcar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by Xia_焱 on 2020/5/26.
 * 邮箱：xiaohaotianV@163.com
 * 多店铺仿淘宝购物车
 */
public class MultiShopAdapter extends BaseExpandableListAdapter {

    private List<StoreBean> groups;
    //这个String对应着StoreInfo的Id，也就是店铺的Id
    private Map<String, List<MultiGoodsBean>> childrens;
    private Context mcontext;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;

    public MultiShopAdapter(List<StoreBean> groups, Map<String, List<MultiGoodsBean>> childrens, Context mcontext) {
        this.groups = groups;
        this.childrens = childrens;
        this.mcontext = mcontext;
    }


    @Override
    public int getGroupCount() {
        return groups == null ? 0 : groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();
        return childrens.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groups.size() == 0) {

            return "";
        } else {
            return groups.get(groupPosition);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groups.size() == 0) {
            return "";
        } else {
            List<MultiGoodsBean> childs = childrens.get(groups.get(groupPosition).getId());
            return childs.get(childPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_shopcar_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (groups.size() != 0) {
            final StoreBean group = (StoreBean) getGroup(groupPosition);
            groupViewHolder.tvShopName.setText(group.getName());
            groupViewHolder.storeCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    group.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());
                }
            });
            groupViewHolder.storeCheckBox.setChecked(group.isChecked());

        } else {
            groupViewHolder.llTitle.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_shopcar_product, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        if (groups.size() != 0) {
            final MultiGoodsBean child = (MultiGoodsBean) getChild(groupPosition, childPosition);
            if (child != null) {
                childViewHolder.tvGoodsName.setText(child.getName());
                childViewHolder.tvMoney.setText("￥" + child.getMoney());
                childViewHolder.tvNumber.setText(child.getNum() + "");
                childViewHolder.singleCheckBox.setChecked(child.isChecked());


                childViewHolder.singleCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        child.setChecked(((CheckBox) v).isChecked());
                        childViewHolder.singleCheckBox.setChecked(((CheckBox) v).isChecked());
                        checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());
                    }
                });
                childViewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.doIncrease(groupPosition, childPosition, childViewHolder.tvNumber, childViewHolder.singleCheckBox.isChecked());
                    }
                });
                childViewHolder.ivMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyCountInterface.doDecrease(groupPosition, childPosition, childViewHolder.tvNumber, childViewHolder.singleCheckBox.isChecked());
                    }
                });

            }
        }else {
            childViewHolder.llTitleA.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    static class GroupViewHolder {
        CheckBox storeCheckBox;
        TextView tvDiscount;
        TextView tvShopName;
        LinearLayout llTitle;

        public GroupViewHolder(View view) {
            tvDiscount = view.findViewById(R.id.tv_discount);
            tvShopName = view.findViewById(R.id.tv_shop_name);
            storeCheckBox = view.findViewById(R.id.store_check_box);
            llTitle = view.findViewById(R.id.ll_title);
        }
    }

    static class ChildViewHolder {

        CheckBox singleCheckBox;
        RoundedImageView imageView;
        TextView tvGoodsName;
        TextView tvSpec;
        TextView tvMoney;
        ImageView ivMinus;
        TextView tvNumber;
        ImageView ivAdd;
        LinearLayout llTitleA;

        public ChildViewHolder(View view) {
            singleCheckBox = view.findViewById(R.id.single_check_box);
            imageView = view.findViewById(R.id.image_View);
            tvGoodsName = view.findViewById(R.id.tv_goods_name);
            tvSpec = view.findViewById(R.id.tv_spec);
            tvMoney = view.findViewById(R.id.tv_money);
            ivMinus = view.findViewById(R.id.iv_minus);
            tvNumber = view.findViewById(R.id.tv_number);
            ivAdd = view.findViewById(R.id.iv_add);
            llTitleA = view.findViewById(R.id.ll_title_a);
        }
    }



    public CheckInterface getCheckInterface() {
        return checkInterface;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public ModifyCountInterface getModifyCountInterface() {
        return modifyCountInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    /**
     * 店铺的复选框
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素的位置
         * @param isChecked     组元素的选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变触发的事件
         *
         * @param groupPosition 组元素的位置
         * @param childPosition 子元素的位置
         * @param isChecked     子元素的选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }


    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素的位置
         * @param childPosition 子元素的位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除子Item
         *
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition);
    }

}
