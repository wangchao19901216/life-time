package com.lifetime.common.object;

/**
 * @author:wangchao
 * @date: 2023/5/9-10:58
 * @description: com.sh3h.common.object
 * @Version:1.0
 */
public class Tuple2<T1, T2> {
    /**
     * 第一个变量。
     */
    private final T1 first;
    /**
     * 第二个变量。
     */
    private final T2 second;

    /**
     * 构造函数。
     *
     * @param first 第一个变量。
     * @param second 第二个变量。
     */
    public Tuple2(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    /**
     * 获取第一个变量。
     *
     * @return 返回第一个变量。
     */
    public T1 getFirst() {
        return first;
    }

    /**
     * 获取第二个变量。
     *
     * @return 返回第二个变量。
     */
    public T2 getSecond() {
        return second;
    }

}

