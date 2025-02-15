package cn.bugstack.mybatis.test.datasource.pooled;

import cn.bugstack.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * @author 小傅哥，微信：fustack
 * @description 有连接池的数据源工厂
 * @date 2022/04/22
 * @github https://github.com/fuzhengwei
 * @copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    public PooledDataSourceFactory() {
        this.dataSource = new PooledDataSource();
    }

}
