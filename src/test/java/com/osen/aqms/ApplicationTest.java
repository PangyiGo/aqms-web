package com.osen.aqms;

import cn.hutool.core.util.IdUtil;

/**
 * User: PangYi
 * Date: 2019-12-10
 * Time: 10:11
 * Description:
 */
public class ApplicationTest {

    public static void main(String[] args) {
        String encode = IdUtil.randomUUID();
        System.out.println(encode);
    }
}
