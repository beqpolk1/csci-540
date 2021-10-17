include Java
import 'org.apache.hadoop.hbase.client.HTable'
import 'org.apache.hadoop.hbase.client.Put'
import 'org.apache.hadoop.hbase.HBaseConfiguration'

table = HTable.new(HBaseConfiguration.create, "food_data")
