package com.aiqing.wallet.home.node;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aiqing.wallet.MainActivity;
import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.dialog.PasswordDialog;
import com.aiqing.wallet.home.transferaccounts.ConfirmTransferActivity;
import com.aiqing.wallet.my.securitycenter.ModifyTransactionPwActivity;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;
import com.aiqing.wallet.utils.SoftInputUtils;
import com.aiqing.wallet.utils.Utils;
import com.huxq17.xprefs.LogUtils;
import com.netease.nim.uikit.common.util.log.LogUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 参与节点
 */
public class ParticipationNodeActivity extends BaseActivity implements View.OnClickListener {
    private TextView vdo_balcance;
    private EditText number;
    private Button all_transfer_to, change_into;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation_node);
        showBackButton();
        initView();
    }

    private void initView() {
        vdo_balcance = findViewById(R.id.vdo_balcance);
        number = findViewById(R.id.number);
        all_transfer_to = findViewById(R.id.all_transfer_to);
        change_into = findViewById(R.id.change_into);
        bundle = getIntent().getExtras();
        vdo_balcance.setText(getString(R.string.vdo_balance) + bundle.getString("BalanceB"));
        if (bundle.getInt("active") == 1) {
            setTitle(R.string.participation_node);
            all_transfer_to.setText(getString(R.string.all_transfer_to));
            change_into.setText(getString(R.string.change_into));
        } else {
            setTitle(R.string.turn_out_node);
            all_transfer_to.setText(getString(R.string.all_out));
            change_into.setText(getString(R.string.turn_out));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.vdo_balcance:
                break;
            case R.id.number:
                break;
            case R.id.all_transfer_to:
                number.setText(bundle.getString("BalanceB"));
                break;
            case R.id.change_into:
                if (isEmpty(getText(number))) {
                    toast(getString(R.string.s_blank_info));
                    return;
                }
                ShowPwDialog();
                break;
        }
    }

    PasswordDialog passwordDialog;

    private void ShowPwDialog() {
        PasswordDialog.Builder builder = new PasswordDialog.Builder(this);
        builder.setCallback(new PasswordDialog.Callback() {
            @Override
            public void onForgetPassword() {
                passwordDialog.dismiss();
                startActivity(new Intent(ParticipationNodeActivity.this, ModifyTransactionPwActivity.class));
            }

            @Override
            public void onConfirm(String password) {
                if (isEmpty(password)) {
                    toast(getString(R.string.pw_not_null));
                } else {
                    changeInto(password);
                }

            }

            @Override
            public void onCancel() {
                passwordDialog.dismiss();
            }
        });
        passwordDialog = builder.create();
        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SoftInputUtils.hideSoftInput(ParticipationNodeActivity.this);
            }
        });
        passwordDialog.show();
    }

    private void changeInto(String password) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("password", password);
            params.put("active", bundle.getInt("active"));//1转入受益区，-1转出收入区
            params.put("currency", bundle.getInt("currency"));
            params.put("amount", getText(number));
            ApiManager.getApi(NodePlanApi.class).incomeExit(ApiTransform.transform(params))
                    .compose(RxSchedulers.<NodePlanApi.Bean>compose())
                    .subscribe(new BaseObserver<NodePlanBean>() {
                        @Override
                        protected void onSuccess(NodePlanBean dataBean) {
                            if (bundle.getInt("active") == 1) {
                                toast(getString(R.string.change_into_suc));
                            } else {
                                toast(getString(R.string.turn_out_suc));
                            }
                            passwordDialog.dismiss();
                            SoftInputUtils.hideSoftInput(ParticipationNodeActivity.this);
                            MainActivity.start(ParticipationNodeActivity.this);
                        }

                        @Override
                        protected void onFailed(String msg) {
                            super.onFailed(msg);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
