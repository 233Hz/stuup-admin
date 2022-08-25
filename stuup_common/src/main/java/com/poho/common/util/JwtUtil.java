package com.poho.common.util;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.CheckResult;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

/**
 * @Author: wupeng
 * @Description: JWT创建与鉴权类
 * @Date: Created in 22:41 2018/6/9
 * @Modified By:
 */
public class JwtUtil {
    /**
     *
     * @param userId
     * @return
     */
    public static String createJwt(String userId) {
        return createJwt(userId,"subject", CommonConstants.JWT_ISS, CommonConstants.JWT_TTL);
    }

    /**
     *
     * @param userId
     * @return
     */
    public static String createOneDayJwt(String userId) {
        return createJwt(userId,"subject", CommonConstants.JWT_ISS, CommonConstants.JWT_TTL_ONE_DAY);
    }

    /**
     *
     * @param userId
     * @param remember
     * @return
     */
    public static String createJwt(String userId, String remember) {
        if ("on".equals(remember)) {
            return createJwt(userId,"subject", CommonConstants.JWT_ISS, CommonConstants.JWT_TTL);
        }
        return createJwt(userId,"subject", CommonConstants.JWT_ISS, CommonConstants.JWT_TTL_COMMON);
    }

    /**
     *
     * @param id
     * @param subject
     * @param issure
     * @param till
     * @return
     */
    public static String createJwt(String id, String subject, String issure, long till) {
        JwtBuilder jwtBuilder = Jwts.builder().setId(id)
                .signWith(SignatureAlgorithm.HS256, new SecretKeySpec(DatatypeConverter.parseBase64Binary(CommonConstants.JWT_SECERT), SignatureAlgorithm.HS256.getJcaName()))
                .setIssuer(issure)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + till));
        return jwtBuilder.compact();
    }

    /**
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static CheckResult parseJwt(String token) {
        CheckResult checkResult = new CheckResult();
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(CommonConstants.JWT_SECERT)).parseClaimsJws(token).getBody();
            checkResult.setSuccess(true);
            checkResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
            checkResult.setCode(CommonConstants.JWT_CODE_EXPIRE);
            checkResult.setSuccess(false);
        } catch (SignatureException e) {
            checkResult.setCode(CommonConstants.JWT_CODE_FAIL);
            checkResult.setSuccess(false);
        } catch (Exception e) {
            checkResult.setCode(CommonConstants.JWT_CODE_FAIL);
            checkResult.setSuccess(false);
        }
        return checkResult;
    }

    public static void main(String[] args) {
        String token = JwtUtil.createJwt("123456");
        System.out.println(token);
        CheckResult result = JwtUtil.parseJwt(token);
        System.out.println(result.getClaims());
    }
}
