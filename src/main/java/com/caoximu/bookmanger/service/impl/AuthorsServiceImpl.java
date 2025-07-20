package com.caoximu.bookmanger.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caoximu.bookmanger.domain.request.AddAuthorRequest;
import com.caoximu.bookmanger.domain.request.AuthorQueryRequest;
import com.caoximu.bookmanger.domain.request.UpdateAuthorRequest;
import com.caoximu.bookmanger.domain.response.AuthorResponse;
import com.caoximu.bookmanger.entity.Authors;
import com.caoximu.bookmanger.entity.Users;
import com.caoximu.bookmanger.exception.BizException;
import com.caoximu.bookmanger.mapper.AuthorsMapper;
import com.caoximu.bookmanger.service.IAuthorsService;
import com.caoximu.bookmanger.service.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * <p>
 * 作者表 服务实现类
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorsServiceImpl extends ServiceImpl<AuthorsMapper, Authors> implements IAuthorsService {

    private final IUsersService usersService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAuthor(AddAuthorRequest request) {
        log.info("开始添加作者，姓名：{}", request.getName());
        
        // 检查作者姓名是否已存在
        LambdaQueryWrapper<Authors> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Authors::getName, request.getName())
                   .eq(Authors::getDeleted, false);
        
        if (this.count(queryWrapper) > 0) {
            throw new BizException("作者姓名已存在");
        }
        
        // 如果指定了用户ID，检查用户是否存在
        if (request.getUserId() != null) {
            Users user = usersService.getById(request.getUserId());
            if (user == null) {
                throw new BizException("关联的用户不存在");
            }
            
            // 检查该用户是否已关联其他作者
            LambdaQueryWrapper<Authors> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.eq(Authors::getUserId, request.getUserId())
                           .eq(Authors::getDeleted, false);
            
            if (this.count(userQueryWrapper) > 0) {
                throw new BizException("该用户已关联其他作者");
            }
        }
        
        // 创建作者实体
        Authors author = new Authors();
        BeanUtils.copyProperties(request, author);
        author.setCreateTime(LocalDateTime.now());
        author.setUpdateTime(LocalDateTime.now());
        author.setDeleted(false);
        
        this.save(author);
        log.info("成功添加作者，ID：{}", author.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAuthor(Long authorId, UpdateAuthorRequest request) {
        log.info("开始更新作者信息，ID：{}", authorId);
        
        // 检查作者是否存在
        Authors existingAuthor = this.getById(authorId);
        if (existingAuthor == null || existingAuthor.getDeleted()) {
            throw new BizException("作者不存在");
        }
        
        // 检查姓名是否与其他作者重复（排除当前作者）
        LambdaQueryWrapper<Authors> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Authors::getName, request.getName())
                   .eq(Authors::getDeleted, false)
                   .ne(Authors::getId, authorId);
        
        if (this.count(queryWrapper) > 0) {
            throw new BizException("作者姓名已存在");
        }
        
        // 如果指定了用户ID，检查用户是否存在和是否已关联其他作者
        if (request.getUserId() != null) {
            Users user = usersService.getById(request.getUserId());
            if (user == null) {
                throw new BizException("关联的用户不存在");
            }
            
            // 检查该用户是否已关联其他作者（排除当前作者）
            LambdaQueryWrapper<Authors> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.eq(Authors::getUserId, request.getUserId())
                           .eq(Authors::getDeleted, false)
                           .ne(Authors::getId, authorId);
            
            if (this.count(userQueryWrapper) > 0) {
                throw new BizException("该用户已关联其他作者");
            }
        }
        
        // 更新作者信息
        Authors author = new Authors();
        BeanUtils.copyProperties(request, author);
        author.setId(authorId);
        author.setUpdateTime(LocalDateTime.now());
        
        this.updateById(author);
        log.info("成功更新作者信息，ID：{}", authorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAuthor(Long authorId) {
        log.info("开始删除作者，ID：{}", authorId);
        
        // 检查作者是否存在
        Authors author = this.getById(authorId);
        if (author == null || author.getDeleted()) {
            throw new BizException("作者不存在");
        }
        
        // TODO: 这里可以添加检查作者是否有关联的图书，如果有则不允许删除
        
        // 逻辑删除
        author.setDeleted(true);
        author.setUpdateTime(LocalDateTime.now());
        this.updateById(author);
        
        log.info("成功删除作者，ID：{}", authorId);
    }

    @Override
    public AuthorResponse getAuthorById(Long authorId) {
        log.info("查询作者详情，ID：{}", authorId);
        
        Authors author = this.getById(authorId);
        if (author == null || author.getDeleted()) {
            throw new BizException("作者不存在");
        }
        
        AuthorResponse response = new AuthorResponse();
        BeanUtils.copyProperties(author, response);
        
        // 如果有关联用户，查询用户姓名
        if (author.getUserId() != null) {
            Users user = usersService.getById(author.getUserId());
            if (user != null) {
                response.setUserName(user.getName());
            }
        }
        
        return response;
    }

    @Override
    public Page<AuthorResponse> getAuthors(AuthorQueryRequest request) {
        log.info("分页查询作者，页码：{}，页大小：{}", request.getCurrent(), request.getSize());
        
        // 构建查询条件
        LambdaQueryWrapper<Authors> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Authors::getDeleted, false);
        
        if (StrUtil.isNotBlank(request.getName())) {
            queryWrapper.like(Authors::getName, request.getName());
        }
        
        if (StrUtil.isNotBlank(request.getNationality())) {
            queryWrapper.eq(Authors::getNationality, request.getNationality());
        }
        
        if (request.getUserId() != null) {
            queryWrapper.eq(Authors::getUserId, request.getUserId());
        }
        
        queryWrapper.orderByDesc(Authors::getCreateTime);
        
        // 分页查询
        Page<Authors> authorPage = new Page<>(request.getCurrent(), request.getSize());
        Page<Authors> result = this.page(authorPage, queryWrapper);
        
        // 转换为响应DTO
        Page<AuthorResponse> responsePage = new Page<>();
        BeanUtils.copyProperties(result, responsePage, "records");
        
        // 转换记录
        responsePage.setRecords(
            result.getRecords().stream().map(author -> {
                AuthorResponse response = new AuthorResponse();
                BeanUtils.copyProperties(author, response);
                
                // 如果有关联用户，查询用户姓名
                if (author.getUserId() != null) {
                    Users user = usersService.getById(author.getUserId());
                    if (user != null) {
                        response.setUserName(user.getName());
                    }
                }
                
                return response;
            }).collect(Collectors.toList())
        );
        
        log.info("查询完成，共找到{}条记录", responsePage.getTotal());
        return responsePage;
    }
}
