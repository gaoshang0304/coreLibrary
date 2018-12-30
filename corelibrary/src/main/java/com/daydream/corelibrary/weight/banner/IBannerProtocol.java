package com.daydream.corelibrary.weight.banner;

import java.io.Serializable;

/**
 * Banner的数据结构必须满足的协议.
 *
 * @author wangheng
 */
public interface IBannerProtocol extends Serializable {

    long serialVersionUID = 4981938402094129713L;

    String getBannerImageUrl();
}
