package com.li.cloud.gateway.security.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.li.cloud.common.basecurd.entity.ReturnData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc 抽象的session失效处理器
 * @date 2020-03-23
 */
public class AbstractSessionStrategy {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String INVALID_MSG = "登录已失效，请重新登录！";

	// 重定向策略
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private ObjectMapper objectMapper = new ObjectMapper();


	protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info(INVALID_MSG);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(ReturnData.unLogin(INVALID_MSG)));
	}

	/**
	 * @desc 构建返回内容
	 * @param request
	 * @return
	 * @date 2020-03-23
	 */
	protected ReturnData buildResponseContent(HttpServletRequest request) {
		String message = INVALID_MSG;
		if (isConcurrency()) {
			message = message + "，有可能是并发登录导致的";
		}
		return ReturnData.unLogin(message);
	}

	/**
	 * @desc  session失效是否是并发导致的
	 * @return
	 * @date 2020-03-23
	 */
	protected boolean isConcurrency() {
		return false;
	}

}
