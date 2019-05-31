package com.spark.newbitrade.activity.safe;

import android.view.View;
import android.widget.EditText;

import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 谷歌验证
 */
public class GoogleUnbindActivity extends BaseActivity implements GoogleContract.UnBindView {
    @BindView(R.id.etGoogleCode)
    EditText etGoogleCode;
    @BindView(R.id.etLoginPwd)
    EditText etLoginPwd;
    private GoogleContract.UnBindPresenter presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_unibind_google_code;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.unbind));
        new GoogleUnbindPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @OnClick(R.id.tvUnbind)
    protected void setOnClickListener(View view) {
        switch (view.getId()) {
            case R.id.tvUnbind:
                String code = etGoogleCode.getText().toString().trim();
                String loginPwd = etLoginPwd.getText().toString().trim();
                if (StringUtils.isEmpty(code, loginPwd)) {
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("codes", code);
                    map.put("password", loginPwd);
                    presenter.doUnbind(map);
                }
                break;
        }
    }

    @Override
    public void setPresenter(GoogleContract.UnBindPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doUnbindSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doUnbindFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }
}
