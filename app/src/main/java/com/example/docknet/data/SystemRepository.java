package com.example.docknet.data;

import android.util.Log;

import com.example.docknet.model.SystemInfo;
import com.example.docknet.network.NetworkClient;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SystemRepository {
    private static final String TAG = "SystemRepository";

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    public static class SystemResult {
        public final SystemInfo info;
        public final String primaryStarType;

        public SystemResult(SystemInfo info, String primaryStarType) {
            this.info = info;
            this.primaryStarType = primaryStarType;
        }
    }

    private final NetworkClient networkClient;
    private final ExecutorService executor;
    // searchCounter / infoCounter are simple "tokens" used to ignore stale responses.
    // Pattern: when scheduling a new request we increment the counter and capture its value (myId).
    // When a response arrives we compare myId with current counter; if they differ, the response is stale and ignored.
    // This is easier for beginners than trying to cancel threads or cancel retrofit calls from multiple places.
    private final AtomicInteger searchCounter = new AtomicInteger(0);
    private final AtomicInteger infoCounter = new AtomicInteger(0);

    public SystemRepository(NetworkClient networkClient) {
        this.networkClient = networkClient;
        // single-thread executor ensures repository tasks run sequentially — avoids additional synchronization.
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void searchSystems(String query, RepositoryCallback<List<String>> callback) {
        if (executor.isShutdown() || executor.isTerminated()) return;
        final int myId = searchCounter.incrementAndGet();
        executor.submit(() -> {
            try {
                String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
                String url = "https://www.edsm.net/api-v1/systems?systemName=" + encoded + "&showInformation=1&showCoordinates=1&showPrimaryStar=1";
                String jsonResponse = networkClient.performApiRequest(url);
                List<String> list = SystemParser.parseSystemListFromJson(jsonResponse);
                if (myId != searchCounter.get()) return; // stale
                callback.onSuccess(list);
            } catch (Exception e) {
                // If stale, ignore
                if (myId != searchCounter.get()) return;
                Log.w(TAG, "searchSystems failed for query=" + query, e);
                callback.onError(e);
            }
        });
    }

    public void getSystemInfo(String systemName, RepositoryCallback<SystemResult> callback) {
        if (executor.isShutdown() || executor.isTerminated()) return;
        final int myId = infoCounter.incrementAndGet();
        executor.submit(() -> {
            try {
                String encoded = URLEncoder.encode(systemName, StandardCharsets.UTF_8);
                String url = "https://www.edsm.net/api-v1/system?systemName=" + encoded + "&showInformation=1&showCoordinates=1&showPrimaryStar=1";
                String jsonResponse = networkClient.performApiRequest(url);
                JSONObject systemObject = new JSONObject(jsonResponse);
                SystemInfo info = SystemParser.parseSystemInfoFromJson(systemObject);
                String starType = info.primaryStarType != null ? info.primaryStarType : "";
                if (myId != infoCounter.get()) return; // stale
                callback.onSuccess(new SystemResult(info, starType));
            } catch (Exception e) {
                if (myId != infoCounter.get()) return;
                Log.w(TAG, "getSystemInfo failed for system=" + systemName, e);
                callback.onError(e);
            }
        });
    }

    public void cancelCurrentRequest() {
        // increment counters so in-flight replies are treated as stale
        searchCounter.incrementAndGet();
        infoCounter.incrementAndGet();
        networkClient.cancelCurrentRequest();
    }

    public void shutdown() {
        try {
            executor.shutdownNow();
        } catch (Exception ignored) {}
        cancelCurrentRequest();
    }
}
