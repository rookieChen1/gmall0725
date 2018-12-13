package com.atguigu.gmall.manage.util;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class GmallUploadUtil {

    public static String uploadImage(MultipartFile multipartFile){
        String path = GmallUploadUtil.class.getClassLoader().getResource("tracker.conf").getPath();

        try {
            ClientGlobal.init(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer connection = null;
        try {
            connection = trackerClient.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageClient storageClient = new StorageClient(connection,null);

        // 获得文件后缀名
        String originalFilename = multipartFile.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        String ext = split[split.length -1];
        String[] gifs = new String[0];
        try {
            gifs = storageClient.upload_file(multipartFile.getBytes(), ext, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

        //  fdfs的文件路径
        String url = "http://192.168.30.100";

        for (int i = 0; i < gifs.length; i++) {
            url =url +"/"+gifs[i];
        }
        return url;
    }
}
