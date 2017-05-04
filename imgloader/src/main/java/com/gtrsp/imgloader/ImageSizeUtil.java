package com.gtrsp.imgloader;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * 图片加载框架
 */
public class ImageSizeUtil {

    /**
     * 根据ImageView获适当的压缩的宽和高
     *
     * @param imageView
     * @return
     */
    public static ImageSize getImageViewSize(ImageView imageView) {

        ImageSize imageSize=new ImageSize();
        DisplayMetrics displayMetrics=imageView.getContext().getResources()
                .getDisplayMetrics();

        ViewGroup.LayoutParams lp=imageView.getLayoutParams();

        int width=imageView.getWidth();// 获取imageview的实际宽度
        if(width<=0) {
            width=lp.width;// 获取imageview在layout中声明的宽度
        }
        if(width<=0) {
            // width = imageView.getMaxWidth();// 检查最大值
            width = getImageViewFieldValue(imageView,"mMaxWidth");
        }
        if(width<=0) {
            width=displayMetrics.widthPixels;
        }

        int height=imageView.getHeight();// 获取imageview的实际高度
        if(height<=0) {
            height=lp.height;// 获取imageview在layout中声明的宽度
        }
        if(height<=0) {
            height=getImageViewFieldValue(imageView,"mMaxHeight");// 检查最大值
        }
        if(height<=0) {
            height=displayMetrics.heightPixels;
        }
        imageSize.width=width;
        imageSize.height=height;

        return imageSize;
    }

    public static class ImageSize {
        int width;
        int height;
    }

    /**
     * 通过反射获取imageview的某个属性值
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName)
    {
        int value = 0;
        try
        {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e)
        {
        }
        return value;

    }

    /**
     * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                           int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);

            inSampleSize = Math.max(widthRadio, heightRadio);
        }

        return inSampleSize;
    }
}

