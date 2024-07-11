//package com.wavemark.scheduler.cucumber.context;
//
//import java.util.Map;
//
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//public class TaskContextHolder {
//
//	private static final ThreadLocal<TaskContext> context = new ThreadLocal<>();
//
//	public static void set(TaskContext taskContext) {
//		context.set(taskContext);
//	}
//
//	public static void add(Map<String, Integer> taskMapContext) {
//		getUserContext().add(taskMapContext);
//	}
//
//	public static void add(String key, Integer value) {
//		getUserContext().add(key, value);
//	}
//
//	public static Integer get(String key) {
//		return getUserContext().get(key);
//	}
//
//	public static int size() {
//		return getUserContext().getTaskMap().size();
//	}
//
//	public static TaskContext remove(String key) {
//		return getUserContext().remove(key);
//	}
//
//	public static void remove() {
//		context.remove();
//	}
//
//	public static TaskContext getUserContext() {
//		return context.get();
//	}
//
//}
