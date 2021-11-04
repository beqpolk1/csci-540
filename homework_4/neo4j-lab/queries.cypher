1. Write a query that will list all the airports in the graph.
MATCH (port:Airport) RETURN port;

2. Write a query that will report the number of airports in the graph.
MATCH (port:Airport) RETURN COUNT(port);

3. Bozeman's airport code is "BZN". Write a query that tells you if Bozeman is in the list. Do not manually look through the list in the previous problem.
// Referenced this source to build existence query
// https://aura.support.neo4j.com/hc/en-us/articles/4402983484819-How-to-verify-if-a-node-exist-or-not-node-existence-query-
OPTIONAL MATCH (port:Airport {name: "BZN"}) RETURN port IS NOT NULL AS airport_exists;

4. Write a query that reports if Helena in the list.
OPTIONAL MATCH (port:Airport {name: "HLN"}) RETURN port IS NOT NULL AS airport_exists;

5. For the previous two questions, try to write your query for Helena and Bozeman so that the only change to the query is the airport code. (For reference Bozeman is in the data set and Helena is not (because Bozeman rocks!)
OPTIONAL MATCH (port:Airport {name: "BZN"}) RETURN port IS NOT NULL AS airport_exists;

6. When I wrote this assignment, I was on a very long flight to Washington Dulles (airport code IAD). According to our data set, what is the shortest path that I could have gone (in terms of number of hops)? (Do not worry about origin and destination.)
MATCH (dulles:Airport {name: "IAD"}),
(yellow:Airport {name:"BZN"}),
path = SHORTESTPATH((dulles) -[*]- (yellow))
RETURN LENGTH(path) / 2;

7. I used to live in NYC (near Laguardia Airport), how many flight options would I have from Laguardia (LGA) to Dulles (IAD). (Hint check out allShortestPaths.)
MATCH (dulles:Airport {name: "IAD"}),
(lag:Airport {name:"LGA"}),
paths = ALLSHORTESTPATHS((dulles) -[*]- (lag))
RETURN COUNT(paths);

8. For each airpoort a, get the average ticket cost of flying to a.
MATCH (t:Ticket) -[:ASSIGN]-> (f:Flight) -[:DESTINATION]-> (a:Airport)
RETURN a.name, round(avg(t.price));