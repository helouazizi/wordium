package com.wordium.posts.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.wordium.posts.dto.MediaRequest;
import com.wordium.posts.dto.MediaType;

@Service
public class CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);

    private final Cloudinary cloudinary;

    private static final String DEFAULT_FOLDER = "posts";
    private static final String DEFAULT_UPLOAD_PRESET = "post_default";
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_COMPLETED = "COMPLETED";

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Generates a Cloudinary signature for uploading files with PENDING status.
     */
    public Map<String, Object> getSignature() {
        long timestamp = System.currentTimeMillis() / 1000L;

        Map<String, Object> params = new HashMap<>();
        params.put("timestamp", timestamp);
        params.put("folder", DEFAULT_FOLDER);
        params.put("upload_preset", DEFAULT_UPLOAD_PRESET);
        params.put("context", "status=" + STATUS_PENDING + "|timestamp=" + timestamp);

        String signature = cloudinary.apiSignRequest(params, cloudinary.config.apiSecret);

        Map<String, Object> response = new HashMap<>();
        response.put("signature", signature);
        response.put("timestamp", timestamp);
        response.put("cloudName", cloudinary.config.cloudName);
        response.put("apiKey", cloudinary.config.apiKey);
        response.put("folder", DEFAULT_FOLDER);
        response.put("upload_preset", DEFAULT_UPLOAD_PRESET);
        response.put("context", "status=" + STATUS_PENDING + "|timestamp=" + timestamp);

        return response;
    }

    /**
     * Scheduled job that deletes stale PENDING uploads from Cloudinary.
     * Runs every hour.
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void deletePendingUploads() {
        try {
            Map<?, ?> resources = cloudinary.api().resources(ObjectUtils.asMap(
                    "type", "upload",
                    "prefix", DEFAULT_FOLDER + "/",
                    "context", true));

            List<Map<String, Object>> files = (List<Map<String, Object>>) resources.get("resources");
            long now = System.currentTimeMillis() / 1000L;

            for (Map<String, Object> file : files) {
                Map<String, Object> context = (Map<String, Object>) file.get("context");
                if (context != null && context.containsKey("custom")) {
                    Map<String, String> custom = (Map<String, String>) context.get("custom");
                    String status = custom.get("status");
                    String tsStr = custom.get("timestamp");

                    if (STATUS_PENDING.equals(status)) {
                        long timestamp = tsStr != null ? Long.parseLong(tsStr) : now;
                        // Delete if older than 24 hours
                        if (now - timestamp > 24 * 3600) {
                            String publicId = (String) file.get("public_id");
                            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                            log.info("Deleted stale pending file: {}", publicId);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("Failed to delete pending Cloudinary uploads", e);
        }
    }

    /**
     * Marks a Cloudinary file as COMPLETED and returns the secure URL.
     */
    public String finalizeUpload(MediaRequest media) {
        String resourceType = media.type() == MediaType.VIDEO ? "video" : "image";

        try {
            cloudinary.uploader().explicit(
                    media.publicId(),
                    ObjectUtils.asMap(
                            "resource_type", resourceType,
                            "type", "upload",
                            "invalidate", true,
                            "context", "status=" + STATUS_COMPLETED));

            Map<?, ?> resource = cloudinary.api().resource(
                    media.publicId(),
                    ObjectUtils.asMap("resource_type", resourceType));

            return (String) resource.get("secure_url");

        } catch (Exception e) {
            log.error("Failed to finalize Cloudinary upload: {}", media.publicId(), e);
            throw new RuntimeException("Failed to finalize Cloudinary upload", e);
        }
    }

}
