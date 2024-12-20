package com.lifetime.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @Auther:wangchao
 * @Date: 2024/12/20-20:14
 * @Description: com.lifetime.common.util
 * @Version:1.0
 */
public class SnowflakeUtil {
    /**
     * 由于底层实现为synchronized方法，因此计算过程串行化，且线程安全。
     *
     * @return 计算后的全局唯一Id。
     */
    public static  long nextLongId() {
        Snowflake snowflake=IdUtil.createSnowflake(1, 0);
        return  snowflake.nextId();
    }

}
