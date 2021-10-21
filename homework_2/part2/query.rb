#The following sources were consulted during development of this script:
#	https://docs.cloudera.com/documentation/enterprise/5-9-x/topics/admin_hbase_filtering.html
#	https://docs.cloudera.com/documentation/enterprise/5-9-x/topics/admin_hbase_scanning.html
#	https://biggists.wordpress.com/2016/10/22/filters-in-hbase-shell/
#	http://hbase.apache.org/1.2/apidocs/overview-summary.html
#	https://www.tutorialspoint.com/hbase/hbase_scan.htm
#	https://www.tabnine.com/code/java/methods/org.apache.hadoop.hbase.filter.SingleColumnValueFilter/%3Cinit%3E

include Java
import 'org.apache.hadoop.hbase.client.HTable'
import 'org.apache.hadoop.hbase.HBaseConfiguration'
import 'org.apache.hadoop.hbase.client.Result'
import 'org.apache.hadoop.hbase.client.ResultScanner'
import 'org.apache.hadoop.hbase.client.Scan'
import 'org.apache.hadoop.hbase.util.Bytes'
import 'org.apache.hadoop.hbase.filter.QualifierFilter'
import 'org.apache.hadoop.hbase.filter.SingleColumnValueFilter'
import 'org.apache.hadoop.hbase.filter.BinaryComparator'

def jbytes(*args)
  return args.map { |arg| arg.to_s.to_java_bytes }
end

food_table = HTable.new(@hbase.configuration, 'food_data_with_stores')

# query 1 - what foods a store might have
myStore = 'g001'
myScan = Scan.new()
qualFilter = QualifierFilter.new(org.apache.hadoop.hbase.filter.CompareFilter::CompareOp::EQUAL, BinaryComparator.new(*jbytes(myStore)))
myScan.setFilter(qualFilter)

scanner = food_table.getScanner(myScan)

print "====================================================================\n"
print "Possible foods for store " + myStore + ":\n"
count = 0
while (result = scanner.next())
	food = Bytes.toString(result.getRow())
	print food + "\n"
	count += 1
end
puts "#{count} foods found\n"
print "====================================================================\n\n"

# query 2 - nutrition profile for store
myStore = 'g001'
myScan = Scan.new()

myScan.addColumn('nutrition'.to_java_bytes, 'saturated_fats'.to_java_bytes)
myScan.addColumn('nutrition'.to_java_bytes, 'added_sugars'.to_java_bytes)
myScan.addColumn('stores'.to_java_bytes, myStore.to_java_bytes)

valFilter = SingleColumnValueFilter.new('stores'.to_java_bytes, myStore.to_java_bytes, org.apache.hadoop.hbase.filter.CompareFilter::CompareOp::EQUAL, BinaryComparator.new(*jbytes('1')))
valFilter.setFilterIfMissing(true)
valFilter.setLatestVersionOnly(true)
myScan.setFilter(valFilter)

scanner = food_table.getScanner(myScan)

print "====================================================================\n"
print "Nutrition profile for foods of store " + myStore + ":\n"
count = 0
while (result = scanner.next())
	food = Bytes.toString(result.getRow())
	sat_fats = Bytes.toString(result.getValue(*jbytes('nutrition', 'saturated_fats')))
	add_sugar = Bytes.toString(result.getValue(*jbytes('nutrition', 'added_sugars')))
	
	print food + "\n"
	print "    Sat. Fats: " + sat_fats + "\n"
	print "    Added Sugars: " + add_sugar + "\n"
	count += 1
end
puts "#{count} foods found\n"
print "====================================================================\n\n"

print "Queries completed\n"
exit