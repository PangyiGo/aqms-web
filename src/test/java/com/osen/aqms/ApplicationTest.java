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
        String start = "2019-12-01";
        String end = "2019-12-10";

        List<String> strings = TableNameUtil.tableNameList(TableNameUtil.Air_history, start, end);

        System.out.println(strings);
    }
}
