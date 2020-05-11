/**
 * 
 */
package com.online.yunding.security.session;

import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc 自定义session失效处理策略
 * @date 2020-03-23
 */
public class CusInvalidSessionStrategy extends AbstractSessionStrategy implements InvalidSessionStrategy {

	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		onSessionInvalid(request, response);
	}

}
