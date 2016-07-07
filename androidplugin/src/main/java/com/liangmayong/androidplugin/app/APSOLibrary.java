package com.liangmayong.androidplugin.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.liangmayong.androidplugin.management.APlugin;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * APSOLibrary
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class APSOLibrary {

    private APSOLibrary() {
    }

    /**
     * getLibraryPath
     *
     * @param pluginPath pluginPath
     * @return library Path
     */
    public static String getLibraryPath(String pluginPath) {
        try {
            return pluginPath.substring(0, pluginPath.lastIndexOf("."));
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * copyPluginSO
     *
     * @param plugin plugin
     */
    public static void copyPluginSO(APlugin plugin) {
        if (plugin != null) {
            String targetDir = getLibraryPath(plugin.getPluginPath());
            ArrayList<String> objDirs = new ArrayList<String>();
            objDirs.add("lib/" + getCpuABI());
            objDirs.add("lib/" + getCpuABI2());
            File file = new File(targetDir);
            if (file.exists()) {
                file.delete();
            }
            unzipFile(plugin.getPluginPath(), targetDir, objDirs);
        }
    }

    /**
     * clearPluginSO
     *
     * @param plugin plugin
     */
    public static void clearPluginSO(APlugin plugin) {
        String targetDir = getLibraryPath(plugin.getPluginPath());
        File file = new File(targetDir);
        if (file.exists()) {
            file.delete();
        }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.DONUT)
    private static final String getCpuABI() {
        return Build.CPU_ABI;
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.FROYO)
    private static final String getCpuABI2() {
        return Build.CPU_ABI2;
    }

    /**
     * isDirEquals
     *
     * @param srcfile srcfile
     * @param objDir  objDir
     * @return boolean
     */
    private final static boolean isDirEquals(String srcfile, String objDir) {
        try {
            int index = srcfile.lastIndexOf("/");
            String dir = null;
            String firstDirName = null;
            if (index != -1)
                dir = srcfile.substring(0, index);
            index = srcfile.indexOf("/");
            if (index != -1)
                firstDirName = srcfile.substring(0, index);
            if (null != dir && dir.equalsIgnoreCase(objDir)) {
                return true;
            } else if (null != firstDirName && firstDirName.equalsIgnoreCase(objDir)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * unzipFile
     *
     * @param zipFile   zipFile
     * @param targetDir targetDir
     * @param objDirs   objDirs
     */
    private final static void unzipFile(String zipFile, String targetDir, ArrayList<String> objDirs) {
        String strEntry = "";
        ZipInputStream zis = null;
        try {
            FileInputStream fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                strEntry = entry.getName();
                boolean find = false;
                for (String objDir : objDirs) {
                    if (isDirEquals(strEntry.toString(), objDir)) {
                        find = true;
                        break;
                    }
                }
                if (find) {
                    String targetEntry = strEntry.substring(strEntry.lastIndexOf("/") + 1);
                    saveZipFile(zis, targetDir + "/" + targetEntry);
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                if (null != zis) {
                    zis.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * saveZipFile
     *
     * @param zis      zis
     * @param filePath filePath
     * @throws Exception e
     */
    private final static void saveZipFile(ZipInputStream zis, String filePath) throws Exception {
        BufferedOutputStream dest = null;
        try {
            int buffer = 4096;
            byte data[] = new byte[buffer];
            int count;
            File entryFile = new File(filePath);
            File entryDir = new File(entryFile.getParent());
            if (!entryDir.exists()) {
                entryDir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(entryFile);
            dest = new BufferedOutputStream(fos, buffer);
            while ((count = zis.read(data, 0, buffer)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
        } catch (Exception e) {
        } finally {
            if (null != dest) {
                dest.close();
                dest = null;
            }
        }
    }
}
