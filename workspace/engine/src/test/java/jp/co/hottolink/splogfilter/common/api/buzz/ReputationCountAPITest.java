package jp.co.hottolink.splogfilter.common.api.buzz;

import jp.co.hottolink.splogfilter.common.api.buzz.constants.BuzzAPIConstants;

public class ReputationCountAPITest {

	public static void main(String[] args) {
		try {
			ReputationCountAPI api = new ReputationCountAPI();
			api.setDomain("blog");
			api.call(args[0], args[1], args[2]);
			System.out.println(api.getCounts().get(0));

			api = new ReputationCountAPI();
			api.setDomain("blog");
			api.setSplog(BuzzAPIConstants.SPLOG_SOFT);
			api.call(args[0], args[1], args[2]);
			System.out.println(api.getCounts().get(0));

			api = new ReputationCountAPI();
			api.setDomain("blog");
			api.setSplog(BuzzAPIConstants.SPLOG_MEDIUM);
			api.call(args[0], args[1], args[2]);
			System.out.println(api.getCounts().get(0));

			api = new ReputationCountAPI();
			api.setDomain("blog");
			api.setSplog(BuzzAPIConstants.SPLOG_HARD);
			api.call(args[0], args[1], args[2]);
			System.out.println(api.getCounts().get(0));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
