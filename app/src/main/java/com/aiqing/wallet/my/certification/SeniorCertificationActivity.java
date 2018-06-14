package com.aiqing.wallet.my.certification;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.aiqing.imagepicker.ImagePicker;
import com.aiqing.wallet.App;
import com.aiqing.wallet.MainActivity;
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
import com.buyi.huxq17.serviceagency.ServiceAgency;
import com.huxq17.xprefs.LogUtils;
import com.netease.nim.uikit.common.util.log.LogUtil;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.aiqing.imagepicker.utils.MediaUtils.fileScan;

public class SeniorCertificationActivity extends BaseActivity {
    private ImageView full_face_photo, reverse_photo;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_certification);
        setWindowStatusBarColor(getResources().getColor(R.color.base_bg));
        setTitle(getResources().getString(R.string.authentication));
        showBackButton();
        bundle = getIntent().getExtras();
        initView();
    }

    private void initView() {
        full_face_photo = findViewById(R.id.full_face_photo);
        reverse_photo = findViewById(R.id.reverse_photo);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.full_face_photo:
                choosePhoto(1);
                break;
            case R.id.reverse_photo:
                choosePhoto(2);
                break;
            case R.id.submit:
                submit();
                break;
        }
    }

    private ImagePicker imagePicker;
    private File file1;
    private File file2;

    private void choosePhoto(final int i) {
        imagePicker = new ImagePicker(this, new ImagePicker.OnImagePickerListener() {
            @Override
            public void onError(String error) {
            }

            @Override
            public void onSuccess(Bundle response) {
                Uri uri = Uri.parse(response.getString("uri"));
                if (i == 1) {
                    file1 = new File(getPath(uri));
                    full_face_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    full_face_photo.setImageURI(uri);
                } else if (i == 2) {
                    file2 = new File(getPath(uri));
                    reverse_photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    reverse_photo.setImageURI(uri);
                }
            }

            @Override
            public void onCancel() {
            }
        });
        imagePicker.showImagePicker();
    }

    private void submit() {
        final long start = System.currentTimeMillis();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        RequestBody requestFile2 =
                RequestBody.create(MediaType.parse("multipart/form-data"), file2);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("imgFront", file1.getName(), requestFile);
        builder.addFormDataPart("imgBack", file2.getName(), requestFile2);
        MultipartBody body = builder.build();//调用即可

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", bundle.getString("name"));
        hashMap.put("idNumber", bundle.getString("idNumber"));
        ApiManager.getApi(UserApi.class).Authentication(hashMap, body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<User>(this) {
                    @Override
                    protected void onSuccess(User user) {
                        toast(getString(R.string.upload_success));
                        deleteTempFile(file1);
                        deleteTempFile(file2);
                        MainActivity.start(SeniorCertificationActivity.this);
                    }

                    @Override
                    protected void onFailed(String msg) {
                        super.onFailed(msg);
                        LogUtils.v("spend:" + (System.currentTimeMillis() - start));
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.e("onRequestPermissionsResult");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("onActivityResult");
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
