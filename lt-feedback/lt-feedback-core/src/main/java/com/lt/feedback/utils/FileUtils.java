package com.lt.feedback.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: cndym
 * 日期: 2010-5-4
 * 时间: 14:28:57
 */
public class FileUtils {
    public static String S = File.separator;

    public static String BAK = ".bak";


    public static List<String> readFile(String filePath) {
        return readFile(filePath, "utf-8");
    }


    public static List<String> readFile(String filePath, String encode) {
        List<String> list = new ArrayList<String>();
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<String>();
        }
        BufferedReader reader = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), encode);
            reader = new BufferedReader(inputStreamReader);
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                if (temp.trim().equals("")) {
                    continue;
                }
                list.add(temp);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return list;
    }

    public static String readLastLine(String fileName, String charset) {
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return "";
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            long len = raf.length();
            if (len == 0L) {
                return "";
            } else {
                long pos = len - 1;
                while (pos > 0) {
                    pos--;
                    raf.seek(pos);
                    if (raf.readByte() == '\n') {
                        break;
                    }
                }
                if (pos == 0) {
                    raf.seek(0);
                }
                byte[] bytes = new byte[(int) (len - pos)];
                raf.read(bytes);
                if (charset == null) {
                    return new String(bytes, "utf-8");
                } else {
                    return new String(bytes, charset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e2) {
                }
            }
        }

    }

    public static void safeReWriteFile(String content, String filePath) {
        writeFile(content, filePath + BAK, true);
        rename(filePath + BAK, filePath);
    }


    public static String writeFile(String content, String filePath, boolean noOverride) {
        FileOutputStream fileOutputStream = null;
        File file = new File(filePath);
        String value = null;
        try {
            if (!file.exists())
                file.createNewFile();
            fileOutputStream = new FileOutputStream(file, noOverride);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(content);
            value = stringBuffer.toString();
            fileOutputStream.write(stringBuffer.toString().getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e1) {
                }
            }
        }
        return value;
    }

    public static void writeLine(String fileName, String content) {
        File file = new File(fileName);
        try {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream out = new FileOutputStream(file, true);
            OutputStreamWriter outWriter = new OutputStreamWriter(out, "UTF-8");
            BufferedWriter bufWrite = new BufferedWriter(outWriter);
            bufWrite.write(content + "\r");
            bufWrite.close();
            outWriter.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("读取" + fileName + "出错！");
        }
    }

    public static List<String> exeCurrDir(String path) {
        List<String> list = new ArrayList<String>();
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File sub : files) {
                    if (!sub.isDirectory()) {
                        list.add(sub.getAbsolutePath());
                    }
                }
            }
        }
        return list;
    }

    public static List<String> exeCurrDirAll(String path) {
        List<String> list = new ArrayList<String>();
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File sub : files) {
                    list.add(sub.getAbsolutePath());
                }
            }
        }
        return list;
    }


    public static void creatDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void exeDeleteDirExp(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (null == files)
            return;
        for (File f : files) {
            if (f.isFile()) {
                f.delete();
            }
            if (f.isDirectory()) {
                File[] sub = f.listFiles();
                if (sub.length == 0) {
                    f.delete();
                } else {
                    exeDeleteDirExp(f.getAbsolutePath());
                }
            }
        }
        files = file.listFiles();
        if (files.length > 0) {
            exeDeleteDirExp(path);
        }

    }

    public static void exeCreatDirExp(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    public static void rename(String name, String newname) {
        delete(newname);
        File file = new File(name);
        File newFile = new File(newname);
        boolean success = file.renameTo(newFile);
        if (!success) {
            throw new IllegalArgumentException("重命名失败");
        }
    }

    public static void exeFindFiles(List<String> list, String path) {
        if (list == null) {
            list = new ArrayList<String>();
        }
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    list.add(f.getAbsolutePath());
                } else if (f.isDirectory()) {
                    exeFindFiles(list, f.getAbsolutePath());
                }
            }
        }
    }

    public static void copyFile(File sourceFile, File targetFile) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inBuff != null)
                try {
                    inBuff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (outBuff != null)
                try {
                    outBuff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void copyFile(String source, String target) {
        (new File(target)).mkdirs();
        File[] file = (new File(source)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                copyFile(file[i], new File(target + file[i].getName()));
            }
            if (file[i].isDirectory()) {
                // 复制目录
                String sourceDir = source + File.separator + file[i].getName();
                String targetDir = target + File.separator + file[i].getName();
                copyDirectory(sourceDir, targetDir);
            }
        }
    }

    public static void copyDirectory(String sourceDir, String targetDir) {
        (new File(targetDir)).mkdirs();
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                File sourceFile = file[i];
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                String dir1 = sourceDir + S + file[i].getName();
                String dir2 = targetDir + S + file[i].getName();
                copyDirectory(dir1, dir2);
            }
        }
    }

}
