package com.emjiayuan.nll.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.emjiayuan.nll.Constants;
import com.emjiayuan.nll.Global;
import com.emjiayuan.nll.R;
import com.emjiayuan.nll.adapter.OrderProductAdapter;
import com.emjiayuan.nll.base.BaseActivity;
import com.emjiayuan.nll.event.UpdateEvent;
import com.emjiayuan.nll.event.WXpaySuccessEvent;
import com.emjiayuan.nll.model.Order;
import com.emjiayuan.nll.model.PayResult;
import com.emjiayuan.nll.model.WXpayInfo;
import com.emjiayuan.nll.utils.MyOkHttp;
import com.emjiayuan.nll.utils.MyUtils;
import com.google.gson.Gson;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.icon_back)
    ImageView mIconBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_save)
    TextView mTvSave;
    @BindView(R.id.icon_search)
    ImageView mIconSearch;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.shr_tv)
    TextView mShrTv;
    @BindView(R.id.tele_tv)
    TextView mTeleTv;
    @BindView(R.id.address_tv)
    TextView mAddressTv;
    @BindView(R.id.all_ll)
    RelativeLayout mAllLl;
    @BindView(R.id.tab_rl)
    LinearLayout mTabRl;
    @BindView(R.id.status_tv)
    TextView mStatusTv;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.bz_tv)
    TextView mBzTv;
    @BindView(R.id.up_down)
    ImageView mUpDown;
    @BindView(R.id.bz_ll)
    LinearLayout mBzLl;
    @BindView(R.id.message)
    TextView mMessage;
    @BindView(R.id.order_time)
    TextView mOrderTime;
    @BindView(R.id.copy)
    TextView mCopy;
    @BindView(R.id.order_num)
    TextView mOrderNum;
    @BindView(R.id.line_l)
    View mLineL;
    @BindView(R.id.total_tv)
    TextView mTotalTv;
    @BindView(R.id.freight_tv)
    TextView mFreightTv;
    @BindView(R.id.money_ll)
    LinearLayout mMoneyLl;
    @BindView(R.id.total)
    TextView mTotal;
    @BindView(R.id.total_pay)
    TextView mTotalPay;
    @BindView(R.id.total_ll)
    LinearLayout mTotalLl;
    @BindView(R.id.limit_tv)
    LinearLayout mLimitTv;
    @BindView(R.id.btn1)
    TextView mBtn1;
    @BindView(R.id.btn2)
    TextView mBtn2;
    @BindView(R.id.handle_ll)
    LinearLayout mHandleLl;
    private OrderProductAdapter mOrderProductAdapter;
    private String orderid;
    private Order order;
    private int type;
    private String orderInfo;
    private PopupWindow mPopupWindow;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mTvTitle.setText("订单详情");
        mTvTitle.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if (intent != null) {
            orderid = intent.getStringExtra("orderid");
            type = intent.getIntExtra("type", 0);
        }
//        if (orderid == null) {
//            //this必须为点击消息要跳转到页面的上下文。
//            XGPushClickedResult clickedResult = XGPushManager.onActivityStarted(this);
//            if (clickedResult != null) {
//                //获取消息附近参数
//                String ster = clickedResult.getCustomContent();
//                try {
//                    JSONObject jsonObject = new JSONObject(ster);
//                    orderid = jsonObject.getString("event_val");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////获取消息标题
//                String set = clickedResult.getTitle();
////获取消息内容
//                String s = clickedResult.getContent();
//                MyUtils.e("推送", ster + "|" + set + "|" + s);
//            }
//        }
        getOrderDetail();

    }

    @Override
    protected void setListener() {
        mIconBack.setOnClickListener(this);
        mCopy.setOnClickListener(this);
    }

    public void getOrderDetail() {

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("orderid", orderid);
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("order.getOrderDetail", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                MyUtils.e("------获取订单详情结果------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void updateOrder(String option, String orderid) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("orderid", orderid);//传递键值对参数
        formBody.add("option", option);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("order.updateOrderStatus", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("------取消订单或确认收货结果------", result);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void setOrder() {
        mShrTv.setText("收货人：" + order.getAddress_name());
        mTeleTv.setText(order.getAddress_phone());
        mAddressTv.setText("地址：" + order.getAddress());
        mMessage.setText(order.getRemark());
        mStatusTv.setText(order.getOrder_status());
        switch (Integer.parseInt(order.getOrdertype())) {
            case 0:
                mHandleLl.setVisibility(View.GONE);
                mBtn1.setVisibility(View.GONE);
                mBtn2.setVisibility(View.VISIBLE);
                mBtn2.setText("再次购买");
                mBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
            case 1:
                mHandleLl.setVisibility(View.VISIBLE);
                mBtn1.setVisibility(View.VISIBLE);
                mBtn2.setVisibility(View.VISIBLE);
                mBtn1.setText("取消订单");
                mBtn2.setText("立即支付");
                mBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MyUtils.isFastClick()) {
                            return;
                        }
                        updateOrder("0", orderid);
                    }
                });
                mBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MyUtils.isFastClick()) {
                            return;
                        }
                        showPopupWindow();
                    }
                });
                break;
            case 2:
                mHandleLl.setVisibility(View.VISIBLE);
                mBtn1.setVisibility(View.GONE);
                mBtn2.setVisibility(View.VISIBLE);
                mBtn2.setText("提醒发货");

                mBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MyUtils.isFastClick()) {
                            return;
                        }
                        String title = "伊穆家园客服";
                        /**
                         * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                         * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                         * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                         */
                        ConsultSource source = new ConsultSource("app", "app", "custom information string");
                        /**
                         * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                         * 如果返回为false，该接口不会有任何动作
                         *
                         * @param context 上下文
                         * @param title   聊天窗口的标题
                         * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                         */
                        Unicorn.openServiceActivity(mActivity, title, source);
                    }
                });
                break;
            case 3:
                mHandleLl.setVisibility(View.VISIBLE);
                mBtn1.setVisibility(View.VISIBLE);
                mBtn2.setVisibility(View.VISIBLE);
                mBtn1.setText("查看物流");
                mBtn2.setText("确认收货");
                mBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MyUtils.isFastClick()) {
                            return;
                        }
                        Intent intent = new Intent(mActivity, LogisticsDetailActivity.class);
                        intent.putExtra("postid", order.getExpressno());
                        intent.putExtra("postcom", order.getExpresscom());
                        intent.putExtra("order_no", order.getOrder_no());
                        intent.putExtra("date", order.getCreatedate());
                        intent.putExtra("address", order.getAddress());
                        startActivity(intent);
                    }
                });
                mBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MyUtils.isFastClick()) {
                            return;
                        }
                        updateOrder("1", orderid);
                    }
                });
                break;
            case 4:
                mHandleLl.setVisibility(View.VISIBLE);
                mBtn1.setVisibility(View.GONE);
                mBtn2.setVisibility(View.VISIBLE);
                if ("已评价".equals(order.getComment())) {
                    mBtn2.setText("已评价");
                    mBtn2.setEnabled(false);
                } else {
                    mBtn2.setText("去评价");
                    mBtn2.setEnabled(true);
                }


                mBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MyUtils.isFastClick()) {
                            return;
                        }
//                        Intent intent = new Intent(mActivity, JudgeActivity.class);
//                        intent.putExtra("order", order);
//                        startActivity(intent);
                    }
                });
                break;
        }
        switch (type) {
            case 0:
                mTotal.setText("合计金额");
                mMoneyLl.setVisibility(View.VISIBLE);
                mTotalPay.setText("¥" + order.getTotalmoney());
                mTotalLl.setVisibility(View.VISIBLE);
                mLineL.setVisibility(View.VISIBLE);
                break;
            case 1:
                mTotal.setText("合计积分");
                mMoneyLl.setVisibility(View.GONE);
                mTotalLl.setVisibility(View.VISIBLE);
                mLineL.setVisibility(View.VISIBLE);
                mTotalPay.setText(order.getPromotion_value() + "积分");
                break;
            case 2:
                mMoneyLl.setVisibility(View.GONE);
                mTotalLl.setVisibility(View.GONE);
                mLineL.setVisibility(View.GONE);
//                total_pay.setText(order.getTotalmoney()+"积分");
                break;
        }
        mOrderTime.setText(order.getCreatetime());
        mOrderNum.setText(order.getOrder_no());
        mTotalTv.setText("¥" + order.getUsemoney());
//        total_pay.setText("¥" + order.getTotalmoney());
        mFreightTv.setText("¥" + order.getExpress_price());


        mOrderProductAdapter = new OrderProductAdapter(R.layout.order_item_in, order.getProduct_list());
        mRvGoods.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvGoods.setAdapter(mOrderProductAdapter);
        mOrderProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent  = new Intent(OrderDetailActivity.this, ProductDetailActivity.class);
                intent.putExtra("productid", order.getProduct_list().get(position).getProductid());
                startActivity(intent);
            }
        });
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("result");
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            order = gson.fromJson(new JSONObject(data).toString(), Order.class);
                            setOrder();
                        } else {
                            MyUtils.showToast(OrderDetailActivity.this, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        if ("200".equals(code)) {
                            EventBus.getDefault().post(new UpdateEvent(""));
                            MyUtils.showToast(mActivity, message);
                            finish();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.ALIPAY:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            orderInfo = data;
                            alipay();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.ALIPAY_RESULT://支付宝结果
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        MyUtils.showToast(mActivity, "支付成功");
                        mPopupWindow.dismiss();
                        getOrderDetail();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        MyUtils.showToast(mActivity, "支付失败");
                        mPopupWindow.dismiss();
                    }
                    break;

                case Constants.WXPAY://微信支付
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            WXpayInfo payInfo = gson.fromJson(data, WXpayInfo.class);
                            WXPay(payInfo);
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_back:
                finish();
                break;
            case R.id.copy:
                MyUtils.copy(order.getOrder_no(), mActivity);
                break;
        }
    }

    /**
     * 弹出Popupwindow
     */
    public void showPopupWindow() {
        View popupWindow_view = LayoutInflater.from(mActivity).inflate(R.layout.payment_layout, null);
        LinearLayout yepay_ll = popupWindow_view.findViewById(R.id.yepay_ll);
        LinearLayout weixin_ll = popupWindow_view.findViewById(R.id.weixin_ll);
        final LinearLayout alipay_ll = popupWindow_view.findViewById(R.id.alipay_ll);
        Button cz_btn = popupWindow_view.findViewById(R.id.cz_btn);
        cz_btn.setText("立即支付");
        Button cancel_btn = popupWindow_view.findViewById(R.id.cancel_btn);
        final CheckBox weixin_check = popupWindow_view.findViewById(R.id.weixin_check);
        final CheckBox alipay_check = popupWindow_view.findViewById(R.id.alipay_check);
        final CheckBox yepay_check = popupWindow_view.findViewById(R.id.yepay_check);
//        yepay_ll.setVisibility(View.VISIBLE);
        weixin_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weixin_check.isChecked()) {
                    weixin_check.setChecked(false);
                } else {
                    weixin_check.setChecked(true);
                    alipay_check.setChecked(false);
                    yepay_check.setChecked(false);
                }
            }
        });
        alipay_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alipay_check.isChecked()) {
                    alipay_check.setChecked(false);
                } else {
                    alipay_check.setChecked(true);
                    weixin_check.setChecked(false);
                    yepay_check.setChecked(false);
                }
            }
        });
        yepay_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yepay_check.isChecked()) {
                    yepay_check.setChecked(false);
                } else {
                    alipay_check.setChecked(false);
                    weixin_check.setChecked(false);
                    yepay_check.setChecked(true);
                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
//                Intent intent=new Intent(mActivity,OrderNormalActivity2.class);
//                intent.putExtra("type",0);
//                startActivity(intent);
//                finish();
            }
        });
        cz_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                if (alipay_check.isChecked() == true) {
                    alipayrequest();
                } else if (weixin_check.isChecked() == true) {
                    WXpayrequest();
                } else {
                    MyUtils.showToast(mActivity, "请选择支付方式");
                }
            }
        });
        mPopupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                MyUtils.setWindowAlpa(mActivity, false);
            }
        });
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(popupWindow_view, Gravity.BOTTOM, 0, 0);
        }
        MyUtils.setWindowAlpa(mActivity, true);
    }

    //    支付宝支付
    private void alipay() {
/**
 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 *
 * orderInfo的获取必须来自服务端；
 */
        /*boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;*/
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderDetailActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = Constants.ALIPAY_RESULT;
                msg.obj = result;
                myHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //微信支付

    private void WXPay(WXpayInfo payInfo) {
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        PayReq request = new PayReq();
        request.appId = payInfo.getAppid();
        request.partnerId = payInfo.getPartnerid();
        request.prepayId = payInfo.getPrepayid();
        request.packageValue = payInfo.getPackageX();
        request.nonceStr = payInfo.getNoncestr();
        request.timeStamp = payInfo.getTimestamp();
        request.sign = payInfo.getSign();
        api.sendReq(request);
    }

    public void alipayrequest() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "COMMON");//传递键值对参数
        formBody.add("orderid", orderid);//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("pay.alipay", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                MyUtils.e("------支付宝支付------", result);
                Message message = new Message();
                message.what = Constants.ALIPAY;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void WXpayrequest() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "COMMON");//传递键值对参数
        formBody.add("orderid", orderid);//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("pay.wxpay", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                MyUtils.e("------微信支付------", result);
                Message message = new Message();
                message.what = Constants.WXPAY;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(WXpaySuccessEvent event) {
        BaseResp resp = event.getResp();
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {   // 0 成功  展示成功页面
                MyUtils.showToast(mActivity, "支付成功");
                getOrderDetail();
            } else if (resp.errCode == -1) {  // -1 错误  可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                MyUtils.showToast(mActivity, "支付出错");
                mPopupWindow.dismiss();
            } else if (resp.errCode == -2) {  // -2 用户取消    无需处理。发生场景：用户不支付了，点击取消，返回APP。
                MyUtils.showToast(mActivity, "取消支付");
                mPopupWindow.dismiss();
            }
        }
    }
}
