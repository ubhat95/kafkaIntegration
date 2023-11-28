#Sample OpenSearch queries 

`must` and `filter` could do similar things but only `must` affects relevance scores of the documents
```
GET /wikimedia_topic/_search
{
  "_source": [],
  "size": 20,
  "query": {
    "bool": {
      "must": [],
      "filter": [
        {
          "term": {
            "type": "categorize"
          }
        },
        {
          "exists": {
            "field": "title"
          }
        },
        {
          "term": {
            "bot": true
          }
        }
        ],
      "should": [],
      "must_not": []
    }
    
  }
}

GET /wikimedia_topic/_search
{
  "sort": [
    {
      "id": {
        "order": "desc"
      }
    }
  ],
  "query": {
    "bool": {
      "must": [
        {"match": {
          "meta.domain": "commons.wikimedia.org"
        }},
        {
          "match": {
            "meta.stream": "mediawiki.recentchange"
          }
        }
      ],
      "should": [
        {"prefix": {
          "meta.uri": {
            "value": "http://"
          }
        }},
        {
          "match": {
            "bot": true
          }
        }
      ],
      "minimum_should_match": 1
    }
  }
}

GET /wikimedia_topic/_search
{
  }

GET _search
{
  "query": {
   "bool": {
     "must": [
       {"match": {
         "meta.domain": "id.wikipedia.org"
       }}
     ]
   }
  }
}

GET _search
{
  "query": {
    "bool" :{
      "filter": [
        {"term": {
          "_index": "wikimedia_topic"
        }},
        {
        "exists": {
          "field": "title"
        }
          
        }
      ]
    }
  }
}


GET wikimedia_topic/_search
{
  "query": {
    "multi_match" : {
      "query":    "Category:Media lacking author information",
      "fields": [  ] 
    }
  },
  "sort": [
    {
      "meta.offset": {
        "order": "desc"
      }
    }
  ]
}


GET /_cat/indices



PUT /purchases
{
"settings": {
   "index": {
     	"number_of_shards": 1,
     	"number_of_replicas": 1
   },
   "analysis": {
 	"analyzer": {
   	"analyzer-name": {
         	"type": "custom",
         	"tokenizer": "keyword",
         	"filter": "lowercase"
   	}
 	}
   },
   "mappings": {
   	"properties": {
     	"@timestamp": {
           	"type": "date"
     	},
     	"price": {
           	"type": "float"
     	},
     	"item_name": {
           	"type": "string",
           	"analyzer": "analyzer-name"
     	}
 	}
   }
 }
}


POST /wikimedia_topic/_doc/
{
"@timestamp": "2023-1-09-T14:59:00",
"item_name" : "New Balance 574",
"price" : 119.99
}


POST /wikimedia_topic/_delete_by_query
{
  "query": {
	"match": {
  	"item_name": "New Balance 574"
	}
  }
}


DELETE /wiki

POST _reindex
{
  "source": {
    "index": "wikimedia_topic"
  },
  "dest": {
    "index": "wiki"
  }
}


PUT /wikimedia_topic
{
  "settings": {
    "index": {
      "number_of_shards": 2,
      "number_of_replicas": 1
    }
  },
  "mappings": {
    "properties": {
      "age": {
        "type": "integer"
      }
    }
  },
  "aliases": {
    "sample-alias1": {}
  }
}
```
