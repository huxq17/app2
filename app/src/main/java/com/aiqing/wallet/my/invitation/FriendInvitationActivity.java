package com.aiqing.wallet.my.invitation;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.utils.ShareUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Hashtable;

public class FriendInvitationActivity extends BaseActivity implements View.OnClickListener {
    private TextView inviteCode, invitation_lg;
    private ImageView QR_code;
    private LinearLayout wechat, firend_circle, qq;
    private String title, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_invitation);
        initView();
        initData();
    }

    @Override
    public void onContentChanged() {
        setWindowStatusBarColor(Color.parseColor("#1DCB8A"));
    }

    private void initData() {
        HashMap<String, Object> params = new HashMap<>();
        ApiManager.getApi(InvitationsApi.class).friendInvitations(ApiTransform.transform(params))
                .compose(RxSchedulers.<InvitationsApi.Bean>compose())
                .subscribe(new BaseObserver<InvitationsList>() {
                    @Override
                    protected void onSuccess(InvitationsList invitationsList) {
                        inviteCode.setText(invitationsList.getInviteCode());
                        QR_code.setImageBitmap(createQRImage(invitationsList.getUrl()));
                        title = invitationsList.getContent();
                        url = invitationsList.getUrl();
                        invitation_lg.setText(title + url);
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }

    private void initView() {
        inviteCode = findViewById(R.id.inviteCode);
        QR_code = findViewById(R.id.QR_code);
        invitation_lg = findViewById(R.id.invitation_lg);
        wechat = findViewById(R.id.wechat);
        firend_circle = findViewById(R.id.firend_circle);
        qq = findViewById(R.id.qq);

        wechat.setOnClickListener(this);
        firend_circle.setOnClickListener(this);
        qq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.copy:
                ClipboardManager copy = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText(title + url);
                Toast.makeText(this, getString(R.string.copy_success), Toast.LENGTH_SHORT).show();
                break;
            case R.id.myInvitationsList:
                startActivity(new Intent(this, MyInvitationsListActivity.class));
                break;
            case R.id.wechat:
            case R.id.firend_circle:
            case R.id.qq:
                ShareUtils.share(this, title, title + url);
                break;
        }
    }

    /**
     * 生成二维码的方法
     *
     * @param address
     * @return
     */
    Bitmap bitmap;
    int QR_WIDTH = 1000;
    int QR_HEIGHT = 1000;

    private Bitmap createQRImage(String address) {
        try {
            //判断URL合法性
            if (address == null || "".equals(address) || address.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(address, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
