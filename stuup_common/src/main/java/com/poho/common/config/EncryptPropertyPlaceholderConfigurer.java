package com.poho.common.config;

import com.poho.common.util.DesUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 21:58 2018/7/18
 * @Modified By:
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        //如果在加密属性名单中发现该属性
        if (isEncryptProp(propertyName)) {
            String decryptValue = DesUtils.getDecryptString(propertyValue);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }

    private boolean isEncryptProp(String propertyName) {
        for (String encryptName : encryptPropNames) {
            if (encryptName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}

