package com.nhx.gmall.manage.util;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

public class PmsUploadUtil {
    public static String uploadImage(MultipartFile multipartFile){
        String url = "http://192.168.50.70:8888";
        String tracker = PmsUploadUtil.class.getResource("/tracker.conf").getPath();
        try{
            ClientGlobal.init(tracker);
        }catch (Exception e){
            e.printStackTrace();
        }
        TrackerClient trackerClient = new TrackerClient();

        TrackerServer trackerServer = null;
        try{
            trackerServer = trackerClient.getTrackerServer();
        }catch (Exception e){
            e.printStackTrace();
        }

        StorageClient storageClient = new StorageClient(trackerServer,null);
        try{
            //获得上传的对象
            byte[] bytes = multipartFile.getBytes();
            //获得文件全名
            String orginalFilename = multipartFile.getOriginalFilename();//a.jpg
            //获取文件后缀名
            String extName = orginalFilename.substring(orginalFilename.lastIndexOf(".")+1);
            String[] uploadInfo = storageClient.upload_file(bytes,extName,null);
            for(String s:uploadInfo){
                url+="/"+s;
                System.out.println(url);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }
}
