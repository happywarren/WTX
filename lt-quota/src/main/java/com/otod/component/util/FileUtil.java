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

import java.io.*;

public class FileUtil {
	// accessMode
	public static int F_READ = 1;
	public static int F_WRITE = 2;
	
	// createFlag:
	// Creates a new file. The function fails if the specified file already exists.
	public static int CREATE_NEW = 1;
	// Creates a new file. If the file exists, the function overwrites the file and clears the existing attributes.
	public static int CREATE_ALWAYS = 2;
	// Opens the file. The function fails if the file does not exist. 
	public static int OPEN_EXISTING = 3;
	// Opens the file, if it exists. If the file does not exist, the function creates the file as if dwCreationDisposition were CREATE_NEW.
	public static int OPEN_ALWAYS = 4; 
	// Opens the file. Once opened, the file is truncated so that its size is zero bytes. The function fails if the file does not exist.
	public static int TRUNCATE_EXISTING = 5; 

	public void createFile(String fileName, int accessMode, int createFlag) {
		this.fileName = fileName;
		this.accessMode = accessMode;
		this.createFlag = createFlag;
	}
	
	public void close() {
		
	}
	
	public String readLine() {
		return null;
	}
	
	public void write(String text) {
		
	}
	
	public void writeLine(String lineText) {
		
	}
	
	
	public static long getFileSize(String fileName) {
		File f = new File(fileName);
		return f.length();
	}
	
	public static boolean rename(String srcFileName, String destFileName) {
		File src = new File(srcFileName);
		File dest = new File(destFileName);
		return src.renameTo(dest);
	}
	
	public static boolean delete(String fileName) {
		File f = new File(fileName);
		return f.delete();
	}
	
	private String fileName;
	private int accessMode;
	private int createFlag;
}
