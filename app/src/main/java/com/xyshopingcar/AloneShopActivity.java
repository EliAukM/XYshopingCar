package com.xyshopingcar;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zhouwei.library.CustomPopWindow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xia_焱 on 2020/5/26.
 * 邮箱：xiaohaotianV@163.com
 * 单商家购物车
 * 一般购物车都是在Fragment中 把Activity中代码挪到Fragment同样适用
 */
public class AloneShopActivity extends AppCompatActivity implements View.OnClickListener, AloneShopAdapter.CheckInterface, AloneShopAdapter.ModifyCountInterface {

    private TextView tvGoodsMoney;
    private TextView tvMoney;
    private CheckBox allCheckBox;
    private RelativeLayout rl_no_data_a;
    private RelativeLayout rl_no_data_b;
    private LinearLayout llView;
    private double mtotalPrice = 0.0;
    private AloneShopAdapter adapterX;
    private List<AloneShopBean> list;
    private CustomPopWindow mCustomPopWindowCause;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alone_shop);
        initView();

    }


    private void initView() {
        initData();
        initAdapter();
        tvGoodsMoney = findViewById(R.id.tv_goods_money);
        tvMoney = findViewById(R.id.tv_money);
        allCheckBox = findViewById(R.id.all_checkBox);

        rl_no_data_a = findViewById(R.id.rl_no_data_a);
        rl_no_data_b = findViewById(R.id.rl_no_data_b);

        allCheckBox.setOnClickListener(this);
        llView = findViewById(R.id.ll_layout);


    }


    private void initData() {
        list = new ArrayList<>();
        list.add(new AloneShopBean("1", "架豆王", "辽宁沈阳新鲜蔬菜", 1, "30.00"));
        list.add(new AloneShopBean("2", "架豆王(精品)", "新鲜蔬菜", 1, "50.00"));
        list.add(new AloneShopBean("3", "架豆王", "蔬菜不隔夜，联系方式xxx", 2, "25.00"));
        list.add(new AloneShopBean("4", "架豆王(良品)", "欢迎采购", 1, "35.00"));
        list.add(new AloneShopBean("5", "架豆王", "蔬菜不隔夜，联系方式xxx", 2, "25.00"));


    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {

        RecyclerView rcList = findViewById(R.id.rc_list);
        rcList.setLayoutManager(new LinearLayoutManager(AloneShopActivity.this));
        adapterX = new AloneShopAdapter(R.layout.item_shop_car, list);
        adapterX.setCheckInterface(this);
        adapterX.setModifyCountInterface(this);
        rcList.setAdapter(adapterX);
        //长按删除
        adapterX.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ISDelete(position);
                return true;

            }
        });

    }

    private void ISDelete(final int position) {

        final View contentView = LayoutInflater.from(AloneShopActivity.this).inflate(R.layout.dialog_windows_delete, null);
        mCustomPopWindowCause = new CustomPopWindow.PopupWindowBuilder(AloneShopActivity.this)
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.6f) // 控制亮度
                .enableOutsideTouchableDissmiss(true)
                .create();
        mCustomPopWindowCause.showAtLocation(llView, Gravity.CENTER, 0, 0);
        contentView.findViewById(R.id.dialog_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomPopWindowCause.dissmiss();
            }
        });
        contentView.findViewById(R.id.dialog_btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                mCustomPopWindowCause.dissmiss();
                adapterX.notifyDataSetChanged();

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_checkBox:
                doCheckAll();
                break;

            case R.id.tv_close_k:
                //结算逻辑
                break;
        }


    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        list.get(groupPosition).setChecked(isChecked);

        if (isCheckAll()) {
            allCheckBox.setChecked(true);//全选
        } else {
            allCheckBox.setChecked(false);//反选
        }

        adapterX.notifyDataSetChanged();

        calulate();
    }


    /**
     * @return 判断组元素是否全选
     */
    private boolean isCheckAll() {

        for (AloneShopBean group : list) {
            if (!group.isChecked()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void doIncrease(int groupPosition, View showCountView, boolean isChecked) {
        AloneShopBean good = adapterX.getData().get(groupPosition);
        int count = good.getNum();
        count++;
        good.setNum(count);
        ((TextView) showCountView).setText(String.valueOf(count));
        adapterX.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void doDecrease(int groupPosition, View showCountView, boolean isChecked) {
        AloneShopBean good = adapterX.getData().get(groupPosition);
        int count = good.getNum();
        if (count == 1) {
            return;
        }
        count--;
        good.setNum(count);
        ((TextView) showCountView).setText("" + count);
        adapterX.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void childDelete(int groupPosition) {
        calulate();
    }

    private void doCheckAll() {
        for (int i = 0; i < list.size(); i++) {
            AloneShopBean group = list.get(i);
            group.setChecked(allCheckBox.isChecked());
        }
        adapterX.notifyDataSetChanged();
        calulate();
    }


    private void calulate() {
        mtotalPrice = 0.00;

        for (int i = 0; i < list.size(); i++) {
            AloneShopBean goods = list.get(i);
            if (goods.isChecked()) {
                BigDecimal b1 = new BigDecimal(goods.getMoney());
                BigDecimal b2 = new BigDecimal(goods.getNum() + "");
                mtotalPrice += b1.multiply(b2).doubleValue();
            }

        }
        String format = String.format("%.2f", mtotalPrice);
        tvMoney.setText("¥ " + format);
        tvGoodsMoney.setText("商品合计: ¥ " + format);

    }

}
