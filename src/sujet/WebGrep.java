package sujet;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class WebGrep extends Thread {
	public void run() {
		Executor executor = Executors.newFixedThreadPool(Tools.numberThreads()); // create a pool of numberThread threads

		FileBloquante urlToParse = new FileBloquante();
		FileBloquante historizesURL = new FileBloquante();

		urlToParse.addAll(Tools.startingURL());
		String[] skipFragments = {"#"};

		executor.execute(() -> {
			while (!urlToParse.isEmpty()) {
				CustomNode url = null;
				try {
					url = urlToParse.take();
					boolean skip = false;
					for (String fragment : skipFragments) {
						if (url.linkToParse.contains(fragment)) {
							skip = true;
							break;
						}
					}
					if (!skip && !historizesURL.contains(url)) {
						historizesURL.put(url);
						Tools.ParsedPage p = Tools.parsePage(url.linkToParse);
						if (!p.matches().isEmpty()) {
							Tools.print(p);
							System.out.println("New href detected on this page: " + p.hrefs().size() +
									", total hrefs left to parse: " + urlToParse.size());
							for (String s : p.hrefs()) {
								CustomNode temp = new CustomNode(s);
								skip = false;
								for (String fragment : skipFragments) {
									if (s.contains(fragment)) {
										skip = true;
										break;
									}
								}
								if (!skip && !urlToParse.contains(temp) && !historizesURL.contains(temp)) {
									urlToParse.put(temp);
								}
							}
						}
					}
				} catch (IOException | InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
}






