package com.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.svg.SvgImageView;


public class MainActivity extends AppCompatActivity {

    SvgImageView craImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        craImageView = findViewById(R.id.CRAImageView);
    }

    public void onViewClick1(View view) {
        craImageView.setGroupColorByIndex(0, getResources().getColor(R.color.colorAccent));
    }

    public void onViewClick2(View view) {
        craImageView.setGroupColorByIndex(1, getResources().getColor(R.color.colorAaccee));
    }

    public void onViewClick3(View view) {
        craImageView.setGroupColorByIndex(2, getResources().getColor(R.color.blue));
    }

    public void onViewClick4(View view) {
        craImageView.setGroupColorByIndex(3, getResources().getColor(R.color.red));
    }

    public void onPathClick1(View view) {
        craImageView.setPathColorByIndex(0, getResources().getColor(R.color.colorAccent));
    }

    public void onPathClick2(View view) {
        craImageView.setPathColorByIndex(1, getResources().getColor(R.color.colorAccent));
    }

    public void onPathClick3(View view) {
        craImageView.setPathColorByIndex(2, getResources().getColor(R.color.colorAccent));
    }

    public void onPathClickAll(View view) {
        craImageView.setPathsColor(getResources().getColor(R.color.colorAccent));
    }

    public void onRestore(View view) {
        craImageView.resetColors();
    }

}
