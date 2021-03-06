package com.wxk.updemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wxk.baselibrary.ExceptionCrashHandler;
import com.wxk.baselibrary.base.BaseActivity;
import com.wxk.baselibrary.http.HttpUtils;
import com.wxk.baselibrary.ioc.CheckNet;
import com.wxk.baselibrary.ioc.OnClick;
import com.wxk.baselibrary.ioc.ViewById;
import com.wxk.baselibrary.log.LogUtils;
import com.wxk.baselibrary.permission.PermissionFailed;
import com.wxk.baselibrary.permission.PermissionSucceed;
import com.wxk.framelibrary.db.DaoSupportFactory;
import com.wxk.framelibrary.db.IDaoSupport;
import com.wxk.framelibrary.http.HttpCallBack;
import com.wxk.framelibrary.ui.CommonNavigationBar;
import com.wxk.updemo.model.Person;
import com.wxk.updemo.model.PhoneModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static com.wxk.baselibrary.permission.PermissionConstants.REQUEST_CODE_CALL_PHONE;
import static com.wxk.baselibrary.permission.PermissionConstants.REQUEST_CODE_SDCARD;

public class MainActivity extends BaseActivity {

    @ViewById(R.id.test_tv)
    private Button mTestTv;

    @Override
    protected String getChildName() {
        return MainActivity.class.getSimpleName();
    }

    @Override
    protected void setContentView(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initTitle() {

        CommonNavigationBar titleBar = new CommonNavigationBar.Builder(this)
                .setTitle("首页")
                .setRightText("文章")
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "这是文章", Toast.LENGTH_SHORT).show();
                    }
                }).build();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        IDaoSupport<Person> daoSupport = DaoSupportFactory.getFactory().getDao(Person.class);
//        daoSupport.insert(new Person("wxk", 20, true));

//        List<Person> datas = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            datas.add(new Person("wxk"+i, 10+i, false));
//        }
//
//        daoSupport.insert(datas);

        List<Person> list = daoSupport.querySupport().query();
        LogUtils.e(TAG, "--->" + list.toString());

        //上传文件
        upLoadCrashFile();

        mTestTv.setText("ioc");
    }

    @OnClick(R.id.test_tv)
    @CheckNet
    private void onClick(View view) {

        HttpUtils.with(this).url("http://apis.juhe.cn/mobile/get") //路径,参数需要放在jni里
                .addParams("phone", "18354214580")
                .cache(true)
                .execute(new HttpCallBack<PhoneModel>() {
                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onSuccess(PhoneModel result) {

                        LogUtils.e(result + "-=-=-=" + result.result.province);
                    }

                    @Override
                    public void onPreExecute() {
                        super.onPreExecute();
                    }
                });
//        PermissionHelper.with(this)
//                .requestCode(PermissionConstants.REQUEST_CODE_CALL_PHONE)
//                .requestPermissions(new String[]{Manifest.permission.CALL_PHONE})
//                .request();

//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setContentView(R.layout.detail_comment_dialog)
//                .fullWidth()
//                .fromBottom(true)
//                .show();
//
//        final EditText commentEt = dialog.getView(R.id.comment_editor);
//
//        dialog.setOnclickListener(R.id.submit_btn, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,
//                        commentEt.getText().toString().trim(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    //成功
    @PermissionSucceed(requestCode = REQUEST_CODE_CALL_PHONE)
    private void callPhone() {

        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + 88888);
        intent.setData(uri);
        startActivity(intent);
    }

    //失败
    @PermissionFailed(requestCode = REQUEST_CODE_SDCARD)
    private void callFailed() {

        Toast.makeText(this, "关闭了拨打电话权限", Toast.LENGTH_SHORT).show();
    }

    private void upLoadCrashFile() {

        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
        if (crashFile.exists()) {

            //上传到服务器 这里模拟请求一下
            InputStreamReader reader = null;
            FileInputStream fis = null;
            int len = 0;
            char[] buffer = new char[1024];

            try {
                fis = new FileInputStream(crashFile);
                reader = new InputStreamReader(fis);
                while ((len = reader.read(buffer)) != -1) {
                    String str = new String(buffer);
                    Log.e("-------", str);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
