# Background
Sky products use a recommendation engine to recommend content to users. Each recommendation has following attributes:

uuid - unique id of content

start - start time of broadcast

end - end time of broadcast.

At the moment most of products are using Java web service which generates recommendations in XML format. However, there is a new requirement to provide recommendations in JSON format and for multiple timeslots (time window).

# Run
Requirement:
- Java 7+
- Maven
- sbt

To test Recs-Engine task: `mvn test`  

To run Recs-Engine task: `mvn spring-boot:run` and open url: `http://localhost:8080/recs/personalised?num=5&start=1436380686147&end=1477380686147&subscriber=asd`

To test Recs-API task  (make sure you had Recs-Engine running first): `sbt test` 

To run Recs-API task  (make sure you had Recs-Engine running first): `sbt run` and open url: `http://localhost:8090/personalised/asd`

# Task: Recs-Engine

Recs-Engine is a Java Spring web service which is generating requested number of recommendations for url query. Each generated recommendation is filtered by set of filters to check if it is relevant, it is discarded if not. Recommendations are generated until specified number is generated.

Path to generate recommendations is /recs/personalised and following request parameters are required:
- num - number of recommendations to generate
- start - timestamp of beginning window for which to generate recommendations
- end - timestamp of ending of window for which to generate recommendations
- subscriber - unique identifier of subscriber

Result is returned in XML format. For example following http call:

`http://localhost:8080/recs/personalised?num=5&start=1415286463203&end=1415294605557
&subscriber=asd`

Will result in following recommendations:

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<recommendations>
<recommendations>
<uuid>1f18536a-e86f-4781-9819-7b3e7d385908</uuid>
<start>1415288463203</start>
<end>1415289998492</end>
</recommendations>
<recommendations>
<uuid>c3a6ac38-16b1-41da-83f8-077aff4841d6</uuid>
<start>1415284772998</start>
<end>1415289302319</end>
</recommendations>
<recommendations>
<uuid>42d34321-3283-467c-89dd-36983d8e4f4e</uuid>
<start>1415290983863</start>
<end>1415294905557</end>
</recommendations>
<recommendations>
<uuid>b5408d7c-688a-48f5-ae04-7d78765c3f3f</uuid>
<start>1415289589667</start>
<end>1415290372753</end>
</recommendations>
<recommendations>
<uuid>4d6bf32d-11d8-4b82-9b2b-07bc612a6060</uuid>
<start>1415290982236</start>
<end>1415292219539</end>
</recommendations>
</recommendations>
```

Your task is to extend existing codebase and implement 2 new features of this API:
- Provide implementation of PercentCompleteFilter class with functionality to filter out content that is already past 60% of its running time with regards to provided recommendations window.
- Add possibility to cache recommendation result for given subscriber for 5 minutes.

# Task: Recs-api
As there is a new requirement to provide recommendations in json, Scala has been chosen as a technology to implement this. Your task will be to extend the provided codebase to complete following:
- Rest web service that will accept request of following url: /personalised/{subscriber} where device is a url segment representing unique subscriber id
- Web service should use Recs-Engine to generate 5 recommendations and convert its result into json.
- Web service should request recommendations for 3 subsequent 1 hour timeslots (starting from current hour) and use start/end request parameters to control that. All recommendations should be merged into single response, see example below.
