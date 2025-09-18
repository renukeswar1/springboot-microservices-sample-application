package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUploadEvent {
    private Long videoId;
    private String originalFilename;
    private byte[] fileData;
    private String fileFormat;
    private long fileSizeBytes;
    private String uploadedBy;
    private long uploadTimestamp;
}