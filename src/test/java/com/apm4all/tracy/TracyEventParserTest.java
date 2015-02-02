package com.apm4all.tracy;

import static org.junit.Assert.*;

import org.junit.Test;

public class TracyEventParserTest {
    static final String TASK_ID_VALUE           = "7623F387";
    static final String PARENT_OPT_ID_VALUE     = "80D1";
    static final String LABEL_VALUE             = "myLabel";
    static final String OPT_ID_VALUE            = "890C";
    static final String MSEC_BEFORE_VALUE       = "1422474838693";
    static final String MSEC_AFTER_VALUE        = "1422474838794";
    static final String MSEC_ELAPSED_VALUE      = "101";
    static final String HOST_KEY                = "host";
    static final String HOST_VALUE              = "localhost";
    static final String COMPONENT_KEY           = "component";
    static final String COMPONENT_VALUE         = "myComponent";
    static final String ANNOTATION_1_KEY        = "annotation1Key";
    static final String ANNOTATION_1_VALUE      = "myAnnotation1Value";
    static final String TRACY_JSON_EVENT = 
    "{\"taskId\":\"" +                      TASK_ID_VALUE + "\","
    + "\"parentOptId\":\"" +                PARENT_OPT_ID_VALUE + "\","
    + "\"label\":\""+                       LABEL_VALUE + "\","
    + "\"optId\":\""+                       OPT_ID_VALUE + "\","
    + "\"msecBefore\":\""+                  MSEC_BEFORE_VALUE + "\","
    + "\"msecAfter\":\""+                   MSEC_AFTER_VALUE + "\","
    + "\"msecElapsed\":\""+                 MSEC_ELAPSED_VALUE + "\","
    + "\"" + HOST_KEY + "\":\""+            HOST_VALUE + "\","
    + "\"" + COMPONENT_KEY + "\":\""+       COMPONENT_VALUE + "\","
    + "\"" + ANNOTATION_1_KEY + "\":\""+    ANNOTATION_1_VALUE + "\"}";

    @Test
    public void testTracyEventParser() {
        TracyEventParser tracyEventParser = new TracyEventParser(TRACY_JSON_EVENT);
        TracyEvent tracyEvent = tracyEventParser.parse();
        assertEquals(TASK_ID_VALUE, tracyEvent.getTaskId());
        assertEquals(PARENT_OPT_ID_VALUE, tracyEvent.getParentOptId());
        assertEquals(LABEL_VALUE, tracyEvent.getLabel());
        assertEquals(OPT_ID_VALUE, tracyEvent.getOptId());
        assertEquals(MSEC_BEFORE_VALUE, Long.toString(tracyEvent.getMsecBefore()));
        assertEquals(MSEC_AFTER_VALUE, Long.toString(tracyEvent.getMsecAfter()));
        assertEquals(MSEC_ELAPSED_VALUE, Long.toString(tracyEvent.getMsecElapsed()));
        assertEquals(HOST_VALUE, tracyEvent.getAnnotation(HOST_KEY));
        assertEquals(COMPONENT_VALUE, tracyEvent.getAnnotation(COMPONENT_KEY));  
        assertEquals(ANNOTATION_1_VALUE, tracyEvent.getAnnotation(ANNOTATION_1_KEY));
    }
}
