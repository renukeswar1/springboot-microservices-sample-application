package org.example.models;

public enum VideoFormat {
    FORMAT_1080P("1080p", 1920, 1080, 5000),
    FORMAT_720P("720p", 1280, 720, 2500),
    FORMAT_480P("480p", 854, 480, 1000);

    private final String name;
    private final int width;
    private final int height;
    private final int bitrate; // kbps

    VideoFormat(String name, int width, int height, int bitrate) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.bitrate = bitrate;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBitrate() {
        return bitrate;
    }
}