package com.apm4all.tracy;

import static org.junit.Assert.*;

import org.junit.Test;

public class TracyGraphTest extends TracyGraph {

    static final String TASK_ID_VALUE           = "7623F387";
    static final String PARENT_OPT_ID_VALUE     = "80D1";
    static final String LABEL_VALUE             = "myLabel";
    static final String OPT_ID_VALUE            = "890C";
    static final long   MSEC_START              = 1422474838000L;
    static final String HOST_KEY                = "host";
    static final String HOST_VALUE              = "localhost";
    static final String COMPONENT_KEY           = "component";
    static final String COMPONENT_VALUE         = "myComponent";
    static final String ANNOTATION_1_KEY        = "annotation1Key";
    static final String ANNOTATION_1_VALUE      = "myAnnotation1Value";

    private TracyEvent createTracyEvent(String label, String optId, String parentOptId,
            long msecBefore, long msecElapsed)    {
        long msecAfter = msecBefore + msecElapsed;
        String tracyEventString =    
                "{\"taskId\":\"" +                      TASK_ID_VALUE + "\","
                + "\"parentOptId\":\"" +                parentOptId + "\","
                + "\"label\":\""+                       label + "\","
                + "\"optId\":\""+                       optId + "\","
                + "\"msecBefore\":\""+                  msecBefore + "\","
                + "\"msecAfter\":\""+                   msecAfter + "\","
                + "\"msecElapsed\":\""+                 msecElapsed + "\","
                + "\"" + HOST_KEY + "\":\""+            HOST_VALUE + "\","
                + "\"" + COMPONENT_KEY + "\":\""+       COMPONENT_VALUE + "\","
                + "\"" + ANNOTATION_1_KEY + "\":\""+    ANNOTATION_1_VALUE + "\"}";
        TracyEventParser tracyEventParser = new TracyEventParser(tracyEventString);
        return tracyEventParser.parse();
    }
    
    
    @Test
    public void testTracyGraph() {
        final String LABEL_1 =             "1";
        final String OPT_ID_1 =            "1000";
        final String PARENT_OPT_ID_1 =     "null";
        final String LABEL_1A =            "1A";
        final String OPT_ID_1A =           "1A00";
        final String PARENT_OPT_ID_1A =    "1000";
        final String LABEL_1AX =           "1AX";
        final String OPT_ID_1AX =          "1AX0";
        final String PARENT_OPT_ID_1AX =   "1A00";
        final String LABEL_1B =            "1B";
        final String OPT_ID_1B =           "1B00";
        final String PARENT_OPT_ID_1B =    "1000";
        final String LABEL_1BX =           "1BX";
        final String OPT_ID_1BX =          "1BX0";
        final String PARENT_OPT_ID_1BX =   "1B00";
        
        TracyGraph tracyGraph = new TracyGraph();
        tracyGraph.add(createTracyEvent(LABEL_1, OPT_ID_1, PARENT_OPT_ID_1, MSEC_START, 100));
        tracyGraph.add(createTracyEvent(LABEL_1A, OPT_ID_1A, PARENT_OPT_ID_1A, MSEC_START, 50));
        tracyGraph.add(createTracyEvent(LABEL_1AX, OPT_ID_1AX, PARENT_OPT_ID_1AX, MSEC_START, 50));
        tracyGraph.add(createTracyEvent(LABEL_1B, OPT_ID_1B, PARENT_OPT_ID_1B, MSEC_START+50, 50));
        tracyGraph.add(createTracyEvent(LABEL_1BX, OPT_ID_1BX, PARENT_OPT_ID_1BX, MSEC_START+50, 50));
        tracyGraph.build();
        
        assertTrue(tracyGraph.isValid());
        System.out.println(tracyGraph.asTimeline());
    }
}
