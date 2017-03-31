package com.wxk.updemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wxk.baselibrary.base.BaseActivity;
import com.wxk.baselibrary.dialog.AlertDialog;
import com.wxk.baselibrary.ioc.CheckNet;
import com.wxk.baselibrary.ioc.OnClick;
import com.wxk.baselibrary.ioc.ViewById;

public class MainActivity extends BaseActivity {

    @ViewById(R.id.test_tv)
    private Button mTestTv;

    @Override
    protected void setContentView(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

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
}
