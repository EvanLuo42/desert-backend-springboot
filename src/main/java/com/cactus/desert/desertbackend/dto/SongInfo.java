package com.cactus.desert.desertbackend.dto;

import lombok.Data;

/**
 * @author EvanLuo42
 * @date 4/29/22 9:28 AM
 */
@Data
public class SongInfo {
    private String songName;
    private String songArtist;
    private String scoreAuthor;
    private String songCoverFile;
}
