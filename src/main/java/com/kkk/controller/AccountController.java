package com.kkk.controller;

import com.kkk.entity.constants.Constants;
import com.kkk.entity.vo.ResponseVO;
import com.kkk.exception.BusinessException;
import com.kkk.redis.RedisUtils;
import com.kkk.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/11/13 15:43
 * @Version V1.0
 */
@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private UserInfoService userInfoService;
    @RequestMapping("checkCode")
    public ResponseVO checkCode() {
        final ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        final String code = captcha.text();
        String checkCodeKey = UUID.randomUUID().toString();
        String checkCodeBase64 = captcha.toBase64();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey, code, 60 * 10);
        HashMap<String, String> mp = new HashMap<>();
        mp.put("checkCode", checkCodeBase64);
        mp.put("checkCodeKey", checkCodeKey);

        return getSuccessResponseVO(mp);
    }

    @RequestMapping(value = "/register")
    public ResponseVO register(@NotEmpty String checkCodeKey,
                               @NotEmpty @Email String email,
                               @NotEmpty String password,
                               @NotEmpty String nickName,
                               @NotEmpty String checkCode) {

        try {
            if (!checkCode.equalsIgnoreCase((String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey))) {
                throw new BusinessException("图片验证码不正确");
            }
            userInfoService.register(email, nickName, password);
            return getSuccessResponseVO(null);
        }finally {
            redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
        }
    }
}
