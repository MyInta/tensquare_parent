package com.tensquare.qa.impl;

import com.tensquare.qa.client.BaseClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * @author inta
 * @date 2019/11/10
 * @describe 熔断机制触发时候的分支线
 */
@Component
public class BaseClientImpl implements BaseClient {
    @Override
    public Result findById(String labelId) {
        return new Result(false, StatusCode.ERROR, "正常路线阻隔，启动熔断机制进行保护中---");
    }
}
