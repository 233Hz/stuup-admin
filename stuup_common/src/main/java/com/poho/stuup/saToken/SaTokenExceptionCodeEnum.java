package com.poho.stuup.saToken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum SaTokenExceptionCodeEnum {

       CODE__1(-1,       "代表这个异常在抛出时未指定异常细分状态码"),
       CODE_10001(10001,	"未能获取有效的上下文处理器"),
       CODE_10002(10002,	"未能获取有效的上下文"),
       CODE_10003(10003,	"JSON 转换器未实现"),
       CODE_10011(10011,	"未能从全局 StpLogic 集合中找到对应 type 的 StpLogic"),
       CODE_10021(10021,	"指定的配置文件加载失败"),
       CODE_10022(10022,	"配置文件属性无法正常读取"),
       CODE_10031(10031,	"重置的侦听器集合不可以为空"),
       CODE_10032(10032,	"注册的侦听器不可以为空"),
       CODE_10301(10301,	"提供的 Same-Token 是无效的"),
       CODE_10311(10311,	"表示未能通过 Http Basic 认证校验"),
       CODE_10321(10321,	"提供的 HttpMethod 是无效的"),
       CODE_11001(11001,	"未能读取到有效Token"),
       CODE_11002(11002,	"登录时的账号id值为空"),
       CODE_11003(11003,	"更改 Token 指向的 账号Id 时，账号Id值为空"),
       CODE_11011(11011,	"未能读取到有效Token"),
       CODE_11012(11012,	"Token无效"),
       CODE_11013(11013,	"Token已过期"),
       CODE_11014(11014,	"Token已被顶下线"),
       CODE_11015(11015,	"Token已被踢下线"),
       CODE_11016(11016,	"Token已被冻结"),
       CODE_11031(11031,	"在未集成 sa-token-jwt 插件时调用 getExtra() 抛出异常"),
       CODE_11041(11041,	"缺少指定的角色"),
       CODE_11051(11051,	"缺少指定的权限"),
       CODE_11061(11061,	"当前账号未通过服务封禁校验"),
       CODE_11062(11062,	"提供要解禁的账号无效"),
       CODE_11063(11063,	"提供要解禁的服务无效"),
       CODE_11064(11064,	"提供要解禁的等级无效"),
       CODE_11071(11071,	"二级认证校验未通过"),
       CODE_12001(12001,	"请求中缺少指定的参数"),
       CODE_12002(12002,	"构建 Cookie 时缺少 name 参数"),
       CODE_12003(12003,	"构建 Cookie 时缺少 value 参数"),
       CODE_12101(12101,	"Base64 编码异常"),
       CODE_12102(12102,	"Base64 解码异常"),
       CODE_12103(12103,	"URL 编码异常"),
       CODE_12104(12104,	"URL 解码异常"),
       CODE_12111(12111,	"md5 加密异常"),
       CODE_12112(12112,	"sha1 加密异常"),
       CODE_12113(12113,	"sha256 加密异常"),
       CODE_12114(12114,	"AES 加密异常"),
       CODE_12115(12115,	"AES 解密异常"),
       CODE_12116(12116,	"RSA 公钥加密异常"),
       CODE_12117(12117,	"RSA 私钥加密异常"),
       CODE_12118(12118,	"RSA 公钥解密异常"),
       CODE_12119(12119,	"RSA 私钥解密异常"),
       CODE_12201(12201,	"参与参数签名的秘钥不可为空"),
       CODE_12202(12202,	"给定的签名无效"),
       CODE_12203(12203,	"timestamp 超出允许的范围");

    private final int code;

    private final String msg;

    public static String getEnumMsgByCode(int code){
           Optional<SaTokenExceptionCodeEnum> optional = Arrays.stream(SaTokenExceptionCodeEnum.values()).filter(e -> e.getCode() == code).findFirst();
           return optional.isPresent() ? optional.get().getMsg() : "";
    }

}
