package com.osen.aqms;

import com.osen.aqms.common.utils.TableNameUtil;

import java.util.List;

/**
 * User: PangYi
 * Date: 2019-12-10
 * Time: 10:11
 * Description:
 */
public class ApplicationTest {

    public static void main(String[] args) {
        List<String> strings = TableNameUtil.tableNameList(TableNameUtil.Air_history, "2019-11-11", "2020-12-12");

        System.out.println(strings);
    }
}
