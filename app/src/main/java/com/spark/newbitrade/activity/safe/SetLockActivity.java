package com.spark.newbitrade.activity.safe;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.ui.lock.LockPatternIndicator;
import com.spark.newbitrade.ui.lock.LockPatternView;
import com.spark.newbitrade.utils.EncryUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SetLockActivity extends BaseActivity {
    public static final int RETURN_SET_LOCK = 0;
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.indicator)
    LockPatternIndicator indicator;
    @BindView(R.id.lockView)
    LockPatternView lockView;
    @BindView(R.id.tvMessage)
    TextView tvMessage;

    private List<LockPatternView.Cell> cells = null;
    private int type; //0  设置  1  关闭
    protected String content = "";

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_set_lock;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, false);
        isNeedChecke = false;
        content = EncryUtils.getInstance().decryptString(SharedPreferenceInstance.getInstance().getLockPwd(), "spark");
        lockView.setOnPatternListener(new LockPatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {
                lockView.removePostClearPatternRunnable();
                lockView.setPattern(LockPatternView.DisplayMode.DEFAULT);
            }

            @Override
            public void onPatternComplete(List<LockPatternView.Cell> cells) {
                patternComplete(cells);
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void patternComplete(List<LockPatternView.Cell> cells) {
        if (cells.size() < 4) {
            lockView.setPattern(LockPatternView.DisplayMode.DEFAULT);
            tvMessage.setText(getString(R.string.gestures_error_tag));
        }
        if (type == 0) {//设置
            if (SetLockActivity.this.cells == null && cells.size() >= 4) {
                SetLockActivity.this.cells = new ArrayList<>(cells);
                tvMessage.setText(getString(R.string.gestures_pwd_confirm));
                lockView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                updateLockPatternIndicator();
                return;
            }
        }
        if (SetLockActivity.this.cells != null) {
            if (isSame(SetLockActivity.this.cells, cells)) {
                if (type == 0) {
                    tvMessage.setText(getString(R.string.set_up_success));
                    saveCells(cells);
                    lockView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                } else if (type == 1) {
                    tvMessage.setText(getString(R.string.cancel_success));
                    clearCells();
                }
                finish();

            } else {
                lockView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockView.postClearPatternRunnable(500);
                if (type == 0) {
                    tvMessage.setText(getString(R.string.pwd_diff));
                } else if (type == 1) {
                    tvMessage.setText(getString(R.string.pwd_error));
                }
            }
        }
    }

    private boolean isSame(List<LockPatternView.Cell> cells, List<LockPatternView.Cell> cells1) {
        if (cells.size() != cells1.size()) return false;
        for (int i = 0, len = cells.size(); i < len; i++) {
            if (cells.get(i).getIndex() != cells1.get(i).getIndex()) return false;
        }
        return true;
    }

    private void updateLockPatternIndicator() {
        if (cells != null) indicator.setIndicator(cells);
    }

    private void clearCells() {
        SharedPreferenceInstance.getInstance().saveLockPwd("");
    }

    private void saveCells(List<LockPatternView.Cell> cells) {
        String str = "";
        for (LockPatternView.Cell cell : cells) {
            str += cell.getIndex();
        }
        SharedPreferenceInstance.getInstance().saveLockPwd(EncryUtils.getInstance().encryptString(str, "spark"));
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt("type", -1);
            if (type == 1) {
                content = EncryUtils.getInstance().decryptString(SharedPreferenceInstance.getInstance().getLockPwd(), "spark");
                cells = new ArrayList<>(content.length());
                for (int i = 0, len = content.length(); i < len; i++) {
                    LockPatternView.Cell cell = lockView.new Cell(0, 0, 0, 0, Integer.parseInt(content.charAt(i) + ""));
                    cells.add(cell);
                }
            }
        }
    }

    @Override
    protected void loadData() {

    }

}
