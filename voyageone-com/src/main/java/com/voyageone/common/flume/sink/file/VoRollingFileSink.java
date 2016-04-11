/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.voyageone.common.flume.sink.file;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.instrumentation.SinkCounter;
import org.apache.flume.serialization.EventSerializer;
import org.apache.flume.serialization.EventSerializerFactory;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VoRollingFileSink extends AbstractSink implements Configurable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private File directory;

    private ScheduledExecutorService rollService;

    private String serializerType;
    private Context serializerContext;

    private SinkCounter sinkCounter;

    private Map<String, LogFileBean> logFileBeanMap;

    public VoRollingFileSink() {
        logFileBeanMap = new ConcurrentHashMap<>();
    }

    @Override
    public void configure(Context context) {

        String directory = context.getString("sink.directory");
        //String rollInterval = context.getString("sink.rollInterval");

        serializerType = context.getString("sink.serializer", "TEXT");
        serializerContext = new Context(context.getSubProperties("sink." + EventSerializer.CTX_PREFIX));

        Preconditions.checkArgument(directory != null, "Directory may not be null");
        Preconditions.checkNotNull(serializerType, "Serializer type is undefined");
        this.directory = new File(directory);

        if (sinkCounter == null) {
            sinkCounter = new SinkCounter(getName());
        }
    }

    @Override
    public void start() {
        logger.info("Starting {}...", this);
        sinkCounter.start();
        super.start();

        rollService = Executors.newScheduledThreadPool(
                1,
                new ThreadFactoryBuilder().setNameFormat("rollingFileSink-roller-" + Thread.currentThread().getId() + "-%d").build());

        /*
        * Every N seconds, mark that it's time to rotate. We purposefully do NOT
        * touch anything other than the indicator flag to avoid error handling
        * issues (e.g. IO exceptions occuring in two different threads.
        * Resist the urge to actually perform rotation in a separate thread!
        */

        long oneDay = 24 * 60 * 60 * 1000;

        long initDelay  = getTimeMillis("00:00:01") - System.currentTimeMillis();
        rollService.scheduleAtFixedRate((Runnable) () -> {
            logger.debug("Marking time to rotate file {}", directory.getAbsolutePath());
            if (logFileBeanMap != null) {
                Map<String, LogFileBean> logFileBeanMapTemp = new ConcurrentHashMap<>();
                logFileBeanMapTemp.putAll(logFileBeanMap);
                logFileBeanMap.clear();

                long sleepTime = 10 * 1000;
                try {
                    Thread.sleep(sleepTime);
                    for (Map.Entry<String, LogFileBean> entry : logFileBeanMapTemp.entrySet()) {
                        String key = entry.getKey();
                        LogFileBean logFileBean = entry.getValue();
                        if (logFileBean != null) {
                            logger.debug("Time to rotate {}", logFileBean.pathController.getCurrentFile(key));
                            if (logFileBean.outputStream != null) {
                                logger.debug("Closing file {}", logFileBean.pathController.getCurrentFile(key));

                                try {
                                    logFileBean.serializer.flush();
                                    logFileBean.serializer.beforeClose();
                                    logFileBean.outputStream.close();
                                    sinkCounter.incrementConnectionClosedCount();
                                } catch (IOException e) {
                                    sinkCounter.incrementConnectionFailedCount();
                                    throw new EventDeliveryException("Unable to rotate file " + logFileBean.pathController.getCurrentFile(key) + " while delivering event", e);
                                } finally {
                                    logFileBean.serializer = null;
                                    logFileBean.outputStream = null;
                                    logFileBean.pathController = null;
                                    logFileBean.currentFile = null;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }


                sinkCounter.incrementConnectionClosedCount();
            }
        }, initDelay, oneDay, TimeUnit.SECONDS);

        logger.info("VoRollingFileSink {} started.", getName());
    }

    @Override
    public Status process() throws EventDeliveryException {
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();
        Status result = Status.READY;
        try {
            transaction.begin();
            int eventAttemptCounter = 0;
            Event event = channel.take();
            if (event != null) {
                String projectFile = event.getHeaders().get("projectFile");
                if (projectFile != null && !"".equals(projectFile.trim())) {
                    if (!logFileBeanMap.containsKey(projectFile)) {
                        logFileBeanMap.put(projectFile, new LogFileBean(projectFile));
                    }
                    LogFileBean logFileBean = logFileBeanMap.get(projectFile);
                    if (!logFileBean.currentFile.exists()) {
                        logFileBeanMap.put(projectFile, new LogFileBean(projectFile));
                        logFileBeanMap.get(projectFile);
                    }

                    sinkCounter.incrementEventDrainAttemptCount();
                    eventAttemptCounter++;
                    logFileBean.serializer.write(event);

                    logFileBean.serializer.flush();
                    logFileBean.outputStream.flush();
                    //logFileBean.outputStream.close();
                    /*
                    * FIXME: Feature: Rotate on size and time by checking bytes written and
                    * setting shouldRotate = true if we're past a threshold.
                    */

                    /*
                    * FIXME: Feature: Control flush interval based on time or number of
                    * events. For now, we're super-conservative and flush on each write.
                    */
                }
            } else {
                // No events found, request back-off semantics from runner
                result = Status.BACKOFF;
            }
            transaction.commit();
            sinkCounter.addToEventDrainSuccessCount(eventAttemptCounter);
        } catch (Exception ex) {
            transaction.rollback();
            logger.error("process error:", ex);
            throw new EventDeliveryException("Failed to process transaction", ex);
        } finally {
            transaction.close();
        }

        return result;
    }

    @Override
    public void stop() {
        logger.info("RollingFile sink {} stopping...", getName());
        sinkCounter.stop();
        super.stop();

        for (Map.Entry<String, LogFileBean> entry : logFileBeanMap.entrySet()) {
            String key = entry.getKey();
            LogFileBean logFileBean = entry.getValue();
            if (logFileBean != null) {
                logger.debug("Closing file {}", logFileBean.pathController.getCurrentFile(key));
                if (logFileBean.outputStream != null) {
                    try {
                        logFileBean.serializer.flush();
                        logFileBean.serializer.beforeClose();
                        logFileBean.outputStream.close();
                        sinkCounter.incrementConnectionClosedCount();
                    } catch (IOException e) {
                        sinkCounter.incrementConnectionFailedCount();
                        logger.error("Unable to close output stream. Exception follows.", e);
                    } finally {
                        logFileBean.serializer = null;
                        logFileBean.outputStream = null;
                        logFileBean.pathController = null;
                        logFileBean.currentFile = null;
                    }
                }
            }
        }
        rollService.shutdown();

        while (!rollService.isTerminated()) {
            try {
                rollService.awaitTermination(1, TimeUnit.SECONDS);
            } catch (Exception e) {
                logger.debug("Interrupted while waiting for roll service to stop. Please report this.", e);
            }
        }
        logger.info("RollingFile sink {} stopped. Event metrics: {}", getName(), sinkCounter);
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }



    /**
     * 获取指定时间对应的毫秒数
     * @param time "HH:mm:ss"
     * @return long
     */
    private long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    private class LogFileBean {
        public File currentFile;

        public VoPathManagerForFile pathController;

        public OutputStream outputStream;

        public EventSerializer serializer;

        public LogFileBean(String projectFile) throws Exception {
            pathController = new VoPathManagerForFile();
            pathController.setBaseDirectory(directory);

            currentFile = pathController.getCurrentFile(projectFile);
            if (!currentFile.getParentFile().exists()) {
                currentFile.getParentFile().mkdir();
            }
            outputStream = new BufferedOutputStream(new FileOutputStream(currentFile, true));

            serializer = EventSerializerFactory.getInstance(serializerType, serializerContext, outputStream);
            serializer.afterCreate();
        }
    }

}
