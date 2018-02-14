package com.jimboweb.heirholzersalgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }

    public  ArrayList<Integer> hierholzersAlgorithm(HashMap<Integer, ArrayList<Integer>> edgesOut, HashMap<Integer, ArrayList<Integer>> edgesIn, ArrayList<Integer> endNodes, ArrayList<Integer> path) {

        Integer currNode=null;
        if(endNodes.size()==2) {
            if(path.isEmpty() || path.contains(endNodes.get(0)))
                currNode=endNodes.get(0);
            else if(path.contains(endNodes.get(1)))
                currNode=endNodes.get(1);
        } else {
            currNode = checkIfEulerian(edgesOut, path, currNode);
        }
        //At this point currNode is the start vertex. If the currNode is null, then the edgesOut is
        //completely traversed.
        if(currNode==null)
            return path;

        //Traverse the edgesOut and find a sub path/trail until you come to the start point. Here the path
        //is a sub path if you haven't traversed the entire edgesOut.
        ArrayList<Integer> subPath = new ArrayList<Integer>();
        while (currNode!=null) {
            subPath.add(currNode);
            ArrayList adjNodes = edgesOut.get(currNode);
            if(adjNodes.size()>0) {
                Integer nextNode=(Integer)adjNodes.get(0);
                edgesOut=removeEdge(edgesOut,edgesIn,currNode,nextNode);
                currNode=nextNode;
            }
            //We have come to the start point and there is nowhere else to go.
            else
                currNode=null;
        }

        //Add subPath to path.
        path = addSubPath(path, subPath);

        //get the endNodes of the remaining edgesOut (After removing some edges due to traversing)
        endNodes= getEndpoints(edgesOut, edgesIn);
        //Recursively call the method untill you traverse the entire edgesOut.
        path=hierholzersAlgorithm(edgesOut, edgesIn, endNodes, path);
        return path;
    }

    private ArrayList<Integer> addSubPath(ArrayList<Integer> path, ArrayList<Integer> subPath) {
        if(path.isEmpty())
            path=subPath;
            //Connect the subPath to path to get a longer trail.
        else if(subPath.size()>1) {
            ArrayList<Integer> adjustedPath = new ArrayList<Integer>();
            boolean subPathAdded = false;
            for (int i = 0; i < path.size(); i++) {
                adjustedPath.add(path.get(i));
                if(!subPathAdded && path.get(i).equals(subPath.get(0))){
                    for (int j = 1; j < subPath.size(); j++) {
                        adjustedPath.add(subPath.get(j));
                        subPathAdded=true;
                    }
                }
            }
            path=adjustedPath;
        }
        return path;
    }

    private Integer checkIfEulerian(HashMap<Integer, ArrayList<Integer>> adjacencyList, ArrayList<Integer> path, Integer currentVertex) {
        Iterator iterator = adjacencyList.keySet().iterator();
        while (iterator.hasNext()) {
            Integer vertex = (Integer)iterator.next();
            ArrayList adjacentVertices = adjacencyList.get(vertex);
            //This edge is not visited. So let that vertex be the start vertex if that vertex is in the path.
            //If the path is empty, i.e. trying to traverse for the first time, then just start from this vertex.
            if(adjacentVertices.size()>0) {
                if(path.isEmpty() || path.contains(vertex)){
                    currentVertex=vertex;
                    break;
                }
            }
        }
        return currentVertex;
    }

    public ArrayList<Integer> getEndpoints(HashMap<Integer, ArrayList<Integer>> edgesOut, HashMap<Integer, ArrayList<Integer>> edgesIn) {
        ArrayList<Integer> endPoints = new ArrayList<Integer>();
        Iterator iterator = edgesOut.keySet().iterator();
        while(iterator.hasNext()) {
            Integer key = (Integer) iterator.next();
            boolean checkEven=edgesOut.get(key).size()==edgesIn.get(key).size();
            if(!checkEven) {
                endPoints.add(key);
            }
        }
        return endPoints;
    }

    public  HashMap<Integer,ArrayList<Integer>> removeEdge(HashMap<Integer,ArrayList<Integer>> edgesOut, HashMap<Integer,ArrayList<Integer>> edgesIn,Integer v1, Integer v2) {
        if(!edgesOut.get(v1).contains(v2))
            return edgesOut;
        edgesOut.get(v1).remove(v2);
        edgesIn.get(v2).remove(v1);
        return edgesOut;
    }
}
