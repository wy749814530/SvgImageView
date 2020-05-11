package com.svg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2020/5/8 0008.
 */

public class SvgImageView extends android.support.v7.widget.AppCompatImageView {
    private int paintColor;
    private int svgResourceId;
    private PathVectorShape vectorShape;
    private ShapeDrawable background;

    public SvgImageView(Context context) {
        super(context);
        init(context, null);
    }

    public SvgImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (attrs == null) {
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SvgView_Style);
        svgResourceId = ta.getResourceId(R.styleable.SvgView_Style_image, 0);
        paintColor = ta.getColor(R.styleable.SvgView_Style_paintColor, Color.argb(0, 0, 0, 0));
        ta.recycle();


        if (svgResourceId != 0) {
            vectorShape = new PathVectorShape(context, svgResourceId);
            background = new ShapeDrawable(vectorShape);
            setBackground(background);
        }
    }


    /**
     * 重新绘制1
     *
     * @param clickedLayers
     */
    private void paintLayers(List<PathVectorShape.PathLayer> clickedLayers) {
        for (PathVectorShape.PathLayer it : clickedLayers) {
            paintLayer(it);
        }
    }

    /**
     * 重新绘制2
     */
    private void paintLayer(PathVectorShape.PathLayer it) {
        it.getPaint().setColor(paintColor);
        invalidate();
    }

    /**
     * 修改指定图层颜色
     *
     * @param index
     * @param color
     */
    public void setGroupColorByIndex(int index, int color) {
        List<PathVectorShape.PathLayer> clickedLayers = vectorShape.getGroupLayersByIndex(index);
        paintColor = color;
        if (!clickedLayers.isEmpty()) {
            paintLayers(clickedLayers);
        }
    }

    /**
     * 修改指定Path颜色
     *
     * @param color
     */
    public void setPathColorByIndex(int index, int color) {
        PathVectorShape.PathLayer clickedLayer = vectorShape.getLayersByIndex(index);
        paintColor = color;
        if (clickedLayer != null) {
            paintLayer(clickedLayer);
        }
    }

    /**
     * 修改所以Path颜色
     *
     * @param color
     */
    public void setPathsColor(int color) {
        List<PathVectorShape.PathLayer> clickedLayers = vectorShape.getAllLayers();
        paintColor = color;
        if (!clickedLayers.isEmpty()) {
            paintLayers(clickedLayers);
        }
    }

    /**
     * 还原颜色
     */
    public void resetColors() {
        vectorShape.resetColors();
        invalidate();
    }
}