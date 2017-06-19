package com.waskj.base.core.util.jwt;

import com.alibaba.fastjson.JSONObject;
import com.waskj.base.core.util.Constants;
import com.waskj.base.core.util.CryptUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 */
public class JwtUtil {

    private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // 时间适配，1 min
    public static long timeFix = 60 * 1000;
    public static final String PRI_KEY = "pri";
    public static final String PUB_KEY = "pub";

    /**
     * 检查token
     *
     * @param token token
     */
    public static boolean checkToken(String token) {
        return !(!StringUtils.hasText(token) || !JwtUtil.validationJWT(token) || JwtUtil.compareJWTExpWithNow(token) <= 0);


    }

    /**
     * @param priMap    服务器需要的一些数据
     * @param pubMap    前端需要的一些数据
     * @param ttlMillis 过期时间
     * @return token
     * @throws Exception 失败异常
     */
    public static String createJWT(Map priMap, Map pubMap, long ttlMillis) throws Exception {
        JwtBuilder builder = Jwts.builder()
                                 .setClaims(createClaims(priMap, pubMap))
                                 .signWith(SignatureAlgorithm.HS256, generalKey());
        if (ttlMillis >= 0) {
            builder.setExpiration(new Date(System.currentTimeMillis() + ttlMillis));
        }
        return builder.compact();
    }


    /**
     * 更新过期时间
     *
     * @param oldToken  旧token
     * @param ttlMillis 过期时间
     * @return 新token
     */
    public static String updateJWTExp(String oldToken, long ttlMillis) {
        String newToken = null;
        try {
            Claims claims = parseJWT(oldToken);
            claims.setExpiration(new Date(System.currentTimeMillis() + ttlMillis));
            newToken = Jwts.builder().setClaims(claims)
                           .signWith(SignatureAlgorithm.HS256, generalKey())
                           .compact();
        } catch (ExpiredJwtException e) {
            logger.debug("ExpiredJwtException: JWT expired .Exception token:[" + oldToken + "]");
            logger.error("ExpiredJwtException: create fail return token = null");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return newToken;
    }

    /**
     * 比较过期时间
     *
     * @param token      token
     * @param compMillis 对比时间
     * @return 如果大于 0 token没有过期、小于0 token 已经过期
     */
    public static long compareJWTExp(String token, long compMillis) {
        Claims claims;
        long expTime = 0;
        try {
            claims = parseJWT(token);
            expTime = claims.getExpiration().getTime();
        } catch (ExpiredJwtException e) {
            logger.debug("ExpiredJwtException: JWT expired .Exception token:[" + token + "]");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return expTime - compMillis;
    }

    /**
     * 如果token 过期解析是会抛出 ExpiredJwtException
     * exp : error message = {io.jsonwebtoken.ExpiredJwtException: JWT expired at 2016-12-20T14:44:22+0800. Current time: 2016-12-20T14:44:23+0800}
     *
     * @param token token
     * @return 如果大于 0 token没有过期
     */
    public static long compareJWTExpWithNow(String token) {
        return compareJWTExp(token, System.currentTimeMillis() - timeFix);
    }

    /**
     * 解密jwt
     *
     * @param token token
     * @return Map
     */
    public static Claims parseJWT(String token) {

        return Jwts.parser()
                   .setSigningKey(generalKey())
                   .parseClaimsJws(token).getBody();
    }

    /**
     * 根据 token  获取  私有Map
     *
     * @param token token
     * @return 私有Map
     */
    public static Map getPriMap(String token) {
        Map map = new HashMap();
        try {
            map = decryptMap((String) parseJWT(token).get(PRI_KEY));
        } catch (ExpiredJwtException e) {
            logger.error("ExpiredJwtException: JWT expired .Exception token:[" + token + "]");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * 根据 token  获取  公有Map
     *
     * @param token token
     * @return 公有Map
     */
    public static Map getPubMap(String token) {
        Map map = new HashMap();
        try {
            map = (Map) parseJWT(token).get(PUB_KEY);
        } catch (ExpiredJwtException e) {
            logger.error("ExpiredJwtException: JWT expired .Exception token:[" + token + "]");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }


    /**
     * 校验token 是否有效.
     *
     * @param token token
     * @return if token is null or empty string or exception, return false
     */
    public static boolean validationJWT(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        boolean b = false;
        try {
            Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(token);
            b = true;
        } catch (ExpiredJwtException e) {
            logger.debug("ExpiredJwtException: JWT expired .Exception token:[" + token + "]");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return b;
    }


    /**
     * 由字符串生成加密key
     */
    private static SecretKey generalKey() {
        byte[] encodedKey = Base64.encodeBase64(Constants.JWT_SECRET.getBytes());
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "DESC");
    }

    /**
     * 生成  Claims
     *
     * @param priMap 私有map
     * @param pubMap 共有map
     */
    private static Claims createClaims(Map priMap, Map pubMap) {
        Claims claims = new DefaultClaims();
        setPubMap(pubMap, claims);
        setPriMap(priMap, claims);
        return claims;
    }

    private static void setPriMap(Map priMap, Claims claims) {
        try {
            if (priMap == null) {
                priMap = new HashMap();
            }
            claims.put(PRI_KEY, CryptUtil.encrypt(JSONObject.toJSONString(priMap), Constants.JWT_SECRET));
        } catch (Exception e) {
            logger.error("加密[priMap]异常");
            logger.error(e.getMessage(), e);
        }
    }

    private static void setPubMap(Map pubMap, Claims claims) {
        if (pubMap == null) {
            pubMap = new HashMap();
        }
        claims.put(PUB_KEY, pubMap);
    }

    private static Map decryptMap(String data) {
        Map map = new HashMap();
        try {
            map = JSONObject.parseObject(CryptUtil.decrypt(data, Constants.JWT_SECRET));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }

}
