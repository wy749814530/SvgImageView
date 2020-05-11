package com.svg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.graphics.PathParser;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/5/7 0007.
 */

public class PathVectorShape extends Shape {

    private final String TAG_VECTOR = "vector";
    private final String TAG_PATH = "path";
    private final String TAG_GROUP = "group";

    private final int attrName = android.R.attr.name;
    private final int attrColor = android.R.attr.fillColor;
    private final int attrPathData = android.R.attr.pathData;
    private final int attrWidth = android.R.attr.viewportWidth;
    private final int attrHeight = android.R.attr.viewportHeight;

    private int defaultViewportValue = 0;
    private int defaultColorValue = -0x21523f22;

    private RectF viewportRect = new RectF();
    private ArrayList<PathLayer> layers = new ArrayList<>();


    public PathVectorShape(Context context, int id) {
        XmlResourceParser parser = context.getResources().getXml(id);
        AttributeSet attributeSet = Xml.asAttributeSet(parser);
        try {
            int parserEventType = 0;
            parserEventType = parser.getEventType();
            String group = null;
            while (parserEventType != XmlPullParser.END_DOCUMENT) {
                switch (parserEventType) {
                    case XmlPullParser.START_TAG:
                        if (TAG_GROUP.equals(parser.getName())) {
                            group = handleGroupTag(context, attributeSet);
                        } else if (TAG_VECTOR.equals(parser.getName())) {
                            TypedArray vector = handleVectorTag(context, attributeSet);
                            vector.recycle();
                        } else if (TAG_PATH.equals(parser.getName())) {
                            TypedArray path = handlePathTag(context, attributeSet, group);
                            path.recycle();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (TAG_GROUP.equals(parser.getName())) {
                            group = null;
                        }
                        break;
                }
                Log.i("parseVectorDrawableXml", "parserEventType : " + parserEventType);
                parserEventType = parser.next();
            }
            Log.i("parseVectorDrawableXml", "Finished");
        } catch (XmlPullParserException e) {
            handleException(e);
        } catch (IOException e) {
            handleException(e);
        }
    }

    // 解析group 标签
    private String handleGroupTag(Context context, AttributeSet attributeSet) {
        int[] attrs = {attrName};
        Integer positionOfNameAttr = intArrayIndexOf(attrs, attrName);
        TypedArray obtainedAttributeSet = context.obtainStyledAttributes(attributeSet, attrs);
        return obtainedAttributeSet.getString(positionOfNameAttr);
    }

    // 解析vector标签,主要设置View图像大小的
    private TypedArray handleVectorTag(Context context, AttributeSet attributeSet) {
        int[] attrs = {attrWidth, attrHeight};
        Integer positionOfWidthAttr = intArrayIndexOf(attrs, attrWidth);
        Integer positionOfHeightAttr = intArrayIndexOf(attrs, attrHeight);
        TypedArray obtainedAttributeSet = context.obtainStyledAttributes(attributeSet, attrs);
        float viewportRight = obtainedAttributeSet.getFloat(positionOfWidthAttr, defaultViewportValue);
        float viewportBottom = obtainedAttributeSet.getFloat(positionOfHeightAttr, defaultViewportValue);
        viewportRect.set(0f, 0f, viewportRight, viewportBottom);
        return obtainedAttributeSet;
    }

    // 解析path标签
    private TypedArray handlePathTag(Context context, AttributeSet attributeSet, String group) {
        int[] attrs = {attrName, attrColor, attrPathData};
        Integer positionOfNameAttr = intArrayIndexOf(attrs, attrName);
        Integer positionOfColorAttr = intArrayIndexOf(attrs, attrColor);
        Integer positionOfPathDataAttr = intArrayIndexOf(attrs, attrPathData);
        TypedArray obtainedAttributeSet = context.obtainStyledAttributes(attributeSet, attrs);
        String pathName = obtainedAttributeSet.getString(positionOfNameAttr);
        if (TextUtils.isEmpty(pathName)) {
            pathName = "noNamePath";
        }
        int pathFillColor = obtainedAttributeSet.getColor(positionOfColorAttr, defaultColorValue);
        String pathData = obtainedAttributeSet.getString(positionOfPathDataAttr);
        if (pathData != null) {
            PathLayer layer = new PathLayer(pathData, pathFillColor, pathName, group);
            layers.add(layer);
        }
        return obtainedAttributeSet;
    }

    public int intArrayIndexOf(int[] attrs, int element) {
        for (int index = 0; index < attrs.length; index++) {
            if (element == attrs[index]) {
                return index;
            }
        }
        return -1;
    }

    private void handleException(Exception e) {
        Log.e("PathVectorShape", "PathVectorShape constructor error");
        e.printStackTrace();
    }

    /**
     * 返回对应位置的path
     *
     * @param x
     * @param y
     * @return
     */
    public PathLayer getLayersAt(int x, int y) {
        for (PathLayer layer : layers) {
            if (layer.region.contains(x, y)) {
                return layer;
            }
        }
        return null;
    }

    /**
     * 返回对应区域Goup内的所有Path
     *
     * @param x
     * @param y
     * @return
     */
    public List<PathLayer> getGroupLayersAt(int x, int y) {
        PathLayer layer0 = getLayersAt(x, y);
        String topLayerGroup = layer0 != null ? layer0.group : null;

        List<PathLayer> layerList = new ArrayList<>();
        for (PathLayer layer : layers) {
            if (layer.group == topLayerGroup && layer.group != null) {
                layerList.add(layer);
            }
        }
        return layerList;
    }


    @Override
    public void onResize(float width, float height) {
        Matrix matrix = new Matrix();
        Region shapeRegion = new Region(0, 0, (int) width, (int) height);
        matrix.setRectToRect(viewportRect, new RectF(0f, 0f, width, height), Matrix.ScaleToFit.FILL);
        for (PathLayer layer : layers) {
            layer.transform(matrix, shapeRegion);
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        for (PathLayer layer : layers) {
            canvas.drawPath(layer.transformedPath, layer.paint);
        }
    }

    /**
     * 回去对应条目的Group中包含的所有Path
     *
     * @param index
     * @return
     */
    public List<PathLayer> getGroupLayersByIndex(int index) {
        int position = 0;
        String groupName = "";
        List<PathLayer> groupLayers = new ArrayList<>();

        for (PathLayer layer : layers) {
            if (layer.group != null) {
                if ("".equals(groupName)) {
                    groupName = layer.group.toString();
                }
                if (groupName.equals(layer.group.toString())) {
                    if (position == index) {
                        groupLayers.add(layer);
                    }
                } else {
                    position = position + 1;
                    if (position == index) {
                        groupLayers.add(layer);
                    }
                    groupName = layer.group.toString();
                }
            }
        }
        return groupLayers;
    }

    /**
     * 获取对应条目的Path
     *
     * @param index
     * @return
     */
    public PathLayer getLayersByIndex(int index) {
        if (layers.size() > index && index >= 0) {
            return layers.get(index);
        }
        return null;
    }

    /**
     * 获取所以Path
     *
     * @return
     */
    public List<PathLayer> getAllLayers() {
        return layers;
    }

    /**
     * 重置view
     */
    public void resetColors() {
        for (PathLayer layer : layers) {
            layer.paint.setColor(layer.baseColor);
        }
    }

    class PathLayer {
        public Path originalPath, transformedPath;
        public String name, group;
        public Paint paint;
        public int baseColor;
        public Region region;

        @SuppressLint("RestrictedApi")
        public PathLayer(String data, int baseColor, String name, String group) {
            originalPath = PathParser.createPathFromPathData(data);
            this.name = name;
            this.group = group;
            this.baseColor = baseColor;
            this.region = new Region();
            transformedPath = new Path();
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(baseColor);
        }

        public void transform(Matrix matrix, Region clip) {
            originalPath.transform(matrix, transformedPath);
            region.setPath(transformedPath, clip);
        }

        public String toString() {
            return name;
        }

        public Paint getPaint() {
            return paint;
        }
    }
}
