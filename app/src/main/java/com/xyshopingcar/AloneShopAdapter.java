package com.xyshopingcar;

import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by Xia_焱 on 2020/5/26.
 * 邮箱：xiaohaotianV@163.com
 * 单商家的购物车适配器
 */
public class AloneShopAdapter extends BaseQuickAdapter<AloneShopBean, BaseViewHolder> {

    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private TextView tvNumber;

    public AloneShopAdapter(int layoutResId, @Nullable List<AloneShopBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AloneShopBean item) {
        final CheckBox singleCheckBox = helper.getView(R.id.single_check_box);
        ImageView ivAdd = helper.getView(R.id.iv_add);
        ImageView ivMinus = helper.getView(R.id.iv_minus);
        tvNumber = helper.getView(R.id.tv_number);
        tvNumber.setText(String.valueOf(item.getNum()));
        helper.setText(R.id.tv_goods_name, item.getName())
                .setText(R.id.tv_describe, item.getDescribe());

        String price = item.getMoney();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("¥").append(price);
        SpannableString spannableString = new SpannableString(stringBuffer);
        AbsoluteSizeSpan absoluteSizeSpan17 = new AbsoluteSizeSpan(AutoSizeUtils.sp2px(mContext, 17));
        AbsoluteSizeSpan absoluteSizeSpan14 = new AbsoluteSizeSpan(AutoSizeUtils.sp2px(mContext, 14));
        spannableString.setSpan(absoluteSizeSpan17, 0, stringBuffer.indexOf("."), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(absoluteSizeSpan14, stringBuffer.indexOf("."), stringBuffer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.price, spannableString);

        singleCheckBox.setChecked(item.isChecked());
        singleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setChecked(((CheckBox) v).isChecked());
                singleCheckBox.setChecked(((CheckBox) v).isChecked());
                checkInterface.checkGroup(helper.getLayoutPosition(), ((CheckBox) v).isChecked());
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(helper.getLayoutPosition(), tvNumber, singleCheckBox.isChecked());
            }
        });
        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(helper.getLayoutPosition(), tvNumber, singleCheckBox.isChecked());
            }
        });
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

    }


    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素的位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, View showCountView, boolean isChecked);

        void doDecrease(int groupPosition, View showCountView, boolean isChecked);


        /**
         * 删除子Item
         *
         * @param groupPosition
         *
         */
        void childDelete(int groupPosition);
    }


}
