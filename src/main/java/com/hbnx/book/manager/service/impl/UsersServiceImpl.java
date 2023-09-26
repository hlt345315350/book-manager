package com.hbnx.book.manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbnx.book.manager.entity.Users;
import com.hbnx.book.manager.service.UsersService;
import com.hbnx.book.manager.mapper.UsersMapper;
import com.hbnx.book.manager.util.ro.PageIn;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author nx
* @description 针对表【users】的数据库操作Service实现
* @createDate 2023-09-26 16:11:51
*/
@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UserDetailsService,UsersService{
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 添加用户
     * @param users 用户
     * @return 返回添加的用户
     */
    public Users addUser(Users users) {
        int result = usersMapper.insert(users);
        if (result > 0 ){
            //插入成功,则返回插入的用户(mybatis-plus插入会自动给实体赋值id)
            return users;
        }else {
            return null;
        }
//        return usersRepository.saveAndFlush(users);
    }

    /**
     * 编辑用户
     * @param users 用户对象
     * @return true or false
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(Users users) {
        return usersMapper.updateUsers(BeanUtil.beanToMap(users))>0;
    }

    /**
     * 用户详情
     * @param id 主键
     * @return 用户详情
     */
    public Users findUserById(Integer id) {
        Users users = usersMapper.selectById(id);
        return users;
        /*Optional<Users> optional = usersRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;*/
    }

    /**
     * 删除用户
     * @param id 主键
     * @return true or false
     */
    public void deleteUser(Integer id) {
        usersMapper.deleteById(id);
//        usersRepository.deleteById(id);
    }


    /**
     * 用户搜索查询(mybatis 分页)
     * @param pageIn
     * @return
     */
    public PageInfo<Users> getUserList(PageIn pageIn) {

        PageHelper.startPage(pageIn.getCurrPage(),pageIn.getPageSize());
        List<Users> listByLike = usersMapper.findListByLike(pageIn.getKeyword());
        return new PageInfo<>(listByLike);
    }

    /**
     * 用户鉴权
     * @param username 用户名
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查找用户
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername,username);
//        log.info("user name is {}",username);
        Users user = usersMapper.selectOne(queryWrapper);
//        Users user = usersRepository.findByUsername(username);
        // 获得角色
        String role = String.valueOf(user.getIsAdmin());
        // 角色集合
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 角色必须以`ROLE_`开头，数据库中没有，则在这里加
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        // 数据库密码是明文, 需要加密进行比对
        return new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), authorities);
    }

    /**
     * 用户名查询用户信息
     * @param username 用户名
     */
    public Users findByUsername(String username) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getUsername,username);
        Users user = usersMapper.selectOne(queryWrapper);
        return user;
//        return usersRepository.findByUsername(username);
    }
}




