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

package org.opensearch.performanceanalyzer.threads;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.performanceanalyzer.PerformanceAnalyzerApp;
import org.opensearch.performanceanalyzer.PerformanceAnalyzerThreads;
import org.opensearch.performanceanalyzer.rca.framework.metrics.ReaderMetrics;
import org.opensearch.performanceanalyzer.rca.stats.measurements.MeasurementSet;
import org.opensearch.performanceanalyzer.threads.exceptions.PAThreadException;

/** Class that wraps a given runnable in a thread with exception handling capabilities. */
public class ThreadProvider {

    private static final Logger LOG = LogManager.getLogger(ThreadProvider.class);

    /**
     * Creates a thread which executes the given runnable when started. If the given runnable throws
     * an uncaught exception, it is then written to the exception queue which will be processed by
     * the exception handler thread.
     *
     * @param innerRunnable The runnable to execute when the thread starts.
     * @param paThread The thread enum value from {@link PerformanceAnalyzerThreads}
     * @return The thread with the wrapped runnable.
     */
    public Thread createThreadForRunnable(
            final Runnable innerRunnable,
            final PerformanceAnalyzerThreads paThread,
            String threadNameAppender) {
        StringBuilder threadName = new StringBuilder(paThread.toString());
        if (!threadNameAppender.isEmpty()) {
            threadName.append("-").append(threadNameAppender);
        }
        String threadNameStr = threadName.toString();
        MeasurementSet metric = paThread.getThreadExceptionCode();
        Thread t =
                new Thread(
                        () -> {
                            try {
                                innerRunnable.run();
                            } catch (Throwable innerThrowable) {
                                LOG.error("A thread crashed: ", innerThrowable);
                                try {
                                    PerformanceAnalyzerApp.exceptionQueue.put(
                                            new PAThreadException(paThread, innerThrowable));
                                } catch (InterruptedException e) {
                                    PerformanceAnalyzerApp.READER_METRICS_AGGREGATOR.updateStat(
                                            metric, "", 1);
                                    LOG.error(
                                            "Thread was interrupted while waiting to put an exception into the queue. "
                                                    + "Message: {}",
                                            e.getMessage(),
                                            e);
                                }
                            }
                            PerformanceAnalyzerApp.READER_METRICS_AGGREGATOR.updateStat(
                                    ReaderMetrics.NUM_PA_THREADS_ENDED,
                                    ReaderMetrics.NUM_PA_THREADS_ENDED.toString(),
                                    1);
                            LOG.info("Thread: {} completed.", threadNameStr);
                        },
                        threadNameStr);

        PerformanceAnalyzerApp.READER_METRICS_AGGREGATOR.updateStat(
                ReaderMetrics.NUM_PA_THREADS_STARTED,
                ReaderMetrics.NUM_PA_THREADS_STARTED.toString(),
                1);
        LOG.info("Spun up a thread with name: {}", threadNameStr);
        return t;
    }

    public Thread createThreadForRunnable(
            final Runnable innerRunnable, final PerformanceAnalyzerThreads paThread) {
        return createThreadForRunnable(innerRunnable, paThread, "");
    }
}
