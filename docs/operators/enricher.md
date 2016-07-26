POJO Enricher
=============

## Operator Objective
Purpose of this operator is to take an incoming POJO (Plain Old Java Object) and use the external sources to enrich the data in incoming tuples 
and finally emitted the enriched data as a output POJO.

POJO Enricher supports enrichment from following external sources:

1. **JSON File Based** - Read the file in-memory having content stored in JSON format and use that to enrich the data.
2. **JDBC Based** - Any JDBC store can act as an external entity to which enricher can talk to and enrich incoming tuples.

POJO Enricher does not hold any state and is **idempotent**, **fault-tolerance** & **statically/dynamically partitionable**.

## Operator Usecase
1. Bank ***transaction records*** usually contains customerId. For further analysis of transaction one want customer name and other customer related information. 
Such information is present in another database. One could enrich the transaction's record with customer information using POJO Enricher.
2. ***Call Data Record (CDR)*** contains only mobile/telephone numbers of the customer. Customer information is missing in CDR.
POJO Enricher can be used to enricher CDR with customer data for further analysis.


## Properties, Attributes and Ports
### <a name="props"></a>Properties of POJOEnricher
| **Property** | **Description** | **Type** | **Mandatory** | **Default Value** |
| -------- | ----------- | ---- | ------------------ | ------------- |
| *includeFields* | List of fields from database that needs to be added to output POJO. | List<String> | Yes | N/A |
| *lookupFields* | List of fields from input POJO which will form a *unique* key for querying to database | List<String> | Yes | N/A |
| *store* | Backend Store from which data should be queries for enrichment | [BackendStore](#backendStore) | Yes | N/A |
| *cacheExpirationInterval* | Cache entry expiry in ms. After this time, the lookup will be done again for given key | int | No | 1 * 60 * 60 * 1000 (1 hour) |
| *cacheCleanupInterval* | Interval in ms after which cache will be removed for any stale entries. | int | No | 1 * 60 * 60 * 1000 (1 hour) |
| *cacheSize* | Number of entry in cache after which eviction will start on each addition based on LRU | int | No | 1000 |

#### <a name="backendStore"></a>Properties of FSLoader (BackendStore)
| **Property** | **Description** | **Type** | **Mandatory** | **Default Value** |
| -------- | ----------- | ---- | ------------------ | ------------- |
| *fileName* | Path of the file, the data from which will be used for enrichment. See [here](#JSONFileFormat) for JSON File format. | String | Yes | N/A |


#### Properties of JDBCLoader (BackendStore)
| **Property** | **Description** | **Type** | **Mandatory** | **Default Value** |
| -------- | ----------- | ---- | ------------------ | ------------- |
| *databaseUrl* | Connection string for connecting to JDBC | String | Yes | N/A |
| *databaseDriver* | JDBC Driver class for connection to JDBC Store. This driver should be there in classpath | String | Yes | N/A |
| *connectionProperties* | Command seperated list of advanced connection properties that need to be passed to JDBC Driver. For eg. *prop1:val1,prop2:val2* | String | No | null |


### Platform Attributes that influences operator behavior
| **Attribute** | **Description** | **Type** | **Mandatory** |
| -------- | ----------- | ---- | ------------------ |
| *input.TUPLE_CLASS* | TUPLE_CLASS attribute on input port which tells operator the class of POJO which will be incoming | Class/String | Yes |
| *output.TUPLE_CLASS* | TUPLE_CLASS attribute on output port which tells operator the class of POJO which need to be emitted | Class/String | Yes |


### Ports
| **Port** | **Description** | **Type** | **Mandatory** |
| -------- | ----------- | ---- | ------------------ |
| *input* | Tuple which needs to be enriched are received on this port | Object (POJO) | Yes |
| *output* | Tuples that are enriched from external source are emitted from on this port | Object (POJO) | No |

## Limitations
Current POJOEnricher contains following limitation:

1. FSLoader loads the file content in memory. That means, a very large amount of data would bloat the memory and make the operator crash. In case the filesize of large, allocate correct memory to the POJOEnricher.
2. Incoming POJO should be a subset of outgoing POJO.
3. [includeFields](#props) should contains fields having same name in database column as well as outgoing POJO. For eg. If name of the database column is "customerName", then outgoing POJO should contains a field with the same name.
4. [lookupFields](#props) should contains fields having same name in database column as well as incoming POJO. For eg. If name of the database column is "customerId", then incoming POJO should contains a field with the same name.

## Example
Example for POJOEnricher can be found at: [https://github.com/DataTorrent/examples/tree/master/tutorials/enricher](https://github.com/DataTorrent/examples/tree/master/tutorials/enricher)

## Advanced

### <a name="JSONFileFormat"></a> File format for JSON based FSLoader
FSLoader expects file to be in specific format:

1. Each line makes on record which becomes part of the store
2. Each line is a valid JSON Object where *key* is name of the field name and *value* is the field value.

Example for the format look like following:
```json
{"circleId":0, "circleName":"A"}
{"circleId":1, "circleName":"B"}
{"circleId":2, "circleName":"C"}
{"circleId":3, "circleName":"D"}
{"circleId":4, "circleName":"E"}
{"circleId":5, "circleName":"F"}
{"circleId":6, "circleName":"G"}
{"circleId":7, "circleName":"H"}
{"circleId":8, "circleName":"I"}
{"circleId":9, "circleName":"J"}
```

