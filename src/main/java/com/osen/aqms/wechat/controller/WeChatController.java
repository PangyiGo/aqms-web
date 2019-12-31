package com.osen.aqms.wechat.controller;

import com.osen.aqms.wechat.utils.SHA1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * User: PangYi
 * Date: 2019-12-27
 * Time: 9:19
 * Description:
 */
@RestController
@RequestMapping("/wechat")
@Slf4j
public class WeChatController {

    @Autowired
    private RestTemplate restTemplate;

    private String appID = "wx8223526471572a27";

    private String appsecret = "99029a22c38403652acddd54f8b75f21";

    private String userInfoUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appID}&secret={appsecret}&code={code}" + "&grant_type=authorization_code";

    @GetMapping("/authorization")
    public void authorization(@RequestParam("code") String code) {
        log.info("code=" + code);

        ResponseEntity<String> entity = restTemplate.getForEntity(userInfoUrl, String.class, appID, appsecret, code);

        log.info(entity.getBody());
    }

    @GetMapping("/validate")
    public String validateSource(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.info(signature);
        log.info(timestamp);
        log.info(nonce);
        log.info(echostr);

        // 字典排序 加密
        String sha1 = SHA1.getSHA1("pangyi", timestamp, nonce);

        return sha1.equals(signature) ? echostr : "";
    }

    @PostMapping("/validate")
    public void reviceMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println(request);

        InputStream stream = request.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        char[] chars = new char[1024];
        int len;
        while ((len = reader.read(chars)) != -1) {
            System.out.println(new String(chars, 0, len));
        }

        String rese = "<xml>\n" + "  <ToUserName><![CDATA[ol48Cs1T7mvhuV4i8C8p_NqlE35k]]></ToUserName>\n" + "  " + "<FromUserName><![CDATA" + "[gh_74bed216094a]]></FromUserName>\n" + "  <CreateTime>" + System.currentTimeMillis() + "</CreateTime" + ">\n" + "  <MsgType><![CDATA[text" + "]]></MsgType>\n" + "  <Content><![CDATA[嗯，说的没错！奥利给]]></Content>\n" + "</xml>";
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(rese);
    }
}
