package com.poho.stuup.api.controller;

import com.poho.stuup.service.RecDefaultService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 默认积分记录表（除综评表） 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-26
 */
@RestController
@RequestMapping("/recDefault")
public class RecDefaultController {

    @Resource
    private RecDefaultService recDefaultService;

}
