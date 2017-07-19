package jasonzou.lib.myapplication;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    // 下载失败
    public static final int DOWNLOAD_ERROR = 2;
    // 下载成功
    public static final int DOWNLOAD_SUCCESS = 1;
    /**
     * 下载完成后  直接打开文件
     */
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DOWNLOAD_SUCCESS:
                    File file = (File) msg.obj;
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//              startActivity(intent);
                    startActivity(Intent.createChooser(intent, "标题"));
                    /**
                     * 弹出选择框   把本activity销毁
                     */
                    finish();
                    break;
                case DOWNLOAD_ERROR:
                    Toast.makeText(MainActivity.this, "文件加载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private ProgressDialog mProgressDialog;
    private File file1;

    /**
     * 传入文件 url  文件路径  和 弹出的dialog  进行 下载文档
     */
    public static File downLoad(String serverpath, String savedfilepath, ProgressDialog pd) {
        try {
            URL url = new URL(serverpath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestProperty("Connection", "close");
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(5000);
            conn.connect();
            String temp = conn.getHeaderField("Content-Disposition");
            Log.i("downLoad() ", "HeaderField=" + temp);
            if (conn.getResponseCode() == 200) {
                int max = conn.getContentLength();
                pd.setMax(max);
                InputStream is = conn.getInputStream();
                File file = new File(savedfilepath);
                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;
                byte[] buffer = new byte[1024];
                int total = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                    pd.setProgress(total);
                }
                fos.flush();
                fos.close();
                is.close();
                return file;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getFileName(String serverurl) {
        return serverurl.substring(serverurl.lastIndexOf("/") + 1);
    }

    /**
     *
     */

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        initView();
//        download();
    }

    private void download() {
        String Strname;
        Strname = "https://www.baidu.com/";
        Strname = "http://dl.download.csdn.net/down11/20170225/41c77089a3f7955e63effa05803cde2e.pdf?response-content-disposition=attachment%3Bfilename%3D%22Java%E6%8A%80%E6%9C%AF%E6%89%8B%E5%86%8C%EF%BC%9A%E7%AC%AC6%E7%89%88.pdf%22&OSSAccessKeyId=9q6nvzoJGowBj4q1&Expires=1500475333&Signature=hiDNG%2FphASKUllmTPVu99zbwQKE%3D";
        Strname = "http://shouji.360tpcdn.com/170417/87d93d33e2ec5ae15b93fdaa642ffeb0/com.hiapk.marketpho_16793302.apk";
        Strname = "https://wkbos.bdimg.com/v1/wenku50//0cadbd5fe1359e21d028d1e967c4f902?responseContentDisposition=attachment%3B%20filename%3D%22Android项目文档.doc%22&responseContentType=application%2Foctet-stream&responseCacheControl=no-cache&authorization=bce-auth-v1%2Ffa1126e91489401fa7cc85045ce7179e%2F2017-07-19T13%3A46%3A54Z%2F3600%2Fhost%2F63ae5e8cf6b512eaed62498a92f8958c0a9d30d7f05f274e38306f15c79c2670&token=9143a287d07760fc17d99f5bd3a429edf13d74c5e35571fd4b356f33e991a353&expire=2017-07-19T14:46:54Z";
        Strname = "http://img4.imgtn.bdimg.com/it/u=929130691,247548933&fm=26&gp=0.jpg";
        Strname = "http://life.sjtu.edu.cn/images/stories/download/jishuzhuanrang.doc";

        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Strname));
//指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir("/download/", "ss");
//获取下载管理器
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);
    }


    private void initView() {
        // TODO Auto-generated method stub
        String Strname = "https://wkbos.bdimg.com/v1/wenku50//0cadbd5fe1359e21d028d1e967c4f902?responseContentDisposition=attachment%3B%20filename%3D%22Android项目文档.doc%22&responseContentType=application%2Foctet-stream&responseCacheControl=no-cache&authorization=bce-auth-v1%2Ffa1126e91489401fa7cc85045ce7179e%2F2017-07-19T13%3A46%3A54Z%2F3600%2Fhost%2F63ae5e8cf6b512eaed62498a92f8958c0a9d30d7f05f274e38306f15c79c2670&token=9143a287d07760fc17d99f5bd3a429edf13d74c5e35571fd4b356f33e991a353&expire=2017-07-19T14:46:54Z";
        Strname = "http://life.sjtu.edu.cn/images/stories/download/jishuzhuanrang.doc";
        Strname = "https://www.baidu.com/";
        Strname = "http://shouji.360tpcdn.com/170417/87d93d33e2ec5ae15b93fdaa642ffeb0/com.hiapk.marketpho_16793302.apk";
        Strname = "http://dl.download.csdn.net/down11/20170225/41c77089a3f7955e63effa05803cde2e.pdf?response-content-disposition=attachment%3Bfilename%3D%22Java%E6%8A%80%E6%9C%AF%E6%89%8B%E5%86%8C%EF%BC%9A%E7%AC%AC6%E7%89%88.pdf%22&OSSAccessKeyId=9q6nvzoJGowBj4q1&Expires=1500475333&Signature=hiDNG%2FphASKUllmTPVu99zbwQKE%3D";

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        //截取最后14位 作为文件名
        String s = Strname.substring(Strname.length() - 14);
        //文件存储
        file1 = new File(Environment.getExternalStorageDirectory(), getFileName(s));
        final String finalStrname = Strname;
        new Thread() {
            public void run() {

                File haha = new File(file1.getAbsolutePath());
                //判断是否有此文件
                if (haha.exists()) {
                    //有缓存文件,拿到路径 直接打开
                    Message msg = Message.obtain();
                    msg.obj = haha;
                    msg.what = DOWNLOAD_SUCCESS;
                    handler.sendMessage(msg);
                    mProgressDialog.dismiss();
                    return;
                }
//              本地没有此文件 则从网上下载打开
                File downloadfile = downLoad(finalStrname, file1.getAbsolutePath(), mProgressDialog);
//              Log.i("Log",file1.getAbsolutePath());
                Message msg = Message.obtain();
                if (downloadfile != null) {
                    // 下载成功,安装....
                    msg.obj = downloadfile;
                    msg.what = DOWNLOAD_SUCCESS;
                } else {
                    // 提示用户下载失败.
                    msg.what = DOWNLOAD_ERROR;
                }
                handler.sendMessage(msg);
                mProgressDialog.dismiss();
            }

            ;
        }.start();
    }

}