package com.caoximu.bookmanger;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.caoximu.bookmanger.domain.request.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.HttpCookie;
import java.time.LocalDate;

/**
 * 图书管理系统完整接口测试类
 * 
 * @author caoximu
 */
class BookMangerApplicationTests {

    // 基础配置
    private static final String BASE_URL = "http://localhost:18886/api";
    
    // 加密后密码 123456
    private static final String ENCRYPTED_PWD = "D/ual88ZF0fzFpy3U/sDq7E/tDy2z65imTyVqDpMDsf9L+jEAO03juj1Cj0PZvVHWkm3TwOq9qUTVfBmbIQNeTxNhbgI+F72+RcsxCXaZr97UkoWWlOrC/o747KYz74EslAqToj/xd/MzZDP4GsZmaUxCEC2hGSrnU/OEa3i4X3qToYmLCEAg1Aa5D8tXslP58azssmC7ERHVVD57vM/ANqxL6NZSoxlAOi5X4DtDiRWksXd+41wyd/DDTCW/1zgjeicUbuvpOSq/KEpH0jPcD2sl3zZlQ0tA9tVqC1zpB4WkVZjzBBrJxkCF2XPOjGADg7AWl85R0nmaSAz+n+PuA==";
    
    // 测试用户信息
    private String adminToken;
    private String userToken = "e0a4019c-bf08-4073-89c5-f9fd7ff11b35";
    private String testBookIsbn = "271f0c12-5e4a-4339-846f-946af72c0d4e";
    private Long testCopyId;
    private Long testBorrowRecordId;

    @BeforeEach
    void setup() {
        System.out.println("=== 开始执行测试 ===");
    }

    // ================================ 认证管理测试 ================================

    /**
     * 测试用户注册
     */
    @Test
    @Order(1)
    void testRegister() {
        System.out.println("\n=== 测试用户注册 ===");
        
        RegisterRequest request = new RegisterRequest();
        request.setName("测试用户");
        request.setEmail("testuser@example.com");
        request.setPassword(ENCRYPTED_PWD);
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.post(BASE_URL + "/auth/register")
                .header("Content-Type", "application/json")
                .body(json)
                .execute();
        
        System.out.println("注册响应: " + response.body());
        
        // 获取用户token
        HttpCookie cookie = response.getCookie("Authorization");
        if (cookie != null) {
            userToken = cookie.getValue();
            System.out.println("用户Token: " + userToken);
        }
    }

    /**
     * 测试管理员登录
     */
    @Test
    @Order(2)
    void testAdminLogin() {
        System.out.println("\n=== 测试管理员登录 ===");
        
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@system.com");
        request.setPassword(ENCRYPTED_PWD);
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.post(BASE_URL + "/auth/login")
                .header("Content-Type", "application/json")
                .body(json)
                .execute();
        
        System.out.println("管理员登录响应: " + response.body());
        
        // 获取管理员token
        HttpCookie cookie = response.getCookie("Authorization");
        if (cookie != null) {
            adminToken = cookie.getValue();
            System.out.println("管理员Token: " + adminToken);
        }
    }

    /**
     * 测试普通用户登录
     */
    @Test
    @Order(3)
    void testUserLogin() {
        System.out.println("\n=== 测试普通用户登录 ===");
        
        LoginRequest request = new LoginRequest();
        request.setEmail("testuser@example.com");
        request.setPassword(ENCRYPTED_PWD);
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.post(BASE_URL + "/auth/login")
                .header("Content-Type", "application/json")
                .body(json)
                .execute();
        
        System.out.println("用户登录响应: " + response.body());
    }

    /**
     * 测试获取Google OAuth2授权URL
     */
    @Test
    @Order(4)
    void testGetGoogleAuthUrl() {
        System.out.println("\n=== 测试获取Google OAuth2授权URL ===");
        
        HttpResponse response = HttpRequest.get(BASE_URL + "/auth/google/auth-url")
                .form("state", "test_state")
                .execute();
        
        System.out.println("Google授权URL响应: " + response.body());
    }

    /**
     * 测试作者认证（需要管理员权限）
     */
    @Test
    @Order(5)
    void testAuthorAuth() {
        System.out.println("\n=== 测试作者认证 ===");
        
        if (adminToken == null) {
            System.out.println("需要先登录管理员账号");
            return;
        }
        
        AuthorAuthRequest request = new AuthorAuthRequest();
        request.setUserId(1L); // 假设要认证的用户ID
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.post(BASE_URL + "/auth/authorAuth")
                .header("Content-Type", "application/json")
                .cookie(new HttpCookie("Authorization", adminToken))
                .body(json)
                .execute();
        
        System.out.println("作者认证响应: " + response.body());
    }

    /**
     * 测试查看所有用户（需要超级管理员权限）
     */
    @Test
    @Order(6)
    void testGetAllUsers() {
        System.out.println("\n=== 测试查看所有用户 ===");
        
        UserQueryRequest request = new UserQueryRequest();
        request.setPage(1);
        request.setPageSize(10);
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.post(BASE_URL + "/auth/users")
                .header("Content-Type", "application/json")
                .cookie(new HttpCookie("Authorization", adminToken))
                .body(json)
                .execute();
        
        System.out.println("查看所有用户响应: " + response.body());
    }

    /**
     * 测试修改个人信息
     */
    @Test
    @Order(7)
    void testUpdateProfile() {
        System.out.println("\n=== 测试修改个人信息 ===");
        
        if (userToken == null) {
            System.out.println("需要先登录用户账号");
            return;
        }
        
        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setName("更新后的用户名");
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.post(BASE_URL + "/auth/updateProfile")
                .header("Content-Type", "application/json")
                .cookie(new HttpCookie("Authorization", adminToken))
                .body(json)
                .execute();
        
        System.out.println("修改个人信息响应: " + response.body());
    }

    /**
     * 测试登出
     */
    @Test
    @Order(8)
    void testLogout() {
        System.out.println("\n=== 测试用户登出 ===");
        
        if (userToken == null) {
            System.out.println("需要先登录用户账号");
            return;
        }
        
        HttpResponse response = HttpRequest.post(BASE_URL + "/auth/logout")
                .cookie(new HttpCookie("Authorization", adminToken))
                .execute();
        
        System.out.println("登出响应: " + response.body());
    }

    // ================================ 图书管理测试 ================================

    /**
     * 测试添加图书（需要管理员权限）
     */
    @Test
    @Order(10)
    void testAddBook() {
        System.out.println("\n=== 测试添加图书 ===");
        
        if (adminToken == null) {
            System.out.println("需要先登录管理员账号");
            return;
        }
        
        AddBookRequest request = new AddBookRequest();
        request.setIsbn("9787111544937");
        request.setTitle("Java核心技术");
        request.setPublisher("机械工业出版社");
        request.setPublishYear(LocalDate.of(2023, 1, 1));
        request.setDescription("Java编程经典教材");
        request.setAuthorId(1L);
        request.setInitialCopies(5);
        request.setLocation("A区1层");
        
        testBookIsbn = request.getIsbn(); // 保存用于后续测试
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.post(BASE_URL + "/books/admin/add")
                .header("Content-Type", "application/json")
                .cookie(new HttpCookie("Authorization", adminToken))
                .body(json)
                .execute();
        
        System.out.println("添加图书响应: " + response.body());
    }

    /**
     * 测试分页查询图书
     */
    @Test
    @Order(11)
    void testSearchBooks() {
        System.out.println("\n=== 测试分页查询图书 ===");
        
        HttpResponse response = HttpRequest.get(BASE_URL + "/books/search")
                .form("current", "1")
                .form("size", "10")
                .form("title", "Java")
                .execute();
        
        System.out.println("查询图书响应: " + response.body());
    }

    /**
     * 测试根据ISBN获取图书详情
     */
    @Test
    @Order(12)
    void testGetBookByIsbn() {
        System.out.println("\n=== 测试根据ISBN获取图书详情 ===");
        
        if (testBookIsbn == null) {
            testBookIsbn = "9787111544937"; // 使用默认ISBN
        }
        
        HttpResponse response = HttpRequest.get(BASE_URL + "/books/" + testBookIsbn)
                .form("includeCopies", "true")
                .execute();
        
        System.out.println("获取图书详情响应: " + response.body());
    }

    /**
     * 测试更新图书信息（需要管理员权限）
     */
    @Test
    @Order(13)
    void testUpdateBook() {
        System.out.println("\n=== 测试更新图书信息 ===");
        
        if (adminToken == null || testBookIsbn == null) {
            System.out.println("需要先登录管理员账号并添加图书");
            return;
        }
        
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Java核心技术（第11版）");
        request.setPublisher("机械工业出版社");
        request.setPublishYear(LocalDate.of(2023, 6, 1));
        request.setDescription("Java编程经典教材，最新版本");
        request.setAuthorId(1L);
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.put(BASE_URL + "/books/admin/" + testBookIsbn)
                .header("Content-Type", "application/json")
                .cookie(new HttpCookie("Authorization", adminToken))
                .body(json)
                .execute();
        
        System.out.println("更新图书信息响应: " + response.body());
    }

    // ================================ 图书副本管理测试 ================================

    /**
     * 测试为图书添加副本（需要管理员权限）
     */
    @Test
    @Order(15)
    void testAddBookCopies() {
        System.out.println("\n=== 测试为图书添加副本 ===");
        
        if (adminToken == null || testBookIsbn == null) {
            System.out.println("需要先登录管理员账号并添加图书");
            return;
        }
        
        AddBookCopyRequest request = new AddBookCopyRequest();
        request.setCount(3);
        request.setLocation("B区2层");
        request.setConditionNotes("状态良好");
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.post(BASE_URL + "/bookCopies/admin/" + testBookIsbn + "/add")
                .header("Content-Type", "application/json")
                .cookie(new HttpCookie("Authorization", adminToken))
                .body(json)
                .execute();
        
        System.out.println("添加图书副本响应: " + response.body());
    }

    /**
     * 测试根据ISBN获取所有副本
     */
    @Test
    @Order(16)
    void testGetCopiesByIsbn() {
        System.out.println("\n=== 测试根据ISBN获取所有副本 ===");
        
        if (testBookIsbn == null) {
            testBookIsbn = "9787111544937"; // 使用默认ISBN
        }
        
        HttpResponse response = HttpRequest.get(BASE_URL + "/bookCopies/isbn/" + testBookIsbn)
                .execute();
        
        System.out.println("获取图书副本响应: " + response.body());
        
        // 从响应中提取副本ID用于后续测试
        try {
            JSONObject jsonResponse = JSONUtil.parseObj(response.body());
            if (jsonResponse.getBool("success") && jsonResponse.getJSONArray("data").size() > 0) {
                testCopyId = jsonResponse.getJSONArray("data").getJSONObject(0).getLong("id");
                System.out.println("提取到副本ID: " + testCopyId);
            }
        } catch (Exception e) {
            System.out.println("无法提取副本ID: " + e.getMessage());
        }
    }

    /**
     * 测试更新副本信息（需要管理员权限）
     */
    @Test
    @Order(17)
    void testUpdateBookCopy() {
        System.out.println("\n=== 测试更新副本信息 ===");
        
        if (adminToken == null || testCopyId == null) {
            System.out.println("需要先登录管理员账号并获取副本ID");
            return;
        }
        
        UpdateBookCopyRequest request = new UpdateBookCopyRequest();
        request.setStatus("available");
        request.setLocation("A区1层");
        request.setConditionNotes("状态良好");
        request.setVersion(1); // 乐观锁版本号
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.put(BASE_URL + "/bookCopies/admin/" + testCopyId)
                .header("Content-Type", "application/json")
                .cookie(new HttpCookie("Authorization", adminToken))
                .body(json)
                .execute();
        
        System.out.println("更新副本信息响应: " + response.body());
    }

    // ================================ 借阅记录管理测试 ================================

    /**
     * 测试借阅图书（需要管理员权限）
     */
    @Test
    @Order(20)
    void testBorrowBook() {
        System.out.println("\n=== 测试借阅图书 ===");
        
        if (adminToken == null || testBookIsbn == null) {
            System.out.println("需要先登录管理员账号并添加图书");
            return;
        }
        
        BorrowBookRequest request = new BorrowBookRequest();
        request.setIsbn(testBookIsbn);
        request.setUserId(1L); // 假设用户ID
        request.setNotes("测试借阅");
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.post(BASE_URL + "/borrowRecords/borrow")
                .header("Content-Type", "application/json")
                .cookie(new HttpCookie("Authorization", adminToken))
                .body(json)
                .execute();
        
        System.out.println("借阅图书响应: " + response.body());
    }

    /**
     * 测试获取当前用户借阅记录
     */
    @Test
    @Order(21)
    void testGetMyBorrowRecords() {
        System.out.println("\n=== 测试获取当前用户借阅记录 ===");
        
        if (userToken == null) {
            System.out.println("需要先登录用户账号");
            return;
        }
        
        HttpResponse response = HttpRequest.get(BASE_URL + "/borrowRecords/my")
                .cookie(new HttpCookie("Authorization", adminToken))
                .form("current", "1")
                .form("size", "10")
                .execute();
        
        System.out.println("获取用户借阅记录响应: " + response.body());
    }

    /**
     * 测试获取所有借阅记录（需要管理员权限）
     */
    @Test
    @Order(22)
    void testGetAllBorrowRecords() {
        System.out.println("\n=== 测试获取所有借阅记录 ===");
        
        if (adminToken == null) {
            System.out.println("需要先登录管理员账号");
            return;
        }
        
        HttpResponse response = HttpRequest.get(BASE_URL + "/borrowRecords/admin/all")
                .cookie(new HttpCookie("Authorization", adminToken))
                .form("current", "1")
                .form("size", "10")
                .execute();
        
        System.out.println("获取所有借阅记录响应: " + response.body());
        
        // 从响应中提取借阅记录ID用于后续测试
        try {
            JSONObject jsonResponse = JSONUtil.parseObj(response.body());
            if (jsonResponse.getBool("success") && 
                jsonResponse.getJSONObject("data").getJSONArray("records").size() > 0) {
                testBorrowRecordId = jsonResponse.getJSONObject("data")
                    .getJSONArray("records").getJSONObject(0).getLong("id");
                System.out.println("提取到借阅记录ID: " + testBorrowRecordId);
            }
        } catch (Exception e) {
            System.out.println("无法提取借阅记录ID: " + e.getMessage());
        }
    }

    /**
     * 测试获取作者图书借阅统计
     */
    @Test
    @Order(23)
    void testGetAuthorBorrowStatistics() {
        System.out.println("\n=== 测试获取作者图书借阅统计 ===");
        
        if (userToken == null) {
            System.out.println("需要先登录用户账号");
            return;
        }
        
        HttpResponse response = HttpRequest.get(BASE_URL + "/borrowRecords/author/statistics")
                .cookie(new HttpCookie("Authorization", adminToken))
                .execute();
        
        System.out.println("获取作者借阅统计响应: " + response.body());
    }

    /**
     * 测试归还图书（需要管理员权限）
     */
    @Test
    @Order(24)
    void testReturnBook() {
        System.out.println("\n=== 测试归还图书 ===");
        
        if (adminToken == null || testBorrowRecordId == null) {
            System.out.println("需要先登录管理员账号并获取借阅记录ID");
            return;
        }
        
        ReturnBookRequest request = new ReturnBookRequest();
        request.setBorrowRecordId(testBorrowRecordId);
        request.setNotes("测试归还");
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.post(BASE_URL + "/borrowRecords/return")
                .header("Content-Type", "application/json")
                .cookie(new HttpCookie("Authorization", adminToken))
                .body(json)
                .execute();
        
        System.out.println("归还图书响应: " + response.body());
    }

    // ================================ 系统配置管理测试 ================================

    /**
     * 测试获取系统所有配置参数（需要管理员权限）
     */
    @Test
    @Order(30)
    void testGetSystemConfigs() {
        System.out.println("\n=== 测试获取系统所有配置参数 ===");
        
        if (adminToken == null) {
            System.out.println("需要先登录管理员账号");
            return;
        }
        
        HttpResponse response = HttpRequest.get(BASE_URL + "/systemConfigs/list")
                .cookie(new HttpCookie("Authorization", adminToken))
                .execute();
        
        System.out.println("获取系统配置响应: " + response.body());
    }

    /**
     * 测试修改系统配置参数（需要管理员权限）
     */
    @Test
    @Order(31)
    void testUpdateSystemConfig() {
        System.out.println("\n=== 测试修改系统配置参数 ===");
        
        if (adminToken == null) {
            System.out.println("需要先登录管理员账号");
            return;
        }
        
        UpdateSystemConfigRequest request = new UpdateSystemConfigRequest();
        request.setConfigKey("max_borrow_days");
        request.setConfigValue("30");
        
        String json = JSONUtil.toJsonStr(request);
        HttpResponse response = HttpRequest.put(BASE_URL + "/systemConfigs/update")
                .header("Content-Type", "application/json")
                .cookie(new HttpCookie("Authorization", adminToken))
                .body(json)
                .execute();
        
        System.out.println("修改系统配置响应: " + response.body());
    }

    // ================================ 限流功能测试 ================================

    /**
     * 测试局部限流
     */
    @Test
    @Order(40)
    void testLocalRateLimit() {
        System.out.println("\n=== 测试局部限流 ===");
        
        for (int i = 1; i <= 5; i++) {
            HttpResponse response = HttpRequest.get(BASE_URL + "/limit/test-local")
                    .execute();
            System.out.println("第" + i + "次请求响应: " + response.body());
            
            try {
                Thread.sleep(100); // 短暂休眠
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 测试全局限流
     */
    @Test
    @Order(41)
    void testGlobalRateLimit() {
        System.out.println("\n=== 测试全局限流 ===");
        
        for (int i = 1; i <= 5; i++) {
            HttpResponse response = HttpRequest.get(BASE_URL + "/limit/test-global")
                    .execute();
            System.out.println("第" + i + "次请求响应: " + response.body());
            
            try {
                Thread.sleep(100); // 短暂休眠
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 测试获取限流配置
     */
    @Test
    @Order(42)
    void testGetRateLimitConfig() {
        System.out.println("\n=== 测试获取限流配置 ===");
        
        HttpResponse response = HttpRequest.get(BASE_URL + "/limit/config")
                .execute();
        
        System.out.println("获取限流配置响应: " + response.body());
    }

    /**
     * 测试压力测试接口
     */
    @Test
    @Order(43)
    void testStressTest() {
        System.out.println("\n=== 测试压力测试接口 ===");
        
        HttpResponse response = HttpRequest.get(BASE_URL + "/limit/stress-test")
                .execute();
        
        System.out.println("压力测试接口响应: " + response.body());
    }

    // ================================ 清理数据测试 ================================

    /**
     * 测试删除图书（需要管理员权限）
     */
    @Test
    @Order(50)
    void testDeleteBook() {
        System.out.println("\n=== 测试删除图书 ===");
        
        if (adminToken == null || testBookIsbn == null) {
            System.out.println("需要先登录管理员账号并添加图书");
            return;
        }
        
        HttpResponse response = HttpRequest.delete(BASE_URL + "/books/admin/" + testBookIsbn)
                .cookie(new HttpCookie("Authorization", adminToken))
                .execute();
        
        System.out.println("删除图书响应: " + response.body());
    }
}
