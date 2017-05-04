package com.gtrsp.imgloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by raoxuting on 2017/5/3.
 */

public class ImageDownloadUtil {

    /**
     * 根据url下载图片并并根据imageview的宽高进行压缩
     * @param url
     * @param imageView
     */
    public static Bitmap downloadImgByUrl(String url, ImageView imageView) {
        InputStream is = null;

        try {
            URL imgurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgurl.openConnection();
            is = new BufferedInputStream(conn.getInputStream());
            is.mark(is.available());

            BitmapFactory.Options ops = new BitmapFactory.Options();
            ops.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, ops);

            //获取imageview想要显示的宽高
            ImageSizeUtil.ImageSize imageViewSize = ImageSizeUtil.getImageViewSize(imageView);
            ops.inSampleSize = ImageSizeUtil.caculateInSampleSize
                    (ops, imageViewSize.width, imageViewSize.height);

            ops.inJustDecodeBounds = false;
            is.reset();
            bitmap = BitmapFactory.decodeStream(is, null, ops);

            conn.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

                try {
                    if (is != null)
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    /**
     * 把图片下载到指定文件
     * @param url
     * @param file
     * @return
     */
    public static boolean downloadImgByUrl(String url, File file) {
        FileOutputStream fos = null;
        InputStream is = null;

        try {
            URL imgurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgurl.openConnection();
            is = conn.getInputStream();
            fos = new FileOutputStream(file);

            int len;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }

            fos.flush();
            conn.disconnect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

                try {
                    if (is != null)
                        is.close();
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return false;
    }
}
