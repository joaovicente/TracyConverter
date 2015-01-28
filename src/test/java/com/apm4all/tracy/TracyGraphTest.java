package com.apm4all.tracy;

import static org.junit.Assert.*;

import org.junit.Test;

public class TracyGraphTest extends TracyGraph {

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

    private TracyEvent createTracyEvent(String label, String optId, String parentOptId)    {
        String tracyEventString =    
                "{\"taskId\":\"" +                      TASK_ID_VALUE + "\","
                + "\"parentOptId\":\"" +                parentOptId + "\","
                + "\"label\":\""+                       label + "\","
                + "\"optId\":\""+                       optId + "\","
                + "\"msecBefore\":\""+                  MSEC_BEFORE_VALUE + "\","
                + "\"msecAfter\":\""+                   MSEC_AFTER_VALUE + "\","
                + "\"msecElapsed\":\""+                 MSEC_ELAPSED_VALUE + "\","
                + "\"" + HOST_KEY + "\":\""+            HOST_VALUE + "\","
                + "\"" + COMPONENT_KEY + "\":\""+       COMPONENT_VALUE + "\","
                + "\"" + ANNOTATION_1_KEY + "\":\""+    ANNOTATION_1_VALUE + "\"}";
        TracyEventParser tracyEventParser = new TracyEventParser(tracyEventString);
        return tracyEventParser.parse();
    }
    
    
    @Test
    public void testTracyGraph() {
        final String LABEL_1 =             "label-1";
        final String OPT_ID_1 =            "0001";
        final String PARENT_OPT_ID_1 =     "null";
        final String LABEL_1A =            "label-1A";
        final String OPT_ID_1A =           "001A";
        final String PARENT_OPT_ID_1A =    "0001";
        final String LABEL_1B =            "label-1B";
        final String OPT_ID_1B =           "001B";
        final String PARENT_OPT_ID_1B =    "0001";
        
        TracyGraph tracyGraph = new TracyGraph();
        tracyGraph.add(createTracyEvent(LABEL_1, OPT_ID_1, PARENT_OPT_ID_1));
        tracyGraph.add(createTracyEvent(LABEL_1A, OPT_ID_1A, PARENT_OPT_ID_1A));
        tracyGraph.add(createTracyEvent(LABEL_1B, OPT_ID_1B, PARENT_OPT_ID_1B));
        tracyGraph.build();
        
        assertTrue(tracyGraph.isValid());
        System.out.println(tracyGraph.toString());
    }
}
