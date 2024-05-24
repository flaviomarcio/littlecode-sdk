package com.littlecode.cloud.s3.tests;

import com.littlecode.cloud.s3.S3ClientUtil;
import com.littlecode.files.IOUtil;
import com.littlecode.parsers.ObjectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class S3ClientUtilTest {

    private S3ClientUtil newS3Object() {
        var s3Object = new S3ClientUtil();
        s3Object.setEndpoint("http://localhost");
        s3Object.setRegion(Region.AP_EAST_1.id());
        s3Object.setBucket("bucketName");
        s3Object.setAccessKey("accessKey");
        s3Object.setSecretKey("secretKey");
        return s3Object;
    }

    private S3ClientUtil newS3ObjectMock() throws IOException {
        var s3Object=newS3Object();
        s3Object.newClient();
        s3Object.setS3Client(Mockito.mock(S3Client.class));
        return s3Object;
    }

    @Test
    @DisplayName("deve validar construtor/@Data")
    public void UT_CHECK_CONSTRUCTOR_DATA() {
        Assertions.assertDoesNotThrow(() -> new S3ClientUtil());
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build());
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().getS3Client());
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().getEndpoint());
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().getRegion());
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().getBucket());
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().getAccessKey());
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().getSecretKey());

        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().setS3Client(null));
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().setEndpoint(""));
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().setRegion(""));
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().setBucket(""));
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().setAccessKey(""));
        Assertions.assertDoesNotThrow(() -> S3ClientUtil.builder().build().setSecretKey(""));
    }

    @Test
    @DisplayName("deve construir credenciais")
    public void UT_CHECK_CONSTRUCTOR_create_AwsBasicCredentials() {
        var s3Object = newS3Object();
        Assertions.assertDoesNotThrow(s3Object::createAwsBasicCredentials);
        s3Object.setAccessKey(null);
        s3Object.setSecretKey(null);
        Assertions.assertDoesNotThrow(s3Object::createAwsBasicCredentials);
        Assertions.assertNull(s3Object.createAwsBasicCredentials());
    }

    @Test
    @DisplayName("deve construir do S3Client")
    public void UT_CHECK_CONSTRUCTOR_new_client() {

        {//ok
            var s3Object=newS3Object();
            Assertions.assertDoesNotThrow(s3Object::newClient);
            Assertions.assertDoesNotThrow(s3Object::newClient);
            s3Object.setEndpoint(null);
            s3Object.setAccessKey(null);
            s3Object.setSecretKey(null);
            Assertions.assertDoesNotThrow(s3Object::newClient);
        }

        {
            var s3Object=newS3Object();
            s3Object.setRegion(null);
            Assertions.assertThrows(NullPointerException.class, s3Object::newClient);
        }

        {
            var s3Object=newS3Object();
            s3Object.setBucket(null);
            Assertions.assertThrows(NullPointerException.class, s3Object::newClient);
        }

    }

    @Test
    @DisplayName("deve validar metodo put")
    public void UT_CHECK_METHOD_PUT() throws IOException {
        var s3Object=newS3ObjectMock();
        var temFile=File.createTempFile("s3-upload", ".txt");
        IOUtil
                .target(temFile)
                .writeAll(ObjectUtil.toString(s3Object));
        Assertions.assertDoesNotThrow(() -> s3Object.put(temFile, "test.txt"));
        Assertions.assertDoesNotThrow(() -> s3Object.put(temFile.toPath(), "test.txt"));
        Assertions.assertDoesNotThrow(() -> s3Object.put(temFile.getAbsolutePath(), "test.txt"));
        Assertions.assertDoesNotThrow(() -> s3Object.put(new FileInputStream(temFile), "test.txt"));
    }

    @Test
    @DisplayName("deve validar metodo get")
    public void UT_CHECK_METHOD_GET() throws IOException {
        var temFile=File.createTempFile("s3-get-mock", ".txt");
        IOUtil
                .target(temFile)
                .writeAll(ObjectUtil.toString(this));
        var s3Object=newS3ObjectMock();
        ResponseInputStream<GetObjectResponse> response = new ResponseInputStream(new Object(),new FileInputStream(temFile));
        Mockito.when(s3Object.getS3Client().getObject(Mockito.any(GetObjectRequest.class))).thenReturn(response);
        Assertions.assertDoesNotThrow(() -> s3Object.get("test.txt"));
        Mockito.when(s3Object.getS3Client().getObject(Mockito.any(GetObjectRequest.class))).thenReturn(null);
        Assertions.assertDoesNotThrow(() -> s3Object.get("test.txt"));
    }

    @Test
    @DisplayName("deve validar metodo exists")
    public void UT_CHECK_METHOD_EXISTS() throws IOException {
        var temFile=File.createTempFile("s3-get-mock", ".txt");
        IOUtil
                .target(temFile)
                .writeAll(ObjectUtil.toString(this));
        var s3Object=newS3ObjectMock();
        ResponseInputStream<GetObjectResponse> response = new ResponseInputStream(new Object(),new FileInputStream(temFile));
        Mockito.when(s3Object.getS3Client().getObject(Mockito.any(GetObjectRequest.class))).thenReturn(response);
        Assertions.assertDoesNotThrow(() -> s3Object.exists("test.txt"));
        Mockito.when(s3Object.getS3Client().getObject(Mockito.any(GetObjectRequest.class))).thenReturn(null);
        Assertions.assertDoesNotThrow(() -> s3Object.exists("test.txt"));
    }

    @Test
    @DisplayName("deve validar metodo delete")
    public void UT_CHECK_METHOD_DELETE() throws IOException {
        var s3Object=newS3ObjectMock();
        Assertions.assertDoesNotThrow(() -> s3Object.delete("test.txt"));

    }



}
