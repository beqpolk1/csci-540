#The following sources were consulted during development of this script:
#	https://www.tutorialspoint.com/hbase/hbase_create_table.htm
include Java
import 'org.apache.hadoop.hbase.client.HTable'
import 'org.apache.hadoop.hbase.HBaseConfiguration'
import 'org.apache.hadoop.hbase.HColumnDescriptor'
import 'org.apache.hadoop.hbase.HTableDescriptor'
import 'org.apache.hadoop.hbase.client.HBaseAdmin'
import 'org.apache.hadoop.hbase.TableName'

conf = HBaseConfiguration.create
admin = HBaseAdmin.new(conf)

tableDescriptor = HTableDescriptor.new(TableName.valueOf("food_data"))
tableDescriptor.addFamily(HColumnDescriptor.new("nutrition"))

admin.createTable(tableDescriptor)
print "Table created\n"
