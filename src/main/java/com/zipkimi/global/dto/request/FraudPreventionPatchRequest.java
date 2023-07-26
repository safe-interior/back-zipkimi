package com.zipkimi.global.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@ApiModel
@AllArgsConstructor
@RequiredArgsConstructor
public class FraudPreventionPatchRequest {
	@ApiModelProperty(name = "게시글 고유번호", example = "1", required = true)
	private long boardId;

	@ApiModelProperty(name = "사용자 고유번호", example = "1", required = true)
	private long userId;

	@ApiModelProperty(name = "제목", example = "TEXT 제목", notes = "영문기준 200자 이하")
	private String title;

	@ApiModelProperty(name = "부제목", example = "TEXT 부제목", notes = "영문기준 300자 이하")
	private String subTitle;

	@ApiModelProperty(name = "내용", example = "TEXT 내용")
	private String content;
}
