//  1. Write a MongoDB query to display the fields restaurant\_id, name, borough and
//  cuisine for all the documents in the collection restaurant.

    db.restaurants.find( {}, {restaurant_id: 1, name: 1, borough: 1, cuisine: 1} ).pretty()
	
//  2. Write a MongoDB query to display the fields restaurant\_id, name, borough and
//  cuisine, but exclude the field \_id for all the documents in the collection
//  restaurant.
    
    db.restaurants.find( {}, {_id: 0, restaurant_id: 1, name: 1, borough: 1, cuisine: 1} ).pretty()
	
//  3. Write a MongoDB query to display all the restaurant which is in the borough
//  Bronx.
    
	db.restaurants.find( {"borough": "Bronx"} ).pretty()
	
//  4. Write a MongoDB query to display the next 5 restaurants after skipping first
//  5 which are in the borough Bronx (Hint: often this technique is called
//  paging)

	db.restaurants.find( {"borough": "Bronx"} ).limit( 5 ).skip( 5 ).pretty()
    
//  5. Write a MongoDB query to find the restaurants who achieved a score more than
//  90.

	db.restaurants.find( { "grades.score": { $gt: 90 } } ).pretty()
    
//  6. Write a MongoDB query to find the restaurants that achieved a score, more
//  than 80 but less than 100 Hint: check out [$elemMatch over an "Array of
//  Embeded Documents"
//  array](https://docs.mongodb.com/manual/reference/operator/query/elemMatch/#array-of-embedded-documents)
    
	db.restaurants.find( { "grades": { $elemMatch: { "score": { $gt: 80, $lt: 100 } } } } ).pretty()
	
//  7. Write a MongoDB query to find the restaurants that do not prepare any cuisine
//  of 'American' and their grade score more than 70 and latitude less than
//  -65.754168.  You MUST use $and operator

	db.restaurants.find({
	  $and: [ 
	    { "cuisine": { $ne: "American " } },
		{ "grades.score": { $gt: 70 } },
		{ "address.coord.0": { $lt: -65.754168 } } 
	  ]
	}).pretty()
    
//  8. Write a MongoDB query to find the restaurants which do not prepare any
//  cuisine of 'American' and achieved a score more than 70 and located in the
//  latitude less than -65.754168. Do NOT use $and operator
    
	db.restaurants.find({
	  "cuisine": { $ne: "American " },
	  "grades.score": { $gt: 70 },
	  "address.coord.0": { $lt: -65.754168 }
	}).pretty()
	
//  9. Write a MongoDB query to find the restaurants which do not prepare any
//  cuisine of 'American ' and achieved a grade point 'A' not belongs to the
//  borough Brooklyn. The document must be displayed according to the cuisine in
//  descending order

	db.restaurants.find({
	  "cuisine": { $ne: "American " },
	  "grades.grade": "A",
	  "borough": { $ne: "Brooklyn" }
	}).sort( { cuisine: -1 } ).pretty()
    
//  10. Write a MongoDB query to find the restaurant Id, name, borough and cuisine
//  for those restaurants which contain 'Wil' as first 3 letters for its name.
    
	db.restaurants.find({
	  "name": { $regex: /^Wil/ }
	},
	{restaurant_id: 1, name: 1, borough: 1, cuisine: 1}).pretty()
	
//  11. Write a MongoDB query to find the restaurant Id, name, borough and cuisine
//  for those restaurants which contain 'ces' as last three letters for its name.
    
	db.restaurants.find({
	  "name": { $regex: /ces$/ }
	},
	{restaurant_id: 1, name: 1, borough: 1, cuisine: 1}).pretty()
	
//  12. Write a MongoDB query to find the restaurant Id, name, borough and cuisine
//  for those restaurants which contain 'Reg' as 3 letters somewhere in its name.

	db.restaurants.find({
	  "name": { $regex: /Reg/ }
	},
	{restaurant_id: 1, name: 1, borough: 1, cuisine: 1}).pretty()
    
//  13. Write a MongoDB query to find the restaurants which belong to the borough
//  Bronx and prepared either American or Chinese dish.

    db.restaurants.find({
	  "cuisine": { $in: ["American ", "Chinese"] }
	}).pretty()
    
//  14. Write a MongoDB query to find the restaurant Id, name, borough and cuisine
//  for those restaurants which belong to the borough Staten Island or Queens or
//  Bronx or Brooklyn.

    db.restaurants.find({
	  "borough": { $in: ["Staten Island", "Queens", "Bronx", "Brooklyn"] }
	},
	{restaurant_id: 1, name: 1, borough: 1, cuisine: 1}).pretty()
    
//  15. Write a MongoDB query to find the restaurant Id, name, borough and cuisine
//  for those restaurants which are not belonging to the borough Staten Island or
//  Queens or Bronx or Brooklyn.
    
	db.restaurants.find({
	  "borough": { $nin: ["Staten Island", "Queens", "Bronx", "Brooklyn"] }
	},
	{restaurant_id: 1, name: 1, borough: 1, cuisine: 1}).pretty()
	
//  16. Write a MongoDB query to find the restaurant Id, name, borough and cuisine
//  for those restaurants which achieved a score which is not more than ten.
//  NOTE: you can answer as less than or equal, but writing try writing as
//  "not-ing" "greater than 10" to get the feeling of nesting operators.

	db.restaurants.find({
	  "grades.score": { $not: { $gt: 10 } }
	},
	{restaurant_id: 1, name: 1, borough: 1, cuisine: 1}).pretty()
    
//  17. Write a MongoDB query to find the restaurant Id, name, borough and cuisine
//  for those restaurants whose name begins with 'Wil' or prepared dish except
//  'American' and 'Chinese'.  Sort descending by name.
	db.restaurants.find({
	  $or: [
	    { "name": /^Wil/ },
		{ "cuisine": { $nin: ["American ", "Chinese"] } }
	  ]
	},
	{restaurant_id: 1, name: 1, borough: 1, cuisine: 1}).sort( { name: -1 } ).pretty()
    
//  18. Write a MongoDB query to find the restaurant Id, name, and grades for those
//  restaurants which achieved a grade of "A" and scored 11 on an ISODate
//  "2014-08-11T00:00:00Z".

	db.restaurants.find({
	  "grades": { $elemMatch: {
        "grade": "A",
        "score": 11,
        "date": ISODate("2014-08-11T00:00:00Z")
      }}
	},
	{restaurant_id: 1, name: 1, grades: 1}).pretty()
    
//  19. Write a MongoDB query to find the restaurant Id, name and grades for those
//  restaurants where the 2nd element of grades array contains a grade of "A" and
//  score 9 on an ISODate "2014-08-11T00:00:00Z".

	db.restaurants.find({
	  "grades.1.grade": "A",
      "grades.1.score": 9,
      "grades.1.date": ISODate("2014-08-11T00:00:00Z")
    },
    {restaurant_id: 1, name: 1, grades: 1}).pretty()
    
//  20. Write a MongoDB query to arrange the just the names (no ids) of the
//  restaurants in ascending order.
    
    db.restaurants.find( {}, {_id: 0, name: 1}).sort( { name: 1 } )
	
//  21. Write a MongoDB query to arranged the name of the cuisine in ascending order
//  and for that same cuisine borough should be in descending order. Output name
//  and borough.

    db.restaurants.find( {}, {_id: 0, cuisine: 1, borough: 1}).sort( { cuisine: 1, borough: -1} )
    
//  22. Write a MongoDB query to know whether all the addresses contains the street
//  or not.

	db.restaurants.find( { "address.street": { $exists: false } } ).count()
    
//  23. Write a MongoDB query which will count the number of documents in the
//  restaurants collection where the coord field value is Double.
    
	db.restaurants.find( { "address.coord": { $type: "double" } } ).count()
	
//  24. Write a MongoDB query that counts the number of restaurants that have a score
//  of 0 mod 7.  (E.g. 0, 7, 14, 21...)

	db.restaurants.find( { "grades.score": { $mod: [7, 0] } }).count()
    
//  25. Write a MongoDB query that gives the average score and output documents with
//  the format `{ name: "xyz", borough: "abc", avgScore: x.y }`
    
    db.restaurants.aggregate([ { $project: { _id: 0, name: "$name", borough: "$borough", avgScore: { $avg: "$grades.score" } } } ]).pretty()
    
//  26. Write a Mongo query that counts the number of restaurants with an "A" rating
//  in Brooklyn.
    
    db.restaurants.find( { "grades.grade": "A", "borough": "Brooklyn" } ).count()
    
//  27. Write a Mongo query that counts the number of restaurants with an "A" rating
//  in each borough.  (You must use one query, not run the previous query once
//  for each borough.)
    
    db.restaurants.aggregate([ 
      { $match: { "grades.grade": "A" } },
      { $group: { _id: "$borough", count: { $sum: 1 } } }
    ])
    
//  28. Write a Mongo query that displays the average score by cuisine type.

    db.restaurants.aggregate([
      { $project: { _id: 0, name: "$name", cuisine: "$cuisine", scoreSum: { $sum: "$grades.score" }, scoreCnt: { $size: "$grades" } } },
      { $group: { _id: "$cuisine", allScores: { $sum: "$scoreSum" }, totCnt: { $sum: "$scoreCnt" } } },
      { $project: { _id: 1, cuisine: "$cuisine", avgRating: { $divide: [ "$allScores", "$totCnt" ] } } }
    ]).pretty()