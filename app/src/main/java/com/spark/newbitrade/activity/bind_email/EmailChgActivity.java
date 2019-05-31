package com.spark.newbitrade.activity.bind_email;

/**
 * Created by Administrator on 2018/9/13 0013.
 */

public class EmailChgActivity
//        extends BaseActivity implements BindEmailContract.UnbindEmailView
{
//    @BindView(R.id.ivBack)
//    ImageView ivBack;
//    @BindView(R.id.tvEmail)
//    TextView tvEmail;
//    @BindView(R.id.etCode)
//    EditText etCode;
//    @BindView(R.id.tvOldCode)
//    TextView tvOldCode;
//    @BindView(R.id.edtNewEmail)
//    EditText edtNewEmail;
//    @BindView(R.id.edtNewCode)
//    EditText edtNewCode;
//    @BindView(R.id.tvNewCode)
//    TextView tvNewCode;
//    @BindView(R.id.tvSubmit)
//    TextView tvSubmit;
//    private String email;
//    private BindEmailContract.UnbindEmailPresenter presenter;
//    private TimeCount timeCountOld;
//    private TimeCount timeCountNew;
//
//    @Override
//    protected void initView() {
//        super.initView();
//        setSetTitleAndBack(false, true);
//    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//        setTitle(getString(R.string.binding_email));
//        timeCountOld = new TimeCount(60000, 1000, tvOldCode);
//        timeCountNew = new TimeCount(60000, 1000, tvNewCode);
//        new EmailChgPresent(Injection.provideTasksRepository(getApplicationContext()), this);
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            email = bundle.getString("email");
//            tvEmail.setText(email);
//        }
//    }
//
//    @Override
//    public void setPresenter(BindEmailContract.UnbindEmailPresenter presenter) {
//        this.presenter = presenter;
//    }
//
//    @Override
//    public void getNewEmailCodeSuccess(String obj) {
//        timeCountNew.start();
//        tvNewCode.setEnabled(false);
//    }
//
//    @Override
//    public void getOldEmailCodeSuccess(String obj) {
//        timeCountOld.start();
//        tvOldCode.setEnabled(false);
//    }
//
//    @Override
//    public void unBindEmailSuccess(String obj) {
//        ToastUtils.showToast(obj);
//        setResult(RESULT_OK);
//        finish();
//    }
//
//    @Override
//    public void doPostFail(Integer code, String toastMessage) {
//        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
//    }
//
//    @Override
//    protected int getActivityLayoutId() {
//        return R.layout.activity_email_chg;
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @OnClick({R.id.tvOldCode, R.id.tvNewCode, R.id.tvSubmit})
//    public void onViewClicked(View view) {
//        HashMap<String, String> map = new HashMap<>();
//        switch (view.getId()) {
//            case R.id.tvOldCode:
//                map.clear();
//                map.put("memberId", String.valueOf(MyApplication.getApp().getCurrentUser().getId()));
//                presenter.getOldEmailCode(map);
//                break;
//            case R.id.tvNewCode:
//                String email = edtNewEmail.getText().toString().trim();
//                if (StringUtils.isEmpty(email)) {
//                    ToastUtils.showToast(R.string.input_email);
//                    return;
//                }
//                if (!StringUtils.isEmail(email)) {
//                    ToastUtils.showToast(R.string.email_not_correct);
//                    return;
//                }
//                map.clear();
//                map.put("memberId", String.valueOf(MyApplication.getApp().getCurrentUser().getId()));
//                map.put("email",email);
//                presenter.getNewEmailCode(map);
//                break;
//            case R.id.tvSubmit:
//                if (StringUtils.isEmpty(etCode.getText().toString())) {
//                    ToastUtils.showToast(R.string.input_code);
//                    return;
//                }
//                if (StringUtils.isEmpty(edtNewEmail.getText().toString())) {
//                    ToastUtils.showToast(R.string.input_email);
//                    return;
//                }
//                if (!StringUtils.isEmail(edtNewEmail.getText().toString())) {
//                    ToastUtils.showToast(R.string.email_not_correct);
//                    return;
//                }
//                if (StringUtils.isEmpty(edtNewCode.getText().toString())) {
//                    ToastUtils.showToast(R.string.input_code);
//                    return;
//                }
//                map.clear();
//                map.put("memberId", String.valueOf(MyApplication.getApp().getCurrentUser().getId()));
//                map.put("oldEmailCode",etCode.getText().toString());
//                map.put("newEmailCode",edtNewCode.getText().toString());
//                map.put("newEmail",edtNewEmail.getText().toString());
//                presenter.unBindEmail(map);
//                break;
//        }
//    }
}
