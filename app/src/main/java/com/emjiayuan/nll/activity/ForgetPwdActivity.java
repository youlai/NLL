package com.emjiayuan.nll.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.emjiayuan.nll.R;
import com.emjiayuan.nll.base.BaseActivity;
import com.emjiayuan.nll.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.icon_back)
    ImageView mIconBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.icon_search)
    ImageView mIconSearch;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_yzm)
    EditText mEtYzm;
    @BindView(R.id.get_yzm)
    TextView mGetYzm;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_repassword)
    EditText mEtRepassword;
    @BindView(R.id.reset)
    Button mReset;
    @BindView(R.id.no_yzm)
    TextView mNoYzm;
    private String mUserName;
    private String mYzm;
    private String mPassword;
    private String mRepassword;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_pwd_forget;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText("找回密码");
    }

    @Override
    protected void setListener() {
        mIconBack.setOnClickListener(this);
        mIconSearch.setOnClickListener(this);
        mGetYzm.setOnClickListener(this);
        mReset.setOnClickListener(this);
        mNoYzm.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_back:
                finish();
                break;
            case R.id.icon_search:
                finish();
                break;
            case R.id.get_yzm:
                finish();
                break;
            case R.id.no_yzm:
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("温馨提示");
                builder.setMessage("是否拨打客服电话4008123337？");
                builder.setCancelable(true);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + "4008123337");
                        intent.setData(data);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                         *  在这里实现你自己的逻辑
                         */
                    }
                });
                builder.create().show();
                break;
            case R.id.reset:
                mUserName = mEtUsername.getText().toString().trim();
                mYzm = mEtYzm.getText().toString().trim();
                mPassword = mEtPassword.getText().toString().trim();
                mRepassword = mEtRepassword.getText().toString().trim();
                if ("".equals(mUserName)) {
                    MyUtils.showToast(mActivity, "请输入手机号！");
                    return;
                }
                if ("".equals(mYzm)) {
                    MyUtils.showToast(mActivity, "请输入验证码！");
                    return;
                }
                if ("".equals(mPassword)) {
                    MyUtils.showToast(mActivity, "请输入密码！");
                    return;
                }
                if ("".equals(mRepassword)) {
                    MyUtils.showToast(mActivity, "请输入确认密码！");
                    return;
                }
                if (!mPassword.equals(mRepassword)) {
                    MyUtils.showToast(mActivity, "两次密码不一致！");
                    return;
                }
                startActivity(new Intent(mActivity, PersonalInfoActivity.class));
                break;
        }
    }
}