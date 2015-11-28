package com.alibaba.otter.canal.example;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * ��Ⱥģʽ�Ĳ�������
 *
 * @author jianghang 2013-4-15 ����04:19:20
 * @version 1.0.4
 */
public class ClusterCanalClientTest extends AbstractCanalClientTest {

    public ClusterCanalClientTest(String destination){
        super(destination);
    }

    public static void main(String args[]) {
        String destination = "example";

        // ���ڹ̶�canal server�ĵ�ַ���������ӣ�����һ̨server����crash������֧��failover
        // CanalConnector connector = CanalConnectors.newClusterConnector(
        // Arrays.asList(new InetSocketAddress(
        // AddressUtils.getHostIp(),
        // 11111)),
        // "stability_test", "", "");

        // ����zookeeper��̬��ȡcanal server�ĵ�ַ���������ӣ�����һ̨server����crash������֧��failover
        CanalConnector connector = CanalConnectors.newClusterConnector("127.0.0.1:2181", destination, "", "");

        final ClusterCanalClientTest clientTest = new ClusterCanalClientTest(destination);
        clientTest.setConnector(connector);
        clientTest.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    logger.info("## stop the canal client");
                    clientTest.stop();
                } catch (Throwable e) {
                    logger.warn("##something goes wrong when stopping canal:\n{}", ExceptionUtils.getFullStackTrace(e));
                } finally {
                    logger.info("## canal client is down.");
                }
            }

        });
    }
}
