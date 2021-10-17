include Java

require 'time'

import 'javax.xml.stream.XMLStreamConstants'
import 'org.apache.hadoop.hbase.client.HTable'
import 'org.apache.hadoop.hbase.client.Put'


def jbytes(*args)
  args.map { |arg| arg.to_s.to_java_bytes }
end



factory = javax.xml.stream.XMLInputFactory.newInstance
https://www.ruby-forum.com/t/how-to-pass-file-contents-in-ruby-as-inputstream-to-java/239773
reader = factory.createXMLStreamReader(java.io.DataInputStream.new(java.io.ByteArrayInputStream.new(IO.read("/mnt/Food_Display_Table.xml").to_java.bytes)));

document = nil
buffer = nil
food_code = nil
count = 0

table = HTable.new(@hbase.configuration, 'food_data')
table.setAutoFlush(false)

while reader.has_next
  case reader.next
  when XMLStreamConstants::START_ELEMENT
	case reader.local_name
	when 'food_display_row' then document = {}
	else buffer = []
	end
  when XMLStreamConstants::CHARACTERS
	buffer << reader.text unless buffer.nil?
  when XMLStreamConstants::END_ELEMENT
	case reader.local_name
	when 'Food_Code' then
	  food_code = buffer.join
	when 'Display_Name' then
	  document['display_name'] = buffer.join
	when 'Portion_Default' then
	  document['portion:portion_default'] = buffer.join
	when 'Portion_Amount' then 
	  document['portion:portion_amount'] = buffer.join
	when 'Factor' then
	  document['portion:factor'] = buffer.join
	when 'Increment' then
	  document['portion:increment'] = buffer.join
	when 'Multiplier' then
	  document['portion:multiplier'] = buffer.join
	when 'Grains' then
	  document['type:grains'] = buffer.join
	when 'Vegetables' then
	  document['type:vegetables'] = buffer.join
	when 'Fruits' then
	  document['type:fruits'] = buffer.join
	when 'Milk' then
	  document['type:milk'] = buffer.join
	when 'Meats' then
	  document['type:meats'] = buffer.join
	when 'Soy' then
	  document['type:soy'] = buffer.join
	when 'Oils' then
	  document['type:oils'] = buffer.join
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
	  key = document['food_code'].to_java_bytes
	  ts = (Time.parse document['timestamp']).to_i

	  p = Put.new(key, ts)
	  p.add(*jbytes("display_name", "", document['display_name']))
	  p.add(*jbytes("portion", "default", document['portion:default']))
	  p.add(*jbytes("type", "grains", document['type:grains']))
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
exit
