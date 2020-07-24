package com.ecmp.ch.mofc.pa.controller.interceptor;

import com.ecmp.context.ContextUtil;
import com.ecmp.core.vo.OperateStatus;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.SessionUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @ClassName TokenGetController
 * @Description: 供前端调试接口获取token
 * @Author Zhangzhanpeng
 * @Date 2020/7/13
 **/
@RestController
@RequestMapping("getToken")
@Api(value = "获取token接口",tags = {"获取token接口"})
public class TokenGetController {

    @GetMapping
    @ApiOperation(value = "调用此接口获取一个token",notes = "调用此接口获取一个token")
    public OperateStatus getToken(){
        SessionUser sessionUser = ContextUtil.mockUser();
        String token = JsonUtils.toJson(sessionUser);
        return new OperateStatus(true,"获取token成功",token);
    }
}
