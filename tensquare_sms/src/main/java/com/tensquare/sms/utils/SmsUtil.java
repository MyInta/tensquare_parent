package com.tensquare.sms.utils;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 短信工具类
 * @author Administrator
 *
 */
@Component
public class SmsUtil {

    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    @Autowired
    private Environment env;

    /**
     * 发送信息
     * @param mobile 目的地手机号
     * @param template_code 模板名称
     * @param sign_name 签名
     * @param param 要传的参数
     * @return
     * @throws ClientException
     */
    public CommonResponse sendSms(String mobile,String template_code,String sign_name,String param) throws ClientException{
        String accessKeyId =env.getProperty("aliyun.sms.accessKeyId");
        String accessKeySecret = env.getProperty("aliyun.sms.accessKeySecret");

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("TemplateCode", template_code);
        request.putQueryParameter("SignName", sign_name);
        request.putQueryParameter("TemplateParam", param);

        CommonResponse response = client.getCommonResponse(request);
        System.out.println(response.getData());
        return response;
    }

    /**
     * 查询发送情况
     * @param mobile 查询号码
     * @param bizId 流水号
     * @return
     * @throws ClientException
     */
    public  CommonResponse querySendDetails(String mobile,String bizId) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "<accessKeyId>", "<accessSecret>");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("QuerySendDetails");

        request.putQueryParameter("RegionId", "cn-hangzhou");
        //必填-查询号码
        request.putQueryParameter("PhoneNumber", mobile);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("20191104");
        request.putQueryParameter("SendDate", ft.format(new Date()));
        //必填-页大小
        request.putQueryParameter("PageSize", "10");
        //必填-当前页码从1开始计数
        request.putQueryParameter("CurrentPage", "1");
        //可选-流水号
        request.putQueryParameter("BizId", bizId);

        CommonResponse response = client.getCommonResponse(request);
        System.out.println(response.getData());
        return response;
    }

}