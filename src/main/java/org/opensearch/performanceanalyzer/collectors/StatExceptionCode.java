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

package org.opensearch.performanceanalyzer.collectors;

public enum StatExceptionCode {
    TOTAL_ERROR("TotalError"),

    // Tracks the number of VM attach/dataDump or detach failures.
    JVM_ATTACH_ERROR("JvmAttachErrror"),

    // This error is thrown if the java_pid file is missing.
    JVM_ATTACH_ERROR_JAVA_PID_FILE_MISSING("JvmAttachErrorJavaPidFileMissing"),

    // The lock could not be acquired within the timeout.
    JVM_ATTACH_LOCK_ACQUISITION_FAILED("JvmAttachLockAcquisitionFailed"),

    // ThreadState could not be found for an OpenSearch thread in the critical OpenSearch path.
    NO_THREAD_STATE_INFO("NoThreadStateInfo"),

    // This metric indicates that we successfully completed a thread-dump. Likewise,
    // an omission of this should indicate that the thread taking the dump got stuck.
    JVM_THREAD_DUMP_SUCCESSFUL("JvmThreadDumpSuccessful"),
    COLLECTORS_MUTED("CollectorsMutedCount"),
    MASTER_METRICS_ERROR("MasterMetricsError"),
    DISK_METRICS_ERROR("DiskMetricsError"),
    THREAD_IO_ERROR("ThreadIOError"),
    SCHEMA_PARSER_ERROR("SchemaParserError"),
    JSON_PARSER_ERROR("JsonParserError"),
    NETWORK_COLLECTION_ERROR("NetworkCollectionError"),
    NODESTATS_COLLECTION_ERROR("NodeStatsCollectionError"),
    OTHER_COLLECTION_ERROR("OtherCollectionError"),
    REQUEST_ERROR("RequestError"),
    REQUEST_REMOTE_ERROR("RequestRemoteError"),
    READER_PARSER_ERROR("ReaderParserError"),
    READER_RESTART_PROCESSING("ReaderRestartProcessing"),
    RCA_SCHEDULER_RESTART_PROCESSING("RCASchedulerRestartProcessing"),
    RCA_NETWORK_ERROR("RcaNetworkError"),
    RCA_VERTEX_RX_BUFFER_FULL_ERROR("RcaVertexRxBufferFullError"),
    RCA_NETWORK_THREADPOOL_QUEUE_FULL_ERROR("RcaNetworkThreadpoolQueueFullError"),
    RCA_SCHEDULER_STOPPED_ERROR("RcaSchedulerStoppedError"),
    OPENSEARCH_REQUEST_INTERCEPTOR_ERROR("OpenSearchRequestInterceptorError"),
    MISCONFIGURED_OLD_GEN_RCA_HEAP_MAX_MISSING("MisconfiguredOldGenRcaHeapMaxMissing"),
    MISCONFIGURED_OLD_GEN_RCA_HEAP_USED_MISSING("MisconfiguredOldGenRcaHeapUsedMissing"),
    MISCONFIGURED_OLD_GEN_RCA_GC_EVENTS_MISSING("MisconfiguredOldGenRcaGcEventsMissing"),
    TOTAL_MEM_READ_ERROR("TotalMemReadError");

    private final String value;

    StatExceptionCode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
