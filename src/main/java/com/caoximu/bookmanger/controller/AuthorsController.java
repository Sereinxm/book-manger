package com.caoximu.bookmanger.controller;

import com.caoximu.bookmanger.service.IAuthorsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 作者表 前端控制器
 * </p>
 *
 * @author caoximu
 * @since 2025-07-19
 */
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorsController {

    private final IAuthorsService authorsService;



}
