package com.wxk.updemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wxk.baselibrary.ExceptionCrashHandler;
import com.wxk.baselibrary.base.BaseActivity;
import com.wxk.baselibrary.dialog.AlertDialog;
import com.wxk.baselibrary.ioc.CheckNet;
import com.wxk.baselibrary.ioc.OnClick;
import com.wxk.baselibrary.ioc.ViewById;
import com.wxk.framelibrary.CommonNavigationBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends BaseActivity {

    @ViewById(R.id.test_tv)
    private Button mTestTv;

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
        //上传文件
        upLoadCrashFile();

        mTestTv.setText("ioc");
    }

    @OnClick(R.id.test_tv)
    @CheckNet
    private void onClick(View view){

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setContentView(R.layout.detail_comment_dialog)
                .fullWidth()
                .fromBottom(true)
                .show();

        final EditText commentEt = dialog.getView(R.id.comment_editor);

        dialog.setOnclickListener(R.id.submit_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        commentEt.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upLoadCrashFile() {

        File crashFile = ExceptionCrashHandler.getInstance().getCrashFile();
        if(crashFile.exists()){

            //上传到服务器 这里模拟请求一下
            InputStreamReader reader = null;
            FileInputStream fis = null;
            int len = 0;
            char[] buffer = new char[1024];

            try {
                fis = new FileInputStream(crashFile);
                reader = new InputStreamReader(fis);
                while ((len = reader.read(buffer)) != -1){
                    String str = new String(buffer);
                    Log.e("-------", str);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(fis != null){
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
