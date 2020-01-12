package com.yjiang.base.core.util;

import com.yjiang.base.modular.system.model.User;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AliyunOSSClientUtil {

    public static final String REGION_CN_HANGZHOU = "cn-beijing";

    public static final String endpoint = "oss-cn-beijing.aliyuncs.com";

    //主账户
    private static final String accessKeyId = "xxxxxx";
    private static final String accessKeySecret = "xxxxxx";

    //子账户
    private static final String subAccessKeyId = "xxxxxx";
    private static final String subAccessKeySecret = "xxxxxx";

    public static final String IMAGE_BUCKET_NAME = "my-share-image";

    public static final String HTML_IMAGE_BUCKET_NAME = "my-share-html-image";

    public static final String VEDIO_BUCKET_NAME = "my-share-video";

    public static final String HTTP_PROTOCOL = "https://";

    public static final long EXPIRE_SECOND = 900;

    private static final OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

    public static void main(String[] args) throws ClientException {
        Map<String, String> map = stsService();
        String key = map.get("accessKeyId");
        String secret = map.get("accessKeySecret");
        String token = map.get("securityToken");
        System.out.println(key);
        System.out.println(secret);
        System.out.println(token);
//        String key = "STS.NJaCD7vdce8d4diza5sGjfMzv";
//        String secret = "5qrPggBH63CDo3RNXaY9uJgyqxjVSUJmqhVKrQ9Sv3Bm";
//        String token = "CAIS8AF1q6Ft5B2yfSjIr4nUCP6Dm7tC0vqPNkLYnmFgf8hGiYjRlDz2IHxKfnVsBeAWsf80nGBQ7/YZlqFpTJtIeFfJd5OMFFebe0fzDbDasumZsJYm6vT8a0XxZjf/2MjNGZabKPrWZvaqbX3diyZ32sGUXD6+XlujQ/br4NwdGbZxZASjaidcD9p7PxZrrNRgVUHcLvGwKBXn8AGyZQhKwlMn0zsvsvrumpzFt0KP0w3Ap7VL99irEP+NdNJxOZpzadCx0dFte7DJuCwqsEQUrvsq0PwUpmyc5oHFWgkK+XSEMuPS/cxuKgBpjCbIhXKFaJIagAEebUefDw3LD5B+D6QcoRCG/kTrCNsVHc0zW96uzrxx/JczovpPcqThm0+BDuFO0T1Bvif4Nw3JTVABcP+urL797tf87vkK/oBnRi791Dux66rlVb/17vKx3uhh0QelttWTCgJUGqG3wn/W/MthdjzYkvQKS3pIO5fva20sMHHLCg==";
        OSSClient ossClient = new OSSClient(endpoint, key, secret, token);
        File file = new File("C:\\Users\\yjiang\\Desktop\\1536678656078.mp4");
        System.out.println(file.getName());

        ossClient.putObject(new PutObjectRequest(VEDIO_BUCKET_NAME, file.getName(), file).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(file.getName())));
    }

    public static Map<String, String> stsService() throws ClientException {
        // 只有 RAM用户（子账号）才能调用 AssumeRole 接口
        // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
        // 首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
        // 扮演角色的时候额外加上的一个权限限制，写法和授权策略一样
        String policy = null;
        // 可以为空 是一个用来标示临时凭证的名称，一般来说建议使用不同的应用程序用户来区分
        String roleSessionName = "roleUser";
        // 临时凭证的有效期，单位是s，最小为900，最大为3600
        DefaultProfile.addEndpoint("", "", "Sts", endpoint);
        IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, subAccessKeyId, subAccessKeySecret);
        // 用profile构造client
        DefaultAcsClient client = new DefaultAcsClient(profile);
        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setMethod(MethodType.POST);
        // 临时Token的会话名称，自己指定用于标识你的用户，主要用于区分Token颁发给谁
        // acs:ram::$accountID:role/$roleName
        request.setRoleArn("acs:ram::1320836909150978:role/jiangying");
        request.setRoleSessionName(roleSessionName);
        request.setPolicy(policy);
        request.setDurationSeconds(EXPIRE_SECOND);
        // 发起请求，并得到response
        final AssumeRoleResponse response = client.getAcsResponse(request);
        System.out.println("Expiration: " + response.getCredentials().getExpiration());
        System.out.println("Access Key Id: " + response.getCredentials().getAccessKeyId());
        System.out.println("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
        System.out.println("Security Token: " + response.getCredentials().getSecurityToken());
        System.out.println("RequestId: " + response.getRequestId());
        Map<String, String> result = new HashMap<String, String>();
        result.put("endpoint", endpoint);
        result.put("accessKeyId", response.getCredentials().getAccessKeyId());
        result.put("accessKeySecret", response.getCredentials().getAccessKeySecret());
        result.put("securityToken", response.getCredentials().getSecurityToken());
//        result.put("expire(s)", String.valueOf(sessionTime));
        return result;
    }

    public static void upload(String bucketName, User user, String fileName, MultipartFile file) throws IOException {
        // 上传文件
        ossClient.putObject(new PutObjectRequest(bucketName, user.getId() + "/" + fileName, file.getInputStream()).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(fileName)));
    }

    public static void upload(String bucketName, User user, String fileName, File file) throws IOException {
        // 上传文件
        ossClient.putObject(new PutObjectRequest(bucketName, user.getId() + "/" + fileName, file).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(fileName)));
    }

    public static void upload(String bucketName, Long id, String fileName, File file) throws IOException {
        // 上传文件
        ossClient.putObject(new PutObjectRequest(bucketName, id + "/" + fileName, file).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(fileName)));
    }

    public static void upload(String bucketName, String fileName, File file) throws IOException {
        // 上传文件
        ossClient.putObject(new PutObjectRequest(bucketName, fileName, file).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(fileName)));
    }

    public static void upload(String bucketName, String fileName, MultipartFile file) throws IOException {
        // 上传文件
        ossClient.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream()).
                <PutObjectRequest>withProgressListener(new PutObjectProgressListener(fileName)));
    }

    public static void delete(String bucketName, String fileName){
        ossClient.deleteObject(bucketName, fileName);
    }

    public static String getPath(String bucketName, String fileName){
        // 设置URL过期时间为1小时。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        String path;
        if(HTML_IMAGE_BUCKET_NAME.equals(bucketName)){
            path = HTTP_PROTOCOL + bucketName + "." + endpoint + "/"  + fileName;
        }else {
            URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
            path = url.getProtocol() + "://" + url.getHost() + "/" + url.getFile();
        }
        return path;
    }

    public static InputStream download(String bucketName, String fileName) {
        OSSObject ossObject = ossClient.getObject(new GetObjectRequest(bucketName, fileName).
                <GetObjectRequest>withProgressListener(new GetObjectProgressListener(fileName)));
        InputStream inputStream = ossObject.getObjectContent();
        ossClient.shutdown();
        return inputStream;
    }

    static class PutObjectProgressListener implements ProgressListener {
        private long bytesWritten = 0;
        private long totalBytes = -1;
        private boolean succeed = false;

        private String fileName;

        public PutObjectProgressListener(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void progressChanged(ProgressEvent progressEvent) {
            long bytes = progressEvent.getBytes();
            ProgressEventType eventType = progressEvent.getEventType();
            switch (eventType) {
                case TRANSFER_STARTED_EVENT:
                    System.out.println("Start to upload......");
                    break;
                case REQUEST_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                    break;
                case REQUEST_BYTE_TRANSFER_EVENT:
                    this.bytesWritten += bytes;
                    if (this.totalBytes != -1) {
                        int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                        //@TODO 把值塞到redis
                        System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                    } else {
                        System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                    }
                    break;
                case TRANSFER_COMPLETED_EVENT:
                    this.succeed = true;
                    System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                    break;
                case TRANSFER_FAILED_EVENT:
                    System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                    break;
                default:
                    break;
            }
        }

        public boolean isSucceed() {
            return succeed;
        }
    }

    static class GetObjectProgressListener implements ProgressListener {
        private long bytesRead = 0;
        private long totalBytes = -1;
        private boolean succeed = false;

        private String fileName;

        public GetObjectProgressListener(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void progressChanged(ProgressEvent progressEvent) {
            long bytes = progressEvent.getBytes();
            ProgressEventType eventType = progressEvent.getEventType();
            switch (eventType) {
                case TRANSFER_STARTED_EVENT:
                    System.out.println("Start to download......");
                    break;
                case RESPONSE_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    System.out.println(this.totalBytes + " bytes in total will be downloaded to a local file");
                    break;
                case RESPONSE_BYTE_TRANSFER_EVENT:
                    this.bytesRead += bytes;
                    if (this.totalBytes != -1) {
                        int percent = (int)(this.bytesRead * 100.0 / this.totalBytes);
                        //@TODO 把值塞到redis
                        System.out.println(bytes + " bytes have been read at this time, download progress: " +
                                percent + "%(" + this.bytesRead + "/" + this.totalBytes + ")");
                    } else {
                        System.out.println(bytes + " bytes have been read at this time, download ratio: unknown" +
                                "(" + this.bytesRead + "/...)");
                    }
                    break;
                case TRANSFER_COMPLETED_EVENT:
                    this.succeed = true;
                    System.out.println("Succeed to download, " + this.bytesRead + " bytes have been transferred in total");
                    break;
                case TRANSFER_FAILED_EVENT:
                    System.out.println("Failed to download, " + this.bytesRead + " bytes have been transferred");
                    break;
                default:
                    break;
            }
        }
        public boolean isSucceed() {
            return succeed;
        }
    }
}
