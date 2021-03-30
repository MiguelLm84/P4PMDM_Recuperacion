package com.miguel_lm.appjsondata.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.miguel_lm.appjsondata.R;
import com.miguel_lm.appjsondata.modelo.User;

public class Activity_Info_Autor extends AppCompatActivity {

    TextView tv_nombre, tv_nickname, tv_email, tv_telefono, tv_company;
    Button btn_aceptar;
    public static final String PARAM_USER = "param_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__info__autor);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        tv_nombre = findViewById(R.id.tv_nombre_completo);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_email = findViewById(R.id.tv_email);
        tv_telefono = findViewById(R.id.tv_telefono);
        tv_company = findViewById(R.id.tv_company);
        btn_aceptar = findViewById(R.id.btn_aceptar);

        User user = (User) getIntent().getSerializableExtra(PARAM_USER);

        tv_nombre.setText(user.getName());
        tv_nickname.setText(user.getNickname());
        tv_email.setText(user.getEmail());
        tv_telefono.setText(user.getPhone());
        tv_company.setText(user.getCompany());

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }
}