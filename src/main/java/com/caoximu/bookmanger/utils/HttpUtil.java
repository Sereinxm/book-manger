package com.caoximu.bookmanger.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP 工具类
 * 
 * @author caoximu
 */
@Slf4j
@Component
public class HttpUtil {

    private static final int CONNECT_TIMEOUT = 10000; // 10秒
    private static final int READ_TIMEOUT = 30000; // 30秒

    /**
     * 发送GET请求
     * 
     * @param urlString 请求URL
     * @param headers 请求头
     * @return 响应结果
     */
    public String sendGet(String urlString, Map<String, String> headers) {
        log.debug("发送GET请求: {}", urlString);
        
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            
            // 设置请求头
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            
            // 设置通用请求头
            connection.setRequestProperty("User-Agent", "BookManager/1.0");
            connection.setRequestProperty("Accept", "application/json");
            
            // 获取响应
            int responseCode = connection.getResponseCode();
            log.debug("GET请求响应码: {}", responseCode);
            
            String response = readResponse(connection, responseCode);
            log.debug("GET请求响应: {}", response);
            
            return response;
            
        } catch (Exception e) {
            log.error("GET请求失败: {}", urlString, e);
            throw new RuntimeException("HTTP GET请求失败", e);
        }
    }

    /**
     * 发送POST请求
     * 
     * @param urlString 请求URL
     * @param params 请求参数
     * @param headers 请求头
     * @return 响应结果
     */
    public String sendPost(String urlString, Map<String, String> params, Map<String, String> headers) {
        log.debug("发送POST请求: {}", urlString);
        
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 设置请求方法
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setDoOutput(true);
            
            // 设置请求头
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            
            // 设置通用请求头
            connection.setRequestProperty("User-Agent", "BookManager/1.0");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept", "application/json");
            
            // 写入请求参数
            if (params != null && !params.isEmpty()) {
                String paramString = buildParamString(params);
                log.debug("POST请求参数: {}", paramString);
                
                try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)) {
                    writer.write(paramString);
                    writer.flush();
                }
            }
            
            // 获取响应
            int responseCode = connection.getResponseCode();
            log.debug("POST请求响应码: {}", responseCode);
            
            String response = readResponse(connection, responseCode);
            log.debug("POST请求响应: {}", response);
            
            return response;
            
        } catch (Exception e) {
            log.error("POST请求失败: {}", urlString, e);
            throw new RuntimeException("HTTP POST请求失败", e);
        }
    }

    /**
     * 构建参数字符串
     */
    private String buildParamString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            try {
                sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()))
                  .append("=")
                  .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("URL编码失败", e);
            }
        }
        return sb.toString();
    }

    /**
     * 读取响应内容
     */
    private String readResponse(HttpURLConnection connection, int responseCode) throws IOException {
        InputStream inputStream;
        if (responseCode >= 200 && responseCode < 300) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
            if (inputStream == null) {
                throw new IOException("HTTP请求失败，响应码: " + responseCode);
            }
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    /**
     * 解析JSON响应
     */
    public JSONObject parseJsonResponse(String response) {
        try {
            return JSONUtil.parseObj(response);
        } catch (Exception e) {
            log.error("解析JSON响应失败: {}", response, e);
            throw new RuntimeException("JSON解析失败", e);
        }
    }
} 