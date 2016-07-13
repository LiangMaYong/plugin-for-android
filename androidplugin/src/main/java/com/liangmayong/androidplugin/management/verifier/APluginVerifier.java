package com.liangmayong.androidplugin.management.verifier;

import java.io.File;

import com.liangmayong.androidplugin.management.APlugin;

/**
 * APluginVerifier
 *
 * @author LiangMaYong
 * @version 1.0
 */
public interface APluginVerifier {

    /**
     * updateVerifier
     *
     * @param originPlugin originPlugin
     * @param targetPlugin targetPlugin
     * @return true or false
     */
    boolean updateVerifier(APlugin originPlugin, APlugin targetPlugin);

    /**
     * installVerifier
     *
     * @param targetFile targetFile
     * @return true or false
     */
    boolean installVerifier(File targetFile);

    /**
     * decompressionVerifier
     *
     * @param targetFile targetFile
     * @return unzipFile
     */
    File decompressionVerifier(File targetFile);
}
