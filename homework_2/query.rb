#https://docs.cloudera.com/documentation/enterprise/5-9-x/topics/admin_hbase_filtering.html
#https://docs.cloudera.com/documentation/enterprise/5-9-x/topics/admin_hbase_scanning.html
#https://biggists.wordpress.com/2016/10/22/filters-in-hbase-shell/
#http://hbase.apache.org/1.2/apidocs/overview-summary.html
#https://www.tutorialspoint.com/hbase/hbase_scan.htm

include Java
import 'org.apache.hadoop.hbase.client.HTable'
import 'org.apache.hadoop.hbase.HBaseConfiguration'
import 'org.apache.hadoop.hbase.client.Result'
import 'org.apache.hadoop.hbase.client.ResultScanner'
import 'org.apache.hadoop.hbase.client.Scan'
import 'org.apache.hadoop.hbase.util.Bytes'
import 'org.apache.hadoop.hbase.filter.RowFilter'
import 'org.apache.hadoop.hbase.filter.RegexStringComparator'

def jbytes(*args)
  return args.map { |arg| arg.to_s.to_java_bytes }
end

food_table = HTable.new(@hbase.configuration, 'food_data')

myScan = Scan.new()
myScan.addColumn('nutrition'.to_java_bytes, 'calories'.to_java_bytes)
rowFilter = RowFilter.new(org.apache.hadoop.hbase.filter.CompareFilter::CompareOp::EQUAL, RegexStringComparator.new('(?i)\w*fried\w*'))
myScan.setFilter(rowFilter)

scanner = food_table.getScanner(myScan)

while (result = scanner.next())
	food = Bytes.toString(result.getRow())
	calories = Bytes.toString(result.getValue(*jbytes('nutrition', 'calories')))
	
	print food + " - " + calories + " calories\n"
end

print "Query completed\n"