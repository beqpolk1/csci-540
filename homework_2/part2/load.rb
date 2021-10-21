#Script primarily adapted from Prof. David Millman's notes on HBase:
#	https://github.com/msu/csci-540-fall2021/blob/master/notes/2021-10-06.md
include Java

import 'javax.xml.stream.XMLStreamConstants'
import 'org.apache.hadoop.hbase.client.HTable'
import 'org.apache.hadoop.hbase.client.Put'
import 'org.apache.hadoop.hbase.client.Result'
import 'org.apache.hadoop.hbase.client.ResultScanner'
import 'org.apache.hadoop.hbase.client.Scan'
import 'org.apache.hadoop.hbase.util.Bytes'
import 'org.apache.hadoop.hbase.filter.RowFilter'
import 'org.apache.hadoop.hbase.filter.RegexStringComparator'


def jbytes(*args)
  args.map { |arg| arg.to_s.to_java_bytes }
end

# dataset 1 - my food pyramid data
factory = javax.xml.stream.XMLInputFactory.newInstance
reader = factory.createXMLStreamReader(java.io.FileInputStream.new("/mnt/dataset/Food_Display_Table.xml"))

document = nil
buffer = nil
display_name = nil
portion_name = nil
count = 0

print "Reading food_display_table data...\n"
table = HTable.new(@hbase.configuration, 'food_data_with_stores')
table.setAutoFlush(false)

while reader.has_next
  case reader.next
  when XMLStreamConstants::START_ELEMENT
	case reader.local_name
	when 'Food_Display_Row' then document = {}
	else buffer = []
	end
  when XMLStreamConstants::CHARACTERS
	buffer << reader.text unless buffer.nil?
  when XMLStreamConstants::END_ELEMENT
	case reader.local_name
	when 'Display_Name' then
	  display_name = buffer.join
	when 'Portion_Display_Name' then
	  portion_name = buffer.join
	when 'Solid_Fats' then
	  document['nutrition:solid_fats'] = buffer.join
	when 'Added_Sugars' then
	  document['nutrition:added_sugars'] = buffer.join
	when 'Alcohol' then
	  document['nutrition:alcohol'] = buffer.join
	when 'Calories' then
	  document['nutrition:calories'] = buffer.join
	when 'Saturated_Fats' then
	  document['nutrition:saturated_fats'] = buffer.join
	when 'Food_Display_Row'
	  full_name = display_name + " [" + portion_name + "]"
	  key = full_name.to_java_bytes

	  print "Adding " + full_name + "...\n"
	  p = Put.new(key)

	  p.add(*jbytes("nutrition", "solid_fats", document['nutrition:solid_fats']))
	  p.add(*jbytes("nutrition", "added_sugars", document['nutrition:added_sugars']))
	  p.add(*jbytes("nutrition", "alcohol", document['nutrition:alcohol']))
	  p.add(*jbytes("nutrition", "calories", document['nutrition:calories']))
	  p.add(*jbytes("nutrition", "saturated_fats", document['nutrition:saturated_fats']))
	  
	  table.put(p)

	  count += 1
	  table.flushCommits() if count % 10 == 0
	  if count % 500 == 0
      puts "#{count} records inserted (#{document['title']})"
	  end
	end
  end
end

table.flushCommits()
print "Done loading food_display_table data\n"

# dataset 2 - preprocessed MS Delta NEMS data
factory = javax.xml.stream.XMLInputFactory.newInstance
reader = factory.createXMLStreamReader(java.io.FileInputStream.new("/mnt/dataset/store_food_data.xml"))

buffer = nil
store_id = nil
food_name = nil
count = 0

print "Reading store_food_data data...\n"

while reader.has_next
  case reader.next
  when XMLStreamConstants::START_ELEMENT
	case reader.local_name
	when 'store' then store_id = nil
	else buffer = []
	end
  when XMLStreamConstants::CHARACTERS
	buffer << reader.text unless buffer.nil?
  when XMLStreamConstants::END_ELEMENT
	case reader.local_name
	when 'store_id' then
	  store_id = buffer.join
	when 'food' then
	  food_name = buffer.join
	  
	  # get all records from the table that match the food name
	  myScan = Scan.new()
	  myPattern = '(?i)\w*' + food_name + '\w*'
	  rowFilter = RowFilter.new(org.apache.hadoop.hbase.filter.CompareFilter::CompareOp::EQUAL, RegexStringComparator.new(myPattern))
	  myScan.setFilter(rowFilter)
	  
	  scanner = table.getScanner(myScan)
	  
	  while (result = scanner.next())
		key = result.getRow()
		print "Adding store " + store_id + " to food " + Bytes.toString(key) + "\n"
		
		p = Put.new(key)
		p.add(*jbytes("stores", store_id, "1"));
		table.put(p)
		
		count += 1
		table.flushCommits if count % 10 == 0
	  end
	end
  end
end

table.flushCommits()
print "Done loading store_food_data data\n"

exit
