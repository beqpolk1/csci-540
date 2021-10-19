#adapted from Dave's notes
include Java

import 'javax.xml.stream.XMLStreamConstants'
import 'org.apache.hadoop.hbase.client.HTable'
import 'org.apache.hadoop.hbase.client.Put'


def jbytes(*args)
  args.map { |arg| arg.to_s.to_java_bytes }
end

factory = javax.xml.stream.XMLInputFactory.newInstance
reader = factory.createXMLStreamReader(java.io.FileInputStream.new("/mnt/Food_Display_Table.xml"))

document = nil
buffer = nil
display_name = nil
count = 0

print "Starting...\n"
table = HTable.new(@hbase.configuration, 'food_data')
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
#	when 'Food_Code' then
#	  food_code = buffer.join
	when 'Display_Name' then
	  display_name = buffer.join
#	when 'Portion_Default' then
#	  document['portion:default'] = buffer.join
#	when 'Portion_Amount' then 
#	  document['portion:amount'] = buffer.join
#	when 'Factor' then
#	  document['portion:factor'] = buffer.join
#	when 'Increment' then
#	  document['portion:increment'] = buffer.join
#	when 'Multiplier' then
#	  document['portion:multiplier'] = buffer.join
#	when 'Grains' then
#	  document['type:grains'] = buffer.join
#	when 'Vegetables' then
#	  document['type:vegetables'] = buffer.join
#	when 'Fruits' then
#	  document['type:fruits'] = buffer.join
#	when 'Milk' then
#	  document['type:milk'] = buffer.join
#	when 'Meats' then
#	  document['type:meats'] = buffer.join
#	when 'Soy' then
#	  document['type:soy'] = buffer.join
#	when 'Oils' then
#	  document['type:oils'] = buffer.join
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
	  key = display_name.to_java_bytes

	  print "Adding " + display_name + "...\n"
	  p = Put.new(key)
	  
#	  p.add(*jbytes("display_name", "", document['display_name']))
#	  p.add(*jbytes("portion", "default", document['portion:default']))
#	  p.add(*jbytes("portion", "amount", document['portion:amount']))
#	  p.add(*jbytes("portion", "factor", document['portion:factor']))
#	  p.add(*jbytes("portion", "increment", document['portion:increment']))
#	  p.add(*jbytes("portion", "multiplier", document['portion:multiplier']))
#	  p.add(*jbytes("type", "grains", document['type:grains']))
#	  p.add(*jbytes("type", "vegetables", document['type:vegetables']))
#	  p.add(*jbytes("type", "fruits", document['type:fruits']))
#	  p.add(*jbytes("type", "milk", document['type:milk']))
#	  p.add(*jbytes("type", "meats", document['type:meats']))
#	  p.add(*jbytes("type", "soy", document['type:soy']))
#	  p.add(*jbytes("type", "oils", document['type:oils']))
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
print "Done loading data\n"
exit
