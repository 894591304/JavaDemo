package com.enation.eop.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.enation.eop.processor.core.Response;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.eop.processor.httpcache.HttpCacheManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.eop.sdk.context.EopContextIniter;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.SaasEopContextIniter;
import com.enation.eop.sdk.utils.JspUtil;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;

import javazoom.upload.UploadException;

public class DispatcherFilter implements Filter {
	public void init(FilterConfig config) {
	}

	private String urlWithPort(HttpServletRequest httpRequest, String domain, String uri) {
		int port = httpRequest.getServerPort();
		String portstr = "";
		if (port != 80) {
			portstr = ":" + port;
		}
		return "http://" + domain + portstr + uri;
	}

	private boolean fromMobile(String uri, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException {
		EopSite site = EopContext.getContext().getCurrentSite();
		if ((site.getMobilesite() != null) && (site.getMobilesite().intValue() != 0)) {
			return false;
		}

		String domain = httpRequest.getServerName().toLowerCase();

		if ("pc".equals(httpRequest.getParameter("viewMode")))
			return false;
		if ((!"/".equals(uri)) && (!"/index.html".equals(uri)) && (!domain.startsWith("m."))) {
			return false;
		}
		if ("/".equals(uri)) {
			uri = "/index.html";
		}
		if (domain.startsWith("m.")) {
			uri = "/m" + uri;
			httpRequest.setAttribute("redirect", uri);
		} else {
			String userAgent = httpRequest.getHeader("user-agent").toLowerCase();

			boolean mobile = false;
			if (userAgent.contains("android")) {
				mobile = true;
			} else if (userAgent.contains("iphone")) {
				mobile = true;
			}
			if (mobile) {
				if (domain.startsWith("www.")) {
					domain = "m" + domain.substring(3);
				} else {
					domain = "m." + domain;
				}
				httpResponse.sendRedirect(urlWithPort(httpRequest, domain, uri));
				return true;
			}
		}
		return false;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (EopSetting.SECURITY) {
			String referer = httpRequest.getHeader("referer");
			if ((null != referer) && ((referer.trim().startsWith(EopSetting.IMG_SERVER_DOMAIN))
					|| (referer.contains(httpRequest.getServerName())))) {
				chain.doFilter(httpRequest, httpResponse);
			} else {
				httpRequest.getRequestDispatcher("/daolian.gif").forward(httpRequest, httpResponse);
			}
		}

		String uri = httpRequest.getServletPath();
		try {
			if (uri.startsWith("/statics")) {
				chain.doFilter(httpRequest, httpResponse);

			} else if ((!uri.startsWith("/install")) && (EopSetting.INSTALL_LOCK.toUpperCase().equals("NO"))) {
				httpResponse.sendRedirect(httpRequest.getContextPath() + "/install");

			} else if ((!uri.startsWith("/install.html")) && (uri.startsWith("/install"))
					&& (!uri.startsWith("/install/images")) && (EopSetting.INSTALL_LOCK.toUpperCase().equals("YES"))) {
				httpResponse.getWriter().write("如要重新安装，请先删除/install/install.lock文件，并重起web容器");
			} else {
				if ("2".equals(EopSetting.RUNMODE)) {
					SaasEopContextIniter.init(httpRequest, httpResponse);
				} else {
					EopContextIniter.init(httpRequest, httpResponse);
				}

				Processor loginprocessor = (Processor) SpringContextHolder.getBean("autoLoginProcessor");
				if (loginprocessor != null) {
					loginprocessor.process(1, httpResponse, httpRequest);
				}
//				if (fromMobile(uri, httpRequest, httpResponse)) {
//					return;
//				}

				Processor processor = ProcessorFactory.newProcessorInstance(uri, httpRequest);

				if (processor == null) {
					chain.doFilter(request, response);
				} else {
					if ((uri.equals("/")) || (uri.endsWith(".html"))) {
						boolean result = HttpCacheManager.getIsCached(uri);
						if (result) {
							return;
						}
					}
					Response eopResponse = processor.process(0, httpResponse, httpRequest);
					if (eopResponse.getStatusCode() == -2) {
						chain.doFilter(request, response);
						return;
					}

					if (eopResponse.getStatusCode() == -1) {
						httpResponse.sendRedirect(eopResponse.getContent());
						return;
					}

					InputStream in = eopResponse.getInputStream();
					if (in != null) {
						byte[] inbytes = IOUtils.toByteArray(in);
						httpResponse.setContentType(eopResponse.getContentType());
						httpResponse.setCharacterEncoding("UTF-8");

						OutputStream output = httpResponse.getOutputStream();
						IOUtils.write(inbytes, output);
					} else {
						chain.doFilter(request, response);
					}
				}
			}
		} catch (RuntimeException exception) {
			exception.printStackTrace();
			response.setContentType("text/html; charset=UTF-8");
			request.setAttribute("message", exception.getMessage());
			String content = JspUtil.getJspOutput("/commons/error.jsp", httpRequest, httpResponse);
			response.getWriter().print(content);
		} finally {
			ThreadContextHolder.remove();
			FreeMarkerPaser.remove();
			EopContext.remove();
		}
	}

	public void destroy() {
	}

	private HttpServletRequest wrapRequest(HttpServletRequest request, String url) throws UploadException, IOException {
		List<String> safeUrlList = EopSetting.safeUrlList;
		for (String safeUrl : safeUrlList) {
			if (safeUrl.equals(url)) {
				return request;
			}
		}
		return new SafeHttpRequestWrapper(request);
	}
}
