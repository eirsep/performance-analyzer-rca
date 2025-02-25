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

package org.opensearch.performanceanalyzer.threads.exceptions;


import org.opensearch.performanceanalyzer.PerformanceAnalyzerThreads;
import org.opensearch.performanceanalyzer.rca.stats.measurements.MeasurementSet;

/** Exception that is thrown when one of the PA threads run into an unhandled exception. */
public class PAThreadException extends Exception {

    private final PerformanceAnalyzerThreads paThread;

    private Throwable innerThrowable;

    public PAThreadException(final PerformanceAnalyzerThreads paThread, final Throwable throwable) {
        this.paThread = paThread;
        this.innerThrowable = throwable;
    }

    /**
     * Gets the name of the thread that threw an unhandled exception.
     *
     * @return The name of the thread.
     */
    public String getPaThreadName() {
        return paThread.toString();
    }

    /**
     * Gets the counter against which we need to record an error metric.
     *
     * @return The {@link MeasurementSet} enum value that represents the error metric name.
     */
    public MeasurementSet getExceptionCode() {
        return paThread.getThreadExceptionCode();
    }

    /**
     * Gets the actual {@link Throwable} thrown by the thread.
     *
     * @return The throwable.
     */
    public Throwable getInnerThrowable() {
        return innerThrowable;
    }
}
