package com.xyshopingcar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static in.srain.cube.views.ptr.util.PtrLocalDisplay.dp2px;

/**
 * Created by Xia_焱 on 2020/5/26.
 * 邮箱：xiaohaotianV@163.com
 * 多商家购物车
 */
public class MultiShopActivity extends AppCompatActivity implements View.OnClickListener, MultiShopAdapter.CheckInterface, MultiShopAdapter.ModifyCountInterface {

    private TextView headRight;
    private ExpandableListView listView;
    private CheckBox allCheckBox;
    private TextView tvPrice;
    private LinearLayout llCalculate;
    private RelativeLayout rlOrderInfo;
    private LinearLayout llDelete;
    private RelativeLayout rlDeleteInfo;
    private LinearLayout llCart;
    private PtrFrameLayout mPtrFrame;
    private LinearLayout llBottomView;
    //false就是编辑，ture就是完成
    private boolean flag = false;
    private double mtotalPrice = 0.0;
    private int mtotalCount = 0;
    private List<StoreBean> groups;
    private HashMap<String, List<MultiGoodsBean>> childs;
    private MultiShopAdapter adapterX;
    private LinearLayout empty_shopcart;
    //把商品ID 通过JSON形式传递给后台，如果有需要你会感谢我的
    private HashMap<String, String> hashMapGoodsId;
    private ArrayList<StoreBean> xc;//选中子类必有父类 所以判断父类

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_shop);
        initView();
        initPtrFrame();
        initData();
        initAdapter();
    }


    private void initPtrFrame() {
        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(this);
        header.setPadding(dp2px(20), dp2px(20), 0, 0);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 2000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }


    private void initView() {
        headRight = findViewById(R.id.head_right);
        headRight.setVisibility(View.VISIBLE);
        headRight.setOnClickListener(this);
        listView = findViewById(R.id.listView);
        allCheckBox = findViewById(R.id.all_checkBox);
        allCheckBox.setOnClickListener(this);

        tvPrice = findViewById(R.id.tv_price);
        llCalculate = findViewById(R.id.ll_calculate);
        llCalculate.setOnClickListener(this);

        rlOrderInfo = findViewById(R.id.rl_order_info);
        llDelete = findViewById(R.id.ll_delete);
        llDelete.setOnClickListener(this);

        rlDeleteInfo = findViewById(R.id.rl_delete_info);
        llCart = findViewById(R.id.ll_cart);
        mPtrFrame = findViewById(R.id.mPtrframe);
        llBottomView = findViewById(R.id.ll_bottom_view);
        empty_shopcart = findViewById(R.id.layout_empty_shop);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        hashMapGoodsId = new HashMap<>();
        xc = new ArrayList<>();//与Demo 无关
        groups = new ArrayList<StoreBean>();
        childs = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            groups.add(new StoreBean(i + "", "天字第" + i + "号店铺"));
            List<MultiGoodsBean> goods = new ArrayList<>();
            goods.add(new MultiGoodsBean("1", "架豆王", "辽宁沈阳新鲜蔬菜", 1, "30.00"));
            goods.add(new MultiGoodsBean("2", "架豆王(精品)", "新鲜蔬菜", 1, "50.00"));
            goods.add(new MultiGoodsBean("3", "架豆王", "蔬菜不隔夜，联系方式xxx", 2, "25.00"));
            goods.add(new MultiGoodsBean("4", "架豆王(良品)", "欢迎采购", 1, "35.00"));

            childs.put(groups.get(i).getId(), goods);
        }


    }


    private void initAdapter() {
        adapterX = new MultiShopAdapter(groups, childs, MultiShopActivity.this);
        adapterX.setCheckInterface(this);//关键步骤1：设置复选框的接口
        adapterX.setModifyCountInterface(this); //关键步骤2:设置增删减的接口
        listView.setGroupIndicator(null); //设置属性 GroupIndicator 去掉向下箭头
        listView.setAdapter(adapterX);
        for (int i = 0; i < adapterX.getGroupCount(); i++) {
            listView.expandGroup(i); //关键步骤4:初始化，将ExpandableListView以展开的方式显示
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int top = -1;
                View firstView = view.getChildAt(firstVisibleItem);
                if (firstView != null) {
                    top = firstView.getTop();
                }
                if (firstVisibleItem == 0 && top == 0) {
                    mPtrFrame.setEnabled(true);
                } else {
                    mPtrFrame.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        AlertDialog dialog;
        switch (v.getId()) {
            case R.id.head_right:
                flag = !flag;
                setVisibilityX();
                break;

            case R.id.all_checkBox:
                doCheckAll();
                break;

            case R.id.ll_calculate:
                GetGoodsId();
                if (xc.size() == 0) {
                    ToastUtilsX.showShortToast(MultiShopActivity.this, "请选择您要结算的商品！");
                    return;
                }
                //结算操作。。。。
                break;

            case R.id.ll_delete:
                if (mtotalCount == 0) {
                    ToastUtilsX.showLongToast(MultiShopActivity.this, "请选择要删除的商品!");
                    return;
                }
                dialog = new AlertDialog.Builder(MultiShopActivity.this).create();
                dialog.setMessage("确认要删除该商品吗?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDelete();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dialog.show();
                break;

        }
    }


    /**
     * @return 判断组元素是否全选
     */
    private boolean isCheckAll() {
        hashMapGoodsId.clear();
        for (StoreBean group : groups) {
            if (!group.isChecked()) {
                return false;
            }
        }
        return true;
    }


    /**
     * 删除操作
     * 1.不要边遍历边删除,容易出现数组越界的情况
     * 2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
     */
    private void doDelete() {
        List<StoreBean> toBeDeleteGroups = new ArrayList<StoreBean>(); //待删除的组元素
        for (int i = 0; i < groups.size(); i++) {
            StoreBean group = groups.get(i);
            if (group.isChecked()) {
                toBeDeleteGroups.add(group);
            }
            List<MultiGoodsBean> toBeDeleteChilds = new ArrayList<MultiGoodsBean>();//待删除的子元素
            List<MultiGoodsBean> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                if (child.get(j).isChecked()) {
                    toBeDeleteChilds.add(child.get(j));
                }
            }
            //获取购物车ID 与Demo 没有关系
            GetGoodsId();
            child.removeAll(toBeDeleteChilds);
        }
        groups.removeAll(toBeDeleteGroups);
        //重新设置购物车
        setCartNum();
        adapterX.notifyDataSetChanged();

    }


    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        hashMapGoodsId.clear();
        StoreBean group = groups.get(groupPosition);
        List<MultiGoodsBean> child = childs.get(group.getId());
        for (int i = 0; i < child.size(); i++) {
            child.get(i).setChecked(isChecked);
        }
        if (isCheckAll()) {
            allCheckBox.setChecked(true);//全选
        } else {
            allCheckBox.setChecked(false);//反选
        }
        adapterX.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        hashMapGoodsId.clear();
        boolean allChildSameState = true; //判断该组下面的所有子元素是否处于同一状态
        StoreBean group = groups.get(groupPosition);
        List<MultiGoodsBean> child = childs.get(group.getId());
        for (int i = 0; i < child.size(); i++) {
            //不选全中
            if (child.get(i).isChecked() != isChecked) {
                allChildSameState = false;
                break;
            }
        }

        if (allChildSameState) {
            group.setChecked(isChecked);//如果子元素状态相同，那么对应的组元素也设置成这一种的同一状态
        } else {
            group.setChecked(false);//否则一律视为未选中
        }

        if (isCheckAll()) {
            allCheckBox.setChecked(true);//全选
        } else {
            allCheckBox.setChecked(false);//反选
        }

        adapterX.notifyDataSetChanged();
        calulate();
    }


    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        MultiGoodsBean good = (MultiGoodsBean) adapterX.getChild(groupPosition, childPosition);
        int count = good.getNum();
        count++;
        good.setNum(count);
        ((TextView) showCountView).setText(String.valueOf(count));
        adapterX.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        MultiGoodsBean good = (MultiGoodsBean) adapterX.getChild(groupPosition, childPosition);
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
    public void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        MultiGoodsBean good = (MultiGoodsBean) adapterX.getChild(groupPosition, childPosition);
        int count = good.getNum();
        ((TextView) showCountView).setText(String.valueOf(count));
        adapterX.notifyDataSetChanged();
        calulate();
    }

    @Override
    public void childDelete(int groupPosition, int childPosition) {
        StoreBean group = groups.get(groupPosition);
        List<MultiGoodsBean> child = childs.get(group.getId());
        child.remove(childPosition);
        if (child.size() == 0) {
            groups.remove(groupPosition);
        }
        adapterX.notifyDataSetChanged();
        calulate();
    }

    /**
     * 全选和反选
     */
    private void doCheckAll() {
        for (int i = 0; i < groups.size(); i++) {
            StoreBean group = groups.get(i);
            group.setChecked(allCheckBox.isChecked());
            List<MultiGoodsBean> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                child.get(j).setChecked(allCheckBox.isChecked());
            }
        }
        adapterX.notifyDataSetChanged();
        calulate();
    }


    /**
     * 计算商品总价格，操作步骤
     * 1.先清空全局计价,计数
     * 2.遍历所有的子元素，只要是被选中的，就进行相关的计算操作
     * 3.给textView填充数据
     */
    private void calulate() {
        mtotalPrice = 0.00;
        mtotalCount = 0;
        for (int i = 0; i < groups.size(); i++) {
            StoreBean group = groups.get(i);
            List<MultiGoodsBean> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                MultiGoodsBean good = child.get(j);
                if (good.isChecked()) {
                    mtotalCount++;

                    BigDecimal b1 = new BigDecimal(good.getMoney());
                    BigDecimal b2 = new BigDecimal(good.getNum() + "");
                    mtotalPrice += b1.multiply(b2).doubleValue();

                }
            }
        }
        String resultX = String.format("%.2f", mtotalPrice);
        tvPrice.setText("￥" + resultX);


        if (mtotalCount == 0) {
            setCartNum();
        }

    }


    /**
     * 显示/隐藏
     */
    private void setVisibilityX() {
        if (flag) {
            rlOrderInfo.setVisibility(View.GONE);
            rlDeleteInfo.setVisibility(View.VISIBLE);
            headRight.setText("完成");
        } else {
            rlOrderInfo.setVisibility(View.VISIBLE);
            rlDeleteInfo.setVisibility(View.GONE);
            headRight.setText("管理");
        }
    }


    /**
     * 设置购物车的数量
     */
    private void setCartNum() {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            StoreBean group = groups.get(i);
            group.setChecked(allCheckBox.isChecked());
            List<MultiGoodsBean> Childs = childs.get(group.getId());
            for (MultiGoodsBean childs : Childs) {
                count++;
            }
        }

        //购物车已经清空
        if (count == 0) {
            clearCart();
        } else {
            headRight.setVisibility(View.VISIBLE);
            empty_shopcart.setVisibility(View.GONE);//这里发生过错误
            llBottomView.setVisibility(View.VISIBLE);
        }

    }


    private void clearCart() {
        empty_shopcart.setVisibility(View.VISIBLE);//这里发生过错误
        headRight.setVisibility(View.GONE);
        llBottomView.setVisibility(View.GONE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        adapterX = null;
        childs.clear();
        groups.clear();
        mtotalPrice = 0.0;
        mtotalCount = 0;

    }


    //获取购物车ID 的拼接
    private void GetGoodsId() {
        StringBuffer permission = new StringBuffer();
        List<StoreBean> toBeDeleteGroups = new ArrayList<StoreBean>(); //待删除的组元素

        for (int i = 0; i < groups.size(); i++) {
            StoreBean group = groups.get(i);
            if (group.isChecked()) {
                toBeDeleteGroups.add(group);
                xc.add(group);
            }
            List<MultiGoodsBean> toBeDeleteChilds = new ArrayList<MultiGoodsBean>();//复用获取ID

            List<MultiGoodsBean> child = childs.get(group.getId());

            for (int j = 0; j < child.size(); j++) {
                if (child.get(j).isChecked()) {
                    toBeDeleteChilds.add(child.get(j));

                }
            }

            for (int k = 0; k < toBeDeleteChilds.size(); k++) {
                permission.append(toBeDeleteChilds.get(k).getId() + ",");
                hashMapGoodsId.put(toBeDeleteChilds.get(k).getId(), toBeDeleteChilds.get(k).getId());
            }


            if (permission.toString().length() > 0) {
                String strX = permission.toString();
                // 拼接完成的商品购物车ID
                String goodsId = strX.substring(0, strX.length() - 1);
            }


        }
    }


}
