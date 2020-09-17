package com.ilexiconn.llibrary.server.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ilexiconn.llibrary.LLibrary;
import net.minecraft.crash.CrashReport;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Utilities to access data from the internet. All methods throw out an error to the console if something goes wrong.
 *
 * @author iLexiconn
 * @since 1.0.0
 */
public class WebUtils {
    public static final String PASTEBIN_URL_PREFIX = "http://pastebin.com/raw.php?i=";

    private static final ListeningExecutorService EXECUTOR = MoreExecutors.listeningDecorator(new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>()));

    /**
     * Get content from a pastebin file as String, split to multiple lines using newlines. Returns null if something goes wrong.
     *
     * @param pasteID the paste id to read data from
     * @return the content from a pastebin file or null
     */
    public static String readPastebin(String pasteID) {
        return readURL(WebUtils.PASTEBIN_URL_PREFIX + pasteID);
    }

    /**
     * Get content from a pastebin file as List, with every newline as a new entry. Returns null if something goes wrong.
     *
     * @param pasteID the paste id to read data from
     * @return the content from a pastebin file or null
     */
    public static List<String> readPastebinAsList(String pasteID) {
        return readURLAsList(WebUtils.PASTEBIN_URL_PREFIX + pasteID);
    }

    /**
     * Get content from a url as String, split to multiple lines using newlines. Returns null if something goes wrong.
     *
     * @param url the url to read data from
     * @return the content from the url or null
     */
    public static String readURL(String url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            return IOUtils.toString(reader);
        } catch (IOException e) {
            LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to receive data from URL: " + url).getCompleteReport());
            return null;
        }
    }

    /**
     * Get content from a url as List, with every newline as a new entry. Returns null if something goes wrong.
     *
     * @param url the url to read data from
     * @return the content from a url or null
     */
    public static List<String> readURLAsList(String url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            return IOUtils.readLines(reader);
        } catch (IOException e) {
            LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to receive data from URL: " + url).getCompleteReport());
            return null;
        }
    }

    /**
     * Download an image from an url. Returns null if something goes wrong.
     *
     * @param url the image url
     * @return the image object or null
     */
    public static BufferedImage downloadImage(String url) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())){
            return ImageIO.read(in);
        } catch (IOException e) {
            LLibrary.LOGGER.error(CrashReport.makeCrashReport(e, "Failed to receive data from URL: " + url).getCompleteReport());
            return null;
        }
    }

    /**
     * Get content from a url as String, split to multiple lines using newlines, asynchronously. Returns null if something goes wrong.
     *
     * @param url the url to read data from
     * @return the future for the returned data
     */
    public static ListenableFuture<String> readURLAsync(String url) {
        return EXECUTOR.submit(() -> WebUtils.readURL(url));
    }

    /**
     * Get content from a url as List, with every newline as a new entry, asynchronously. Returns null if something goes wrong.
     *
     * @param url the url to read data from
     * @return the future for the returned data
     */
    public static ListenableFuture<List<String>> readURLAsListAsync(String url) {
        return EXECUTOR.submit(() -> WebUtils.readURLAsList(url));
    }

    /**
     * Get content from a pastebin file as String, split to multiple lines using newlines asynchronously. Returns null if something goes wrong.
     *
     * @param pasteID the paste id to read data from
     * @return the future for the returned data
     */
    public static ListenableFuture<String> readPastebinAsync(String pasteID) {
        return EXECUTOR.submit(() -> WebUtils.readPastebin(pasteID));
    }

    /**
     * Get content from a pastebin file as String, with every newline as a new entry, asynchronously. Returns null if something goes wrong.
     *
     * @param pasteID the paste id to read data from
     * @return the future for the returned data
     */
    public static ListenableFuture<List<String>> readPastebinAsListAsync(String pasteID) {
        return EXECUTOR.submit(() -> WebUtils.readURLAsList(pasteID));
    }
}
