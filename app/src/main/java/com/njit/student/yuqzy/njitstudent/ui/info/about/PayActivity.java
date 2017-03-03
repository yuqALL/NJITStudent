package com.njit.student.yuqzy.njitstudent.ui.info.about;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.njit.student.yuqzy.njitstudent.App;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.utils.SettingsUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PayActivity extends AppCompatActivity implements View.OnLongClickListener{

    private Toolbar toolbar;
    private ImageView WeChatPay,ZhiFuBaoPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_pay);
        toolbar=(Toolbar)findViewById(R.id.title);
        setSupportActionBar(toolbar);
        toolbar.setTitle("感谢支持");
        initViews();
    }
    protected void setDisplayHomeAsUpEnabled(boolean enable) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void initViews() {
        setDisplayHomeAsUpEnabled(true);
        WeChatPay=(ImageView)findViewById(R.id.wechat_pay_qrcode);
        ZhiFuBaoPay=(ImageView)findViewById(R.id.zhifubao_pay_qrcode);
        WeChatPay.setOnLongClickListener(this);
        ZhiFuBaoPay.setOnLongClickListener(this);
    }

    private void initTheme(){
        int themeIndex = SettingsUtil.getTheme();
        switch (themeIndex){
            case 0:
                setTheme(R.style.LapisBlueTheme);
                break;
            case 1:
                setTheme(R.style.PaleDogwoodTheme);
                break;
            case 2:
                setTheme(R.style.GreeneryTheme);
                break;
            case 3:
                setTheme(R.style.PrimroseYellowTheme);
                break;
            case 4:
                setTheme(R.style.FlameTheme);
                break;
            case 5:
                setTheme(R.style.IslandParadiseTheme);
                break;
            case 6:
                setTheme(R.style.KaleTheme);
                break;
            case 7:
                setTheme(R.style.PinkYarrowTheme);
                break;
            case 8:
                setTheme(R.style.NiagaraTheme);
                break;

        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId())
        {
            case R.id.wechat_pay_qrcode:
                toWeChatScan();
                break;
            case R.id.zhifubao_pay_qrcode:
                toAliPayScan();
                break;
        }
        return false;
    }

    private void toWeChatScan() {
        saveImage(R.drawable.wechat,"wechatpay");
        try {

            startActivity(getPackageManager().getLaunchIntentForPackage("com.tencent.mm"));
        } catch (Exception e) {
            //若无法正常跳转，在此进行错误处理
            Toast.makeText(this, "无法跳转到微信，请检查您是否安装了微信！", Toast.LENGTH_SHORT).show();
        }
    }

    private void toAliPayScan() {
        saveImage(R.drawable.alipay,"alipay");
        try {
            //利用Intent打开支付宝
            //支付宝跳过开启动画打开扫码和付款码的url scheme分别是alipayqr://platformapi/startapp?saId=10000007和
            //alipayqr://platformapi/startapp?saId=20000056
            Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            //若无法正常跳转，在此进行错误处理
            Toast.makeText(this, "无法跳转到支付宝，请检查您是否安装了支付宝！", Toast.LENGTH_SHORT).show();
        }
    }
    public void saveImage(int id,String name) {
        Bitmap bmp= BitmapFactory.decodeResource(App.getContext().getResources(),id);
        File appDir = new File(Environment.getExternalStorageDirectory(), "NJITStu");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
