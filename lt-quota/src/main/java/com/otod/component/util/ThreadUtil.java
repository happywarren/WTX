/*
 * Source code of the book of Thinking in Java Component Design
 * ��������Java ������
 * ����: �׵���
 * Email: kshark2008@gmail.com
 * Date: 2008-12
 * Copyright 2008-2010
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

package com.otod.component.util;

public class ThreadUtil {
	public static boolean sleep(long millis) {
		try {
			Thread.sleep(millis);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	public static void interrupt(Thread t) {
		try {
			t.interrupt();
		} catch (Exception e) {}
	}
	
	public static void stop(Thread t, long millis) {
		interrupt(t);
		
		try {
			t.join(millis);
		} catch (Exception e) {}
	}
	
	public static void waitObject(Object obj) {
		try {
			obj.wait();
		} catch (InterruptedException e) {
		}
	}
	
	public static void waitObject(Object obj, long millis) {
		try {
			obj.wait(millis);
		} catch (InterruptedException e) {
		}
	}
	
	public static void syncWait(Object obj) {
		synchronized(obj) {
			try {
				obj.wait();
			} catch (InterruptedException e) {
			}
		}
	}
	
	public static void syncWait(Object obj, long millis) {
		synchronized(obj) {
			try {
				obj.wait(millis);
			} catch (InterruptedException e) {
			}
		}
	}
}
