/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 *
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */

/*
 * Copyright 2019-2021 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.opensearch.performanceanalyzer.model;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.opensearch.performanceanalyzer.metrics.AllMetrics;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.AdmissionControlDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.AdmissionControlValue;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.AggregatedOSDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.CacheConfigDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.CacheConfigValue;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.CircuitBreakerDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.CircuitBreakerValue;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.CommonMetric;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.DiskDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.DiskValue;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.ElectionTermValue;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.EmptyDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.HeapDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.HeapValue;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.HttpMetric;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.HttpOnlyDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.IPDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.IPValue;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.LatencyDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.MasterPendingTaskDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.MasterPendingValue;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.MetricUnits;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.OSMetrics;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.ShardBulkMetric;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.ShardOperationMetric;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.ShardStatsDerivedDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.ShardStatsValue;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.TCPDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.TCPValue;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.ThreadPoolDimension;
import org.opensearch.performanceanalyzer.metrics.AllMetrics.ThreadPoolValue;

public class MetricsModel {

    public static final Map<String, MetricAttributes> ALL_METRICS;

    static {
        Map<String, MetricAttributes> allMetricsInitializer = new HashMap<>();
        // OS Metrics
        allMetricsInitializer.put(
                OSMetrics.CPU_UTILIZATION.toString(),
                new MetricAttributes(MetricUnits.CORES.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.PAGING_MAJ_FLT_RATE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT_PER_SEC.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.PAGING_MIN_FLT_RATE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT_PER_SEC.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.PAGING_RSS.toString(),
                new MetricAttributes(MetricUnits.PAGES.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.SCHED_RUNTIME.toString(),
                new MetricAttributes(
                        MetricUnits.SEC_PER_CONTEXT_SWITCH.toString(),
                        AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.SCHED_WAITTIME.toString(),
                new MetricAttributes(
                        MetricUnits.SEC_PER_CONTEXT_SWITCH.toString(),
                        AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.SCHED_CTX_RATE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT_PER_SEC.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.HEAP_ALLOC_RATE.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE_PER_SEC.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.IO_READ_THROUGHPUT.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE_PER_SEC.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.IO_WRITE_THROUGHPUT.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE_PER_SEC.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.IO_TOT_THROUGHPUT.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE_PER_SEC.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.IO_READ_SYSCALL_RATE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT_PER_SEC.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.IO_WRITE_SYSCALL_RATE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT_PER_SEC.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.IO_TOTAL_SYSCALL_RATE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT_PER_SEC.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.THREAD_BLOCKED_TIME.toString(),
                new MetricAttributes(
                        MetricUnits.SEC_PER_EVENT.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.THREAD_BLOCKED_EVENT.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.THREAD_WAITED_TIME.toString(),
                new MetricAttributes(
                        MetricUnits.SEC_PER_EVENT.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                OSMetrics.THREAD_WAITED_EVENT.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), AggregatedOSDimension.values()));
        // Latency Metric
        allMetricsInitializer.put(
                CommonMetric.LATENCY.toString(),
                new MetricAttributes(
                        MetricUnits.MILLISECOND.toString(), LatencyDimension.values()));

        allMetricsInitializer.put(
                ShardOperationMetric.SHARD_OP_COUNT.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), AggregatedOSDimension.values()));
        allMetricsInitializer.put(
                ShardBulkMetric.DOC_COUNT.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), AggregatedOSDimension.values()));

        // HTTP Metrics
        allMetricsInitializer.put(
                HttpMetric.HTTP_REQUEST_DOCS.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), HttpOnlyDimension.values()));
        allMetricsInitializer.put(
                HttpMetric.HTTP_TOTAL_REQUESTS.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), HttpOnlyDimension.values()));

        // Cache Max Size Metrics
        allMetricsInitializer.put(
                CacheConfigValue.CACHE_MAX_SIZE.toString(),
                new MetricAttributes(MetricUnits.BYTE.toString(), CacheConfigDimension.values()));

        // Circuit Breaker Metrics
        allMetricsInitializer.put(
                CircuitBreakerValue.CB_ESTIMATED_SIZE.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), CircuitBreakerDimension.values()));
        allMetricsInitializer.put(
                CircuitBreakerValue.CB_CONFIGURED_SIZE.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), CircuitBreakerDimension.values()));
        allMetricsInitializer.put(
                CircuitBreakerValue.CB_TRIPPED_EVENTS.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), CircuitBreakerDimension.values()));

        // Heap Metrics
        allMetricsInitializer.put(
                HeapValue.GC_COLLECTION_EVENT.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), HeapDimension.values()));
        allMetricsInitializer.put(
                HeapValue.GC_COLLECTION_TIME.toString(),
                new MetricAttributes(MetricUnits.MILLISECOND.toString(), HeapDimension.values()));
        allMetricsInitializer.put(
                HeapValue.HEAP_COMMITTED.toString(),
                new MetricAttributes(MetricUnits.BYTE.toString(), HeapDimension.values()));
        allMetricsInitializer.put(
                HeapValue.HEAP_INIT.toString(),
                new MetricAttributes(MetricUnits.BYTE.toString(), HeapDimension.values()));
        allMetricsInitializer.put(
                HeapValue.HEAP_MAX.toString(),
                new MetricAttributes(MetricUnits.BYTE.toString(), HeapDimension.values()));
        allMetricsInitializer.put(
                HeapValue.HEAP_USED.toString(),
                new MetricAttributes(MetricUnits.BYTE.toString(), HeapDimension.values()));

        // Disk Metrics
        allMetricsInitializer.put(
                DiskValue.DISK_UTILIZATION.toString(),
                new MetricAttributes(MetricUnits.PERCENT.toString(), DiskDimension.values()));
        allMetricsInitializer.put(
                DiskValue.DISK_WAITTIME.toString(),
                new MetricAttributes(MetricUnits.MILLISECOND.toString(), DiskDimension.values()));
        allMetricsInitializer.put(
                DiskValue.DISK_SERVICE_RATE.toString(),
                new MetricAttributes(
                        MetricUnits.MEGABYTE_PER_SEC.toString(), DiskDimension.values()));

        // TCP Metrics
        allMetricsInitializer.put(
                TCPValue.Net_TCP_NUM_FLOWS.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), TCPDimension.values()));
        allMetricsInitializer.put(
                TCPValue.Net_TCP_TXQ.toString(),
                new MetricAttributes(
                        MetricUnits.SEGMENT_PER_FLOW.toString(), TCPDimension.values()));
        allMetricsInitializer.put(
                TCPValue.Net_TCP_RXQ.toString(),
                new MetricAttributes(
                        MetricUnits.SEGMENT_PER_FLOW.toString(), TCPDimension.values()));
        allMetricsInitializer.put(
                TCPValue.Net_TCP_LOST.toString(),
                new MetricAttributes(
                        MetricUnits.SEGMENT_PER_FLOW.toString(), TCPDimension.values()));
        allMetricsInitializer.put(
                TCPValue.Net_TCP_SEND_CWND.toString(),
                new MetricAttributes(MetricUnits.BYTE_PER_FLOW.toString(), TCPDimension.values()));
        allMetricsInitializer.put(
                TCPValue.Net_TCP_SSTHRESH.toString(),
                new MetricAttributes(MetricUnits.BYTE_PER_FLOW.toString(), TCPDimension.values()));

        // IP Metrics
        allMetricsInitializer.put(
                IPValue.NET_PACKET_RATE4.toString(),
                new MetricAttributes(MetricUnits.PACKET_PER_SEC.toString(), IPDimension.values()));
        allMetricsInitializer.put(
                IPValue.NET_PACKET_DROP_RATE4.toString(),
                new MetricAttributes(MetricUnits.PACKET_PER_SEC.toString(), IPDimension.values()));
        allMetricsInitializer.put(
                IPValue.NET_PACKET_RATE6.toString(),
                new MetricAttributes(MetricUnits.PACKET_PER_SEC.toString(), IPDimension.values()));
        allMetricsInitializer.put(
                IPValue.NET_PACKET_DROP_RATE6.toString(),
                new MetricAttributes(MetricUnits.PACKET_PER_SEC.toString(), IPDimension.values()));
        allMetricsInitializer.put(
                IPValue.NET_THROUGHPUT.toString(),
                new MetricAttributes(MetricUnits.BYTE_PER_SEC.toString(), IPDimension.values()));

        // Thread Pool Metrics
        allMetricsInitializer.put(
                ThreadPoolValue.THREADPOOL_QUEUE_SIZE.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), ThreadPoolDimension.values()));
        allMetricsInitializer.put(
                ThreadPoolValue.THREADPOOL_REJECTED_REQS.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), ThreadPoolDimension.values()));
        allMetricsInitializer.put(
                ThreadPoolValue.THREADPOOL_TOTAL_THREADS.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), ThreadPoolDimension.values()));
        allMetricsInitializer.put(
                ThreadPoolValue.THREADPOOL_ACTIVE_THREADS.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), ThreadPoolDimension.values()));
        allMetricsInitializer.put(
                ThreadPoolValue.THREADPOOL_QUEUE_LATENCY.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), ThreadPoolDimension.values()));
        allMetricsInitializer.put(
                ThreadPoolValue.THREADPOOL_QUEUE_CAPACITY.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), ThreadPoolDimension.values()));

        // Shard Stats Metrics
        allMetricsInitializer.put(
                ShardStatsValue.INDEXING_THROTTLE_TIME.toString(),
                new MetricAttributes(
                        MetricUnits.MILLISECOND.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.CACHE_QUERY_HIT.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.CACHE_QUERY_MISS.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.CACHE_QUERY_SIZE.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.CACHE_FIELDDATA_EVICTION.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.CACHE_FIELDDATA_SIZE.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.CACHE_REQUEST_HIT.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.CACHE_REQUEST_MISS.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.CACHE_REQUEST_EVICTION.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.CACHE_REQUEST_SIZE.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.REFRESH_EVENT.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.REFRESH_TIME.toString(),
                new MetricAttributes(
                        MetricUnits.MILLISECOND.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.FLUSH_EVENT.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.FLUSH_TIME.toString(),
                new MetricAttributes(
                        MetricUnits.MILLISECOND.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.MERGE_EVENT.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.MERGE_TIME.toString(),
                new MetricAttributes(
                        MetricUnits.MILLISECOND.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.MERGE_CURRENT_EVENT.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.INDEXING_BUFFER.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.SEGMENTS_TOTAL.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.SEGMENTS_MEMORY.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.TERMS_MEMORY.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.STORED_FIELDS_MEMORY.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.TERM_VECTOR_MEMORY.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.NORMS_MEMORY.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.POINTS_MEMORY.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.DOC_VALUES_MEMORY.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.INDEX_WRITER_MEMORY.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.VERSION_MAP_MEMORY.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.BITSET_MEMORY.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));
        allMetricsInitializer.put(
                ShardStatsValue.SHARD_SIZE_IN_BYTES.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(), ShardStatsDerivedDimension.values()));

        // Master Metrics
        allMetricsInitializer.put(
                MasterPendingValue.MASTER_PENDING_QUEUE_SIZE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), MasterPendingTaskDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.MasterMetricValues.MASTER_TASK_QUEUE_TIME.toString(),
                new MetricAttributes(
                        MetricUnits.MILLISECOND.toString(),
                        AllMetrics.MasterMetricDimensions.values()));

        allMetricsInitializer.put(
                AllMetrics.MasterMetricValues.MASTER_TASK_RUN_TIME.toString(),
                new MetricAttributes(
                        MetricUnits.MILLISECOND.toString(),
                        AllMetrics.MasterMetricDimensions.values()));

        allMetricsInitializer.put(
                AllMetrics.FaultDetectionMetric.FOLLOWER_CHECK_LATENCY.toString(),
                new MetricAttributes(
                        MetricUnits.MILLISECOND.toString(),
                        AllMetrics.FaultDetectionDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.FaultDetectionMetric.LEADER_CHECK_LATENCY.toString(),
                new MetricAttributes(
                        MetricUnits.MILLISECOND.toString(),
                        AllMetrics.FaultDetectionDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.FaultDetectionMetric.FOLLOWER_CHECK_FAILURE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), AllMetrics.FaultDetectionDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.FaultDetectionMetric.LEADER_CHECK_FAILURE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), AllMetrics.FaultDetectionDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.MasterThrottlingValue.MASTER_THROTTLED_PENDING_TASK_COUNT.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), EmptyDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.MasterThrottlingValue.DATA_RETRYING_TASK_COUNT.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), EmptyDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.ShardStateValue.SHARD_STATE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), AllMetrics.ShardStateDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.ClusterApplierServiceStatsValue.CLUSTER_APPLIER_SERVICE_LATENCY
                        .toString(),
                new MetricAttributes(MetricUnits.MILLISECOND.toString(), EmptyDimension.values()));

        allMetricsInitializer.put(
                ElectionTermValue.ELECTION_TERM.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), EmptyDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.ClusterApplierServiceStatsValue.CLUSTER_APPLIER_SERVICE_FAILURE
                        .toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), EmptyDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.MasterClusterUpdateStatsValue.PUBLISH_CLUSTER_STATE_LATENCY.toString(),
                new MetricAttributes(MetricUnits.MILLISECOND.toString(), EmptyDimension.values()));

        allMetricsInitializer.put(
                AllMetrics.MasterClusterUpdateStatsValue.PUBLISH_CLUSTER_STATE_FAILURE.toString(),
                new MetricAttributes(MetricUnits.COUNT.toString(), EmptyDimension.values()));

        allMetricsInitializer.put(
                AdmissionControlValue.REJECTION_COUNT.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), AdmissionControlDimension.values()));
        allMetricsInitializer.put(
                AdmissionControlValue.THRESHOLD_VALUE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), AdmissionControlDimension.values()));
        allMetricsInitializer.put(
                AdmissionControlValue.CURRENT_VALUE.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(), AdmissionControlDimension.values()));

        // Shard Indexing Pressure Metrics
        allMetricsInitializer.put(
                AllMetrics.ShardIndexingPressureValue.REJECTION_COUNT.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT.toString(),
                        AllMetrics.ShardIndexingPressureDimension.values()));
        allMetricsInitializer.put(
                AllMetrics.ShardIndexingPressureValue.CURRENT_BYTES.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(),
                        AllMetrics.ShardIndexingPressureDimension.values()));
        allMetricsInitializer.put(
                AllMetrics.ShardIndexingPressureValue.CURRENT_LIMITS.toString(),
                new MetricAttributes(
                        MetricUnits.BYTE.toString(),
                        AllMetrics.ShardIndexingPressureDimension.values()));
        allMetricsInitializer.put(
                AllMetrics.ShardIndexingPressureValue.AVERAGE_WINDOW_THROUGHPUT.toString(),
                new MetricAttributes(
                        MetricUnits.COUNT_PER_SEC.toString(),
                        AllMetrics.ShardIndexingPressureDimension.values()));
        allMetricsInitializer.put(
                AllMetrics.ShardIndexingPressureValue.LAST_SUCCESSFUL_TIMESTAMP.toString(),
                new MetricAttributes(
                        MetricUnits.MILLISECOND.toString(),
                        AllMetrics.ShardIndexingPressureDimension.values()));

        ALL_METRICS = Collections.unmodifiableMap(allMetricsInitializer);
    }
}
