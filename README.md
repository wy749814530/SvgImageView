#使用方式
```java
    <com.svg.SvgImageView
        android:id="@+id/svgImageView"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_margin="18dp"
        appL:image="@drawable/ic_car"
     />
     
     SvgImageView svgImageView = findViewById(R.id.svgImageView);


    /**
     * 修改图层颜色
     *
     * @param view
     */
    svgImageView.setGroupColorByIndex(0, getResources().getColor(R.color.colorAccent));


    /**
     * 修改Path颜色
     *
     * @param view
     */
    svgImageView.setPathColorByIndex(0, getResources().getColor(R.color.colorAccent));

    /**
     * 修改所以Path颜色
     *
     * @param view
     */
    svgImageView.setPathsColor(getResources().getColor(R.color.colorAccent));

    /**
     * 还原SVG图原始色
     *
     * @param view
     */
    public void onRestore(View view) {
        svgImageView.resetColors();
    }

```
![alt 属性文本](https://github.com/wy749814530/SvgImageView/blob/master/res/4C25A000C5F25BFEDB90AE3A005755B7.jpg)
