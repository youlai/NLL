package com.emjiayuan.nll.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.nll.R;
import com.emjiayuan.nll.activity.EnterpriseActivity;
import com.emjiayuan.nll.activity.HelpActivity;
import com.emjiayuan.nll.activity.HezuoActivity;
import com.emjiayuan.nll.activity.OrderNormalActivity;
import com.emjiayuan.nll.activity.SpitActivity;
import com.emjiayuan.nll.activity.address.AddressActivity;
import com.emjiayuan.nll.base.BaseLazyFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class PersonalFragment extends BaseLazyFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.nickname)
    TextView mNickname;
    @BindView(R.id.my_top)
    LinearLayout mMyTop;
    @BindView(R.id.normal_dfk_ll)
    LinearLayout mNormalDfkLl;
    @BindView(R.id.normal_dfh_ll)
    LinearLayout mNormalDfhLl;
    @BindView(R.id.normal_dsh_ll)
    LinearLayout mNormalDshLl;
    @BindView(R.id.normal_all_ll)
    LinearLayout mNormalAllLl;
    @BindView(R.id.soup_dfk_ll)
    LinearLayout mSoupDfkLl;
    @BindView(R.id.soup_dfh_ll)
    LinearLayout mSoupDfhLl;
    @BindView(R.id.soup_dsh_ll)
    LinearLayout mSoupDshLl;
    @BindView(R.id.soup_all_ll)
    LinearLayout mSoupAllLl;
    @BindView(R.id.wlcx_ll)
    LinearLayout mWlcxLl;
    @BindView(R.id.address_ll)
    LinearLayout mAddressLl;
    @BindView(R.id.service_ll)
    LinearLayout mServiceLl;
    @BindView(R.id.spit_ll)
    LinearLayout mSpitLl;
    @BindView(R.id.help_ll)
    LinearLayout mHelpLl;
    @BindView(R.id.hezuo_ll)
    LinearLayout mHezuoLl;
    @BindView(R.id.record_ll)
    LinearLayout mRecordLl;
    @BindView(R.id.collection_ll)
    LinearLayout mCollectionLl;
    @BindView(R.id.qyjs_ll)
    LinearLayout mQyjsLl;
    @BindView(R.id.setting_ll)
    LinearLayout mSettingLl;
    @BindView(R.id.banner)
    Banner mBanner;


    private String mParam1;
    private String mParam2;
    private String[] images = new String[]{
            "http://qiniu.emjiayuan.com/upload_file/ems/2018092111329525354",
            "http://qiniu.emjiayuan.com/upload_file/ems/2018100815514348272",
            "http://qiniu.emjiayuan.com/upload_file/ems/2018101113731538120",
            "http://qiniu.emjiayuan.com/upload_file/ems/2018100911071275167",
            "http://qiniu.emjiayuan.com/upload_file/ems/2018071817991346559",
    };

    public PersonalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalFragment newInstance(String param1, String param2) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        super.initImmersionBar();
//        mImmersionBar.fitsSystemWindows(false);
//        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void initView() {
        refreshLayout.setEnableLoadMore(false);

    }

    @Override
    protected void setListener() {
        mNormalDfkLl.setOnClickListener(this);
        mNormalDfhLl.setOnClickListener(this);
        mNormalDshLl.setOnClickListener(this);
        mNormalAllLl.setOnClickListener(this);

        mSoupDfkLl.setOnClickListener(this);
        mSoupDfhLl.setOnClickListener(this);
        mSoupDshLl.setOnClickListener(this);
        mSoupAllLl.setOnClickListener(this);

        mWlcxLl.setOnClickListener(this);
        mAddressLl.setOnClickListener(this);
        mServiceLl.setOnClickListener(this);
        mSpitLl.setOnClickListener(this);
        mHelpLl.setOnClickListener(this);
        mHezuoLl.setOnClickListener(this);
        mRecordLl.setOnClickListener(this);
        mCollectionLl.setOnClickListener(this);
        mQyjsLl.setOnClickListener(this);
        mSettingLl.setOnClickListener(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(String name) {

    }

    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()){
            case R.id.normal_dfk_ll:
                intent=new Intent(mActivity,OrderNormalActivity.class);
                intent.putExtra("order_type","普通订单");
                intent.putExtra("curTab",0);
                startActivity(intent);
                break;
            case R.id.normal_dfh_ll:
                intent=new Intent(mActivity,OrderNormalActivity.class);
                intent.putExtra("order_type","普通订单");
                intent.putExtra("curTab",1);
                startActivity(intent);
                break;
            case R.id.normal_dsh_ll:
                intent=new Intent(mActivity,OrderNormalActivity.class);
                intent.putExtra("order_type","普通订单");
                intent.putExtra("curTab",2);
                startActivity(intent);
                break;
            case R.id.normal_all_ll:
                intent=new Intent(mActivity,OrderNormalActivity.class);
                intent.putExtra("order_type","普通订单");
                intent.putExtra("curTab",3);
                startActivity(intent);
                break;
            case R.id.soup_dfk_ll:
                intent=new Intent(mActivity,OrderNormalActivity.class);
                intent.putExtra("order_type","汤料订单");
                intent.putExtra("curTab",0);
                startActivity(intent);
                break;
            case R.id.soup_dfh_ll:
                intent=new Intent(mActivity,OrderNormalActivity.class);
                intent.putExtra("order_type","汤料订单");
                intent.putExtra("curTab",1);
                startActivity(intent);
                break;
            case R.id.soup_dsh_ll:
                intent=new Intent(mActivity,OrderNormalActivity.class);
                intent.putExtra("order_type","汤料订单");
                intent.putExtra("curTab",2);
                startActivity(intent);
                break;
            case R.id.soup_all_ll:
                intent=new Intent(mActivity,OrderNormalActivity.class);
                intent.putExtra("order_type","汤料订单");
                intent.putExtra("curTab",3);
                startActivity(intent);
                break;
            case R.id.wlcx_ll:
                startActivity(new Intent(mActivity,EnterpriseActivity.class));
                break;
            case R.id.address_ll:
                startActivity(new Intent(mActivity,AddressActivity.class));
                break;
            case R.id.service_ll:
                startActivity(new Intent(mActivity,EnterpriseActivity.class));
                break;
            case R.id.spit_ll:
                startActivity(new Intent(mActivity,SpitActivity.class));
                break;
            case R.id.help_ll:
                startActivity(new Intent(mActivity,HelpActivity.class));
                break;
            case R.id.hezuo_ll:
                startActivity(new Intent(mActivity,HezuoActivity.class));
                break;
            case R.id.record_ll:
                startActivity(new Intent(mActivity,EnterpriseActivity.class));
                break;
            case R.id.collection_ll:
                startActivity(new Intent(mActivity,EnterpriseActivity.class));
                break;
            case R.id.qyjs_ll:
                startActivity(new Intent(mActivity,EnterpriseActivity.class));
                break;
            case R.id.setting_ll:
                startActivity(new Intent(mActivity,EnterpriseActivity.class));
                break;
        }
    }
}