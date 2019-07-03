package com.hisense.serverestimate.utils;

import org.springframework.core.io.InputStreamSource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/4/28 22:57
 * @Version 1.0
 */
public class HiFileUtil {
    /**
     * 保存上传的文件到服务器
     * @param path  如：/root/files/2018-09-08
     * @param name  如:12334344453dsferw.xls
     * @param file  如：Multipartfile类型文件
     * @return
     * @throws IOException
     */
    public static boolean saveFile(String path, String name, InputStreamSource file) throws IOException {
        try{
            BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(path
                    + File.separator + name));
            byte[] buf = new byte[1024];
            int size = 0;
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.close();
            bis.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public static boolean downloadFile(HttpServletResponse response,String fileUrl,String fileName){
        try{
            File file = new File(fileUrl);
            // 如果文件名存在，则进行下载
            if (file.exists()) {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }catch (Exception e ){
            return false;
        }
    }
    public static boolean copyFile(String src, String dest) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        File f=new File(src);
        boolean result = false;
        try {
            fi = new FileInputStream(src);
            fo = new FileOutputStream(dest);
            in = fi.getChannel();// 得到对应的文件通道
            out = fo.getChannel();// 得到对应的文件通道
            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            } finally {
            }
        }
        return result;
    }
}
