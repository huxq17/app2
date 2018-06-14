package com.aiqing.wallet.my;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiqing.imagepicker.ImagePicker;
import com.aiqing.wallet.App;
import com.aiqing.wallet.R;
import com.aiqing.wallet.common.ApiManager;
import com.aiqing.wallet.common.ApiTransform;
import com.aiqing.wallet.common.base.BaseActivity;
import com.aiqing.wallet.imageloader.ImageLoader;
import com.aiqing.wallet.rxjava.BaseObserver;
import com.aiqing.wallet.rxjava.RxSchedulers;
import com.aiqing.wallet.user.User;
import com.aiqing.wallet.user.UserApi;
import com.aiqing.wallet.user.UserService;
import com.aiqing.wallet.utils.SoftInputUtils;
import com.buyi.huxq17.serviceagency.ServiceAgency;
import com.huxq17.xprefs.LogUtils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.aiqing.imagepicker.utils.MediaUtils.fileScan;

public class PersonalDataActivity extends BaseActivity {
    private TextView id, username;
    private ImageView avatar, photo;
    private EditText nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.personal_data));
        showBackButton();
        initView();
        id.setText(getResources().getString(R.string.ID) + UserService.getID());
        username.setText(UserService.getUserName());
        nickname.setText(UserService.getNickName());
        focusNickName(false);
        ServiceAgency.getService(ImageLoader.class).loadImage(UserService.getAvatar(), avatar);
    }

    private void initView() {
        id = findViewById(R.id.id);
        username = findViewById(R.id.username);
        nickname = findViewById(R.id.nickname);
        photo = findViewById(R.id.photo);
        avatar = findViewById(R.id.avatar);
        setEdListener();
        findViewById(android.R.id.content).setOnClickListener(this);
//        findViewById(R.id.modify).setOnClickListener(this);
    }

    private void focusNickName(boolean focus) {
        if (focus) {
            nickname.setFocusable(true);
            nickname.setFocusableInTouchMode(true);
            nickname.requestFocus();
            nickname.setSelection(nickname.getText().length());
        } else {
            nickname.setFocusable(false);
            nickname.setFocusableInTouchMode(false);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case android.R.id.content:
                updateMsg();
                break;
            case R.id.nickname:
            case R.id.modify:
                focusNickName(true);
                SoftInputUtils.showSoftInput(this);
                break;
            case R.id.photo:
                uploadAvatar();
                break;
        }
    }

    private void setEdListener() {
        nickname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    updateMsg();
                    return true;
                }
                return false;
            }
        });
//        //键盘显示监听
//        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            //当键盘弹出隐藏的时候会 调用此方法。
//            @Override
//            public void onGlobalLayout() {
//                final Rect rect = new Rect();
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//                final int screenHeight = getWindow().getDecorView().getRootView().getHeight();
//                final int heightDifference = screenHeight - rect.bottom;
//                LogUtils.e("heightDifference=" + heightDifference + ";rect.bottom=" + rect.bottom);
//                boolean visible = heightDifference > screenHeight / 3;
//                if (visible) {
//                    //软键盘显示
//                } else {
//                    //软键盘隐藏
//                    updateMsg();
//                }
//            }
//        });
    }

    private void updateMsg() {
        SoftInputUtils.hideSoftInput(this);
        focusNickName(false);
        if (UserService.getNickName().equals(getText(nickname))) {
            return;
        }
        HashMap<String, Object> params = new LinkedHashMap<>();
        params.put("nickname", getText(nickname));
        ApiManager.getApi(UserApi.class).updataMsg(ApiTransform.transform(params))
                .compose(RxSchedulers.<UserApi.Bean>compose())
                .subscribe(new BaseObserver<User>(this) {
                    @Override
                    protected void onSuccess(User dataBean) {
                        UserService.setNickName(getText(nickname));
                        toast(getString(R.string.modify_success));
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                    }
                });
    }


    private ImagePicker imagePicker;

    private void uploadAvatar() {
        imagePicker = new ImagePicker(this, new ImagePicker.OnImagePickerListener() {
            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(Bundle response) {
                Uri uri = Uri.parse(response.getString("uri"));
                final File tempFile = new File(getPath(uri));
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), tempFile);
                MultipartBody.Part MultipartFile =
                        MultipartBody.Part.createFormData("avatar", tempFile.getName(), requestFile);
                ApiManager.getApi(UserApi.class).uploadFile(MultipartFile).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<User>(PersonalDataActivity.this) {
                            @Override
                            protected void onSuccess(User user) {
                                toast(getString(R.string.upload_success));
                                deleteTempFile(tempFile);
                                ServiceAgency.getService(ImageLoader.class).loadImage(user.Avatar, avatar);
                            }

                            @Override
                            protected void onFailed(String msg) {
                                super.onFailed(msg);
                            }
                        });
            }

            @Override
            public void onCancel() {

            }
        });
        imagePicker.showImagePicker();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public void deleteTempFile(File tempFile) {
        if (tempFile.getName().contains("resize") && tempFile.exists()) {
            tempFile.delete();
            fileScan(App.getContext(), tempFile.getAbsolutePath());
        }
    }


}
