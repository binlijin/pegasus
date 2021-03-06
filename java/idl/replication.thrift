include "base.thrift"

namespace cpp dsn.replication
namespace java dsn.replication

struct partition_configuration
{
    1:base.gpid pid;
    2:i64 ballot;
    3:i32 max_replica_count;
    4:base.rpc_address primary;
    5:list<base.rpc_address> secondaries;
    6:list<base.rpc_address> last_drops;
    7:i64 last_committed_decree;
}

struct query_cfg_request
{
    1:string app_name;
    2:list<i32> partition_indices;
}

struct query_cfg_response
{
    1:base.error_code err;
    2:i32 app_id;
    3:i32 partition_count;
    4:bool is_stateful;
    5:list<partition_configuration> partitions;
}
