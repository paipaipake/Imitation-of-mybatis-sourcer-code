package cn.bugstack.mybatis.test.builderTest;

import org.junit.Test;

/**
 * @author HanXu
 * @date 2023/3/15
 **/
public class ApiTest {

    @Test
    public void demo(){
        ResourcePoolConfig build = new ResourcePoolConfig.Builder().build();

        ResourcePoolConfig config = new ResourcePoolConfig.Builder()
                .setName("dbconnectionpool")
                .setMaxTotal(16)
                .setMaxIdle(14)
                .setMinIdle(12)
                .build();
    }
}
