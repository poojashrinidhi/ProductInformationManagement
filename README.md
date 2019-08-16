# ProductInformationManagement

Task: Develop microservices for product information managent
----------------------------------------------------------------------------------------------
- Achieved via : <br/>
  - Importer-service : Reads data from csv and puts to Kafka Queue<br/>
  - Aggregator-service : Consumes from Kafka queue and dumps to embedded hsql database for further analytics exposed via REST.<br/>
  - Both services are containerized along with kafka using docker compose.
  
Configurations :
------------------
Update relevant configurations in :
importer-service/src/main/resources/application.properties
aggregator-service/src/main/resources/application.properties

Instructions to Run :
---------------------
docker-compose build <br/>
docker-compose up -d <br/>

Importer Service Endpoint:
--------------------------
<pre>
GET http://localhost:9001/import/productCsv
Successfully Imported Product CSV file and sent to Kafka Queue
</pre>
<br/>

Aggregator Service Endpoints
----------------------------
<pre>
GET http://localhost:9002/products/
[
    {
        "createdTimeStamp": "2019-08-16T23:21:18.386+0000",
        "lastModifiedTimeStamp": "2019-08-16T23:21:18.386+0000",
        "uuid": "6a227cf5-34e4-4f48-a77c-63576929aecd",
        "name": "Nokia Mobile",
        "description": "smart phone",
        "provider": "Nokia",
        "available": true,
        "measurementUnits": "PC"
    },
    {
        "createdTimeStamp": "2019-07-16T23:21:39.519+0000",
        "lastModifiedTimeStamp": "2019-08-16T23:23:09.878+0000",
        "uuid": "6a227cf5-34e4-4f48-a77c-63576929aece",
        "name": "Lenovo Mobile",
        "description": "smart phone",
        "provider": "Lenovo",
        "available": true,
        "measurementUnits": "PC"
    },
    {
        "createdTimeStamp": "2019-08-16T23:21:18.225+0000",
        "lastModifiedTimeStamp": "2019-08-16T23:21:18.225+0000",
        "uuid": "fccc13f1-f337-480b-9305-a5bb56bcaa11",
        "name": "Samsung Galaxy Mobile",
        "description": "smart phone",
        "provider": "Samsung Galaxy",
        "available": true,
        "measurementUnits": "PC"
    }
]
</pre>

<pre>
GET http://localhost:9002/products/statistics
{
    "productStatistics": {
        "2019-08-16": {
            "numberOfItemsCreated": 2,
            "numberOfItemsModified": 1
        },
        "2019-07-16": {
            "numberOfItemsCreated": 1,
            "numberOfItemsModified": 0
        }
    }
}
</pre>
