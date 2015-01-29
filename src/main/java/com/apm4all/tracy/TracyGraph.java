/*
 * Copyright 2015 Joao Vicente
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apm4all.tracy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

public class TracyGraph {
    private DefaultDirectedGraph<TracyEvent,DefaultEdge> graph = new DefaultDirectedGraph<TracyEvent, DefaultEdge>(DefaultEdge.class);
    private Map<String, TracyEvent> optIdToTracyEventMap = new HashMap<String, TracyEvent>();
    private List<String> duplicateOptIds = new ArrayList<String>();
    private Set<String> taskIds = new HashSet<String>();
    private TracyEvent rootTracyEvent = null;
    private TreeMap<String, TracyEvent> msecBeforeOrderedSet = new TreeMap<String, TracyEvent>();
    //TODO: Order and edge weight by timestamp+optId of the child 
    
    public void add(TracyEvent tracyEvent)  {
        graph.addVertex(tracyEvent);
        if (optIdToTracyEventMap.containsKey(tracyEvent.getOptId()))    {
            duplicateOptIds.add(tracyEvent.getOptId());
        }
        taskIds.add(tracyEvent.getTaskId());
        optIdToTracyEventMap.put(tracyEvent.getOptId(), tracyEvent);
        StringBuilder msecOrderKey = new StringBuilder();
        msecOrderKey.append(new Long(tracyEvent.getMsecBefore()).toString() )
            .append("-")
            .append(tracyEvent.getOptId());
        msecBeforeOrderedSet.put(msecOrderKey.toString(), tracyEvent);
    }
    
    public void build() {
        //TODO: perform msec parent->child offsets
        List<TracyEvent> reverseMsecOrderedTracyEvents = new ArrayList<TracyEvent>();
        
        for(Map.Entry<String ,TracyEvent> entry : msecBeforeOrderedSet.entrySet()) {
            reverseMsecOrderedTracyEvents.add(entry.getValue());
        }
        Collections.reverse(reverseMsecOrderedTracyEvents);
        for (TracyEvent tracyEvent : reverseMsecOrderedTracyEvents)  {
            TracyEvent parentOptTracyEvent = optIdToTracyEventMap.get(tracyEvent.getParentOptId());
            if (parentOptTracyEvent != null)    {
                graph.addEdge(parentOptTracyEvent, tracyEvent);
                System.out.println("Build() adding Edge FROM "+ tracyEvent.getLabel() + " TO " 
                + parentOptTracyEvent.getLabel());
            }
            else {
                rootTracyEvent = tracyEvent;
            }
        }
        // TODO: throwException if graph !isValid()
    }
  
    public boolean isValid()    {
        int nodesWithoutParentCount = 0;
        int nodesWithoutParentOrChildrenCount = 0;
        
        for (TracyEvent tracyEvent : optIdToTracyEventMap.values()) {
            boolean hasParent = graph.incomingEdgesOf(tracyEvent).size() > 0;
            boolean hasChildren = graph.outgoingEdgesOf(tracyEvent).size() > 0;
            if (false == hasParent)    {
                nodesWithoutParentCount++;
            }
            if (false == hasParent && false == hasChildren) {
                nodesWithoutParentOrChildrenCount++;
            }
        }
        // Single taskId
        // No duplicate optIds
        // Has a single root
        // Each node has either parent or children
        return (
                1 == taskIds.size()
                && 0 == duplicateOptIds.size()
                && 1 == nodesWithoutParentCount && rootTracyEvent != null
                && 0 == nodesWithoutParentOrChildrenCount);
    }
    
    public String toString()  {
        return graph.toString();
    }
    
	public String asTimeline()  {
	    StringBuilder sb = new StringBuilder();
	    GraphIterator<TracyEvent, DefaultEdge> iterator = 
	            new DepthFirstIterator<TracyEvent, DefaultEdge>(graph);
	    while (iterator.hasNext()) {
	        TracyEvent tracyEvent = iterator.next();
	        sb.append("\r\n "
	                + "label=\"" + tracyEvent.getLabel() + "\","
	                + "elapsed=\"" + tracyEvent.getMsecElapsed()
	                );
	    }
	    return sb.toString();
	}
	
	// get-depth-next
	// if has children return children
	// if no children return me
	
}
