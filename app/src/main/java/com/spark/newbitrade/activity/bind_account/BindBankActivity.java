package com.spark.newbitrade.activity.bind_account;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定银行卡
 * Created by Administrator on 2018/5/2 0002.
 */

public class BindBankActivity extends BaseActivity implements BindBankContract.View {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.llBank)
    LinearLayout llBank;
    @BindView(R.id.etBranch)
    EditText etBranch;
    @BindView(R.id.llBranch)
    LinearLayout llBranch;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etConfirmAccount)
    EditText etConfirmAccount;
    @BindView(R.id.llConfirmAccount)
    LinearLayout llConfirmAccount;
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.tvBank)
    TextView tvBank;
    private BindBankPresenterImpl presenter;
    private NormalListDialog dialog;
    private String[] bankNames;
    private PayWaySetting payWaySetting;
    private boolean isUpdate = false;//添加或者更新

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_bank;
    }


    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        User user = MyApplication.getApp().getCurrentUser();
        if (user != null && StringUtils.isNotEmpty(user.getRealName())) {
            etName.setText(user.getRealName());
        }
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.bank_card_binding));
        presenter = new BindBankPresenterImpl(this);
        bankNames = getResources().getStringArray(R.array.bank_name);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payWaySetting = (PayWaySetting) bundle.getSerializable("data");
            if (payWaySetting != null) {
                isUpdate = true;
                tvConfirm.setText(getResources().getString(R.string.str_text_change));
                tvBank.setText(payWaySetting.getBank());
                etBranch.setText(payWaySetting.getBranch());
                etAccount.setText(payWaySetting.getPayAddress());
                etName.setText(payWaySetting.getRealName());
            }
        }
        if (isUpdate) {
            tvTitle.setText(getString(R.string.text_change) + getString(R.string.unionpay_account));
        } else {
            tvTitle.setText(getString(R.string.binding) + getString(R.string.unionpay_account));
        }
    }

    @OnClick({R.id.llBank, R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llBank:
                showDialog();
                break;
            case R.id.tvConfirm:
                confirm();
                break;
        }
    }

    /**
     * 提交数据
     */
    private void confirm() {
        String name = etName.getText().toString();
        String bank = tvBank.getText().toString();
        String branch = etBranch.getText().toString();
        String account = etAccount.getText().toString();
        String reaccount = etConfirmAccount.getText().toString();
        String pwd = etNewPwd.getText().toString();
        if (!StringUtils.isEmpty(name, bank, branch, account, reaccount, pwd)) {
            if (account.equals(reaccount)) {
                bindOrUpdateBnak(bank, branch, pwd, name, account);
            } else {
                ToastUtils.showToast(getString(R.string.diff_cardnumber));
            }
        } else {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        }

    }

    private void bindOrUpdateBnak(String bank, String branch, String pwd, String name, String account) {
        if (isUpdate) {
            presenter.doUpdateBank(payWaySetting.getId(), GlobalConstant.card, account, bank, branch, pwd, name);
        } else {
            presenter.doBindBank(GlobalConstant.card, account, bank, branch, pwd, name);
        }
    }

    /**
     * 选择开户银行
     */
    private void showDialog() {
        dialog = new NormalListDialog(activity, bankNames);
        dialog.title("请选择银行");
        dialog.titleBgColor(getResources().getColor(R.color.main_font_content));
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bankName = bankNames[position];
                tvBank.setText(bankName);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void doBindBankSuccess(MessageResult obj) {
        if (StringUtils.isNotEmpty(obj.getMessage())) {
            if (obj.getMessage().equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj.getMessage());
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doUpdateBankSuccess(MessageResult obj) {
        if (StringUtils.isNotEmpty(obj.getMessage())) {
            if (obj.getMessage().equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj.getMessage());
        }
        setResult(RESULT_OK);
        finish();
    }


}
