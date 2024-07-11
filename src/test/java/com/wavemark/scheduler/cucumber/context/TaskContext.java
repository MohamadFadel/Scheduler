//package com.wavemark.scheduler.cucumber.context;
//
//import java.io.Serializable;
//import java.util.HashMap;
//import java.util.Map;
//
//public class TaskContext implements Serializable {
//
//	private static final long serialVersionUID = 4312251626594624662L;
//
//	private final Map<String, Integer> taskMap = new HashMap<>();
//
//	public TaskContext add(Map<String, Integer> taskMapContext) {
//		taskMap.putAll(taskMapContext);
//		return this;
//	}
//
//	public TaskContext add(String key, Integer value) {
//		taskMap.put(key, value);
//		return this;
//	}
//
//	public Integer get(String key) {
//		return taskMap.get(key);
//	}
//
//	public TaskContext remove(String key) {
//		taskMap.remove(key);
//		return this;
//	}
//
//	public Map<String, Integer> getTaskMap() {
//		return taskMap;
//	}
//}
