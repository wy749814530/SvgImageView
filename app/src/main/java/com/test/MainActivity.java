package com.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.svg.SvgImageView;


public class MainActivity extends AppCompatActivity {

    SvgImageView svgImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        svgImageView = findViewById(R.id.svgImageView);
    }

    /**
     * 修改图层颜色
     *
     * @param view
     */
    public void onViewClick1(View view) {
        svgImageView.setGroupColorByIndex(0, getResources().getColor(R.color.colorAccent));
    }

    public void onViewClick2(View view) {
        svgImageView.setGroupColorByIndex(1, getResources().getColor(R.color.colorAaccee));
    }

    public void onViewClick3(View view) {
        svgImageView.setGroupColorByIndex(2, getResources().getColor(R.color.blue));
    }

    public void onViewClick4(View view) {
        svgImageView.setGroupColorByIndex(3, getResources().getColor(R.color.red));
    }

    /**
     * 修改Path颜色
     *
     * @param view
     */
    public void onPathClick1(View view) {
        svgImageView.setPathColorByIndex(0, getResources().getColor(R.color.colorAccent));
    }

    public void onPathClick2(View view) {
        svgImageView.setPathColorByIndex(1, getResources().getColor(R.color.colorAccent));
    }

    public void onPathClick3(View view) {
        svgImageView.setPathColorByIndex(2, getResources().getColor(R.color.colorAccent));
    }

    /**
     * 修改所以Path颜色
     *
     * @param view
     */
    public void onPathClickAll(View view) {
        svgImageView.setPathsColor(getResources().getColor(R.color.colorAccent));
    }

    /**
     * 还原SVG图原始色
     *
     * @param view
     */
    public void onRestore(View view) {
        svgImageView.resetColors();
    }

}
