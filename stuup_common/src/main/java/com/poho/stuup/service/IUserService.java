package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.custom.CusUser;
import com.poho.stuup.model.Menu;
import com.poho.stuup.model.User;
import com.poho.stuup.model.dto.SimpleUserDTO;
import com.poho.stuup.model.vo.SimpleUserVO;

import java.util.List;
import java.util.Map;

/**
 * @Author wupeng
 * @Description 用户处理接口
 * @Date 2020-07-22 21:32
 * @return
 */
public interface IUserService {
    int deleteByPrimaryKey(Long oid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


    ResponseModel checkLogin(String loginName, String password);

    /**
     * @param key
     * @param state
     * @param pageInt
     * @param pageSize
     * @return
     */
    ResponseModel findDataPageResult(String key, String state, int pageInt, int pageSize);

    /**
     * @param user
     * @return
     */
    ResponseModel saveOrUpdate(User user);

    /**
     * @param ids
     * @return
     */
    ResponseModel del(String ids);

    /**
     * @return
     */
    ResponseModel queryList();

    /**
     * @param userId
     * @param params
     * @return
     */
    ResponseModel updatePassword(String userId, Map params);


    /**
     * @param oid
     * @param yearId
     * @return
     */
    ResponseModel findUserData(Long oid, Long yearId);

    /**
     * @param userList
     * @return
     */
    Map<String, Object> importUserList(List<User> userList);

    /**
     * @description: 获取当前用户信息
     * @param: userId
     * @return: com.poho.stuup.custom.CusUser
     * @author BUNGA
     * @date: 2023/6/20 10:26
     */
    CusUser getUserInfo(Long userId);

    /**
     * 查询用户菜单权限
     *
     * @param userId 用户id
     * @return
     */
    ResponseModel<List<Menu>> queryUserAuthority(long userId);

    /**
     * @description: 简单的用户列表查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.SimpleUserVO>
     * @author BUNGA
     * @date: 2023/6/14 19:18
     */
    IPage<SimpleUserVO> getSimpleUserPage(Page<SimpleUserVO> page, SimpleUserDTO query);
}
