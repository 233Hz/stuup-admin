package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.LoginLogMapper;
import com.poho.stuup.model.LoginLog;
import com.poho.stuup.service.LoginLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登入日志 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-22
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

}
