### Running the KB Demos
> Note that this project requires the Java 11 JRE
* Base command (run from root 'knowledgeBase' directory): ``` java -cp knowledgeBase-0.1-jar-with-dependencies.jar csci.project.knowledgeBase.testing.DemoTest ```
  * Note that you must run from the root 'knowledgeBase' dir in order for the application to find the 'knowledge_base' directory, which houses the datafiles
* If you run a demo that involves deletions, please reset the knowledge base prior to running another demo
  * In the directory 'knowledgeBase\knowledge_base\backup', there is a directory 'outdoor_gear_kb' that contains a clean version of the outdoor gear KB
  * Copy 'knowledgeBase\knowledge_base\backup\outdoor_gear_kb' to 'knowledgeBase\knowledge_base', overwriting & replacing the files in 'knowledgeBase\knowledge_base\outdoor_gear_kb'
* Valid command line argument values:
  * ``` read_only ```: Runs a demo version with one simulated user that does basic reads and queries against the KB
  * ``` delete_only ```: Runs a demo version with one simulated user that executes delete statements against the KB
  * ``` read_delete ```: Runs a demo version with two simulated users: one that executes basic reads and queries and one that executes deletes. Illustrates how one user can affect the data that another use sees/has access to.
  * ``` force_err ```: Runs a demo version that forces an error in the 'Reconciler' module after a query completes.
  * If no argument is provided, the default behavior is ``` read_only ```. If the provided argument is not one of the above values, then nothing will run.