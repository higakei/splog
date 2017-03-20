package jp.co.hottolink.splogfilter.api.servlet.santi.waisetsu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import jp.co.hottolink.fusion.core.util.net.RssUtil;
import jp.co.hottolink.fusion.core.util.net.feed.FeedChannel;
import jp.co.hottolink.fusion.core.util.net.feed.FeedItem;
import jp.co.hottolink.splogfilter.api.constants.SplogAPIConstants;
import jp.co.hottolink.splogfilter.api.servlet.abst.AbstractAnalyzeServlet;
import jp.co.hottolink.splogfilter.common.extension.jp.co.hottolink.fusion.core.util.string.HtmlAnarizer;
import jp.co.hottolink.splogfilter.santi.waisetsu.WaisetsuAnalyzer;

/**
 * <p>
 * RSSフィードをわいせつ語フィルタで分析するサーブレット.
 * </p>
 * @author higa
 */
public class RssWaisetsuAnalyzeServlet extends AbstractAnalyzeServlet {

	/**
	 * <p>
	 * serialVersionUID.
	 * </p>
	 */
	private static final long serialVersionUID = -5250149226474164318L;

	/**
	 * <p>
	 * テンプレートパス.
	 * </p>
	 */
	private static final String TEMPLATE_PATH = "waisetsu/rss.vm";

	/**
	 * <p>
	 * ロガー.
	 * </p>
	 */
	private static Logger logger = Logger.getLogger(RssWaisetsuAnalyzeServlet.class);

	/*
	 * (non-Javadoc)
	 * @see jp.co.hottolink.splogfilter.api.servlet.abst.AbstractAnalyzeServlet#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return logger;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// パラメータの取得
			String url = request.getParameter("url");
			String detail = request.getParameter("detail");

			// URL
			if ((url == null) || url.isEmpty()) {
				outputErrorXML(response, "url is required");
				return;
			}

			// 詳細表示の設定
			boolean showDetail = false;
			if ("show".equals(detail)) {
				showDetail = true;
			}

			// RSSフィードの取得
			FeedChannel channel = RssUtil.getRss(url);
			if (channel == null) {
				outputErrorXML(response, "no data");
				return;
			}

			// RSSアイテムの取得
			List<FeedItem> items = channel.getItems();
			if ((items == null) || items.isEmpty()) {
				outputErrorXML(response, "no data");
				return;
			}
			// 分析
			logger.info("----- analyze(" + items.size() + ") start -----");
			WaisetsuAnalyzer analyzer = new WaisetsuAnalyzer();
			for (int i = 0; i < items.size(); i++) {
				FeedItem item = items.get(i);

				// HTMLタグの除去
				String description = item.getDescription();
				description = HtmlAnarizer.toPlaneText(description);
				item.setDescription(description);

				// APIに問い合わせ
				try {
					Map<String, Object> result = analyzer.analyze(item);
					if (result.containsKey("cause")) {
						logger.info((i + 1) + "\t" + item.getLink() + "\t" + result.get("cause"));
					} else {
						logger.info((i + 1) + "\t" + item.getLink() + "\t" + result.get("judge"));
					}
				} catch (Exception e) {
					logger.warn((i + 1) + "\t" + item.getLink(), e);
					analyzer.addErrorResult(e, item);
					continue;
				}
			}
			logger.info("----- analyze(" + items.size() + ") end -----");

			// Velocityコンテキストに値を設定
			VelocityContext context = createVelocityContext();
			context.put("showDetail", showDetail);
			context.put("results", analyzer.getResults());

			// XML出力
			response.setContentType(SplogAPIConstants.CONTENT_TYPE_XML);
			response.setHeader("charset", encoding);
			response.setCharacterEncoding(encoding);
			PrintWriter writer = response.getWriter();
			Velocity.mergeTemplate(TEMPLATE_PATH, encoding, context, writer);

		} catch (Exception e) {
			logger.error("", e);
			outputErrorXML(response, "analyze fialed");
		}
	}
}
