[![Java CI](https://github.com/opensearch-project/performance-analyzer-rca/workflows/Java%20CI/badge.svg)](https://github.com/opensearch-project/performance-analyzer-rca/actions?query=workflow%3A%22Java+CI%22)
[![codecov](https://codecov.io/gh/opensearch-project/performance-analyzer-rca/branch/main/graph/badge.svg)](https://codecov.io/gh/opensearch-project/performance-analyzer-rca)
[![Documentation](https://img.shields.io/badge/api-reference-blue.svg)](https://opensearch.org/docs/monitoring-plugins/pa/reference/)
[![Chat](https://img.shields.io/badge/chat-on%20forums-blue)](https://discuss.opendistrocommunity.dev/c/performance-analyzer/)
![PRs welcome!](https://img.shields.io/badge/PRs-welcome!-success)

<img src="https://opensearch.org/assets/brand/SVG/Logo/opensearch_logo_default.svg" height="64px"/>

<!-- TOC -->

- [OpenSearch Performance Analyzer RCA](#opensearch-performace-analyzer-rca)
- [RCA Overview](#rca-overview)
- [Design RFC](#design-rfc)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [Code of Conduct](#code-of-conduct)
- [Security](#security)
- [License](#license)
- [Copyright](#copyright)

<!-- /TOC -->

## OpenSearch Performance Analyzer RCA

The Performance Analyzer RCA is a framework that builds on the Performance Analyzer engine to
support Root Cause Analysis (RCA) of performance and reliability problems in OpenSearch
clusters. This framework executes real time root cause analyses using Performance Analyzer
metrics. Root cause analysis can significantly improve operations, administration and
provisioning of OpenSearch clusters, and it can enable OpenSearch client teams to tune
their workloads to reduce errors.

## RCA Overview
The RCA framework is modelled as a distributed data-flow graph where data flows downstream 
from the leaf nodes to the root. Leaf nodes of the graph represent `Performance Analyzer` metrics
on which intermediate computations are performed. The intermediate nodes can be RCAs or other derived 
symptoms which helps in computation of the final RCA. The framework operates on a single analysis graph
which is composed of multiple connected-components. Each connected component contains one top-level RCA at its root.

### Terminologies

__DataStream a.k.a FlowUnit__: A flow-unit is a data object that is exchanged between nodes. Each unit contains a timestamp and the data generated at that timestamp. Data flows downstream from the leaf nodes to the root.

__RCA__: An RCA is a function which operates on multiple datastreams. These datastreams can have zero or more metrics, symptoms or other RCAs. 

__Symptom__: A symptom is an intermediate function operating on metric or symptom datastreams. For example - A high CPU utilization symptom can calculate moving average over k samples and categorize CPU utilization(high/low) 
based on a threshold. A symptom node typically outputs a boolean - `presence`(true) or `absence`(false) of a symptom.

__Metrics__: Metrics are typically served as continuous datastreams to downstream nodes in the graph from Performance Analyzer (PA). The RCA framework can also be extended to pull custom metrics from other data sources.

### Components

__Framework__: The RCA runtime operates on an `AnalysisGraph`. You can extend this class and override the `construct` method to build your own RCAs. You should specify the path to the class in the `analysis-graph-implementor` section of `pa_config/rca.conf`.  The `addLeaf` and `addAllUpstreams` helper methods are useful when you define the dependencies between nodes of the graph.

__Scheduler__: The scheduler invokes the `operate` method on each graph node in topological order as defined in the `AnalysisGraph`. Nodes with no dependencies can be executed in parallel. Use flow-units to share data between RCA nodes instead of shared objects to avoid data races and performance bottlenecks.

__Networking__: The networking layer handles RPCs for remote RCA execution. If a host depends on a remote datastream for RCA computation, it subscribes to the datastream on startup. Subsequently, the output of every RCA execution on the upstream host is streamed to the downstream subscriber.

__WireHopper__: Interface between the scheduler and the networking layer to help in remote RCA execution.

__Context__: The context contains a brief summary of the RCA. For example, a High CPU utilization symptom context will contain the average CPU utilization when the symptom was triggered. 

__Thresholds__: Thresholds are static values that must be exceeded to trigger symptoms and RCAs. Thresholds can be dynamically updated and do not require a process restart. Thresholds are user defined and often depend on hardware configuration and OpenSearch versions. The threshold store supports tags to help associate metadata with a threshold.

__Tags__: Tags are key-value pairs that are specified in the configuration file(rca.conf). Tags can be associated with both hosts and RCA nodes.
* RCA nodes are only executed on hosts with the exact same tags as the RCA node. A common use-case of tags is to restrict certain RCA nodes to only execute on the master node. 
* Tags are also used by hosts to find and subscribe to remote datastreams. For example, a cluster-wide RCA running on the master can subscribe to datastreams from all data hosts in the cluster.

## Design RFC
[RFC](https://github.com/opensearch-project/performance-analyzer-rca/blob/main/docs/rfc-rca.pdf)

## Rest API to get the RCAs
* To get response for all the available RCA, use:
```
curl --url "localhost:9600/_plugins/_performanceanalyzer/rca" -XGET
```
* To get response for a specific RCA, use:
```
curl --url "localhost:9600/_plugins/_performanceanalyzer/rca?name=HighHeapUsageClusterRca" -XGET
```
The sample RCA response from above api
```
{
    "HighHeapUsageClusterRca": [
        {
            "rca_name": "HighHeapUsageClusterRca",
            "state": "unhealthy",
            "timestamp": 1587426650942,
            "HotClusterSummary": [
                {
                    "number_of_nodes": 2,
                    "number_of_unhealthy_nodes": 1,
                    "HotNodeSummary": [
                        {
                            "host_address": "192.168.144.2",
                            "node_id": "JtlEoRowSI6iNpzpjlbp_Q",
                            "HotResourceSummary": [
                                {
                                    "resource_type": "old gen",
                                    "threshold": 0.65,
                                    "value": 0.81827232588145373,
                                    "avg": NaN,
                                    "max": NaN,
                                    "min": NaN,
                                    "unit_type": "heap usage in percentage",
                                    "time_period_seconds": 600,
                                    "TopConsumerSummary": [
                                        {
                                            "name": "CACHE_FIELDDATA_SIZE",
                                            "value": 590702564
                                        },
                                        {
                                            "name": "CACHE_REQUEST_SIZE",
                                            "value": 28375
                                        },
                                        {
                                            "name": "CACHE_QUERY_SIZE",
                                            "value": 12687
                                        }
                                    ],
                                }
                            ]
                        }
                    ] 
                }
            ]
        }
    ]
}
```

### Temperature profiles
There are ways to get the temperature profile of a cluster and of individual nodes.

The cluster level RCA can only be queried from the elected master using the following rest API.

`curl "localhost:9600/_plugins/_performanceanalyzer/rca?name=ClusterTemperatureRca"`

In order to get the temperature of a particular node, we can use:

`curl "localhost:9600/_plugins/_performanceanalyzer/rca?name=AllTemperatureDimensions&local=true"`

## Rest API to get the ACTIONS suggested by the decider

This api returns the last suggested action set by the decider framework.

```
curl --url "localhost:9600/_plugins/_performanceanalyzer/actions" -XGET
```

The sample response from the above API:

```
{
    "LastSuggestedActionSet": [
        {
            "actionName": "ModifyQueueCapacity",
            "actionable": true,
            "coolOffPeriod": 300000,
            "muted": false,
            "nodeIds": "{oSN5LEstThe25y8NrCGyAg}",
            "nodeIps": "{10.212.52.87}",
            "summary": {
                "Id": "oSN5LEstThe25y8NrCGyAg",
                "Ip": "10.212.52.87",
                "canUpdate": true,
                "coolOffPeriodInMillis": 300000,
                "currentCapacity": 0,
                "desiredCapacity": 1000,
                "resource": 5
            }
            "timestamp": 1603671580290
        }
    ]
}
```

## Building, Deploying, and Running the RCA Framework
Please refer to the [Install Guide](./INSTALL.md) for detailed information on building, installing and running the RCA framework.

## Current Limitations
* This is alpha code and is in development.
* We don't have 100% unit test coverage yet and will continue to add new unit tests. 
* We have tested and verified RCA artifacts only for Docker images. Other distributions are yet to be built and tested and will be available as a part of the final release.

## Documentation

Please refer to the [technical documentation](https://opensearch.org/docs/monitoring-plugins/pa/index/) detailed information on installing and configuring Performance Analyzer.

## Contributing

See [developer guide](DEVELOPER_GUIDE.md) and [how to contribute to this project](CONTRIBUTING.md).

## Code of Conduct

This project has adopted the [Amazon Open Source Code of Conduct](CODE_OF_CONDUCT.md). For more information see the [Code of Conduct FAQ](https://aws.github.io/code-of-conduct-faq), or contact [opensource-codeofconduct@amazon.com](mailto:opensource-codeofconduct@amazon.com) with any additional questions or comments.

## Security

If you discover a potential security issue in this project we ask that you notify AWS/Amazon Security via our [vulnerability reporting page](http://aws.amazon.com/security/vulnerability-reporting/). Please do **not** create a public GitHub issue.

## License

This project is licensed under the [Apache v2.0 License](LICENSE).

## Copyright

Copyright 2020-2021 Amazon.com, Inc. or its affiliates. All Rights Reserved.
