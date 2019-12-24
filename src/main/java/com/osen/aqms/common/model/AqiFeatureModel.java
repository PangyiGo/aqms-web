package com.osen.aqms.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * User: PangYi
 * Date: 2019-12-23
 * Time: 18:58
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiFeatureModel {

    private int nowAqi;

    private int yeaAqi;

    private int MonAqi;

    private BigDecimal nowPM25 = new BigDecimal(0);
    private BigDecimal yeaPM25 = new BigDecimal(0);
    private BigDecimal monPM25 = new BigDecimal(0);

    private BigDecimal nowPM10 = new BigDecimal(0);
    private BigDecimal yeaPM10 = new BigDecimal(0);
    private BigDecimal monPM10 = new BigDecimal(0);

    private BigDecimal nowSo2 = new BigDecimal(0);
    private BigDecimal yeaSo2 = new BigDecimal(0);
    private BigDecimal monSo2 = new BigDecimal(0);

    private BigDecimal nowNo2 = new BigDecimal(0);
    private BigDecimal yeaNo2 = new BigDecimal(0);
    private BigDecimal monNo2 = new BigDecimal(0);

    private BigDecimal nowCo = new BigDecimal(0);
    private BigDecimal yeaCo = new BigDecimal(0);
    private BigDecimal monCo = new BigDecimal(0);

    private BigDecimal nowO3 = new BigDecimal(0);
    private BigDecimal yeaO3 = new BigDecimal(0);
    private BigDecimal monO3 = new BigDecimal(0);

    private BigDecimal nowVoc = new BigDecimal(0);
    private BigDecimal yeaVoc = new BigDecimal(0);
    private BigDecimal monVoc = new BigDecimal(0);

}
