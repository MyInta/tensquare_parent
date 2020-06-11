package com.tensquare.qa.client;

import com.tensquare.qa.impl.BaseClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author inta
 * @date 2019/11/7
 * @describe 之所以写Component不是说有什么实现类要加进去，而是因为不写在远程调用
 * Feigh的时候，因为没有在容器中检测到而提醒报错，但不加都不影响操作，我这边是强迫症去红
 */
@Component
@FeignClient(value = "tensquare-base", fallback = BaseClientImpl.class)
public interface BaseClient {

    @RequestMapping(value = "/label/{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId);
}
