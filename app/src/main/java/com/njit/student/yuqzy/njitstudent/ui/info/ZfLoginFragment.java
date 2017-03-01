package com.njit.student.yuqzy.njitstudent.ui.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode;
import com.njit.student.yuqzy.njitstudent.Event.SecretCode;
import com.njit.student.yuqzy.njitstudent.R;
import com.njit.student.yuqzy.njitstudent.net.ZfNetData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.Subscription;

import static com.njit.student.yuqzy.njitstudent.Event.LoginResponseCode.REALM_SCORE_STO_OK;

public class ZfLoginFragment extends Fragment implements View.OnClickListener{

    private ImageView zf_login_yanzhengma,zf_login_yanzhengma_change;
    private EditText et_zf_login_mima,et_zf_login_username,et_zf_login_yanzhengma;
    private Button zf_login_btn,zf_clear_btn;

    private ZfNetData network;
    public ZfLoginFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragment_zf_login, container, false);
        zf_login_yanzhengma=(ImageView) rootview.findViewById(R.id.zf_login_yanzhengma);
        zf_login_yanzhengma_change=(ImageView) rootview.findViewById(R.id.zf_login_yanzhengma_change);
        zf_login_yanzhengma_change.setOnClickListener(this);
        et_zf_login_mima=(EditText)rootview.findViewById(R.id.et_zf_login_mima);
        et_zf_login_username=(EditText)rootview.findViewById(R.id.et_zf_login_username);
        et_zf_login_yanzhengma=(EditText)rootview.findViewById(R.id.et_zf_login_yanzhengma);
        zf_login_btn=(Button)rootview.findViewById(R.id.zf_login_btn);
        zf_login_btn.setOnClickListener(this);
        zf_clear_btn=(Button)rootview.findViewById(R.id.zf_clear_btn);
        zf_clear_btn.setOnClickListener(this);
        network=new ZfNetData(getContext());
        network.getSecretCode();
        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SecretCode event) {
        zf_login_yanzhengma.setImageBitmap(event.getBitmap());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginResponseCode event) {

        switch (event.getCode()) {

            case REALM_SCORE_STO_OK:

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.zf_login_yanzhengma_change:
                network.getSecretCode();
                break;
            case R.id.zf_login_btn:
                network.zfLogin(et_zf_login_username.getText().toString(),et_zf_login_mima.getText().toString(),et_zf_login_yanzhengma.getText().toString());

                break;
            case R.id.zf_clear_btn:
                et_zf_login_username.setText("");
                et_zf_login_mima.setText("");
                et_zf_login_yanzhengma.setText("");
                network.getSecretCode();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
