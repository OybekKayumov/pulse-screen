package com.ok.netflix.clone.netflix_clone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoStatsResponse {

	private long totalVideos;

	private long publishedVideos;

	private long totalDuration;
}
