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

package org.opensearch.performanceanalyzer.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.performanceanalyzer.PerformanceAnalyzerApp;
import org.opensearch.performanceanalyzer.metrics.PerformanceAnalyzerMetrics;
import org.opensearch.performanceanalyzer.rca.framework.metrics.ReaderMetrics;

public class FileHelper {
    private static final Logger log = LogManager.getLogger(FileHelper.class);
    private static boolean jvmSupportMillisecondFileModityTime = true;
    private static long SECOND_TO_MILLISECONDS = 1000;

    static {
        try {
            // Create tmp file and test if we can read millisecond
            for (int i = 0; i < 2; i++) {
                File tmpFile = File.createTempFile("performanceanalyzer", ".tmp");
                tmpFile.deleteOnExit();
                jvmSupportMillisecondFileModityTime = tmpFile.lastModified() % 1000 != 0;
                if (jvmSupportMillisecondFileModityTime) {
                    break;
                }
                Thread.sleep(2);
            }
        } catch (Exception ex) {
            log.error("Having issue creating tmp file. Using default value.", ex);
        }
        log.info("jvmSupportMillisecondFileModityTime: {}", jvmSupportMillisecondFileModityTime);
    }

    public static long getLastModified(File file, long startTime, long endTime) {
        if (!file.isFile() || jvmSupportMillisecondFileModityTime) {
            return file.lastModified();
        }

        if (file.lastModified() < startTime - SECOND_TO_MILLISECONDS
                || file.lastModified() > endTime) {
            return file.lastModified();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                String[] fields = line.split(PerformanceAnalyzerMetrics.sKeyValueDelimitor);
                if (fields[0].equals(PerformanceAnalyzerMetrics.METRIC_CURRENT_TIME)) {
                    return Long.parseLong(fields[1]);
                }
            }
        } catch (Exception ex) {
            PerformanceAnalyzerApp.READER_METRICS_AGGREGATOR.updateStat(ReaderMetrics.OTHER, "", 1);
            log.debug(
                    "Having issue to read current time from the content of file. Using file metadata; exception: {} ExceptionCode: {}",
                    () -> ex,
                    () -> ReaderMetrics.OTHER.toString());
        }
        return file.lastModified();
    }
}
