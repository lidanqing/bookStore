package com.li.product.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * tomcat7֮ǰ�����������⣬tomcat8֮��Ͳ���Ҫ��
 * ͨ�ý�� get �� post���������
 * 
 * request ʵ��HttpServletRequest�ӿ�̫�鷳��ÿ��������Ҫ��д��ֱ�Ӽ̳�������HttpServletRequestWrapper
 * 
 */

/**
 * tomcat7:
	URIEncoding
	This specifies the character encoding used to decode the URI bytes, after %xx decoding the URL. 
	If not specified, ISO-8859-1 will be used
	��������������ý���url���������ûָ����Ĭ����ISO-8859-1��
	useBodyEncodingForURI
	This specifies if the encoding specified in contentType should be used for URI query parameters,
	instead of using the URIEncoding. This setting is present for compatibility with Tomcat 4.1.x, 
	where the encoding specified in the contentType, or explicitly set using Request.
	setCharacterEncoding method was also used for the parameters from the URL. The default value is false.
	���������˵�Ƿ�ʹ��contentType �����encoding������url�еĲ�����������ʹ��URIEncoding��
	��Ҫ��Ϊ�˼���Tomcat 4.1.x��Ĭ��ֵ��false��������setCharacterEncoding�Ժ�ʵ����Ҳ��������contentType ��
	tomcat8:
	URIEncoding 
	This specifies the character encoding used to decode the URI bytes, after %xx decoding the URL. 
	If not specified, UTF-8 will be used unless the org.apache.catalina.STRICT_SERVLET_COMPLIANCE 
	system property is set to true in which case ISO-8859-1 will be used.
	��������������ý���url���������ûָ����Ĭ����UTF-8������������org.apache.catalina.STRICT_SERVLET_COMPLIANCE���ϵͳ����Ϊtrue�����ʱ���ʹ��ISO-8859-1��
 * 
 * 
 * ��1��URIEncoding��useBodyEncodingForURI�����������⡣useBodyEncodingForURI��Ҫ��Ϊ�˼����ϰ汾��������URIEncoding��
        ��2��tomcat8֮ǰ��URL�в�����Ĭ�Ͻ�����ISO-8859-1����tomcat8��Ĭ�Ͻ���Ϊutf-8��
        ��3���ڹ�������ʹ�� request.setCharacterEncoding() ֻ���������� POST ���󣬶� GET ����Ч��
        ��4��tomcat������ռ�����취��
	tomcat8֮ǰ������URIEncoding + Post�Ĺ����� ���� ����useBodyEncodingForURI+Post��������
	tomcat8֮��ֻҪ����һ��Post�������Ϳ����ˣ�tomcat9��tomcat8��һ���ġ�
 *
 */
public class EncodingFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// ������������
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletRequest myRequest = new MyRequest(httpServletRequest);

		request.setCharacterEncoding("utf-8");
		// ������Ӧ����
		response.setContentType("text/html;charset=utf-8");
		//response.setCharacterEncoding("UTF-8");//���ý��ַ���"UTF-8"����������ͻ��������
		
		//response.setHeader("content-type", "text/html;charset=UTF-8");//ͨ��������Ӧͷ�����������UTF-8�ı�����ʾ����
		
		//����ͻ���post����UTF-8�ַ����뽫�����ݴ��䵽�������˵ģ���˷�����Ҳ��Ҫ������UTF-8�ַ�������н��գ�Ҫ����ɴ˲�����
		//����������ֱ��ʹ�ô�ServletRequest�ӿڼ̳ж�����"setCharacterEncoding(charset)"��������ͳһ�ı�������
		//request.setCharacterEncoding("UTF-8");
		/**
		 * ������get��ʽ��������ݣ�request��ʹ��������ָ���ı����������Ҳ����Ч�ģ���Ϊ��Tomcat5.0�У�
		 * Ĭ�������ʹ��ISO- 8859-1��URL�ύ�����ݺͱ���GET��ʽ�ύ�����ݽ������±��루���룩��
		 * ����ʹ�øò�����URL�ύ�����ݺͱ���GET��ʽ�ύ�����ݽ������±��루���룩��
		 * Ҫ��������⣬Ӧ����Tomcat�������ļ���Connector��ǩ������useBodyEncodingForURI���� URIEncoding���ԣ�
		 * ����useBodyEncodingForURI������ʾ�Ƿ���request.setCharacterEncoding ������URL�ύ�����ݺͱ���GET��ʽ�ύ�����ݽ������±��룬
		 * ��Ĭ������£��ò���Ϊfalse��Tomcat4.0�иò���Ĭ��Ϊtrue��
		 * Ĭ�ϵĻ���ʹ��ISO8859-1����ַ��������������ݣ��ͻ�����UTF-8�ı��봫�����ݵ��������ˣ�
		 * ���������˵�request����ʹ�õ���ISO8859-1����ַ��������������ݣ��������Ϳͻ��˹�ͨ�ı��벻һ����˲Ż������������ġ�
		 * ����취���ڽ��յ����ݺ��Ȼ�ȡrequest������ISO8859-1�ַ�������յ���ԭʼ���ݵ��ֽ����飬
		 * Ȼ��ͨ���ֽ�������ָ���ı��빹���ַ���������������⡣
		 * new String(name.getBytes("ISO8859-1"), "UTF-8") ;
		 * ��ȡrequest������ISO8859-1�ַ�������յ���ԭʼ���ݵ��ֽ����飬Ȼ��ͨ���ֽ�������ָ���ı��빹���ַ����������������
		 * 
		 * ���ֽ������������
		 */

		chain.doFilter(myRequest, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}

/**
* @Description: Servlet API���ṩ��һ��request�����Decorator���ģʽ��Ĭ��ʵ����HttpServletRequestWrapper,
* (HttpServletRequestWrapper��ʵ����request�ӿ��е����з���������Щ�������ڲ�ʵ�ֶ��ǽ���������һ������װ�ĵ� request����Ķ�Ӧ����)
* �Ա����û��ڶ�request���������ǿʱ��Ҫʵ��request�ӿ��е����з�����
* ���Ե���Ҫ��ǿrequest����ʱ��ֻ��Ҫдһ����̳�HttpServletRequestWrapper�࣬Ȼ������д��Ҫ��ǿ�ķ�������
*   1.ʵ���뱻��ǿ������ͬ�Ľӿ� 
    2������һ��������ס����ǿ����
    3������һ�����캯�������ձ���ǿ����
    4��������Ҫ��ǿ�ķ���
    5�����ڲ�����ǿ�ķ�����ֱ�ӵ��ñ���ǿ����Ŀ����󣩵ķ���
*/ 
// �Զ���request����
class MyRequest extends HttpServletRequestWrapper {

	private HttpServletRequest request;

	//��ε���getParameterMap()��������ָ㻵��
	private boolean hasEncode;

	public MyRequest(HttpServletRequest request) {
		super(request);// super����д
		this.request = request;
	}

	// ����Ҫ��ǿ���� ���и���
	// ���õ���ѡ��֮��ģ��ܶ����
	// ��request.getParameterMap()�ķ���ֵʹ�÷���ʱӦ����Map<String,String[]>��ʽ��
	// ��Ϊ��ʱ��checkbox�������������һ��name��Ӧ�Ը�value��ʱ�����Ը�Map�м�ֵ����<String-->String[]>��ʵ�֡�
	@Override
	public Map getParameterMap() {
		// �Ȼ������ʽ
		String method = request.getMethod();
		if (method.equalsIgnoreCase("post")) {
			// post����
			try {
				// ����post����
				request.setCharacterEncoding("utf-8");
				return request.getParameterMap();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else if (method.equalsIgnoreCase("get")) {
			// get����
			Map<String, String[]> parameterMap = request.getParameterMap();
			if (!hasEncode) { // ȷ��get�ֶ������߼�ֻ����һ��
				for (String parameterName : parameterMap.keySet()) {
					String[] values = parameterMap.get(parameterName);
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							try {
								// ����get����
								values[i] = new String(values[i]
										.getBytes("ISO-8859-1"), "utf-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
					}
				}
				hasEncode = true;
			}
			return parameterMap;
		}

		return super.getParameterMap();
	}

	@Override
	public String getParameter(String name) {
		Map<String, String[]> parameterMap = getParameterMap();
		String[] values = parameterMap.get(name);
		if (values == null) {
			return null;
		}
		return values[0]; // ȡ�ز����ĵ�һ��ֵ
	}

	@Override
	public String[] getParameterValues(String name) {
		Map<String, String[]> parameterMap = getParameterMap();
		String[] values = parameterMap.get(name);
		return values;
	}

}
