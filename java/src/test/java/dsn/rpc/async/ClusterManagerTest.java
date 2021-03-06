// Copyright (c) 2017, Xiaomi, Inc.  All rights reserved.
// This source code is licensed under the Apache License Version 2.0, which
// can be found in the LICENSE file in the root directory of this source tree.

package dsn.rpc.async;

import dsn.api.KeyHasher;
import dsn.api.ReplicationException;
import dsn.base.error_code;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * ClusterManager Tester.
 *
 * @author sunweijie@xiaomi.com
 * @version 1.0
 */
public class ClusterManagerTest {
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getReplicaSession(rpc_address address)
     */
    @Test
    public void testGetReplicaSession() throws Exception {
        String[] address_list = {
            "127.0.0.1:1", "127.0.0.1:2", "127.0.0.1:3"
        };

        ClusterManager testManager = new ClusterManager(1000, 1, null, address_list);

        // input an invalid rpc address
        dsn.base.rpc_address address = new dsn.base.rpc_address();
        ReplicaSession session = testManager.getReplicaSession(address);
        Assert.assertNull(session);
    }

    /**
     * Method: openTable(String name, KeyHasher h)
     */
    @Test
    public void testOpenTable() throws Exception {
        // test invalid meta list
        String[] addr_list = {"127.0.0.1:123", "127.0.0.1:124", "127.0.0.1:125"};
        ClusterManager testManager = new ClusterManager(1000, 1, null, addr_list);

        TableHandler result = null;
        try {
            result = testManager.openTable("testName", KeyHasher.DEFAULT);
        } catch (ReplicationException e) {
            Assert.assertEquals(error_code.error_types.ERR_SESSION_RESET, e.err_type);
        } finally {
            Assert.assertNull(result);
        }

        // test partially invalid meta list
        String[] addr_list2 = {"127.0.0.1:123", "127.0.0.1:34603", "127.0.0.1:34601", "127.0.0.1:34602"};
        testManager = new ClusterManager(1000, 1, null, addr_list2);
        try {
            result = testManager.openTable("hehe", KeyHasher.DEFAULT);
        } catch (ReplicationException e) {
            Assert.assertEquals(error_code.error_types.ERR_OBJECT_NOT_FOUND, e.err_type);
        } finally {
            Assert.assertNull(result);
        }

        // test open an valid table
        try {
            result = testManager.openTable("temp", KeyHasher.DEFAULT);
        } catch (ReplicationException e) {
            Assert.fail();
        } finally {
            Assert.assertNotNull(result);
            // in onebox, we create a table named temp with 8 partitions in default.
            Assert.assertEquals(8, result.getPartitionCount());
        }
    }
}
