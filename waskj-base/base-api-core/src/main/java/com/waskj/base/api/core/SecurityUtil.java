package com.waskj.base.api.core;

import com.waskj.base.domain.core.UserEntity;
import com.waskj.base.domain.core.vo.ApprovePermissionVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 权限工具类
 *
 * @author 徐成龙
 * @date 2016-05-18
 */
public abstract class SecurityUtil {

    private static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    public final static Map<String, ApprovePermissionVO> approvePermission = new HashMap<String, ApprovePermissionVO>();

    private static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    private static Object getPrincipal() {
        return getSubject().getPrincipal();
    }

    private static Session getSession(){
        return getSubject().getSession();
    }

    private static ThreadLocal userHolder = new InheritableThreadLocal();

    // 将当前登录用户写入到userHolder中
    public static void pushUser(Object user){
        if( user != null )
            userHolder.set(user);
        else
            throw new RuntimeException("user can not be null!");
    }

    // 删除当前登录用户
    public static void popUser(){
        userHolder.remove();
    }


    /**
     * 获取当前登录用户
     *
     * @param clazz
     * @param <T>
     * @throws NoAccountLoginException
     * @return
     */
    public static <T> T currentUser(Class<T> clazz) throws NoAccountLoginException  {
        if( userHolder.get() != null )
            return clazz.cast(userHolder.get());

        T user = null;
        if( getSubject() != null && getSubject().getPrincipals() != null ){
            user = getSubject().getPrincipals().oneByType(clazz);
        }

        if( user == null )
            throw new NoAccountLoginException();
        return user;
    }

    public static UserEntity<Long> currentUser(){
        return (UserEntity<Long>) currentUser(UserEntity.class);
    }

    public static Long currentUserId(){
        return currentUserId(Long.class);
    }

    public static <T> T currentUserId(Class<T> idType){
        if(!isLogined())
            throw new RuntimeException("user is not login!");
        UserEntity entity = currentUser(UserEntity.class);
        Serializable id = entity.getId();
        return idType.cast(id);
    }

    /**
     * 用户登录
     *
     * @param loginId  用户账号
     * @param password 用户密码
     * @return true 登录成功<br/>false 登录失败
     */
    public static boolean login(String loginId, String password) {
        boolean isSuccess = true;
        UsernamePasswordToken token = new UsernamePasswordToken(loginId, password);
        try {
            getSubject().login(token);
        } catch (AuthenticationException e) {
            logger.error("账号{} 登录失败", e);
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 注销当前登录
     */
    public static void logout() {
        popUser();
        getSubject().logout();
    }

    /**
     * 判断当前用户是否登录
     *
     * @return true  已经登录  <br/>
     * false 没有登录
     */
    public static boolean isLogined() {
        return getSubject().isAuthenticated();
    }


    public static void setAttribute(Object key,Object value){
        if( getSession() == null ){
            throw new RuntimeException("无法获取session！");
        }

        getSession().setAttribute(key,value);

    }

    public static <T> T getAttribute(Object key,Class<T> clazz){
        return getSession() == null? null : (T)getSession().getAttribute(key);
    }

    public static Object getAttribute(Object key){
        return getSession() == null ? null : getSession().getAttribute(key);
    }

    public static void removeAttribute(Object key){
        if( getSession() != null )
            getSession().removeAttribute(key);
    }

    // ============== 权限,角色判断部分
    public static boolean hasPermission(String permission) {
        return getSubject().isPermitted(permission);
    }

    public static boolean[] hasPermissions(String... permissions) {
        return getSubject().isPermitted(permissions);
    }

    public static boolean hasAllPermissions(String... permissions) {
        return getSubject().isPermittedAll(permissions);
    }

    public static boolean hasRoles(String role) {
        return getSubject().hasRole(role);
    }

    public static boolean[] hasRoles(List<String> roles) {
        return getSubject().hasRoles(roles);
    }

    public static boolean hasAllRole(Collection<String> roles) {
        return getSubject().hasAllRoles(roles);
    }

    // ========================  辅助工具方法

    private final static int DEFAULT_SALT_MAX = 1000;
    /**
     * 生成随机盐值
     * @param max 随机数取值上限
     * @return
     */
    public static String generateSalt(int max){
        int num =  (int)(Math.random() * max);
        return String.valueOf(num);
    }
    public static String generateSalt(){
        return generateSalt(DEFAULT_SALT_MAX);
    }

    /**
     * md5 加密
     * @param orignPwd 原始密码
     * @return  md5 加密后的密码
     */
    public static String encryptMd5(String orignPwd){
        Md5Hash hash = new Md5Hash(orignPwd);
        return hash.toHex().toString();
    }

    /**
     * md5 加密
     * @param originPwd 原始密码
     * @param salt  盐
     * @return  md5 带盐加密后的密码
     */
    public static String encryptMd5(String originPwd,String salt){
        Md5Hash hash = new Md5Hash(originPwd,salt);
        return hash.toHex().toString();
    }

    /**
     * Sha256 加密
     * @param orignPwd 原始密码
     * @return  Sha256 加密后的密码
     */
    public static String encryptSha256(String orignPwd){
        Sha256Hash hash = new Sha256Hash(orignPwd);
        return hash.toHex().toString();
    }

    /**
     * Sha256 加密
     * @param originPwd 原始密码
     * @param salt  盐
     * @return  Sha256 带盐加密后的密码
     */
    public static String encryptSha256(String originPwd,String salt){
        Sha256Hash hash = new Sha256Hash(originPwd,salt);
        return hash.toHex().toString();
    }

    /**
     * Sha512 加密
     * @param orignPwd 原始密码
     * @return  Sha512 加密后的密码
     */
    public static String encryptSha512(String orignPwd){
        Sha512Hash hash = new Sha512Hash(orignPwd);
        return hash.toHex().toString();
    }

    /**
     * Sha512 加密
     * @param originPwd 原始密码
     * @param salt  盐
     * @return  Sha512 带盐加密后的密码
     */
    public static String encryptSha512(String originPwd,String salt){
        Sha512Hash hash = new Sha512Hash(originPwd,salt);
        return hash.toHex().toString();
    }

    /**
     * 用户密码加密，密码加盐.
     * @param user  用户信息
     */
    public static boolean pwdEncrypt(UserEntity user){
        if (user == null || !StringUtils.hasText(user.getPassword())) {
            // 用户为null，或者密码没有数据
            logger.debug("操作失败，原因： 用户为null或者用户密码没有值！");
            return false;
        }
        String salt = generateSalt();
        user.setPassword(encryptMd5(user.getPassword(), salt));
        user.setSalt(salt);
        return true;
    }


    /**
     * 对比密码，目标对象密码为已加密
     * @param original 要对比对象
     * @param target 对比的目标对象
     * @return
     */
    public static boolean comparePwdEncrypt(UserEntity original, UserEntity target){
        if (original == null || !StringUtils.hasText(original.getPassword())||target == null || !StringUtils.hasText(target.getPassword())) {
            // 用户为null，或者密码没有数据
            logger.debug("操作失败，原因： 用户为null或者用户密码没有值！");
            return false;
        }
        String oriPwd = encryptMd5(original.getPassword(),original.getSalt());
        if(target.getPassword().equals(oriPwd)){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 没有账号登录异常
     */
    public static class NoAccountLoginException extends RuntimeException {

    }

    /**
     * 获取流程阶段所对应的权限
     * @param processCode
     * @return
     */
    public static ApprovePermissionVO getApprovePermission(String processCode){
        String key = null;
        for (Iterator<String> it = approvePermission.keySet().iterator(); it.hasNext();) {
            key = it.next();
            if (key.indexOf(processCode + ",") > -1) {
                return approvePermission.get(key);
            }
        }
        return null;
    }

}